package com.example.dicodingevent.data.repository

import com.example.dicodingevent.data.api.ApiService
import com.example.dicodingevent.data.model.HeaderListEvent
import com.example.dicodingevent.data.model.HeaderSingleEvent

class EventRepository(private val apiService: ApiService) {
    suspend fun getUpcomingEvents(): HeaderListEvent {
        return apiService.getUpcomingEvents(1)
    }

    suspend fun getFinishedEvents(): HeaderListEvent {
        return apiService.getFinishedEvents(0)
    }

    suspend fun getLimitUpcomingEvents(): HeaderListEvent {
        return apiService.getLimitUpcomingEvents(1, 5)
    }

    suspend fun getLimitFinishedEvents(): HeaderListEvent {
        return apiService.getLimitFinishedEvents(0, 5)
    }

    suspend fun getEventById(id: Int): HeaderSingleEvent {
        return apiService.getEventById(id)
    }

    suspend fun getEventsByKeyword(keyword: String): HeaderListEvent {
        return apiService.getEventsByKeyword(-1, keyword)
    }
}