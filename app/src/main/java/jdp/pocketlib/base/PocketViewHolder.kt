/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.base

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by jamesdeperio on 7/7/2017
 *  jamesdeperio.github.com.codepocket.adapter
 */
abstract class PocketViewHolder : PocketAdapterContract.Holder {
    lateinit var viewHolder: RecyclerView.ViewHolder

    private inner class Holder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var bindViewHolder : (rootView: View) -> Unit
    override fun onCreateView(view: View, position: Int) {
        bindViewHolder(view)
        onCreateViewHolder(view, position)
    }

    override fun setView(view: View) {
        viewHolder = Holder(view)
    }

    private var layout: Int = 0
    override fun getContentView(): Int = layout
    override fun setContentView(layoutID: Int) {
        layout = layoutID
    }
}
