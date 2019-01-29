package jdp.pocketlib.ext

import java.io.File

fun installAPK(filePath: String): Boolean {
    val file = File(filePath)
    return if (file.exists()) try {
        val proc:Process = Runtime.getRuntime().exec(arrayOf("su", "-c", "pm install -r $filePath"))
        proc.waitFor()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
    else false
}

fun shutdownDevice(): Boolean = try {
    val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot -p"))
    proc.waitFor()
    true
} catch (ex: Exception) {
    ex.printStackTrace()
    false
}

 fun rebootDevice(): Boolean = try {
      val proc = Runtime.getRuntime()
              .exec(arrayOf("su", "-c", "reboot "))
      proc.waitFor()
      true
  } catch (ex: Exception) {
      ex.printStackTrace()
      false
  }