@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import java.io.File
inline fun String.installAPK(): Boolean {
    val file = File(this)
    try {
        if (file.exists()) {
            val proc:Process = Runtime.getRuntime().exec(arrayOf("su", "-c", "pm install -r $this"))
            proc.waitFor()
            return true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return false
}

inline fun shutdownDevice(): Boolean = try {
    val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot -p"))
    proc.waitFor()
    true
} catch (ex: Exception) {
    ex.printStackTrace()
    false
}

inline fun rebootDevice(): Boolean = try {
      val proc = Runtime.getRuntime()
              .exec(arrayOf("su", "-c", "reboot "))
      proc.waitFor()
      true
  } catch (ex: Exception) {
      ex.printStackTrace()
      false
  }