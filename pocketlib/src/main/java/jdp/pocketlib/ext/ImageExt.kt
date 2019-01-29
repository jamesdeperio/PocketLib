@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

import java.io.ByteArrayOutputStream

inline fun Bitmap.encodeToBase64String(compressFormat: Bitmap.CompressFormat=Bitmap.CompressFormat.PNG, quality: Int=80): String {
    val byteArrayOS = ByteArrayOutputStream()
    this.compress(compressFormat, quality, byteArrayOS)
    return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT)
}

inline fun String.toBitmap(): Bitmap {
    val decodedBytes = Base64.decode(this, 0)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}