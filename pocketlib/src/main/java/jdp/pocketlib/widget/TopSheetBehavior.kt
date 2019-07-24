@file:Suppress("DEPRECATION", "OverridingDeprecatedMember")

package jdp.pocketlib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import androidx.annotation.IntDef
import androidx.core.os.ParcelableCompat
import androidx.core.os.ParcelableCompatCreatorCallbacks
import androidx.core.view.MotionEventCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.VelocityTrackerCompat
import androidx.core.view.ViewCompat
import jdp.pocketlib.R
import java.lang.ref.WeakReference
import kotlin.math.abs

class TopSheetBehavior<V : View> @SuppressLint("PrivateResource") constructor(context: Context, attrs: AttributeSet) :
    androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<V>(context, attrs) {
    private val mMaximumVelocity: Float
    var peekHeight: Int = 0
        set(peekHeight) {
            field = Math.max(0, peekHeight)
            if (mViewRef != null && mViewRef!!.get() != null)
                mMinOffset = Math.max(-mViewRef!!.get()!!.height, -(mViewRef!!.get()!!.height - this.peekHeight))
        }
    private var mMinOffset: Int = 0
    private var mMaxOffset: Int = 0
    var isHideable: Boolean = false
    var skipCollapsed: Boolean = false
    @field:[State] private var mState = STATE_COLLAPSED
    private var mViewDragHelper: androidx.customview.widget.ViewDragHelper? = null
    private var mIgnoreEvents: Boolean = false
    private var mLastNestedScrollDy: Int = 0
    private var mNestedScrolled: Boolean = false
    private var mParentHeight: Int = 0
    private var mViewRef: WeakReference<V>? = null
    private var mNestedScrollingChildRef: WeakReference<View>? = null
    private var mCallback: TopSheetCallback? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mActivePointerId: Int = 0
    private var mInitialY: Int = 0
    private var mTouchingScrollingChild: Boolean = false

    var state: Int
        @State get() = mState
        set(@State state) {
            if (state == mState) return
            if (mViewRef == null) {
                when {
                    state == STATE_COLLAPSED || state == STATE_EXPANDED || isHideable && state == STATE_HIDDEN -> mState = state
                }
                return
            }
            val child = mViewRef!!.get() ?: return
            val top: Int  = when {
                state == STATE_COLLAPSED -> mMinOffset
                state == STATE_EXPANDED -> mMaxOffset
                isHideable && state == STATE_HIDDEN -> -child.height
                else -> 0
            }
            setStateInternal(STATE_SETTLING)

            if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top))
                ViewCompat.postOnAnimation(child, SettleRunnable(child, state))
        }

    private val yVelocity: Float
        get() {
            mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity)
            return VelocityTrackerCompat.getYVelocity(mVelocityTracker!!, mActivePointerId)
        }

    private val mDragCallback = object : androidx.customview.widget.ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            if (mState == STATE_DRAGGING) return false
            if (mTouchingScrollingChild) return false
            if (mState == STATE_EXPANDED && mActivePointerId == pointerId) {
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && ViewCompat.canScrollVertically(scroll, -1)) return false
            }
            return mViewRef != null && mViewRef!!.get() === child
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            dispatchOnSlide(top)
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == androidx.customview.widget.ViewDragHelper.STATE_DRAGGING) setStateInternal(STATE_DRAGGING)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val top: Int
            @State val targetState: Int
            when {
                yvel > 0 -> {
                    top = mMaxOffset
                    targetState = STATE_EXPANDED
                }
                isHideable && shouldHide(releasedChild, yvel) -> {
                    top = -mViewRef!!.get()!!.height
                    targetState = STATE_HIDDEN
                }
                yvel == 0f -> {
                    val currentTop = releasedChild.top
                    if (Math.abs(currentTop - mMinOffset) > Math.abs(currentTop - mMaxOffset)) {
                        top = mMaxOffset
                        targetState = STATE_EXPANDED
                    } else {
                        top = mMinOffset
                        targetState = STATE_COLLAPSED
                    }
                }
                else -> {
                    top = mMinOffset
                    targetState = STATE_COLLAPSED
                }
            }
            if (mViewDragHelper!!.settleCapturedViewAt(releasedChild.left, top)) {
                setStateInternal(STATE_SETTLING)
                ViewCompat.postOnAnimation(releasedChild, SettleRunnable(releasedChild, targetState))
            } else setStateInternal(targetState)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int = constrain(top, if (isHideable) -child.height else mMinOffset, mMaxOffset)

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int = child.left

        override fun getViewVerticalDragRange(child: View): Int = when {
            isHideable -> child.height
            else -> mMaxOffset - mMinOffset
        }
    }


    abstract class TopSheetCallback {
        abstract fun onStateChanged(bottomSheet: View, @State newState: Int)
        abstract fun onSlide(bottomSheet: View, slideOffset: Float)
    }

    @IntDef(STATE_EXPANDED, STATE_COLLAPSED, STATE_DRAGGING, STATE_SETTLING, STATE_HIDDEN)
    @Retention(AnnotationRetention.SOURCE)
    annotation class State


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BottomSheetBehavior_Layout)
        peekHeight = a.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, 0)
        isHideable = a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false)
        skipCollapsed = a.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false)
        a.recycle()
        val configuration = ViewConfiguration.get(context)
        mMaximumVelocity = configuration.scaledMaximumFlingVelocity.toFloat()
    }

    override fun onSaveInstanceState(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V): Parcelable? = SavedState(super.onSaveInstanceState(parent, child)!!, mState)

    override fun onRestoreInstanceState(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(parent, child, ss.superState!!)
        mState = if (ss.state == STATE_DRAGGING || ss.state == STATE_SETTLING) STATE_COLLAPSED else ss.state

    }

    override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) ViewCompat.setFitsSystemWindows(child, true)
        val savedTop = child.top
        parent.onLayoutChild(child, layoutDirection)
        mParentHeight = parent.height
        mMinOffset = Math.max(-child.height, -(child.height - peekHeight))
        mMaxOffset = 0
        when {
            mState == STATE_EXPANDED -> ViewCompat.offsetTopAndBottom(child, mMaxOffset)
            isHideable && mState == STATE_HIDDEN -> ViewCompat.offsetTopAndBottom(child, -child.height)
            mState == STATE_COLLAPSED -> ViewCompat.offsetTopAndBottom(child, mMinOffset)
            mState == STATE_DRAGGING || mState == STATE_SETTLING -> ViewCompat.offsetTopAndBottom(child, savedTop - child.top)
        }
        if (mViewDragHelper == null) mViewDragHelper = androidx.customview.widget.ViewDragHelper.create(parent, mDragCallback)
        mViewRef = WeakReference(child)
        mNestedScrollingChildRef = WeakReference<View>(findScrollingChild(child))
        return true
    }

    override fun onInterceptTouchEvent(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (!child.isShown) return false
        val action = MotionEventCompat.getActionMasked(event)
        if (action == MotionEvent.ACTION_DOWN) reset()
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain()
        mVelocityTracker!!.addMovement(event)
        when (action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mTouchingScrollingChild = false
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                if (mIgnoreEvents) {
                    mIgnoreEvents = false
                    return false
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val initialX = event.x.toInt()
                mInitialY = event.y.toInt()
                val scroll = mNestedScrollingChildRef!!.get()
                if (scroll != null && parent.isPointInChildBounds(scroll, initialX, mInitialY)) {
                    mActivePointerId = event.getPointerId(event.actionIndex)
                    mTouchingScrollingChild = true
                }
                mIgnoreEvents = mActivePointerId == MotionEvent.INVALID_POINTER_ID && !parent.isPointInChildBounds(child, initialX, mInitialY)
            }
        }
        if (!mIgnoreEvents && mViewDragHelper!!.shouldInterceptTouchEvent(event)) return true
       val scroll = mNestedScrollingChildRef!!.get()
        return action == MotionEvent.ACTION_MOVE && scroll != null &&
                !mIgnoreEvents && mState != STATE_DRAGGING &&
                !parent.isPointInChildBounds(scroll, event.x.toInt(), event.y.toInt()) &&
                Math.abs(mInitialY - event.y) > mViewDragHelper!!.touchSlop
    }

    override fun onTouchEvent(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, event: MotionEvent): Boolean {
        if (!child.isShown) return false
        val action = MotionEventCompat.getActionMasked(event)
        if (mState == STATE_DRAGGING && action == MotionEvent.ACTION_DOWN) return true
        if (mViewDragHelper == null) return true
        mViewDragHelper!!.processTouchEvent(event)
        if (action == MotionEvent.ACTION_DOWN) reset()
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain()
        mVelocityTracker!!.addMovement(event)
        if (action == MotionEvent.ACTION_MOVE && !mIgnoreEvents && abs(mInitialY - event.y) > mViewDragHelper!!.touchSlop)
                mViewDragHelper!!.captureChildView(child, event.getPointerId(event.actionIndex))

        return !mIgnoreEvents
    }

    override fun onStartNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
        mLastNestedScrollDy = 0
        mNestedScrolled = false
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, target: View, dx: Int, dy: Int, consumed: IntArray) {
        val scrollingChild = mNestedScrollingChildRef!!.get()
        if (target !== scrollingChild) return
        val currentTop = child.top
        val newTop = currentTop - dy
        if (dy > 0) {
            if (!ViewCompat.canScrollVertically(target, 1)) when {
                newTop >= mMinOffset || isHideable -> {
                    consumed[1] = dy
                    ViewCompat.offsetTopAndBottom(child, -dy)
                    setStateInternal(STATE_DRAGGING)
                }
                else -> {
                    consumed[1] = currentTop - mMinOffset
                    ViewCompat.offsetTopAndBottom(child, -consumed[1])
                    setStateInternal(STATE_COLLAPSED)
                }
            }
        } else if (dy < 0) {
            when {
                newTop < mMaxOffset -> {
                    consumed[1] = dy
                    ViewCompat.offsetTopAndBottom(child, -dy)
                    setStateInternal(STATE_DRAGGING)
                }
                else -> {
                    consumed[1] = currentTop - mMaxOffset
                    ViewCompat.offsetTopAndBottom(child, -consumed[1])
                    setStateInternal(STATE_EXPANDED)
                }
            }
        }
        dispatchOnSlide(child.top)
        mLastNestedScrollDy = dy
        mNestedScrolled = true
    }

    override fun onStopNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, target: View) {
        if (child.top == mMaxOffset) {
            setStateInternal(STATE_EXPANDED)
            return
        }
        if (target !== mNestedScrollingChildRef!!.get() || !mNestedScrolled) return
        val top: Int
        val targetState: Int
        when {
            mLastNestedScrollDy < 0 -> {
                top = mMaxOffset
                targetState = STATE_EXPANDED
            }
            isHideable && shouldHide(child, yVelocity) -> {
                top = -child.height
                targetState = STATE_HIDDEN
            }
            mLastNestedScrollDy == 0 -> {
                val currentTop = child.top
                if (Math.abs(currentTop - mMinOffset) > Math.abs(currentTop - mMaxOffset)) {
                    top = mMaxOffset
                    targetState = STATE_EXPANDED
                } else {
                    top = mMinOffset
                    targetState = STATE_COLLAPSED
                }
            }
            else -> {
                top = mMinOffset
                targetState = STATE_COLLAPSED
            }
        }
        if (mViewDragHelper!!.smoothSlideViewTo(child, child.left, top)) {
            setStateInternal(STATE_SETTLING)
            ViewCompat.postOnAnimation(child, SettleRunnable(child, targetState))
        } else setStateInternal(targetState)
        mNestedScrolled = false
    }

    override fun onNestedPreFling(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: V, target: View, velocityX: Float, velocityY: Float): Boolean {
        return target === mNestedScrollingChildRef!!.get() && (mState != STATE_EXPANDED ||
                super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY))
    }

    fun setTopSheetCallback(callback: TopSheetCallback) {
        mCallback = callback
    }

    private fun setStateInternal(@State state: Int) {
        if (mState == state) return
        mState = state
        val bottomSheet = mViewRef!!.get()
        if (bottomSheet != null && mCallback != null) mCallback!!.onStateChanged(bottomSheet, state)
    }

    private fun reset() {
        mActivePointerId = androidx.customview.widget.ViewDragHelper.INVALID_POINTER
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    private fun shouldHide(child: View, yvel: Float): Boolean {
        if (child.top > mMinOffset) return false
        val newTop = child.top + yvel * HIDE_FRICTION
        return Math.abs(newTop - mMinOffset) / peekHeight.toFloat() > HIDE_THRESHOLD
    }

    private fun findScrollingChild(view: View): View? {
        if (view is NestedScrollingChild) return view
        if (view is ViewGroup) {
            var i = 0
            val count = view.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(view.getChildAt(i))
                if (scrollingChild != null) return scrollingChild
                i++
            }
        }
        return null
    }

    private fun dispatchOnSlide(top: Int) {
        val bottomSheet = mViewRef!!.get()
        if (bottomSheet != null && mCallback != null) when {
            top < mMinOffset -> mCallback!!.onSlide(bottomSheet, (top - mMinOffset).toFloat() / peekHeight)
            else -> mCallback!!.onSlide(bottomSheet, (top - mMinOffset).toFloat() / (mMaxOffset - mMinOffset))
        }
    }

    private inner class SettleRunnable internal constructor(
            private val mView: View,
            @param:[State] @field:[State] private val mTargetState: Int) : Runnable {
        override fun run() {
            when {
                mViewDragHelper != null && mViewDragHelper!!.continueSettling(true) -> ViewCompat.postOnAnimation(mView, this)
                else -> setStateInternal(mTargetState)
            }
        }
    }

    protected class SavedState : androidx.customview.view.AbsSavedState {
        @field:[State] internal val state: Int

        @JvmOverloads
        constructor(source: Parcel, loader: ClassLoader? = null) : super(source, loader) {

            state = source.readInt()
        }

        constructor(superState: Parcelable, @State state: Int) : super(superState) {
            this.state = state
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(state)
        }

        companion object {

            @field:[JvmField] val CREATOR: Parcelable.Creator<SavedState> = ParcelableCompat.newCreator(
                    object : ParcelableCompatCreatorCallbacks<SavedState> {
                        override fun createFromParcel(p0: Parcel?, p1: ClassLoader?): SavedState {
                            return SavedState(p0!!, p1)
                        }

                        override fun newArray(p0: Int): Array<SavedState?> {
                            return arrayOfNulls(p0)
                        }


                    })
        }
    }

    companion object {
        const val STATE_DRAGGING = 1
        const val STATE_SETTLING = 2
        const val STATE_EXPANDED = 3
        const val STATE_COLLAPSED = 4
        const val STATE_HIDDEN = 5

        private const val HIDE_THRESHOLD = 0.5f
        private const val HIDE_FRICTION = 0.1f

        @Suppress("UNCHECKED_CAST")
        fun <V : View> from(view: V): TopSheetBehavior<V> {
            val params = view.layoutParams as? androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
                    ?: throw IllegalArgumentException("The view is not a child of CoordinatorLayout")
            val behavior = params
                    .behavior as? TopSheetBehavior<*> ?: throw IllegalArgumentException(
                    "The view is not associated with TopSheetBehavior")
            return behavior as TopSheetBehavior<V>
        }

        internal fun constrain(amount: Int, low: Int, high: Int): Int = when {
                    amount < low -> low
                    amount > high -> high
                    else -> amount
                }

        internal fun constrain(amount: Float, low: Float, high: Float): Float = when {
                    amount < low -> low
                    amount > high -> high
                    else -> amount
                }
    }
}