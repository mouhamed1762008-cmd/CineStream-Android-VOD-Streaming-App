package com.cinestream.app.data.model

enum class ContentType {
    MOVIE,
    SERIES
}

data class CastMember(
    val name: String,
    val role: String,
    val imageUrl: String
)

data class Episode(
    val id: String,
    val episodeNumber: Int,
    val seasonNumber: Int = 1,
    val title: String,
    val duration: String,
    val overview: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

data class Season(
    val seasonNumber: Int,
    val title: String = "Season $seasonNumber",
    val episodes: List<Episode>
)

data class ContentItem(
    val id: String,
    val title: String,
    val type: ContentType,
    val overview: String,
    val releaseYear: Int,
    val rating: Double,
    val ageRating: String = "16+",
    val duration: String, // e.g. "2h 15m" or "3 Seasons"
    val genres: List<String>,
    val posterUrl: String,
    val backdropUrl: String,
    val videoUrl: String, // Main movie stream or primary episode
    val director: String = "Christopher Nolan",
    val cast: List<CastMember> = emptyList(),
    val seasons: List<Season>? = null,
    val isFeatured: Boolean = false,
    val isTrending: Boolean = false,
    val isTopRated: Boolean = false,
    val isRecentlyAdded: Boolean = false
)
