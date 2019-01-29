/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by jamesdeperio on 8/26/2017
 *  jamesdeperio.github.com.codepocket.view
 */

class PocketViewPager : ViewPager {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean = try {
        super.onTouchEvent(ev)
    } catch (ex: IllegalArgumentException) {
        ex.printStackTrace()
        false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean = try {
        super.onInterceptTouchEvent(ev)
    } catch (ex: IllegalArgumentException) {
        ex.printStackTrace()
        false
    }
}
