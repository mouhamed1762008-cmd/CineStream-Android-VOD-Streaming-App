package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.repository.ContentRepository
import com.example.ui.MainViewModel
import com.example.ui.NavigationTab
import com.example.ui.components.CineBottomNavBar
import com.example.ui.screens.DetailsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MoviesScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SeriesScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.CineBackground
import com.example.ui.theme.CineStreamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineStreamTheme {
                CineStreamApp()
            }
        }
    }
}

@Composable
fun CineStreamApp(
    viewModel: MainViewModel = viewModel()
) {
    val isOnboarded by viewModel.isOnboarded.collectAsStateWithLifecycle()
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val avatarIndex by viewModel.avatarIndex.collectAsStateWithLifecycle()
    val streamQuality by viewModel.streamQuality.collectAsStateWithLifecycle()
    val autoplayNext by viewModel.autoplayNext.collectAsStateWithLifecycle()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsStateWithLifecycle()

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedDetailItem by viewModel.selectedDetailItem.collectAsStateWithLifecycle()
    val activePlayback by viewModel.activePlayback.collectAsStateWithLifecycle()

    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()
    val continueWatchingList by viewModel.continueWatchingList.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CineBackground)
    ) {
        // Screen 1: Onboarding flow if name not set
        if (!isOnboarded) {
            OnboardingScreen(
                onContinue = { name ->
                    viewModel.saveOnboardingName(name)
                }
            )
        } else {
            // Fullscreen Video Player (Screen 7) - completely takes over, hides bottom navigation
            val playback = activePlayback
            if (playback != null) {
                VideoPlayerScreen(
                    item = playback.contentItem,
                    initialEpisode = playback.episode,
                    initialPositionMs = playback.initialPositionMs,
                    onBackClick = { viewModel.closePlayer() },
                    onProgressUpdate = { posMs, durMs ->
                        viewModel.recordPlaybackProgress(
                            item = playback.contentItem,
                            episode = playback.episode,
                            positionMs = posMs,
                            durationMs = durMs
                        )
                    }
                )
            } else {
                // Main Application Layout with Floating Bottom Navigation
                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    containerColor = CineBackground,
                    bottomBar = {
                        // Only show bottom navigation when not in details view
                        if (selectedDetailItem == null) {
                            CineBottomNavBar(
                                currentTab = currentTab,
                                onTabSelected = { tab -> viewModel.switchTab(tab) }
                            )
                        }
                    }
                ) { _ ->
                    // Details Screen overlay or Main Tabs
                    val detailItem = selectedDetailItem
                    if (detailItem != null) {
                        DetailsScreen(
                            item = detailItem,
                            isWatchlisted = viewModel.isWatchlisted(detailItem.id),
                            onBackClick = { viewModel.closeDetails() },
                            onPlayClick = { item, episode ->
                                viewModel.playContent(item, episode)
                            },
                            onToggleWatchlist = { item ->
                                viewModel.toggleWatchlist(item)
                            },
                            onSimilarClick = { item ->
                                viewModel.openDetails(item)
                            }
                        )
                    } else {
                        AnimatedContent(
                            targetState = currentTab,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "tabContent"
                        ) { tab ->
                            when (tab) {
                                NavigationTab.HOME -> {
                                    HomeScreen(
                                        userName = userName,
                                        avatarIndex = avatarIndex,
                                        continueWatchingList = continueWatchingList,
                                        onContentClick = { item -> viewModel.openDetails(item) },
                                        onPlayClick = { item -> viewModel.playContent(item) },
                                        onContinueWatchingClick = { cwItem ->
                                            val content = ContentRepository.getContentById(cwItem.id)
                                            if (content != null) {
                                                val episode = content.seasons?.flatMap { it.episodes }
                                                    ?.find { it.id == cwItem.episodeId }
                                                viewModel.playContent(content, episode, cwItem.positionMs)
                                            }
                                        },
                                        onSearchClick = { viewModel.switchTab(NavigationTab.SEARCH) },
                                        onProfileClick = { viewModel.switchTab(NavigationTab.PROFILE) }
                                    )
                                }

                                NavigationTab.MOVIES -> {
                                    MoviesScreen(
                                        onMovieClick = { movie -> viewModel.openDetails(movie) },
                                        onPlayClick = { movie -> viewModel.playContent(movie) }
                                    )
                                }

                                NavigationTab.SERIES -> {
                                    SeriesScreen(
                                        onSeriesClick = { series -> viewModel.openDetails(series) },
                                        onPlayClick = { series -> viewModel.playContent(series) }
                                    )
                                }

                                NavigationTab.SEARCH -> {
                                    SearchScreen(
                                        recentSearches = recentSearches,
                                        onContentClick = { item -> viewModel.openDetails(item) },
                                        onRecordSearch = { q -> viewModel.recordSearchQuery(q) },
                                        onRemoveRecentSearch = { q -> viewModel.removeSearchQuery(q) },
                                        onClearAllRecentSearches = { viewModel.clearSearchHistory() }
                                    )
                                }

                                NavigationTab.PROFILE -> {
                                    ProfileScreen(
                                        userName = userName,
                                        avatarIndex = avatarIndex,
                                        streamQuality = streamQuality,
                                        autoplayNext = autoplayNext,
                                        notificationsEnabled = notificationsEnabled,
                                        watchlist = watchlist,
                                        continueWatchingList = continueWatchingList,
                                        onUpdateUserName = { newName -> viewModel.updateUserName(newName) },
                                        onAvatarChange = { newIdx -> viewModel.setAvatarIndex(newIdx) },
                                        onQualityChange = { q -> viewModel.setStreamQuality(q) },
                                        onAutoplayToggle = { enabled -> viewModel.setAutoplayNext(enabled) },
                                        onNotificationsToggle = { enabled -> viewModel.setNotifications(enabled) },
                                        onWatchlistClick = { item -> viewModel.openDetails(item) },
                                        onContinueWatchingClick = { cwItem ->
                                            val content = ContentRepository.getContentById(cwItem.id)
                                            if (content != null) {
                                                val episode = content.seasons?.flatMap { it.episodes }
                                                    ?.find { it.id == cwItem.episodeId }
                                                viewModel.playContent(content, episode, cwItem.positionMs)
                                            }
                                        },
                                        onRemoveContinueWatching = { id -> viewModel.removeContinueWatching(id) },
                                        onLogout = { viewModel.logout() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
