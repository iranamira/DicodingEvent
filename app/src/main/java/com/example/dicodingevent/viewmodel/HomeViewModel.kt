package com.example.dicodingevent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _finishedEvents

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshLoading = MutableLiveData<Boolean>()
    val isRefreshLoading: LiveData<Boolean> = _isRefreshLoading

    fun getUpcomingAndFinishedEvents() {
        if (_upcomingEvents.value == null || _finishedEvents.value == null) {
            _isLoading.value = true

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val upcomingEvent = eventRepository.getLimitUpcomingEvents().listEvents
                    val finishedEvent = eventRepository.getLimitFinishedEvents().listEvents
                    _upcomingEvents.postValue(upcomingEvent)
                    _finishedEvents.postValue(finishedEvent)
                } catch (e: Exception) {
                    _exception.postValue(true)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun refreshUpcomingAndFinishedEvents() {
        _isRefreshLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val upcomingEvent = eventRepository.getLimitUpcomingEvents().listEvents
                val finishedEvent = eventRepository.getLimitFinishedEvents().listEvents
                _upcomingEvents.postValue(upcomingEvent)
                _finishedEvents.postValue(finishedEvent)
            } catch (e: Exception) {
                _exception.postValue(true)
            } finally {
                _isRefreshLoading.postValue(false)
            }
        }
    }

    fun resetExceptionValue() {
        _exception.value = false
    }
}