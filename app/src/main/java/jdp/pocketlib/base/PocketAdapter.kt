/*
 * jamesdeperio/PocketLib is licensed under the
 *
 * Apache License 2.0
 * A permissive license whose main conditions require preservation of copyright and license notices. Contributors provide an express grant of patent rights. Licensed works, modifications, and larger works may be distributed under different terms and without source code.
 */

package jdp.pocketlib.base

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by jamesdeperio on 7/7/2017
 *  jamesdeperio.github.com.codepocket.adapter
 */
abstract class PocketAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), PocketAdapterContract.Adapter {
    private val pocketViewHolderList = ArrayList<PocketViewHolder>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        (0 until pocketViewHolderList.size)
                .filter {  viewType==it }
                .forEach {
                    pocketViewHolderList[it].setView(LayoutInflater.from(parent.context).inflate(pocketViewHolderList[it].getContentView(), parent, false))
                    return pocketViewHolderList[it].viewHolder
                }
        return pocketViewHolderList[0].viewHolder
    }

    private var selectedLayout: Int = 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
            = pocketViewHolderList[selectedLayout].onCreateViewHolder(holder.itemView,position)

    override fun getItemViewType(position: Int): Int {
        selectedLayout = viewTypeCondition(position)
        return selectedLayout
    }

    override fun viewTypeCondition(position: Int): Int = 0

    override fun addViewHolder(viewHolder: PocketViewHolder) {
        pocketViewHolderList.add(viewHolder)
    }


}

