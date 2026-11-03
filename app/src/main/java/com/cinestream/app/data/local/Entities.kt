package com.cinestream.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val id: String,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
    val type: String, // MOVIE or SERIES
    val releaseYear: Int,
    val duration: String,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "continue_watching")
data class ContinueWatchingEntity(
    @PrimaryKey val id: String,
    val title: String,
    val backdropUrl: String,
    val posterUrl: String,
    val positionMs: Long,
    val durationMs: Long,
    val episodeId: String? = null,
    val episodeTitle: String? = null,
    val seasonNumber: Int? = null,
    val episodeNumber: Int? = null,
    val videoUrl: String,
    val type: String, // MOVIE or SERIES
    val lastWatchedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val searchedAt: Long = System.currentTimeMillis()
)
