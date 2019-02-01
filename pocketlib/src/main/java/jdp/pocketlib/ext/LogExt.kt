@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.util.Log

inline fun Any.VERBOSE(message: String) {
    Log.v(javaClass.simpleName, message)
}

inline fun Any.DEBUG(message: String) {
    Log.d(javaClass.simpleName, message)
}

inline fun Any.INFO(message: String) {
    Log.i(javaClass.simpleName, message)
}

inline fun Any.WARNING(message: String) {
    Log.w(javaClass.simpleName, message)
}

inline fun Any.ERROR(message: String) {
    Log.e(javaClass.simpleName, message)
}

inline fun Any.WTF(message: String) {
    Log.wtf(javaClass.simpleName, message)
}

