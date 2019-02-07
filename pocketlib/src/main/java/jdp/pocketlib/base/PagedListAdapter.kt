package jdp.pocketlib.base

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup


abstract class PagedListAdapter: HasAdapterContract.Adapter {

    private val pocketViewHolderList = ArrayList<ViewHolder>()
    private var selectedLayout: Int = 0
    private val adapter= BasePagedAdapter()

    fun  getDelegate(): BasePagedAdapter = adapter

    override fun viewTypeCondition(position: Int): Int = 0

    override fun addViewHolder(viewHolder: ViewHolder) {
        pocketViewHolderList.add(viewHolder)
    }

    abstract fun isContentListTheSame(): Boolean
    abstract fun isItemListTheSame(): Boolean
    abstract fun getItemCount(): Int

    inner class BasePagedAdapter : PagedListAdapter<Any, RecyclerView.ViewHolder> {
        constructor() : super(object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(o: Any, t1: Any): Boolean = isItemListTheSame()
            override fun areContentsTheSame(o: Any, t1: Any): Boolean = isContentListTheSame()
        })

        override fun getItemViewType(position: Int): Int {
            selectedLayout = this@PagedListAdapter.viewTypeCondition(position)
            return selectedLayout
        }
         constructor(diffCallback: DiffUtil.ItemCallback<Any>) : super(diffCallback)

         constructor(config: AsyncDifferConfig<Any>) : super(config)

         override fun getItemCount(): Int  = this@PagedListAdapter.getItemCount()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            (0 until pocketViewHolderList.size)
                    .filter {  viewType==it }
                    .forEach {
                        pocketViewHolderList[it].setView(LayoutInflater.from(parent.context).inflate(pocketViewHolderList[it].layout, parent, false))
                        return pocketViewHolderList[it].viewHolder
                    }
            return pocketViewHolderList[0].viewHolder
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
                = pocketViewHolderList[selectedLayout].onBindViewHolder(holder.itemView,position)
    }
}


