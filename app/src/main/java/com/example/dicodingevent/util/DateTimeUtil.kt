package com.example.dicodingevent.util

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    private fun getDayFromDate(dateString: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(
                dateString,
                DateTimeFormatter.ofPattern("yyyy-[M][MM]-[d][dd]")
            )
            val dayOfWeek = date.dayOfWeek
            dayOfWeek.getDisplayName(TextStyle.FULL, Locale("id", "ID"))
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date!!
            calendar.getDisplayName(
                Calendar.DAY_OF_WEEK,
                Calendar.LONG,
                Locale("id", "ID")
            )!!
        }
    }

    private fun convertDateToIndonesianFormat(dateString: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.parse(
                dateString,
                DateTimeFormatter.ofPattern("yyyy-[M][MM]-[d][dd]")
            )

            val indonesianDateFormat = DateTimeFormatter.ofPattern(
                "dd MMMM yyyy",
                Locale("id", "ID")
            )
            date.format(indonesianDateFormat)
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            val date: Date? = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        }
    }

    fun getIndonesianDateFormat(dateString: String): String {
        val indonesianDay = getDayFromDate(dateString)
        val indonesianDate = convertDateToIndonesianFormat(dateString)
        return "$indonesianDay, $indonesianDate"
    }
}