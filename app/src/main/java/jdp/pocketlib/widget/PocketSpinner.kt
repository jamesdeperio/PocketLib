package jdp.pocketlib.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import jdp.pocketlib.R


class PocketSpinner : FrameLayout {
    private var hint:String? ="Hint"
    private var text:String? =""
    private var textColor:Int =Color.BLACK
    private var hintColor:Int =Color.BLACK
    private var bgColor:Int =Color.BLACK
    private var gravity:Int =Gravity.START
    private var layout: View

    fun setFontFamilyFromAsset(fontPath:String): PocketSpinner {
        val font = Typeface.createFromAsset(resources.assets, fontPath)
        layout.findViewById<EditText>(R.id.textbox).typeface = font
        return this
    }

    fun setHint(hint:String): PocketSpinner {
        layout.findViewById<EditText>(R.id.textbox).hint = hint
        return this
    }

    fun setText(text:String): PocketSpinner {
        layout.findViewById<EditText>(R.id.textbox).hint = text
        return this
    }
    fun setHintColor(color:Int): PocketSpinner {
        layout.findViewById<EditText>(R.id.textbox).setHintTextColor(color)
        return this
    }
    fun setTextColor(color:Int): PocketSpinner {
        layout.findViewById<EditText>(R.id.textbox).setTextColor(color)
        return this
    }

    fun setGravity(gravity:Int): PocketSpinner {
        layout.findViewById<EditText>(R.id.textbox).gravity = gravity
        return this
    }

    constructor(context: Context): super(context) {
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_pocketspinner, this)
        if (!isInEditMode)    setup(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_pocketspinner, this)
        if (!isInEditMode)  setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_pocketspinner, this)
        if (!isInEditMode)   setup(attrs)
    }


    private fun setup(attrs: AttributeSet?) {
        if (attrs != null){
            val attrib = context.obtainStyledAttributes(attrs, R.styleable.PocketSpinner)
            hint = attrib.getString(R.styleable.PocketSpinner_pkt_hint)
            text = attrib.getString(R.styleable.PocketSpinner_pkt_text)
            textColor = attrib.getColor(R.styleable.PocketSpinner_pkt_font_color,Color.BLACK)
            bgColor = attrib.getColor(R.styleable.PocketSpinner_pkt_background_color,Color.WHITE)
            hintColor = attrib.getColor(R.styleable.PocketSpinner_pkt_hint_color,Color.BLUE)
            gravity = attrib.getInt(R.styleable.PocketSpinner_pkt_gravity,0)
            attrib.recycle()
        }
        layout.findViewById<EditText>(R.id.textbox).apply {
            this.setText(this@PocketSpinner.text?:"")
            this.setHintTextColor(hintColor)
            this.setTextColor(textColor)
            this.gravity = when(this@PocketSpinner.gravity){
                0-> Gravity.START
                1-> Gravity.END
                2-> Gravity.CENTER
                else -> Gravity.START
            }
            this.hint=this@PocketSpinner.hint?:""
        }

        setBackgroundColor(this@PocketSpinner.bgColor)

        layout.findViewById<ImageView>(R.id.textboxImg).apply {
            this.setBackgroundColor(this@PocketSpinner.bgColor)
        }
      }
}
