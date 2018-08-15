package jdp.pocketlib.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import jdp.pocketlib.R

class PocketDialog(context: Context,type:PocketDialog.Type,private var isFullScreen:Boolean=false) {
    enum class Type {
         DIALOG_NO_INTERNET_CONNECTION,
         DIALOG_WARNING,
         DIALOG_REWARD,
         DIALOG_BASIC,
         DIALOG_ERROR,
         DIALOG_SUCCESS,
        DIALOG_LOADER
    }
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
         }
        this.window!!.attributes.windowAnimations = R.style.DialogAnimation
        this.setContentView(R.layout.dialog_pocket)
        this.setOnDismissListener {
            if (isFullScreen) this.window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
    }

    private val buttonContainer= dialog.findViewById<LinearLayout>(R.id.buttonContainer)!!

    val title= dialog.findViewById<TextView>(R.id.tvTitle)!!
    val description= dialog.findViewById<TextView>(R.id.tvDesc)!!
    @SuppressLint("SetTextI18n")
    val okAction= dialog.findViewById<Button>(R.id.btnOk)!!.apply {
        this.setOnClickListener { dialog.dismiss() }
        if (type==Type.DIALOG_LOADER)
            this.visibility=View.GONE
        ( this.layoutParams as LinearLayout.LayoutParams).apply {
            this.setMargins(1,1,1,1)
            this.width=150

        }
    }
    val cancelAction= dialog.findViewById<Button>(R.id.btnCancel)!!.apply {
        this.setOnClickListener { dialog.dismiss() }
        if (type!=Type.DIALOG_WARNING)
            this.visibility=View.GONE
        ( this.layoutParams as LinearLayout.LayoutParams).apply {
            this.setMargins(1,1,1,1)
            this.width=150
        }
    }
    val view= dialog.findViewById<LinearLayout>(R.id.container)!!
    @SuppressLint("SetTextI18n")
    val lottie= dialog.findViewById<LottieAnimationView>(R.id.lottieView)!!.apply {
        setActionButtonGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)
        when (type) {
            Type.DIALOG_SUCCESS -> {
                this.setAnimation(R.raw.success2)
                title.text="Success!"
            }
            Type.DIALOG_REWARD -> {
                this.setAnimation(R.raw.trophy)
                title.text="Congratulations!"
            }
            Type.DIALOG_NO_INTERNET_CONNECTION -> {
                this.setAnimation(R.raw.trophy)
                title.text="No Internet Connection!"
                description.text="Cannot process request. Please try again."
                okAction.text = "Retry"
            }
            Type.DIALOG_ERROR -> {
                title.text="Oops..."
                this.setAnimation(R.raw.error)
            }
            Type.DIALOG_LOADER ->{
                title.text="Loading..."
                this.repeatCount=-1
                this.setAnimation(R.raw.loader)
            }
            Type.DIALOG_WARNING -> {
                title.text="Oops..."
                this.setAnimation(R.raw.warning)
                setActionButtonGravity(Gravity.END)
            }
            else -> {
                title.text="Info"
                this.setAnimation(R.raw.loader)
                ( title.layoutParams as LinearLayout.LayoutParams).apply {
                    gravity=Gravity.START
                    topMargin=24
                    leftMargin=24
                }
                setActionButtonGravity(Gravity.END)
                this.visibility=View.GONE
            }
        }
    }

    fun setActionButtonGravity(gravity: Int) :PocketDialog {
        ( buttonContainer.layoutParams as LinearLayout.LayoutParams).apply {
            this.gravity=gravity
        }
        return this
    }


    fun show() :PocketDialog {
        dialog.show()
        if (isFullScreen){
            dialog.window!!.decorView.systemUiVisibility = dialog.window!!.decorView.systemUiVisibility
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }
        lottie.playAnimation()
        return this
    }

    fun dismiss() :PocketDialog {
        dialog.dismiss()
        return this
    }

}
