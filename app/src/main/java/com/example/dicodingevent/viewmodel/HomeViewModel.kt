package com.example.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.api.ApiClient
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val eventRepository: EventRepository

    init {
        val apiService = ApiClient.apiClient
        eventRepository = EventRepository(apiService)
    }

    val upcomingEvents = MutableLiveData<List<Event>>()
    val finishedEvents = MutableLiveData<List<Event>>()
    val exception = MutableLiveData<Boolean>()

    fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                upcomingEvents.value = eventRepository.getLimitUpcomingEvents().listEvents
            } catch (e: Exception) {
                Log.e("Exception", "Unexpected Exception")
                exception.value = true
            }
        }
    }

    fun getFinishedEvents() {
        viewModelScope.launch {
            try {
                finishedEvents.value = eventRepository.getLimitFinishedEvents().listEvents
            } catch (e: Exception) {
                Log.e("Exception", "Unexpected Exception")
            }
        }
    }

    fun resetExceptionValue() {
        exception.value = false
    }
}