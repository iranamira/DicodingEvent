package com.example.dicodingevent.api

import com.example.dicodingevent.models.UpcomingEvent
import retrofit2.http.GET

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvent(): UpcomingEvent
}