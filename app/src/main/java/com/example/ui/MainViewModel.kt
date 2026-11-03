package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CineStreamDatabase
import com.example.data.local.WatchlistEntity
import com.example.data.local.ContinueWatchingEntity
import com.example.data.model.ContentItem
import com.example.data.model.Episode
import com.example.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String) {
    HOME("Home"),
    MOVIES("Movies"),
    SERIES("Series"),
    SEARCH("Search"),
    PROFILE("Profile")
}

data class PlayerPlaybackArgs(
    val contentItem: ContentItem,
    val episode: Episode? = null,
    val initialPositionMs: Long = 0L
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferencesRepository(application)
    private val database = CineStreamDatabase.getInstance(application)
    private val watchlistDao = database.watchlistDao()
    private val continueWatchingDao = database.continueWatchingDao()
    private val searchHistoryDao = database.searchHistoryDao()

    val userName: StateFlow<String> = userPrefs.userNameFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val isOnboarded: StateFlow<Boolean> = userPrefs.isOnboardedFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val avatarIndex: StateFlow<Int> = userPrefs.avatarIndexFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val streamQuality: StateFlow<String> = userPrefs.streamQualityFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, "1080p Ultra HD")

    val autoplayNext: StateFlow<Boolean> = userPrefs.autoplayNextFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val notificationsEnabled: StateFlow<Boolean> = userPrefs.notificationsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _selectedDetailItem = MutableStateFlow<ContentItem?>(null)
    val selectedDetailItem: StateFlow<ContentItem?> = _selectedDetailItem.asStateFlow()

    private val _activePlayback = MutableStateFlow<PlayerPlaybackArgs?>(null)
    val activePlayback: StateFlow<PlayerPlaybackArgs?> = _activePlayback.asStateFlow()

    val watchlist: StateFlow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val continueWatchingList: StateFlow<List<ContinueWatchingEntity>> = continueWatchingDao.getAllContinueWatching()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSearches: StateFlow<List<String>> = searchHistoryDao.getRecentSearches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveOnboardingName(name: String) {
        viewModelScope.launch {
            userPrefs.saveUserOnboarding(name)
        }
    }

    fun updateUserName(name: String) {
        viewModelScope.launch {
            userPrefs.updateUserName(name)
        }
    }

    fun setAvatarIndex(index: Int) {
        viewModelScope.launch {
            userPrefs.setAvatarIndex(index)
        }
    }

    fun setStreamQuality(quality: String) {
        viewModelScope.launch {
            userPrefs.updateStreamQuality(quality)
        }
    }

    fun setAutoplayNext(enabled: Boolean) {
        viewModelScope.launch {
            userPrefs.setAutoplayNext(enabled)
        }
    }

    fun setNotifications(enabled: Boolean) {
        viewModelScope.launch {
            userPrefs.setNotifications(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.logout()
            _activePlayback.value = null
            _selectedDetailItem.value = null
            _currentTab.value = NavigationTab.HOME
        }
    }

    fun switchTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun openDetails(item: ContentItem) {
        _selectedDetailItem.value = item
    }

    fun closeDetails() {
        _selectedDetailItem.value = null
    }

    fun playContent(item: ContentItem, episode: Episode? = null, initialPositionMs: Long = 0L) {
        _activePlayback.value = PlayerPlaybackArgs(
            contentItem = item,
            episode = episode,
            initialPositionMs = initialPositionMs
        )
    }

    fun closePlayer() {
        _activePlayback.value = null
    }

    fun toggleWatchlist(item: ContentItem) {
        viewModelScope.launch {
            val exists = watchlist.value.any { it.id == item.id }
            if (exists) {
                watchlistDao.deleteById(item.id)
            } else {
                watchlistDao.insert(
                    WatchlistEntity(
                        id = item.id,
                        title = item.title,
                        posterUrl = item.posterUrl,
                        backdropUrl = item.backdropUrl,
                        rating = item.rating,
                        type = item.type.name,
                        releaseYear = item.releaseYear,
                        duration = item.duration
                    )
                )
            }
        }
    }

    fun isWatchlisted(itemId: String): Boolean {
        return watchlist.value.any { it.id == itemId }
    }

    fun recordPlaybackProgress(
        item: ContentItem,
        episode: Episode?,
        positionMs: Long,
        durationMs: Long
    ) {
        viewModelScope.launch {
            if (durationMs <= 0) return@launch
            // Don't save if watched less than 5 seconds or virtually done (>95%)
            if (positionMs < 5000L || positionMs > (durationMs * 0.95)) {
                if (positionMs > (durationMs * 0.95)) {
                    continueWatchingDao.deleteById(item.id)
                }
                return@launch
            }

            continueWatchingDao.insertOrUpdate(
                ContinueWatchingEntity(
                    id = item.id,
                    title = item.title,
                    backdropUrl = item.backdropUrl,
                    posterUrl = item.posterUrl,
                    positionMs = positionMs,
                    durationMs = durationMs,
                    episodeId = episode?.id,
                    episodeTitle = episode?.title,
                    seasonNumber = episode?.seasonNumber,
                    episodeNumber = episode?.episodeNumber,
                    videoUrl = episode?.videoUrl ?: item.videoUrl,
                    type = item.type.name,
                    lastWatchedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun removeContinueWatching(id: String) {
        viewModelScope.launch {
            continueWatchingDao.deleteById(id)
        }
    }

    fun recordSearchQuery(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            searchHistoryDao.insertSearch(
                com.example.data.local.SearchHistoryEntity(
                    query = query.trim()
                )
            )
        }
    }

    fun removeSearchQuery(query: String) {
        viewModelScope.launch {
            searchHistoryDao.deleteSearch(query)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            searchHistoryDao.clearHistory()
        }
    }
}
