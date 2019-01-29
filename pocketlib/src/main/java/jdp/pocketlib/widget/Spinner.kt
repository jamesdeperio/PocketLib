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


class Spinner : FrameLayout {

    private var layout: View

    fun setFontFamilyFromAsset(fontPath:String): Spinner {
        val font = Typeface.createFromAsset(resources.assets, fontPath)
        layout.findViewById<TextView>(R.id.textbox).typeface = font
        layout.findViewById<TextView>(R.id.tvHint).typeface = font
        return this
    }

    fun setHint(hint:String): Spinner {
        layout.findViewById<TextView>(R.id.tvHint).text = hint
        return this
    }

    fun setText(text:String): Spinner {
        layout.findViewById<TextView>(R.id.textbox).text = text
        return this
    }
    fun setHintColor(color:Int): Spinner {
        layout.findViewById<TextView>(R.id.tvHint).setTextColor(color)
        return this
    }

    fun setHintSize(size:Float): Spinner {
        layout.findViewById<TextView>(R.id.tvHint).textSize = size
        return this
    }

    fun setTextSize(size:Float): Spinner {
        layout.findViewById<TextView>(R.id.textbox).textSize = size
        return this
    }

    fun setTextColor(color:Int): Spinner {
        layout.findViewById<TextView>(R.id.textbox).setTextColor(color)
        return this
    }

    fun setGravity(gravity:Int): Spinner {
        layout.findViewById<TextView>(R.id.textbox).gravity = gravity
        return this
    }
    fun setBottomLineColor(color:Int): Spinner {
        layout.findViewById<FrameLayout>(R.id.bottomLine).setBackgroundColor(color)
        return this
    }

    fun setHintGravity(gravity:Int): Spinner {
        layout.findViewById<TextView>(R.id.tvHint).gravity = gravity
        return this
    }

    constructor(context: Context): super(context) {
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)    setup(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)  setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_spinner, this)
        if (!isInEditMode)   setup(attrs)
    }


    private fun setup(attrs: AttributeSet?) {
        if (attrs != null) {
            val attrib = context.obtainStyledAttributes(attrs, R.styleable.Spinner)
            layout.findViewById<FrameLayout>(R.id.bottomLine).apply {
                this.setBackgroundColor(attrib.getColor(R.styleable.Spinner_pkt_bottomline_color, Color.BLACK))
            }
            layout.findViewById<TextView>(R.id.tvHint).apply {
                this.setTextColor(attrib.getColor(R.styleable.Spinner_pkt_hint_color, Color.BLUE))
                this.text = attrib.getString(R.styleable.Spinner_pkt_hint)?: ""
                this.textSize = attrib.getInt(R.styleable.Spinner_pkt_hint_size,15).toFloat()
                this.gravity = when (attrib.getInt(R.styleable.Spinner_pkt_hint_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
            }
            layout.findViewById<TextView>(R.id.textbox).apply {
                this.text = attrib.getString(R.styleable.Spinner_pkt_text)?: ""
                this.textSize = attrib.getInt(R.styleable.Spinner_pkt_text_size,15).toFloat()
                this.setTextColor( attrib.getColor(R.styleable.Spinner_pkt_font_color, Color.BLACK))
                this.gravity = when (attrib.getInt(R.styleable.Spinner_pkt_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
            }
            setBackgroundColor(attrib.getColor(R.styleable.Spinner_pkt_background_color, Color.WHITE))
            attrib.recycle()
        }
      }
}
