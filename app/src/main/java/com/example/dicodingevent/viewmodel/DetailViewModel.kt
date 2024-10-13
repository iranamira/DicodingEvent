package com.example.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.api.ApiClient
import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val eventRepository: EventRepository

    init {
        val apiService = ApiClient.apiClient
        eventRepository = EventRepository(apiService)
    }

    val event = MutableLiveData<Event>()
    val exception = MutableLiveData<Boolean>()

    fun getEvent(id: Int) {
        viewModelScope.launch {
            try {
                event.value = eventRepository.getEventById(id).event
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