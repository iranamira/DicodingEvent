package com.example.dicodingevent.data.api

import com.example.dicodingevent.data.model.Event
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvents(): Event

    @GET("events")
    suspend fun getFinishedEvents(@Query("active") active: Int): Event

    @GET("events")
    suspend fun getLimitUpcomingEvents(@Query("limit") limit: Int): Event

    @GET("events")
    suspend fun getLimitFinishedEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): Event
}