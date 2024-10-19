package com.example.dicodingevent.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreference(private val dataStore: DataStore<Preferences>) {
    private val darkModeKey = booleanPreferencesKey("dark_mode")
    private val notificationKey = booleanPreferencesKey("notification")

    val darkMode: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[darkModeKey] ?: false
    }
    val notification: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[notificationKey] ?: false
    }

    suspend fun updateDarkMode(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[darkModeKey] = status
        }
    }

    suspend fun updateNotification(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[notificationKey] = status
        }
    }
}