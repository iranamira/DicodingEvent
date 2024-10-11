package com.example.dicodingevent.repository

import com.example.dicodingevent.api.ApiService
import com.example.dicodingevent.models.UpcomingEvent

class UpcomingRepository(private val apiService: ApiService) {
    suspend fun getUpcomingEvents(): UpcomingEvent {
        return apiService.getUpcomingEvent()
    }
}