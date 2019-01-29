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
internal interface PocketAdapterContract {
    interface Adapter {
        fun viewTypeCondition(position: Int): Int
        fun addViewHolder(viewHolder: PocketViewHolder)
    }
    interface Holder {
        fun setContentView(layoutID: Int)
        fun setView(view: View)
        fun onBindViewHolder(view: View, position: Int)
    }
}
