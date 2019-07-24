package jdp.pocketlib.widget

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import jdp.pocketlib.R


@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
class TopSheetDialog : AppCompatDialog {
    private var topSheetBehavior: TopSheetBehavior<FrameLayout>? = null
    private val mTopSheetCallback = object : TopSheetBehavior.TopSheetCallback() {
        override fun onStateChanged(topSheet: View, @BottomSheetBehavior.State newState: Int) {
            if (newState == TopSheetBehavior.STATE_HIDDEN) dismiss()
        }
        override fun onSlide(topSheet: View, slideOffset: Float) {
            /*
        DO NOTHING
         */
        }
    }

    constructor(context: Context) : super(context, R.style.Theme_Design_TopSheetDialog) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    constructor(context: Context, @StyleRes theme: Int) : super(context, theme) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(wrapInTopSheet(layoutResId, null, null))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun setContentView(view: View) {
        super.setContentView(wrapInTopSheet(0, view, null))
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(wrapInTopSheet(0, view, params))
    }

    private fun wrapInTopSheet(layoutResId: Int, v: View?, params: ViewGroup.LayoutParams?): View {
        var view = v
        val coordinator = View.inflate(context, R.layout.dialog_top_sheet, null) as androidx.coordinatorlayout.widget.CoordinatorLayout
        if (layoutResId != 0 && view == null)
            view = layoutInflater.inflate(layoutResId, coordinator, false)
        val topSheet = coordinator.findViewById<FrameLayout>(R.id.design_top_sheet)
        topSheetBehavior = TopSheetBehavior.from(topSheet)
        topSheetBehavior!!.setTopSheetCallback(mTopSheetCallback)
        if (params == null) topSheet.addView(view)
        else topSheet.addView(view, params)
        coordinator.findViewById<View>(R.id.top_sheet_touch_outside).setOnClickListener {
            if (isShowing) cancel()
        }
        return coordinator
    }


    override fun show() {
        super.show()
        topSheetBehavior!!.state = TopSheetBehavior.STATE_EXPANDED
    }
}