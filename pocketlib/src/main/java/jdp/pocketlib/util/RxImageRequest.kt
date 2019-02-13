package jdp.pocketlib.util


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import jdp.pocketlib.base.BaseFragment
import jdp.pocketlib.manager.Bus
import jdp.pocketlib.manager.EventBusManager

@SuppressLint("ValidFragment")
class TempFragment(private val eventBusManager: EventBusManager, private val type: String) :BaseFragment() {
    override fun onInitialization(savedInstanceState: Bundle?) {
        when (type) {
            RxImageRequest.GALLERY -> startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1)
            RxImageRequest.CAMERA -> startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE),1)
            RxImageRequest.MULTIPLE_IMAGE -> {
                val i =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    i.action = Intent.ACTION_GET_CONTENT
                }
                startActivityForResult(i,1)

            }
        }
    }
    override fun onViewDidLoad(savedInstanceState: Bundle?) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data!=null) {
            when (type) {
                RxImageRequest.GALLERY -> eventBusManager.sendEvent(RxImageRequest.BITMAP_REQUEST,MediaStore.Images.Media.getBitmap(activity!!.contentResolver, data.data))
                RxImageRequest.CAMERA -> eventBusManager.sendEvent(RxImageRequest.BITMAP_REQUEST ,data.extras!!.get("data") as Bitmap)
                RxImageRequest.MULTIPLE_IMAGE -> {
                    val imageUris = ArrayList<Uri>()
                    val clipData:ClipData? = data.clipData
                    if (clipData != null) (0 until clipData.itemCount).forEach { i ->
                        imageUris.add(clipData.getItemAt(i).uri)
                    } else imageUris.add(data.data!!)
                    eventBusManager.sendEvent(RxImageRequest.BITMAP_REQUEST ,imageUris)
                }
            }
        }
    }
}
object RxImageRequest {
    const val BITMAP_REQUEST="BITMAP_REQUEST"
    const val GALLERY="GALLERY"
    const val MULTIPLE_IMAGE="MULTIPLE_IMAGE"
    const val CAMERA="CAMERA"
    fun request(type:String,fragmentManager:FragmentManager,callback:(bitmap:Bitmap)-> Unit) {
        val eventBusManager= EventBusManager(Bus.PublishSubject)
        val tempFragment :Fragment= TempFragment(eventBusManager,type)
        fragmentManager.beginTransaction()
            .add(tempFragment, tempFragment.javaClass.simpleName)
            .commit()
        eventBusManager.subscribeReceiver(BITMAP_REQUEST) {
            callback(it as Bitmap)
            fragmentManager.beginTransaction()
                .remove(tempFragment)
                .commit()
            eventBusManager.disposeAllChannel()
        }
    }

    @Suppress("UNCHECKED_CAST")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun requestMultipleImage(fragmentManager:FragmentManager, callback:(bitmap: ArrayList<Uri>)-> Unit) {
        val eventBusManager= EventBusManager(Bus.PublishSubject)
        val tempFragment :Fragment= TempFragment(eventBusManager,MULTIPLE_IMAGE)
        fragmentManager.beginTransaction()
            .add(tempFragment, tempFragment.javaClass.simpleName)
            .commit()
        eventBusManager.subscribeReceiver(BITMAP_REQUEST) {
            callback(it as ArrayList<Uri>)
            fragmentManager.beginTransaction()
                .remove(tempFragment)
                .commit()
            eventBusManager.disposeAllChannel()
        }
    }
}