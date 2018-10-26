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
import android.widget.*
import jdp.pocketlib.R


class PocketSpinner : FrameLayout {

    private var layout: View

    fun setFontFamilyFromAsset(fontPath:String): PocketSpinner {
        val font = Typeface.createFromAsset(resources.assets, fontPath)
        layout.findViewById<Button>(R.id.textbox).typeface = font
        layout.findViewById<TextView>(R.id.tvHint).typeface = font
        return this
    }

    fun setHint(hint:String): PocketSpinner {
        layout.findViewById<TextView>(R.id.tvHint).text = hint
        return this
    }

    fun setText(text:String): PocketSpinner {
        layout.findViewById<Button>(R.id.textbox).hint = text
        return this
    }
    fun setHintColor(color:Int): PocketSpinner {
        layout.findViewById<TextView>(R.id.tvHint).setTextColor(color)
        return this
    }

    fun setHintSize(size:Float): PocketSpinner {
        layout.findViewById<TextView>(R.id.tvHint).textSize = size
        return this
    }

    fun setTextSize(size:Float): PocketSpinner {
        layout.findViewById<Button>(R.id.textbox).textSize = size
        return this
    }

    fun setTextColor(color:Int): PocketSpinner {
        layout.findViewById<Button>(R.id.textbox).setTextColor(color)
        return this
    }

    fun setGravity(gravity:Int): PocketSpinner {
        layout.findViewById<Button>(R.id.textbox).gravity = gravity
        return this
    }

    fun setHintGravity(gravity:Int): PocketSpinner {
        layout.findViewById<TextView>(R.id.tvHint).gravity = gravity
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
        if (attrs != null) {
            val attrib = context.obtainStyledAttributes(attrs, R.styleable.PocketSpinner)
            layout.findViewById<TextView>(R.id.tvHint).apply {
                this.setTextColor(attrib.getColor(R.styleable.PocketSpinner_pkt_hint_color, Color.BLUE))
                this.text = attrib.getString(R.styleable.PocketSpinner_pkt_hint)?: ""
                this.textSize = attrib.getInt(R.styleable.PocketSpinner_pkt_hint_size,15).toFloat()
                this.gravity = when (attrib.getInt(R.styleable.PocketSpinner_pkt_hint_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
            }
            layout.findViewById<Button>(R.id.textbox).apply {
                this.text = attrib.getString(R.styleable.PocketSpinner_pkt_text)?: ""
                this.textSize = attrib.getInt(R.styleable.PocketSpinner_pkt_text_size,15).toFloat()
                this.setTextColor( attrib.getColor(R.styleable.PocketSpinner_pkt_font_color, Color.BLACK))
                this.gravity = when (attrib.getInt(R.styleable.PocketSpinner_pkt_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
            }
            setBackgroundColor(attrib.getColor(R.styleable.PocketSpinner_pkt_background_color, Color.WHITE))
            attrib.recycle()
        }
      }
}
