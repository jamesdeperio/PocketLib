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
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.NetworkInterface
import java.util.*


fun getIPAddress(): String? {
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

private val cpu_file = arrayOf("cat sys/devices/system/cpu/cpu0/cpufreq/cpu_temp" ,
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

fun getCPUTemperature(): Float {
    try {
        cpu_file.forEach { it ->
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

private fun formatSize(it: Long): String {
    var suffix: String? = null
    var size = it
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

fun getAvailablelStorageSize(absolutePath: String): String {
    try{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val path = File(absolutePath)
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            return formatSize(availableBlocks * blockSize)
        }
        return "N/A"
    }catch (e:Exception){e.printStackTrace()
        return "N/A"
    }
}

fun getTotalStorageSize(absolutePath: String): String {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            val path = File(absolutePath)
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            return formatSize(totalBlocks * blockSize)
        }
        return "N/A"
    }catch (e:Exception){
        return "N/A"
    }
}

fun getRamInfo(context: Context): String {
    val mi = ActivityManager.MemoryInfo()
    val activityManager = context.getSystemService(Service.ACTIVITY_SERVICE) as ActivityManager
    activityManager.getMemoryInfo(mi)
    val availableMegs = mi.availMem / 0x100000L
    val totalMegs = mi.totalMem / 0x100000L
    return availableMegs.toString()+" MB of "+totalMegs+" MB"
}

@SuppressLint("MissingPermission")
fun isNetworkConnectionAvailable(context: Context): Boolean {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED )
        throw RuntimeException("ACCESS_NETWORK_STATE is not permitted!")

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun getScreenSize(windowManager: WindowManager): Point {
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size
}

fun lockOrientation(activity: Activity) {
    val currentOrientation = activity.resources.configuration.orientation
    if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    else activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
}
fun unlockOrientation(activity: Activity) {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
}

fun setVolume(context: Context,volume:Int) {
    val  audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
}

fun enableFullscreen (decorView: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    else decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN)
}