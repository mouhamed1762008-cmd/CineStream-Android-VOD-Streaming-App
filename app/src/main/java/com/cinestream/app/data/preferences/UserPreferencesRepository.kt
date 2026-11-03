package com.cinestream.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cinestream_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
        val STREAM_QUALITY = stringPreferencesKey("stream_quality")
        val AUTOPLAY_NEXT = booleanPreferencesKey("autoplay_next")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val AVATAR_INDEX = intPreferencesKey("avatar_index")
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: ""
    }

    val isOnboardedFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_ONBOARDED] ?: false
    }

    val streamQualityFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.STREAM_QUALITY] ?: "1080p Ultra HD"
    }

    val autoplayNextFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AUTOPLAY_NEXT] ?: true
    }

    val notificationsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
    }

    val avatarIndexFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.AVATAR_INDEX] ?: 0
    }

    suspend fun saveUserOnboarding(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name.trim()
            preferences[PreferencesKeys.IS_ONBOARDED] = true
        }
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name.trim()
        }
    }

    suspend fun updateStreamQuality(quality: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.STREAM_QUALITY] = quality
        }
    }

    suspend fun setAutoplayNext(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTOPLAY_NEXT] = enabled
        }
    }

    suspend fun setNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setAvatarIndex(index: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AVATAR_INDEX] = index
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDED] = false
            preferences[PreferencesKeys.USER_NAME] = ""
        }
    }
}
