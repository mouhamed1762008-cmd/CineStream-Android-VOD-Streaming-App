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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.data.local.ContinueWatchingEntity
import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.repository.ContentRepository
import com.example.ui.components.CineTopHeader
import com.example.ui.components.ContinueWatchingCard
import com.example.ui.components.PosterCard
import com.example.ui.theme.CineBackground
import com.example.ui.theme.CineBorder
import com.example.ui.theme.CinePrimary
import com.example.ui.theme.CineRatingGold
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@Composable
fun HomeScreen(
    userName: String,
    avatarIndex: Int,
    continueWatchingList: List<ContinueWatchingEntity>,
    onContentClick: (ContentItem) -> Unit,
    onPlayClick: (ContentItem) -> Unit,
    onContinueWatchingClick: (ContinueWatchingEntity) -> Unit,
    onSearchClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredItem = remember { ContentRepository.getFeatured() }
    val trending = remember { ContentRepository.getTrending() }
    val popularMovies = remember { ContentRepository.getPopularMovies() }
    val recommended = remember { ContentRepository.getRecommended() }
    val topRated = remember { ContentRepository.getTopRated() }
    val popularSeries = remember { ContentRepository.getPopularSeries() }
    val recentlyAdded = remember { ContentRepository.getRecentlyAdded() }
    val actionAdventure = remember { ContentRepository.getByGenre("Action") }
    val comedy = remember { ContentRepository.getByGenre("Comedy") }
    val drama = remember { ContentRepository.getByGenre("Drama") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CineBackground)
            .testTag("home_screen_scroll"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top Header
        item {
            CineTopHeader(
                userName = userName,
                avatarIndex = avatarIndex,
                onSearchClick = onSearchClick,
                onProfileClick = onProfileClick
            )
        }

        // Hero Featured Banner
        item {
            HeroFeaturedBanner(
                item = featuredItem,
                onPlayClick = { onPlayClick(featuredItem) },
                onInfoClick = { onContentClick(featuredItem) }
            )
        }

        // Section: Continue Watching (only if items exist)
        if (continueWatchingList.isNotEmpty()) {
            item {
                SectionHeader(title = "Continue Watching")
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    items(continueWatchingList, key = { it.id }) { item ->
                        ContinueWatchingCard(
                            item = item,
                            onClick = { onContinueWatchingClick(item) }
                        )
                    }
                }
            }
        }

        // Section: Trending Now
        item {
            ContentSectionRow(
                title = "Trending Now",
                items = trending,
                onItemClick = onContentClick
            )
        }

        // Section: Popular Movies
        item {
            ContentSectionRow(
                title = "Popular Movies",
                items = popularMovies,
                onItemClick = onContentClick
            )
        }

        // Section: Recommended For You
        item {
            ContentSectionRow(
                title = "Recommended For You",
                items = recommended,
                onItemClick = onContentClick
            )
        }

        // Section: Top Rated
        item {
            ContentSectionRow(
                title = "Top Rated Movies & Shows",
                items = topRated,
                onItemClick = onContentClick
            )
        }

        // Section: Popular Series
        item {
            ContentSectionRow(
                title = "Popular Series",
                items = popularSeries,
                onItemClick = onContentClick
            )
        }

        // Section: Recently Added
        item {
            ContentSectionRow(
                title = "Recently Added",
                items = recentlyAdded,
                onItemClick = onContentClick
            )
        }

        // Section: Action & Adventure
        item {
            ContentSectionRow(
                title = "Action & Adventure",
                items = actionAdventure,
                onItemClick = onContentClick
            )
        }

        // Section: Comedy
        item {
            ContentSectionRow(
                title = "Comedy & Animation",
                items = comedy,
                onItemClick = onContentClick
            )
        }

        // Section: Drama
        item {
            ContentSectionRow(
                title = "Drama & Romance",
                items = drama,
                onItemClick = onContentClick
            )
        }
    }
}

@Composable
fun HeroFeaturedBanner(
    item: ContentItem,
    onPlayClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(440.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CineSurfaceVariant)
            .testTag("hero_featured_banner")
    ) {
        // High-res backdrop
        AsyncImage(
            model = item.backdropUrl,
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient fade to black at bottom and sides
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x66080A10),
                            Color(0xEE080A10),
                            CineBackground
                        ),
                        startY = 120f
                    )
                )
        )

        // Hero Info Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            // Genre and Type Badges
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = CinePrimary,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (item.type == ContentType.SERIES) "FEATURED SERIES" else "FEATURED MOVIE",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = CineRatingGold,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${item.rating} • ${item.releaseYear} • ${item.duration}",
                        color = CineTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 30.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.overview,
                color = CineTextSecondary,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: Play Now & More Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPlayClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CinePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("hero_play_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Play Now",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onInfoClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0x661A1F2E),
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("hero_info_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = CineTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "More Info",
                        color = CineTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ContentSectionRow(
    title: String,
    items: List<ContentItem>,
    onItemClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 10.dp)) {
        SectionHeader(title = title)

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            items(items, key = { it.id }) { item ->
                PosterCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}
