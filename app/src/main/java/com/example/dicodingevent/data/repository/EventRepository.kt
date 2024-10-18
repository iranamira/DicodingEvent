package com.example.dicodingevent.data.repository

import com.example.dicodingevent.data.local.dao.EventDao
import com.example.dicodingevent.data.local.entity.Event
import com.example.dicodingevent.data.remote.api.ApiService
import com.example.dicodingevent.data.remote.model.HeaderListEvent
import com.example.dicodingevent.data.remote.model.HeaderSingleEvent

class EventRepository(private val apiService: ApiService, private val eventDao: EventDao) {
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

    suspend fun saveEventToFavorite(event: Event) {
        eventDao.insertEvent(event)
    }

    suspend fun getAllFavoriteEvent(): List<Event> {
        return eventDao.getAllEvent()
    }

    suspend fun checkIsEventExistInFavorite(id: Int): Int {
        return eventDao.isEventExist(id)
    }

    suspend fun removeEventFromFavorite(id: Int) {
        eventDao.deleteEvent(id)
    }
}