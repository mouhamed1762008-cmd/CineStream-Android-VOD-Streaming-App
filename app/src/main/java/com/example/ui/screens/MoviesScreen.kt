package com.example.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ContentItem
import com.example.data.repository.ContentRepository
import com.example.ui.components.PosterCard
import com.example.ui.theme.CineBackground
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CineCard
import com.example.ui.theme.CinePrimary
import com.example.ui.theme.CineRatingGold
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

enum class MovieSortOption(val label: String) {
    POPULARITY("Popularity"),
    RATING("Rating"),
    RELEASE_YEAR("Release Year")
}

@Composable
fun MoviesScreen(
    onMovieClick: (ContentItem) -> Unit,
    onPlayClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedGenre by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf(MovieSortOption.POPULARITY) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val allMovies = remember { ContentRepository.getAllMovies() }
    val featuredMovie = remember { allMovies.firstOrNull { it.isFeatured } ?: allMovies.first() }

    val filteredMovies = remember(selectedGenre, selectedSort) {
        val list = if (selectedGenre == "All") {
            allMovies
        } else {
            allMovies.filter { it.genres.any { g -> g.equals(selectedGenre, ignoreCase = true) } }
        }

        when (selectedSort) {
            MovieSortOption.POPULARITY -> list.sortedByDescending { it.rating * (if (it.isTrending) 1.2 else 1.0) }
            MovieSortOption.RATING -> list.sortedByDescending { it.rating }
            MovieSortOption.RELEASE_YEAR -> list.sortedByDescending { it.releaseYear }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CineBackground)
            .statusBarsPadding()
            .testTag("movies_screen"),
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
                        text = "Movies",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Feature films, blockbusters & classics",
                        color = CineTextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Sorting Menu
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
                        MovieSortOption.values().forEach { option ->
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

        // Featured Movie Banner
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
                    model = featuredMovie.backdropUrl,
                    contentDescription = featuredMovie.title,
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
                            text = "SPOTLIGHT MOVIE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = featuredMovie.title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${featuredMovie.rating} ★ • ${featuredMovie.releaseYear} • ${featuredMovie.duration}",
                        color = CineRatingGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onPlayClick(featuredMovie) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CinePrimary),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = { onMovieClick(featuredMovie) },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier.height(38.dp)
                        ) {
                            Text("Details", fontSize = 12.sp)
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
                    text = if (selectedGenre == "All") "All Feature Films" else "$selectedGenre Movies",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${filteredMovies.size} Movies",
                    color = CineTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Movies Grid Display
        item {
            val rows = filteredMovies.chunked(3)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rows.forEach { rowMovies ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowMovies.forEach { movie ->
                            Box(modifier = Modifier.weight(1f)) {
                                PosterCard(
                                    item = movie,
                                    onClick = { onMovieClick(movie) },
                                    cardWidth = null,
                                    cardHeight = null
                                )
                            }
                        }
                        // Fill extra slot if row has fewer than 3 items
                        repeat(3 - rowMovies.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
