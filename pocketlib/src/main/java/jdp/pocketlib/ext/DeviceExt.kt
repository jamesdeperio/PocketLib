@file:Suppress("NOTHING_TO_INLINE")

package jdp.pocketlib.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.StatFs
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.util.*


inline fun getIPAddress(): String? {
    try {
        Collections.list(NetworkInterface.getNetworkInterfaces()).forEach {
            val addrs = Collections.list(it.inetAddresses)
            for (addr in addrs) if (!addr.isLoopbackAddress) {
                val sAddr = addr.hostAddress
                val isIPv4 = sAddr.indexOf(':') < 0
                if (isIPv4)  return sAddr
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

inline fun getCPUTemperature(): Float {
    val cpuFile = arrayOf("cat sys/devices/system/cpu/cpu0/cpufreq/cpu_temp" ,
        "cat sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp" ,
        "cat sys/class/thermal/thermal_zone1/temp",
        "cat sys/class/i2c-adapter/i2c-4/4-004c/temperature" ,
        "cat sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature" ,
        "cat sys/devices/platform/omap/omap_temp_sensor.0/temperature"  ,
        "cat sys/devices/platform/tegra_tmon/temp1_input"   ,
        "cat sys/kernel/debug/tegra_thermal/temp_tj"    ,
        "cat sys/devices/platform/s5p-tmu/temperature"     ,
        "cat sys/class/thermal/thermal_zone0/temp"     ,
        "cat sys/devices/virtual/thermal/thermal_zone0/temp"   ,
        "cat sys/class/hwmon/hwmon0/device/temp1_input" ,
        "cat sys/devices/virtual/thermal/thermal_zone1/temp" ,
        "cat sys/devices/platform/s5p-tmu/curr_temp" )
    try {
        cpuFile.forEach {
            val p:Process? = Runtime.getRuntime().exec(it)
            System.gc()
            if (p==null) return@forEach
            p.waitFor()
            val reader = BufferedReader(InputStreamReader(p.inputStream))
            val line :String? = reader.readLine()
            reader.close()
            if (line!=null) return line.toFloat()
        }
        return 0.0f
    } catch (e: Exception) {
        e.printStackTrace()
        return 0.0f
    }
}

inline fun Long.formatSize(): String {
    var suffix: String? = null
    var size = this
    if (size >= 1024) {
        suffix = "KB"
        size /= 1024
        if (size >= 1024) {
            suffix = "MB"
            size /= 1024
        }
    }
    val resultBuffer = StringBuilder(java.lang.Long.toString(size))
    var commaOffset = resultBuffer.length - 3
    while (commaOffset > 0) {
        resultBuffer.insert(commaOffset, ',')
        commaOffset -= 3
    }
    if (suffix != null) resultBuffer.append(suffix)
    return resultBuffer.toString()
}

inline fun String.getAvailablelStorageSize(): String {
    try{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val path = File(this)
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            return (availableBlocks * blockSize).formatSize()
        }
        return "N/A"
    }catch (e:Exception){e.printStackTrace()
        return "N/A"
    }
}

inline fun String.getTotalStorageSize(): String {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val path = File(this)
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            return (totalBlocks * blockSize).formatSize()
        }
        return "N/A"
    }catch (e:Exception){
        return "N/A"
    }
}

inline fun Context.getRamInfo(): String {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = this.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L
    val totalMegs = mi.totalMem / 0x100000L
    return "$availableMegs MB of $totalMegs MB"
}

@SuppressLint("MissingPermission")
inline fun Context.isNetworkConnectionAvailable(): Boolean {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED )
        throw RuntimeException("ACCESS_NETWORK_STATE is not permitted!")

    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

inline fun WindowManager.getScreenSize(): Point {
    val display = this.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

inline fun Activity.lockOrientation() {
    val currentOrientation = this.resources.configuration.orientation
    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    else this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
}

inline fun Activity.unlockOrientation() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
}

inline fun Context.setVolume(volume:Int) {
    val  audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
}

inline fun View.enableFullscreen () {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        this.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    else this.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}