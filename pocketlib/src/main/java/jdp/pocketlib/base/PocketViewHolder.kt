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
    var layout: Int = 0
    override fun setView(view: View) {
        viewHolder =  Holder(view)
    }
    override fun setContentView(layoutID: Int) {
        layout = layoutID
    }
    inner class Holder(view: View):  RecyclerView.ViewHolder(view)
}
