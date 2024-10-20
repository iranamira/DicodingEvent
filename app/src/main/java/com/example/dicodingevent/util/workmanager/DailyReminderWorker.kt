package com.example.dicodingevent.util.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingevent.R
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.ui.activity.DetailActivity
import com.example.dicodingevent.util.DateTimeUtil.convertBeginDate

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters,
    private val eventRepository: EventRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val newestEvent = getNewestEvent()
            showNotification(newestEvent)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun getNewestEvent(): Event {
        return eventRepository.getUpcomingEvents().listEvents[0]
    }

    private fun showNotification(newestEvent: Event) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        val notificationId = 1
        val channelId = "EVENT_REMINDER_CHANNEL"
        val channelName = "Event Reminder"

        val intent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra("event_id", newestEvent.id)
        }

        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(newestEvent.name)
            .setSmallIcon(R.drawable.notification)
            .setContentText(convertBeginDate(applicationContext, newestEvent.beginTime))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText(applicationContext.resources.getString(R.string.notification_subtext))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder)
    }
}