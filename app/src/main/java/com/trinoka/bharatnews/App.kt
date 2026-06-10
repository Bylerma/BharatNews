package com.trinoka.bharatnews

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.trinoka.bharatnews.utils.PreferenceManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val preferenceManager = PreferenceManager(this)
        MainScope().launch {
            val isDarkMode = preferenceManager.darkModeFlow.first()
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    override fun attachBaseContext(base: Context) {
        val sharedPrefs = base.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val languageCode = sharedPrefs.getString("language", "en") ?: "en"
        super.attachBaseContext(updateLocale(base, languageCode))
    }

    private fun updateLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }
}