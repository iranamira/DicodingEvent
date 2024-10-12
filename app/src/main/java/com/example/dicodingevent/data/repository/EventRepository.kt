package com.example.dicodingevent.data.repository

import com.example.dicodingevent.data.api.ApiService
import com.example.dicodingevent.data.model.Event

class EventRepository(private val apiService: ApiService) {
    suspend fun getUpcomingEvents(): Event {
        return apiService.getUpcomingEvents()
    }

    suspend fun getFinishedEvents(): Event {
        return apiService.getFinishedEvents(0)
    }

    suspend fun getLimitUpcomingEvents(): Event {
        return apiService.getLimitUpcomingEvents(5)
    }

    suspend fun getLimitFinishedEvents(): Event {
        return apiService.getLimitFinishedEvents(0, 5)
    }
}