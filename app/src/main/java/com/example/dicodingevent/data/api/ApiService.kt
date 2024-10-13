package com.example.dicodingevent.data.api

import com.example.dicodingevent.data.model.HeaderListEvent
import com.example.dicodingevent.data.model.HeaderSingleEvent
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getUpcomingEvents(@Query("active") active: Int): HeaderListEvent

    @GET("events")
    suspend fun getFinishedEvents(@Query("active") active: Int): HeaderListEvent

    @GET("events")
    suspend fun getLimitUpcomingEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): HeaderListEvent

    @GET("events")
    suspend fun getLimitFinishedEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int
    ): HeaderListEvent

    @GET("events/{id}")
    suspend fun getEventById(@Path("id") id: Int): HeaderSingleEvent

    @GET("events")
    suspend fun getUpcomingEventsByKeyword(
        @Query("active") active: Int,
        @Query("q") q: String
    ): HeaderListEvent
}