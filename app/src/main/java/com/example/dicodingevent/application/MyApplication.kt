package com.example.dicodingevent.application

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.dicodingevent.data.local.LocalDatabase
import com.example.dicodingevent.data.remote.api.ApiClient
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.util.workmanager.MyWorkerFactory

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupWorkerFactory()
    }

    private fun setupWorkerFactory() {
        val eventRepository = EventRepository(
            ApiClient.apiClient,
            LocalDatabase.getDatabase(this).getEventDao()
        )
        val workerFactory = MyWorkerFactory(eventRepository)
        WorkManager.initialize(this, Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build())
    }
}