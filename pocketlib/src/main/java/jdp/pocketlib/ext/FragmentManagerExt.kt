@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.FragmentManager
import jdp.pocketlib.builder.PageBuilder
import jdp.pocketlib.util.FragmentTransaction
import jdp.pocketlib.util.PermissionResult
import jdp.pocketlib.util.RxImageRequest
import jdp.pocketlib.util.RxPermissionRequest

inline fun FragmentManager.add(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.add(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun FragmentManager.replace(allowStateLoss:Boolean=false,isAnimationEnabled: Boolean = true,properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.replace(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun FragmentManager.createViewPager(properties: PageBuilder.() -> Unit) {
    PageBuilder.build(this,properties)
}

inline fun FragmentManager.useGallery(noinline callback:(bitmap:Bitmap) -> Unit) {
    RxImageRequest.request(RxImageRequest.GALLERY,this,callback)
}

inline fun FragmentManager.useCamera(noinline callback:(bitmap:Bitmap) -> Unit) {
    RxImageRequest.request(RxImageRequest.CAMERA,this,callback)
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
inline fun FragmentManager.useMultipleSelectGallery(noinline callback:(uris:ArrayList<Uri>) -> Unit) {
    RxImageRequest.requestMultipleImage(this,callback)
}

inline fun FragmentManager.requestPermissions(vararg permission: String, noinline callback:(result:PermissionResult) -> Unit) {
    RxPermissionRequest.requestPermission(permissions = *permission,fragmentManager = this,callback = callback)
}

