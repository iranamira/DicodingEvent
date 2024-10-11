package com.example.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.api.ApiInstance
import com.example.dicodingevent.models.ListEvents
import com.example.dicodingevent.models.UpcomingEvent
import com.example.dicodingevent.repository.UpcomingRepository
import kotlinx.coroutines.launch

class UpcomingViewModel : ViewModel() {
    private val upcomingRepository: UpcomingRepository

    init {
        val apiService = ApiInstance.apiInstance
        upcomingRepository = UpcomingRepository(apiService)
    }

    val upcomingEvents = MutableLiveData<List<ListEvents>>()

    fun getUpcomingEvents() {
        viewModelScope.launch {
            try {
                upcomingEvents.value = upcomingRepository.getUpcomingEvents().listEvents
            } catch (e: Exception) {
                Log.e("Exception", "Unexpected Exception")
            }
        }
    }
}