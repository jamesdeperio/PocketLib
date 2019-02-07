/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.base

import android.view.View

/**
 * Created by jamesdeperio on 7/7/2017
 *  jamesdeperio.github.com.codepocket.adapter
 */
internal interface HasAdapterContract {
    interface Adapter {
        fun viewTypeCondition(position: Int): Int
        fun addViewHolder(viewHolder: ViewHolder)
    }
    interface PagedListAdapter {
        fun notifyDataSetChanged()
        fun notifyItemChanged(position: Int)
        fun notifyItemChanged(position: Int,payLoad: Any?)
        fun notifyItemInserted(position: Int)
        fun notifyItemMoved(fromPosition: Int, toPosition:Int)
        fun notifyItemRangeChanged(positionStart: Int, itemCount:Int)
        fun notifyItemRangeChanged(positionStart: Int, itemCount:Int, payLoad:Any?)
        fun notifyItemRangeInserted(positionStart: Int, itemCount:Int)
        fun notifyItemRemoved(position:Int)
        fun getInstance(): jdp.pocketlib.base.PagedListAdapter.BasePagedAdapter
    }
    interface Holder {
        fun setContentView(layoutID: Int)
        fun setView(view: View)
        fun onBindViewHolder(view: View, position: Int)
    }
}
