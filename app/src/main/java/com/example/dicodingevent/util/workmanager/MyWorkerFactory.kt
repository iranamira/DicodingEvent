package com.example.dicodingevent.util.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.dicodingevent.data.repository.EventRepository

class MyWorkerFactory(private val eventRepository: EventRepository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return when (workerClassName) {
            DailyReminderWorker::class.java.name -> DailyReminderWorker(
                appContext,
                workerParameters,
                eventRepository
            )
            else -> throw IllegalArgumentException("Unknown worker class name: $workerClassName")
        }
    }
}