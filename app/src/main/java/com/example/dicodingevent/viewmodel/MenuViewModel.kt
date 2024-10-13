package com.example.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.api.ApiClient
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val eventRepository: EventRepository

    init {
        val apiService = ApiClient.apiClient
        eventRepository = EventRepository(apiService)
    }

    val allEventsByKeyword = MutableLiveData<List<Event>>()
    val exception = MutableLiveData<Boolean>()

    fun getAllEventsByKeyword(keyword: String) {
        viewModelScope.launch {
            try {
                allEventsByKeyword.value = eventRepository.getUpcomingEventByKeyword(keyword).listEvents
            } catch (e: Exception) {
                Log.e("Exception", "Unexpected Exception")
                exception.value = true
            }
        }
    }

    fun resetExceptionValue() {
        exception.value = false
    }
}