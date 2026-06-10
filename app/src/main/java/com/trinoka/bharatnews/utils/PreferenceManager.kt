package com.trinoka.bharatnews.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferenceManager(context: Context) {

    private val dataStore = context.applicationContext.dataStore
    private val sharedPrefs = context.applicationContext.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
        val TEXT_SIZE_KEY = floatPreferencesKey("text_size")
        val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications")
    }

    val darkModeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_MODE_KEY] ?: false
    }

    val languageFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: sharedPrefs.getString("language", "en") ?: "en"
    }

    val textSizeFlow: Flow<Float> = dataStore.data.map { preferences ->
        preferences[TEXT_SIZE_KEY] ?: 16f
    }

    val notificationsFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_KEY] ?: true
    }

    suspend fun setDarkMode(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isEnabled
        }
    }

    suspend fun setLanguage(languageCode: String) {
        sharedPrefs.edit().putString("language", languageCode).apply()
        dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }

    suspend fun setTextSize(size: Float) {
        dataStore.edit { preferences ->
            preferences[TEXT_SIZE_KEY] = size
        }
    }

    suspend fun setNotifications(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_KEY] = isEnabled
        }
    }
    
    fun getSyncLanguage(): String {
        return sharedPrefs.getString("language", "en") ?: "en"
    }
}