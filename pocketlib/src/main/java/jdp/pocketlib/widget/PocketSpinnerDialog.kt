package jdp.pocketlib.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.SearchView
import android.text.InputType
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import jdp.pocketlib.R
import jdp.pocketlib.base.PocketAdapter
import jdp.pocketlib.base.PocketViewHolder
import jdp.pocketlib.layoutmanager.PocketLinearLayout
class PocketSpinnerDialog<T>(context: Context, private var isFullScreen:Boolean=false) : SearchView.OnQueryTextListener, DialogInterface.OnDismissListener {
    private val dialog=Dialog(context).apply {
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
        this.setOnDismissListener(this@PocketSpinnerDialog)
    }
    val title = dialog.findViewById<TextView>(R.id.tvTitle)!!
    val closeButton = dialog.findViewById<Button>(R.id.btnClose)!!.apply {
        this.setOnClickListener { dialog.dismiss() }
    }
    val topSeparator = dialog.findViewById<View>(R.id.topSeparator)!!
    val bottomSeparator = dialog.findViewById<View>(R.id.bottomSeparator)!!
    val view = dialog.findViewById<View>(R.id.container)!!
    private val searchView = dialog.findViewById<SearchView>(R.id.searchView)!!.apply {
        this.setOnQueryTextListener(this@PocketSpinnerDialog)
    }
    private val adapter = PocketSpinnerAdapter().apply {
        val viewHolder= PocketSpinnerViewHolder(this)
        viewHolder.setContentView(R.layout.item_list_spinner)
        this.addViewHolder(viewHolder)
    }
    private val recyclerView = dialog.findViewById<PocketRecyclerView>(R.id.recyclerView)!!.apply {
        this.adapter=this@PocketSpinnerDialog.adapter
        this.layoutManager=PocketLinearLayout(context,LinearLayout.VERTICAL,false)
    }
    var selectedObject:T? = null
    var selectedItem:String=""
    var selectedIndex=-1
    var itemBackgroundColor="#ffffff"
    var itemTextColor="#1d1d1d"
   private var button:Button?= null
   private var textInputEditText:TextInputEditText?= null
   private var pocketSpinner:PocketSpinner?= null
    fun setSpinnerView(pocketSpinner:PocketSpinner){
        this.pocketSpinner=pocketSpinner
        this.pocketSpinner!!.findViewById<TextView>(R.id.textbox).setOnClickListener { show() }
    }
    fun setSpinnerView(button:Button){
        this.button=button
        this.button!!.setOnClickListener { show() }
    }
    fun setSpinnerView(textInputEditText:TextInputEditText){
        this.textInputEditText=textInputEditText
        this.textInputEditText!!.isClickable=true
        this.textInputEditText!!.isFocusable = false
        this.textInputEditText!!.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        this.textInputEditText!!.setOnClickListener { show() }
    }

    private var listener: Listener<T> = object :Listener<T> {
        override fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int,spinner: View?) {
            button?.text = selectedItem
            textInputEditText?.setText(selectedItem)
            pocketSpinner?.setText(selectedItem)
        }
    }

    fun setOnItemSelectedListener(listener: Listener<T>) {
        this.listener=listener
    }

    fun setOnItemSelectedListener(onItemSelectedListener:(selectedObject: T, selectedItem: String, selectedIndex: Int, spinner: View?)-> Unit) {
        this.listener=object :Listener<T>{
            override fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int, spinner: View?) {
                button?.text = selectedItem
                textInputEditText?.setText(selectedItem)
                pocketSpinner?.setText(selectedItem)
                onItemSelectedListener(selectedObject,selectedItem,selectedIndex,spinner)
            }
        }
    }


    override fun onDismiss(p0: DialogInterface?) {
        searchView.setQuery("",false)
    }

    fun show(): PocketSpinnerDialog<T> {
        dialog.show()
        if (isFullScreen){
            dialog.window!!.decorView.systemUiVisibility = dialog.window!!.decorView.systemUiVisibility
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        return this
    }

    fun dismiss(): PocketSpinnerDialog<T> {
        dialog.dismiss()
        return this
    }

    fun addItem (items:MutableMap<T,String>): PocketSpinnerDialog<T> {
        items.forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.key,itemString = it.value )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        return this
    }

    fun addItem (items:MutableList<T>): PocketSpinnerDialog<T> {
        items.withIndex().forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.value ,itemString = it.value.toString() )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        return this
    }

    fun addItem (itemObject:MutableList<T>,itemString:MutableList<String>): PocketSpinnerDialog<T> {
        if (itemObject.size!=itemString.size) throw RuntimeException("objectlist and itemlist did not match!")
        itemObject.withIndex().forEach {
            adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = it.value ,itemString = itemString[it.index] )
        }
        adapter.searchItemList=adapter.itemList
        adapter.notifyDataSetChanged()
        return this
    }

    fun addItem (itemObject:T,itemString:String): PocketSpinnerDialog<T> {
        adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = itemObject ,itemString = itemString)
        adapter.searchItemList=adapter.itemList
        adapter.notifyItemInserted(adapter.itemCount)
        return this
    }

    fun addItem (itemString:T): PocketSpinnerDialog<T> {
        adapter.itemList[adapter.itemList.size]= PocketSpinnerItem(item = itemString ,itemString = itemString as String)
        adapter.searchItemList=adapter.itemList
        adapter.notifyItemInserted(adapter.itemCount)
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
    inner class PocketSpinnerAdapter: PocketAdapter()  {
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
    inner class PocketSpinnerViewHolder(private val adapter: PocketSpinnerAdapter) : PocketViewHolder() {
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
                    listener.onItemSelected(selectedObject!!,selectedItem,selectedIndex,pocketSpinner?:button?:textInputEditText)
                    dialog.dismiss()
                }
            }
        }

    }
    companion object Properties {
        val NO_SELECTED_ITEM=-1
    }
    interface Listener<T> {
        fun onItemSelected(selectedObject: T, selectedItem: String, selectedIndex: Int,spinner: View?)
    }
}
