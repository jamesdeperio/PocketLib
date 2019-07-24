@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("EvaluationExt")
@file:JvmMultifileClass


package jdp.pocketlib.ext

import java.text.SimpleDateFormat
import java.util.*
inline fun String.isNumber():Boolean = this.matches("-?\\d+(\\.\\d+)?".toRegex())
inline fun String.hasNumber():Boolean = !this.matches("[0-9]+".toRegex())
inline fun String.isDouble():Boolean = this.matches("-?\\d+(\\.\\d+)".toRegex())
inline fun String.isInteger():Boolean = this.matches("-?\\d".toRegex())


@Suppress("ReplaceCallWithBinaryOperator")
inline fun String.isTimeBetween(argStartTime: String, argEndTime: String): Boolean {
    val reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$"
    val pattern = "HH:mm:ss"
    if (argStartTime.matches(reg.toRegex()) && argEndTime.matches(reg.toRegex()) && this.matches(reg.toRegex())) {
        val valid: Boolean
        var startTime: java.util.Date = SimpleDateFormat(pattern, Locale.getDefault())
                .parse(argStartTime)
        val startCalendar = Calendar.getInstance()
        startCalendar.time = startTime
        var currentTime: java.util.Date = SimpleDateFormat(pattern, Locale.getDefault())
                .parse(this)
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentTime

        var endTime: java.util.Date = SimpleDateFormat(pattern, Locale.getDefault())
                .parse(argEndTime)
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endTime

        if (currentTime.compareTo(endTime) < 0) {
            currentCalendar.add(Calendar.DATE, 1)
            currentTime = currentCalendar.time
        }

        if (startTime.compareTo(endTime) < 0) {
            startCalendar.add(Calendar.DATE, 1)
            startTime = startCalendar.time
        }

        if (currentTime.before(startTime)) valid = false
        else {
            if (currentTime.after(endTime)) {
                endCalendar.add(Calendar.DATE, 1)
                endTime = endCalendar.time
            }
            valid = currentTime.before(endTime)
        }
        return valid
    } else {
        throw IllegalArgumentException("Not a valid time, expecting HH:MM:SS format")
    }
}

@Suppress("ReplaceCallWithBinaryOperator")
inline fun Date.isTimeBetween(argStartTime: Date, argEndTime: Date): Boolean {
    val valid: Boolean
    var startTime: java.util.Date =argStartTime
    val startCalendar = Calendar.getInstance()
    startCalendar.time = startTime

    var currentTime: java.util.Date = this
    val currentCalendar = Calendar.getInstance()
    currentCalendar.time = currentTime

    var endTime: java.util.Date = argEndTime
    val endCalendar = Calendar.getInstance()
    endCalendar.time = endTime

    if (currentTime.compareTo(endTime) < 0) {
        currentCalendar.add(Calendar.DATE, 1)
        currentTime = currentCalendar.time
    }
    if (startTime.compareTo(endTime) < 0) {
        startCalendar.add(Calendar.DATE, 1)
        startTime = startCalendar.time
    }
    if (currentTime.before(startTime)) valid = false
    else {
        if (currentTime.after(endTime)) {
            endCalendar.add(Calendar.DATE, 1)
            endTime = endCalendar.time
        }
        valid = currentTime.before(endTime)
    }
    return valid

}