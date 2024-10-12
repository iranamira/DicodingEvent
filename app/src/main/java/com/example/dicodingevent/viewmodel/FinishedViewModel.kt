package com.example.dicodingevent.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.api.ApiClient
import com.example.dicodingevent.data.model.ListEvents
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class FinishedViewModel : ViewModel() {
    private val eventRepository: EventRepository

    init {
        val apiService = ApiClient.apiClient
        eventRepository = EventRepository(apiService)
    }

    val finishedEvents = MutableLiveData<List<ListEvents>>()
    val exception = MutableLiveData<Boolean>()

    fun getFinishedEvents() {
        viewModelScope.launch {
            try {
                finishedEvents.value = eventRepository.getFinishedEvents().listEvents
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