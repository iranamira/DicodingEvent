package com.example.dicodingevent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.entity.Event
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _favoriteEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _favoriteEvents

    fun getFavoriteEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val event = eventRepository.getAllFavoriteEvent()
            _favoriteEvents.postValue(event)
        }
    }
}