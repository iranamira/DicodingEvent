package com.example.dicodingevent.util

import android.content.Context
import android.os.Build
import com.example.dicodingevent.R
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

    private fun getIndonesianDateFormat(dateString: String): String {
        val indonesianDay = getDayFromDate(dateString)
        val indonesianDate = convertDateToIndonesianFormat(dateString)
        return "$indonesianDay, $indonesianDate"
    }

    fun convertDate(context: Context, begin: String, end: String): String {
        val beginDate = begin.substring(0, 10)
        val beginTime = begin.substring(11)
        val endDate = end.substring(0, 10)
        val endTime = end.substring(11)

        val beginFinal = getIndonesianDateFormat(beginDate)
        val endFinal = getIndonesianDateFormat(endDate)

        return if (beginFinal == endFinal) {
            context.resources.getString(
                R.string.event_date_same_day,
                beginFinal,
                beginTime,
                endTime
            )
        } else {
            context.resources.getString(
                R.string.event_date_different_day,
                beginFinal,
                beginTime,
                endFinal,
                endTime
            )
        }
    }

    fun convertBeginDate(context: Context, date: String): String {
        val beginDate = date.substring(0, 10)
        val beginTime = date.substring(11)

        val beginFinal = getIndonesianDateFormat(beginDate)

        return context.resources.getString(R.string.notification_content_text, beginFinal, beginTime)
    }
}