@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import jdp.pocketlib.builder.PageBuilder
import jdp.pocketlib.util.FragmentTransaction
import jdp.pocketlib.util.PermissionResult
import jdp.pocketlib.util.RxImageRequest
import jdp.pocketlib.util.RxPermissionRequest

inline fun androidx.fragment.app.FragmentManager.add(allowStateLoss:Boolean=false, isAnimationEnabled: Boolean = true, properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.add(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun androidx.fragment.app.FragmentManager.replace(allowStateLoss:Boolean=false, isAnimationEnabled: Boolean = true, properties: FragmentTransaction.() -> Unit) {
    FragmentTransaction.replace(this, allowStateLoss,isAnimationEnabled,properties)
}

inline fun androidx.fragment.app.FragmentManager.createViewPager(properties: PageBuilder.() -> Unit) {
    PageBuilder.build(this,properties)
}

inline fun androidx.fragment.app.FragmentManager.useGallery(noinline callback:(bitmap:Bitmap) -> Unit) {
    RxImageRequest.request(RxImageRequest.GALLERY,this,callback)
}

inline fun androidx.fragment.app.FragmentManager.useCamera(noinline callback:(bitmap:Bitmap) -> Unit) {
    RxImageRequest.request(RxImageRequest.CAMERA,this,callback)
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
inline fun androidx.fragment.app.FragmentManager.useMultipleSelectGallery(noinline callback:(uris:ArrayList<Uri>) -> Unit) {
    RxImageRequest.requestMultipleImage(this,callback)
}

inline fun androidx.fragment.app.FragmentManager.requestPermissions(vararg permission: String, noinline callback:(result:ArrayList<PermissionResult>) -> Unit) {
    RxPermissionRequest.requestPermission(permissions = *permission,fragmentManager = this,callback = callback)
}

