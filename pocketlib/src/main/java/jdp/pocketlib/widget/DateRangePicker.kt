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
import java.util.*


class DateRangePicker : FrameLayout{

    private var layout: View
    var errorDialog= Dialog(context = context,type = Dialog.Type.DIALOG_ERROR)
    var errorMessage:String ="Start Date should be greater than End Date"
    private var isFullScreen:Boolean=false
    lateinit var fragmentManager: androidx.fragment.app.FragmentManager
    var dialog: DateRangePickerFragmentDialog = DateRangePickerFragmentDialog.newInstance()
    private var listener: OnDateRangeSelectedListener? = null
    private var invalidListener: OnDateRangeInvalidListener? = null

    fun setOnDateRangeSelectedListener(task:(start:Calendar, end:Calendar)-> Unit) {
        listener = object : OnDateRangeSelectedListener {
            override fun onDateRangeSelected(start: Calendar, end: Calendar) {
                task(start, end)
            }
        }
    }

    fun setOnDateRangeInvalidListener(task:()-> Unit) {
        invalidListener = object : OnDateRangeInvalidListener {
            override fun onInvalidDateRange() {
                task()
            }
        }
    }
    interface OnDateRangeSelectedListener {
        fun onDateRangeSelected(start: Calendar, end: Calendar)
     }
    interface OnDateRangeInvalidListener {
        fun onInvalidDateRange()
    }
    fun setFontFamilyFromAsset(fontPath:String): DateRangePicker {
        val font = Typeface.createFromAsset(resources.assets, fontPath)
        layout.findViewById<TextView>(R.id.textbox).typeface = font
        layout.findViewById<TextView>(R.id.tvHint).typeface = font
        return this
    }

    fun setHint(hint:String): DateRangePicker {
        layout.findViewById<TextView>(R.id.tvHint).text = hint
        return this
    }

    fun setText(text:String): DateRangePicker {
        layout.findViewById<TextView>(R.id.textbox).text = text
        return this
    }
    fun setHintColor(color:Int): DateRangePicker {
        layout.findViewById<TextView>(R.id.tvHint).setTextColor(color)
        return this
    }

    fun setHintSize(size:Float): DateRangePicker {
        layout.findViewById<TextView>(R.id.tvHint).textSize = size
        return this
    }

    fun setTextSize(size:Float): DateRangePicker {
        layout.findViewById<TextView>(R.id.textbox).textSize = size
        return this
    }
    fun getText():String = layout.findViewById<TextView>(R.id.textbox).text.toString()

    fun setTextColor(color:Int): DateRangePicker {
        layout.findViewById<TextView>(R.id.textbox).setTextColor(color)
        return this
    }

    fun setGravity(gravity:Int): DateRangePicker {
        layout.findViewById<TextView>(R.id.textbox).gravity = gravity
        return this
    }
    fun setBottomLineColor(color:Int): DateRangePicker {
        layout.findViewById<FrameLayout>(R.id.bottomLine).setBackgroundColor(color)
        return this
    }

    fun setHintGravity(gravity:Int): DateRangePicker {
        layout.findViewById<TextView>(R.id.tvHint).gravity = gravity
        return this
    }

    constructor(context: Context, isFullScreen:Boolean): super(context) {
        this.isFullScreen=isFullScreen
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_daterangepicker, this)
        if (!isInEditMode)    setup(null,context)
    }
    constructor(context: Context): super(context) {
        layout  = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_daterangepicker, this)
        if (!isInEditMode)    setup(null, context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_daterangepicker, this)
        if (!isInEditMode)  setup(attrs, context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        layout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.widget_daterangepicker, this)
        if (!isInEditMode)   setup(attrs, context)
    }


    private fun setup(attrs: AttributeSet?, context: Context) {
        if (attrs != null) {
            val attrib = context.obtainStyledAttributes(attrs, R.styleable.DateRangePicker)
            val  f= attrib.getString(R.styleable.DateRangePicker_date_range_font)?: ""
            var font:Typeface? = null
            if (f.isNotEmpty()) font=Typeface.createFromAsset(resources.assets, f)
            layout.findViewById<FrameLayout>(R.id.bottomLine).apply {
                this.setBackgroundColor(attrib.getColor(R.styleable.DateRangePicker_date_range_bottomline_color, Color.BLACK))
                this.setOnClickListener { dialog.show(fragmentManager,"DatePicker") }
            }
            dialog.listener =object :DateRangePickerFragmentDialog.OnDateRangeSelectedListener {
                override fun onDateRangeSelected(start: Calendar, end: Calendar) {
                    listener?.onDateRangeSelected(start,end)
                    setText("${start.get(Calendar.MONTH)+1}/${start.get(Calendar.DAY_OF_MONTH)}/${start.get(Calendar.YEAR)} - ${end.get(Calendar.MONTH)+1}/${end.get(Calendar.DAY_OF_MONTH)}/${end.get(Calendar.YEAR)}")

                }

                override fun onInvalidDateRange() {
                    if (invalidListener==null){
                        errorDialog.description.text=errorMessage
                        errorDialog.show()
                    }else invalidListener?.onInvalidDateRange()
                }

            }
            layout.findViewById<TextView>(R.id.tvHint).apply {
                this.setTextColor(attrib.getColor(R.styleable.DateRangePicker_date_range_hint_color, Color.BLUE))
                this.text = attrib.getString(R.styleable.DateRangePicker_date_range_hint)?: ""
                this.textSize = attrib.getInt(R.styleable.DateRangePicker_date_range_hint_size,15).toFloat()
                this.gravity = when (attrib.getInt(R.styleable.DateRangePicker_date_range_hint_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
                this.setOnClickListener { dialog.show(fragmentManager,"DatePicker") }
                if (font!=null) this.typeface=font
            }
            layout.findViewById<TextView>(R.id.textbox).apply {
                this.text = attrib.getString(R.styleable.DateRangePicker_date_range_text)?: "MM/dd/yyyy - MM/dd/yyyy"
                this.textSize = attrib.getInt(R.styleable.DateRangePicker_date_range_text_size,15).toFloat()
                this.setTextColor( attrib.getColor(R.styleable.DateRangePicker_date_range_font_color, Color.BLACK))
                this.gravity = when (attrib.getInt(R.styleable.DateRangePicker_date_range_gravity, 0)) {
                    0 -> Gravity.START
                    1 -> Gravity.END
                    2 -> Gravity.CENTER
                    else -> Gravity.START
                }
                this.setOnClickListener { dialog.show(fragmentManager,"DatePicker") }
                if (font!=null) this.typeface=font
            }
            setBackgroundColor(attrib.getColor(R.styleable.DateRangePicker_date_range_background_color, Color.WHITE))
            attrib.recycle()
        }
      }
}
