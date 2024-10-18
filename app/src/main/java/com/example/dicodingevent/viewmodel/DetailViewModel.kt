package com.example.dicodingevent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event as RemoteEvent
import com.example.dicodingevent.data.local.entity.Event as LocalEvent
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _event = MutableLiveData<RemoteEvent>()
    val event: LiveData<RemoteEvent> = _event

    private val _exception = MutableLiveData<Boolean>()
    val exception: LiveData<Boolean> = _exception

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isEventExistInFavorite = MutableLiveData<Boolean>()
    val isEventExistInFavorite: LiveData<Boolean> = _isEventExistInFavorite

    fun getEvent(id: Int) {
        if (_event.value == null) {
            _isLoading.value = true

            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val event = eventRepository.getEventById(id).event
                    _event.postValue(event)
                    _exception.postValue(false)
                } catch (e: Exception) {
                    _exception.postValue(true)
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun checkIsEventExistInFavorite(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        val isExist = eventRepository.checkIsEventExistInFavorite(id)
        if (isExist > 0) {
            _isEventExistInFavorite.postValue(true)
        } else {
            _isEventExistInFavorite.postValue(false)
        }
    }

    fun saveEventToFavorite(event: LocalEvent) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.saveEventToFavorite(event)
    }

    fun removeEventFromFavorite(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        eventRepository.removeEventFromFavorite(id)
    }

    fun resetExceptionValue() {
        _exception.value = false
    }
}