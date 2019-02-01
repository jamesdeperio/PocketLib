package jdp.pocketlib.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import jdp.pocketlib.R

open class GaugeView : View {
    private var defaultWidth:Float = dp2px(200f)
    private var defaultHeight:Float = dp2px(100f)
    private var path = Path()
    private var hexRed:String = ""
    private var hexBlue:String = ""
    private var hexGreen:String = ""
    private var progressTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var ringPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var progress: Int = 0
        set(value) {
            field=value
            invalidate()
        }

    var width:Float = defaultWidth
    var height :Float= defaultHeight

    var angle:Int = 1
    var lineStrokeWidth:Int = 1
    var showProgressText:Boolean = true
    var centerX:Float = 0f
    var centerY:Float = 0f
    var ringWidth:Float = 0f
    var red:Int = 250
    var blue:Int = 63
    var  green:Int = 63


    constructor(context: Context): super(context) {
        if (!isInEditMode)    setup(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!isInEditMode)  setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (!isInEditMode)   setup(attrs)
    }

    private fun setup(attrs: AttributeSet?) {
        val attrSet: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.GaugeView)
        this.progress = attrSet.getInt(R.styleable.GaugeView_gauge_progress, 0)
        this.angle = attrSet.getInt(R.styleable.GaugeView_gauge_angle, 0)
        this.lineStrokeWidth = attrSet.getInt(R.styleable.GaugeView_gauge_width_stroke, 0)
        this.showProgressText = attrSet.getBoolean(R.styleable.GaugeView_gauge_show_progress_text, true)
        attrSet.recycle()
        this.linePaint.color = Color.BLACK
        this.linePaint.strokeWidth = dp2px(this.lineStrokeWidth.toFloat())
        this.ringPaint.color = Color.parseColor("#F9F9F9")
        this.ringPaint.style = Paint.Style.FILL
        this.path.fillType = Path.FillType.WINDING
        this.circlePaint.color = Color.parseColor("#0E000000")
        this.circlePaint.setShadowLayer(dp2px(4f), 0f, 0f, Color.parseColor("#F9F9F9"))
        this.circlePaint.style = Paint.Style.STROKE
        this.circlePaint.strokeWidth = dp2px(1f)
        this.progressTextPaint.textAlign = Paint.Align.CENTER
    }

    fun updateProgress(progress: Int, duration: Long = Math.abs((progress - this.progress)) * 30L) {
        val animator: ObjectAnimator = ObjectAnimator.ofInt(this, "progress", progress).setDuration(duration)
        animator.interpolator= AnticipateOvershootInterpolator()
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var w = measureHandler(widthMeasureSpec, this.defaultWidth.toInt())
        var h = measureHandler(heightMeasureSpec, this.defaultHeight.toInt())
        when {
            widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY -> if (w > h * 2) w = h * 2 else h = w / 2
            widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY -> h = w / 2
            widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY -> w = h * 2
        }

        if (w > h * 2) w = h * 2
        else h = w / 2

        this.width = w.toFloat()
        this.height = h.toFloat()
        this.centerX = this.width / 2f
        this.centerY = this.height
        this.ringWidth = this.width / 10f
        this.progressTextPaint.textSize = (this.width / 7)
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(this.centerX, this.centerY)
        this.path.addCircle(0f, 0f, this.ringWidth * 4, Path.Direction.CW)
        this.path.addCircle(0f, 0f, this.ringWidth * 5, Path.Direction.CCW)
        canvas.drawPath(this.path, this.ringPaint)
        canvas.drawCircle(0f, 0f, this.ringWidth * 4 + dp2px(3f), this.circlePaint)
        canvas.drawCircle(0f, 0f, this.ringWidth * 5 - dp2px(3f), this.circlePaint)

        var  mRed:Int = this.red
        var mGreen:Int = this.green
        var mBlue :Int= this.blue

        val count = 180 / this.angle / 3
        (1 until (this.progress / 100f * 180 / this.angle).toInt()).forEach {
            canvas.save()
            canvas.rotate(it * this.angle.toFloat(), 0f, 0f)
            when (it) {
                in 0 until count -> {
                    mRed -= 153 / count
                    mGreen += 75 / count
                    mBlue += 192 / count
                    if (mRed < 97) mRed = 97
                    if (mGreen > 138) mGreen = 138
                    if (mBlue > 255) mBlue = 255
                }
                in count until count * 2 -> {
                    mGreen += 42 / count
                    if (mGreen > 180) mGreen = 180
                }
                in count * 2 until count * 3 -> {
                    mRed -= 35 / count
                    mGreen += 52 / count
                    mBlue -= 101 / count
                    if (mRed < 62) mRed = 62
                    if (mGreen > 232) mGreen = 232
                    if (mBlue < 154) mBlue = 154
                }
            }

            this.hexRed = Integer.toHexString(mRed)
            this.hexBlue = Integer.toHexString(mBlue)
            this.hexGreen = Integer.toHexString(mGreen)
            if (this.hexRed.length < 2) this.hexRed = "0$hexRed"
            if (this.hexBlue.length < 2) this.hexBlue = "0$hexBlue"
            if (this.hexGreen.length < 2) this.hexGreen = "0$hexGreen"

            this.linePaint.color = Color.parseColor("#ff$hexRed$hexGreen$hexBlue")
            canvas.drawLine(-this.ringWidth * 4 - dp2px(6f), 0f, -this.ringWidth * 5 + dp2px(6f), 0f, this.linePaint)
            canvas.restore()
        }

        if (this.showProgressText) drawProgressText(canvas)
        canvas.restore()
    }

    private fun drawProgressText(canvas: Canvas) = canvas.drawText("${this.progress}%", 0f, -dp2px(5f), this.progressTextPaint)

    private fun dp2px(dpValue: Float): Float = (dpValue * resources.displayMetrics.density + 0.5f)

    @SuppressLint("SwitchIntDef")
    private fun measureHandler(measureSpec: Int, defaultSize: Int): Int {
        val specMode:Int = View.MeasureSpec.getMode(measureSpec)
        val specSize:Int = View.MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            View.MeasureSpec.EXACTLY -> specSize
            View.MeasureSpec.AT_MOST -> Math.min(defaultSize, specSize)
            else -> defaultSize
        }
    }
}