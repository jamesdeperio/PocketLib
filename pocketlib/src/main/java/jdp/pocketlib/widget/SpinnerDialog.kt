package jdp.pocketlib.widget

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.support.v7.widget.SearchView
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import jdp.pocketlib.R
import jdp.pocketlib.base.Adapter
import jdp.pocketlib.base.ViewHolder
import jdp.pocketlib.layoutmanager.LinearLayoutManager
import android.app.Dialog as AndroidDialog
class SpinnerDialog<T>(context: Context, private var isFullScreen:Boolean=false) : SearchView.OnQueryTextListener, DialogInterface.OnDismissListener {
    private val dialog=AndroidDialog(context).apply {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (isFullScreen) {
            this.window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN -> window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                else -> window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

            }
             this.setOnDismissListener {
                if (isFullScreen) this.window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            }
        }
        this.window!!.attributes.windowAnimations = R.style.DialogAnimation
        this.setContentView(R.layout.dialog_spinner)
        this.setOnDismissListener(this@SpinnerDialog)
    }
    val title = dialog.findViewById<TextView>(R.id.tvTitle)!!
    val closeButton = dialog.findViewById<Button>(R.id.btnClose)!!.apply {
        this.setOnClickListener { dialog.dismiss() }
    }
    val topSeparator = dialog.findViewById<View>(R.id.topSeparator)!!
    val bottomSeparator = dialog.findViewById<View>(R.id.bottomSeparator)!!
    val view = dialog.findViewById<View>(R.id.container)!!
    private val searchView = dialog.findViewById<SearchView>(R.id.searchView)!!.apply {
        this.setOnQueryTextListener(this@SpinnerDialog)
    }
    private val adapter = SpinnerAdapter().apply {
        val viewHolder= SpinnerViewHolder(this)
        viewHolder.setContentView(R.layout.item_list_spinner)
        this.addViewHolder(viewHolder)
    }
    private val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)!!.apply {
        this.adapter=this@SpinnerDialog.adapter
        this.layoutManager=LinearLayoutManager(context,LinearLayout.VERTICAL,false)
    }
    var selectedObject:T? = null
    var selectedItem:String=""
    var selectedIndex=-1
    var itemBackgroundColor="#ffffff"
    var itemTextColor="#1d1d1d"
    private var listener: Listener<T> = object :Listener<T> {
        override fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int) {
        }
    }

    fun setOnItemSelectedListener(listener: Listener<T>) {
        this.listener=listener
    }

    fun setOnItemSelectedListener(onItemSelectedListener:(selectedObject: T, selectedItem: String, selectedIndex: Int)-> Unit) {
        this.listener=object :Listener<T>{
            override fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int) {
                onItemSelectedListener(selectedObject,selectedItem,selectedIndex)
            }
        }
    }


    override fun onDismiss(p0: DialogInterface?) {
        searchView.setQuery("",false)
    }

    fun show(): SpinnerDialog<T> {
        dialog.show()
        if (isFullScreen){
            dialog.window!!.decorView.systemUiVisibility = dialog.window!!.decorView.systemUiVisibility
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        return this
    }

    fun dismiss(): SpinnerDialog<T> {
        dialog.dismiss()
        return this
    }

    fun addItem (items:MutableMap<T,String>): SpinnerDialog<T> {
        items.forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.key,itemString = it.value )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        if (adapter.itemList.size==1) {
            selectedIndex=0
            selectedItem=adapter.itemList[0]!!.itemString!!
        }
        return this
    }

    fun addItem (items:MutableList<T>): SpinnerDialog<T> {
        items.withIndex().forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.value ,itemString = it.value.toString() )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        if (adapter.itemList.size==1) {
            selectedIndex=0
            selectedItem=adapter.itemList[0]!!.itemString!!
        }
        return this
    }

    fun addItem (itemObject:MutableList<T>,itemString:MutableList<String>): SpinnerDialog<T> {
        if (itemObject.size!=itemString.size) throw RuntimeException("objectlist and itemlist did not match!")
        itemObject.withIndex().forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.value ,itemString = itemString[it.index] )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        if (adapter.itemList.size==1) {
            selectedIndex=0
            selectedItem=adapter.itemList[0]!!.itemString!!
        }
        return this
    }

    fun addItem (itemObject:T,itemString:String): SpinnerDialog<T> {
        adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = itemObject ,itemString = itemString)
        adapter.searchItemList=adapter.itemList
        adapter.notifyItemInserted(adapter.itemCount)
        if (adapter.itemList.size==1) {
            selectedIndex=0
            selectedItem=adapter.itemList[0]!!.itemString!!
        }
        return this
    }

    fun addItem (itemString:T): SpinnerDialog<T> {
        adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = itemString ,itemString = itemString as String)
        adapter.searchItemList=adapter.itemList
        adapter.notifyItemInserted(adapter.itemCount)
        if (adapter.itemList.size==1) {
            selectedIndex=0
            selectedItem=adapter.itemList[0]!!.itemString!!
        }
        return this
    }

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText!!.isNotEmpty()){
            val filteredItemList:MutableMap<Int, PocketSpinnerItem<T>> = adapter.filter(newText.toLowerCase())
            adapter.searchItemList=filteredItemList
        }else adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        return true
    }


    data class PocketSpinnerItem <T> (var item:T? = null, var itemString:String? = null)
    inner class SpinnerAdapter: Adapter()  {
        var itemList:MutableMap<Int, PocketSpinnerItem<T>> = HashMap()
        var searchItemList:MutableMap<Int, PocketSpinnerItem<T>> = HashMap()
        override fun getItemCount(): Int = searchItemList.size

        fun filter(newText: String):MutableMap<Int,PocketSpinnerItem<T>> = HashMap<Int, PocketSpinnerItem<T>>().apply {
               var x=0
                itemList.filter { it.value.itemString!!.toLowerCase().contains(newText) }
                    .forEach {
                        this[x]=it.value
                        x++
                    }
            }
    }
    inner class SpinnerViewHolder(private val adapter: SpinnerAdapter) : ViewHolder() {
        override fun onBindViewHolder(view: View, position: Int) {
            view.findViewById<TextView>(R.id.tvItem).apply {
                this.text=adapter.searchItemList[position]!!.itemString
                this.setTextColor(Color.parseColor(itemTextColor))
            }
            view.findViewById<View>(R.id.itemContainer).apply {
                this.setBackgroundColor(Color.parseColor(itemBackgroundColor))
                this.setOnClickListener {
                    selectedItem=adapter.searchItemList[position]!!.itemString!!
                    selectedObject=adapter.searchItemList[position]!!.item!!
                    selectedIndex=position
                    listener.onItemSelected(selectedObject!!,selectedItem,selectedIndex)
                    dialog.dismiss()
                }
            }
        }

    }
    companion object Properties {
        const val NO_SELECTED_ITEM=-1
    }
    interface Listener<T> {
        fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int)
    }
}
