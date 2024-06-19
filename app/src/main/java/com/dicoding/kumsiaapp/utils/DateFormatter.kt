package com.dicoding.kumsiaapp.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    fun formatDate(currentDate: String): String? {
        val currentFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        val targetFormat = "dd MMMM yyyy, HH:mm"
        val timezone = "GMT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timezone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(currentDate)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }

    fun formatDOB(dob: String): String? {
        val currentFormat = "yyyy-MM-dd"
        val targetFormat = "dd MMMM yyyy"
        val timezone = "GMT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timezone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        var targetDate: String? = null
        try {
            val date = currentDf.parse(dob)
            if (date != null) {
                targetDate = targetDf.format(date)
            }
        } catch (ex: ParseException) {
            ex.printStackTrace()
        }
        return targetDate
    }

    fun commentDateFormat(date: String): String {
        val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        currentFormat.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate = currentFormat.parse(date)

        val now = Calendar.getInstance().time
        val diff = now.time - parsedDate!!.time

        val oneSec = 1000L
        val oneMin = 60 * oneSec
        val oneHour = 60 * oneMin
        val oneDay = 24 * oneHour
        val oneMonth = 30 * oneDay
        val oneYear = 365 * oneDay

        return when {
            diff < oneMin -> "just now"
            diff < oneHour -> "${diff / oneMin} min ago"
            diff < oneDay -> "${diff / oneHour} hours ago"
            diff < 2 * oneDay -> "yesterday"
            diff < oneMonth -> "${diff / oneDay} days ago"
            diff < oneYear -> "${diff / oneMonth} months ago"
            else -> "${diff / oneYear} years ago"
        }
    }

    fun getCurrentDate(): String {
        val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val now = Calendar.getInstance().time
        return currentFormat.format(now)
    }
}