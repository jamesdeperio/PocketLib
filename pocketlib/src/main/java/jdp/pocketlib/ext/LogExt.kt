@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.util.Log

inline fun Any.v(message: String) {
    Log.v(javaClass.simpleName, message)
}

inline fun Any.d(message: String) {
    Log.d(javaClass.simpleName, message)
}

inline fun Any.i(message: String) {
    Log.i(javaClass.simpleName, message)
}

inline fun Any.w(message: String) {
    Log.w(javaClass.simpleName, message)
}

inline fun Any.e(message: String) {
    Log.e(javaClass.simpleName, message)
}

inline fun Any.wtf(message: String) {
    Log.wtf(javaClass.simpleName, message)
}

