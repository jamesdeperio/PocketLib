package jdp.pocketlib.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import jdp.pocketlib.R


class Spinner<T> : FrameLayout{

    private var layout: View
    private var isFullScreen:Boolean=false
    lateinit var dialog: SpinnerDialog<T>

    fun setFontFamilyFromAsset(fontPath:String): Spinner<T> {
        val font = Typeface.createFromAsset(resources.assets, fontPath)
        layout.findViewById<TextView>(R.id.textbox).typeface = font
        layout.findViewById<TextView>(R.id.tvHint).typeface = font
        return this
    }

    fun setHint(hint:String): Spinner<T> {
        layout.findViewById<TextView>(R.id.tvHint).text = hint
        return this
    }

    fun setText(text:String): Spinner<T> {
        layout.findViewById<TextView>(R.id.textbox).text = text
        return this
    }
    fun getText():String = layout.findViewById<TextView>(R.id.textbox).text.toString()
    fun setHintColor(color:Int): Spinner<T> {
        layout.findViewById<TextView>(R.id.tvHint).setTextColor(color)
        return this
    }

    fun setHintSize(size:Float): Spinner<T> {
        layout.findViewById<TextView>(R.id.tvHint).textSize = size
        return this
    }

    fun setTextSize(size:Float): Spinner<T> {
        layout.findViewById<TextView>(R.id.textbox).textSize = size
        return this
    }

    fun setTextColor(color:Int): Spinner<T> {
        layout.findViewById<TextView>(R.id.textbox).setTextColor(color)
        return this
    }

    fun setGravity(gravity:Int): Spinner<T> {
        layout.findViewById<TextView>(R.id.textbox).gravity = gravity
        return this
    }
    fun setBottomLineColor(color:Int): Spinner<T> {
        layout.findViewById<FrameLayout>(R.id.bottomLine).setBackgroundColor(color)
        return this
    }

    fun setHintGravity(gravity:Int): Spinner<T> {
        layout.findViewById<TextView>(R.id.tvHint).gravity = gravity
        return this
    }

    constructor(context: Context, isFullScreen:Boolean): super(context) {
        this.isFullScreen=isFullScreen
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)    setup(null,context)
    }
    constructor(context: Context): super(context) {
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)    setup(null, context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)  setup(attrs, context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)   setup(attrs, context)
    }

    fun addItem (items:MutableMap<T,String>): Spinner<T> {
        dialog.addItem(items)
        if (dialog.adapter.itemList.size==1) setText(items.values.toMutableList()[0])
        return this
    }

    fun addItem (items:MutableList<T>): Spinner<T> {
        dialog.addItem(items)
        if (dialog.adapter.itemList.size==1) setText(items[0].toString())
        return this
    }

    fun addItem (itemObject:MutableList<T>,itemString:MutableList<String>): Spinner<T> {
        dialog.addItem(itemObject,itemString)
        if (dialog.adapter.itemList.size==1) setText(itemString[0])
        return this
    }

    fun addItem (itemObject:T,itemString:String): Spinner<T> {
        dialog.addItem(itemObject,itemString)
        if (dialog.adapter.itemList.size==1) setText(itemString)
        return this
    }

    fun addItem (itemString:T): Spinner<T> {
        dialog.addItem(itemString)
        if (dialog.adapter.itemList.size==1) setText(itemString.toString())
        return this
    }
    private fun setup(attrs: AttributeSet?, context: Context) {
        if (attrs != null) {
            dialog = SpinnerDialog(context,isFullScreen)
            dialog.setOnItemSelectedListener { _, selectedItem, _ ->
                setText(selectedItem)
            }
            val attrib = context.obtainStyledAttributes(attrs, R.styleable.Spinner)
            val  f= attrib.getString(R.styleable.Spinner_spinner_font)?: ""
            var font:Typeface? = null
            if (f.isNotEmpty()) font=Typeface.createFromAsset(resources.assets, f)
            layout.findViewById<FrameLayout>(R.id.bottomLine).apply {
                this.setBackgroundColor(attrib.getColor(R.styleable.Spinner_spinner_bottomline_color, Color.BLACK))
                this.setOnClickListener { dialog.show() }
            }
            layout.findViewById<TextView>(R.id.tvHint).apply {
                this.setTextColor(attrib.getColor(R.styleable.Spinner_spinner_hint_color, Color.BLUE))
                this.text = attrib.getString(R.styleable.Spinner_spinner_hint)?: ""
                this.textSize = attrib.getInt(R.styleable.Spinner_spinner_hint_size,15).toFloat()
                this.gravity = when (attrib.getInt(R.styleable.Spinner_spinner_hint_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
                this.setOnClickListener { dialog.show() }
                if (font!=null) this.typeface=font
            }
            layout.findViewById<TextView>(R.id.textbox).apply {
                this.text = attrib.getString(R.styleable.Spinner_spinner_text)?: ""
                this.textSize = attrib.getInt(R.styleable.Spinner_spinner_text_size,15).toFloat()
                this.setTextColor( attrib.getColor(R.styleable.Spinner_spinner_font_color, Color.BLACK))
                this.gravity = when (attrib.getInt(R.styleable.Spinner_spinner_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
                this.setOnClickListener { dialog.show() }
                if (font!=null) this.typeface=font
            }
            setBackgroundColor(attrib.getColor(R.styleable.Spinner_spinner_background_color, Color.WHITE))
            attrib.recycle()
        }
      }
}
