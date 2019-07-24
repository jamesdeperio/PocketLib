/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.layoutmanager

import android.content.Context
import android.util.Log

/**
 * Created by jamesdeperio on 8/26/2017
 *  jamesdeperio.github.com.codepocket.view
 */
class LinearLayoutManager(context: Context, horizontal: Int, b: Boolean) : androidx.recyclerview.widget.LinearLayoutManager(context, horizontal, b) {
    var isScrollEnabled = true

    override fun canScrollVertically(): Boolean = isScrollEnabled && super.canScrollVertically()

    override fun canScrollHorizontally(): Boolean = isScrollEnabled && super.canScrollHorizontally()

    override fun supportsPredictiveItemAnimations(): Boolean = false

    override fun onLayoutChildren(recycler: androidx.recyclerview.widget.RecyclerView.Recycler?, state: androidx.recyclerview.widget.RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("LinearLayoutManager", "catch animation error ${e.message}")
        }
    }
}
