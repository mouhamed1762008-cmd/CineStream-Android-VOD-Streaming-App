package com.cinestream.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cinestream.app.data.model.ContentItem
import com.cinestream.app.data.model.ContentType
import com.cinestream.app.data.repository.ContentRepository
import com.cinestream.app.ui.components.PosterCard
import com.cinestream.app.ui.theme.CineBackground
import com.cinestream.app.ui.theme.CineBorder
import com.cinestream.app.ui.theme.CineCard
import com.cinestream.app.ui.theme.CinePrimary
import com.cinestream.app.ui.theme.CineRatingGold
import com.cinestream.app.ui.theme.CineSurfaceVariant
import com.cinestream.app.ui.theme.CineTextMuted
import com.cinestream.app.ui.theme.CineTextPrimary
import com.cinestream.app.ui.theme.CineTextSecondary

enum class SeriesSortOption(val label: String) {
    POPULARITY("Most Popular"),
    RATING("Highest Rated"),
    SEASONS("Most Seasons"),
    NEWEST("Latest Release")
}

@Composable
fun SeriesScreen(
    onSeriesClick: (ContentItem) -> Unit,
    onPlayClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGenre by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf(SeriesSortOption.POPULARITY) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val allSeries = remember { ContentRepository.getAllSeries() }
    val featuredSeries = remember { allSeries.firstOrNull { it.isFeatured } ?: allSeries.first() }

    val filteredSeries = remember(selectedGenre, selectedSort) {
        val list = if (selectedGenre == "All") {
            allSeries
        } else {
            allSeries.filter { it.genres.any { g -> g.equals(selectedGenre, ignoreCase = true) } }
        }

        when (selectedSort) {
            SeriesSortOption.POPULARITY -> list.sortedByDescending { it.rating * (if (it.isTrending) 1.2 else 1.0) }
            SeriesSortOption.RATING -> list.sortedByDescending { it.rating }
            SeriesSortOption.SEASONS -> list.sortedByDescending { it.seasons?.size ?: 1 }
            SeriesSortOption.NEWEST -> list.sortedByDescending { it.releaseYear }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CineBackground)
            .statusBarsPadding()
            .testTag("series_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Screen Title Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "TV Series",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Binge-worthy sagas and limited series",
                        color = CineTextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Sorting Menu Button
                Box {
                    Surface(
                        color = CineSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                        modifier = Modifier.clickable { sortMenuExpanded = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "Sort",
                                tint = CinePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedSort.label,
                                color = CineTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = { sortMenuExpanded = false },
                        modifier = Modifier.background(CineCard)
                    ) {
                        SeriesSortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.label,
                                        color = if (option == selectedSort) CinePrimary else CineTextPrimary,
                                        fontWeight = if (option == selectedSort) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    selectedSort = option
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Featured Series Hero
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CineCard)
            ) {
                AsyncImage(
                    model = featuredSeries.backdropUrl,
                    contentDescription = featuredSeries.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x88080A10),
                                    CineBackground
                                ),
                                startY = 80f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        color = CinePrimary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "SPOTLIGHT SERIES",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = featuredSeries.title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${featuredSeries.rating} ★ • ${featuredSeries.duration} • ${featuredSeries.releaseYear}",
                        color = CineRatingGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onPlayClick(featuredSeries) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CinePrimary),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Watch Season 1", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { onSeriesClick(featuredSeries) },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text("Episodes", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Genre Filter Chips Horizontal Row
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                items(ContentRepository.GENRES) { genre ->
                    val isSelected = genre == selectedGenre
                    Surface(
                        color = if (isSelected) CinePrimary else CineSurfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CinePrimary else CineBorder
                        ),
                        modifier = Modifier.clickable { selectedGenre = genre }
                    ) {
                        Text(
                            text = genre,
                            color = if (isSelected) Color.White else CineTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Section Title with count
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedGenre == "All") "All TV Series" else "$selectedGenre Series",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${filteredSeries.size} Titles",
                    color = CineTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Series Cards List
        items(filteredSeries, key = { it.id }) { series ->
            SeriesDetailRowCard(
                item = series,
                onClick = { onSeriesClick(series) },
                onPlayClick = { onPlayClick(series) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun SeriesDetailRowCard(
    item: ContentItem,
    onClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CineCard,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(110.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CineSurfaceVariant)
            ) {
                AsyncImage(
                    model = item.posterUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = CineRatingGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "%.1f".format(item.rating),
                            color = CineRatingGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "•",
                        color = CineTextMuted,
                        fontSize = 11.sp
                    )

                    Surface(
                        color = CinePrimary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = item.duration, // e.g. "3 Seasons"
                            color = CinePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "${item.releaseYear}",
                        color = CineTextMuted,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.overview,
                    color = CineTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.genres.joinToString(" • "),
                    color = CineTextMuted,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
