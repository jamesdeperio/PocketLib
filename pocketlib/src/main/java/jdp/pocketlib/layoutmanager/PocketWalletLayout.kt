package jdp.pocketlib.layoutmanager
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import jdp.pocketlib.R


class PocketWalletLayout: RecyclerView.LayoutManager() {
    var isScrollEnabled = true

    private val addedChildren: List<View>
        get() = (0 until childCount).map { getChildAt(it) ?: throw NullPointerException() }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
            RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT)

    override fun isAutoMeasureEnabled(): Boolean = true

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        try {
            when {
                state.itemCount == 0 -> return
                else -> {
                    detachAndScrapAttachedViews(recycler)
                    (0 until state.itemCount).forEach { i ->
                        val view = recycler.getViewForPosition(i)
                        measureChild(view, 0, 0)
                        addView(view)
                        val layoutParams: RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams
                        val left: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) layoutParams.marginStart else layoutParams.leftMargin
                        val top: Int = (view.measuredHeight * i * 0.35).toInt()
                        val right: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) view.measuredWidth + layoutParams.marginEnd else view.measuredWidth + layoutParams.rightMargin
                        val bottom: Int = top + view.measuredHeight
                        layoutDecorated(view, left, top, right, bottom)
                        view.setTag(InitializedPosition.TOP.key, top)
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            Log.e("GridLayoutManager", "catch animation error ${e.message}")
        }

    }


    override fun canScrollVertically(): Boolean = isScrollEnabled && super.canScrollVertically()

    override fun canScrollHorizontally(): Boolean = isScrollEnabled && super.canScrollHorizontally()


    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int = dy.also { deltaY ->
        if (childCount == 0) return@also
        addedChildren.forEachIndexed { index, view ->
            val initializedTop:Int = view.getTag(InitializedPosition.TOP.key) as Int
            val layoutParams:RecyclerView.LayoutParams = view.layoutParams as RecyclerView.LayoutParams
            val left:Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) layoutParams.marginStart else layoutParams.leftMargin
            val top:Int = Math.min(Math.max((view.top - deltaY), index * 30), initializedTop)
            val right:Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) view.measuredWidth + layoutParams.marginEnd else view.measuredWidth +layoutParams.rightMargin
            val bottom:Int = top + view.measuredHeight
            layoutDecorated(view, left, top, right, bottom)
        }
    }

    private enum class InitializedPosition(val key: Int) {
        TOP(R.integer.top)
    }
}