package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CastMember
import com.example.data.model.ContentItem
import com.example.data.model.ContentType
import com.example.data.model.Episode
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

@Composable
fun DetailsScreen(
    item: ContentItem,
    isWatchlisted: Boolean,
    onBackClick: () -> Unit,
    onPlayClick: (ContentItem, Episode?) -> Unit,
    onToggleWatchlist: (ContentItem) -> Unit,
    onSimilarClick: (ContentItem) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackClick)
    val context = LocalContext.current
    val similarItems = remember(item.id) { ContentRepository.getSimilar(item) }

    var selectedSeasonIndex by remember { mutableIntStateOf(0) }
    val seasons = item.seasons ?: emptyList()
    val activeSeason = seasons.getOrNull(selectedSeasonIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CineBackground)
            .testTag("details_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Backdrop with Gradient Overlay & Back Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                AsyncImage(
                    model = item.backdropUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dramatic Dark Scrim Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x99080A10),
                                    Color(0x44080A10),
                                    Color(0xCC080A10),
                                    CineBackground
                                )
                            )
                        )
                )

                // Top Back Button & Share Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x88080A10))
                            .testTag("details_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Watch ${item.title} on CineStream! High definition cinematic streaming.")
                                type = "text/plain"
                            }
                            try {
                                val chooserIntent = Intent.createChooser(sendIntent, "Share with").apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(chooserIntent)
                            } catch (_: Exception) { }
                        },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x88080A10))
                            .testTag("details_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }

                // Poster & Title Header overlay near bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Floating Poster
                    Box(
                        modifier = Modifier
                            .width(96.dp)
                            .height(144.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CineSurfaceVariant)
                    ) {
                        AsyncImage(
                            model = item.posterUrl,
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Surface(
                            color = CinePrimary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (item.type == ContentType.SERIES) "TV SERIES" else "FEATURE FILM",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = item.title,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Metadata line: Rating, Year, Age, Duration
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = CineRatingGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "%.1f".format(item.rating),
                                    color = CineRatingGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(text = "•", color = CineTextMuted, fontSize = 12.sp)

                            Text(
                                text = "${item.releaseYear}",
                                color = CineTextSecondary,
                                fontSize = 12.sp
                            )

                            Text(text = "•", color = CineTextMuted, fontSize = 12.sp)

                            Surface(
                                color = Color(0x33FFFFFF),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = item.ageRating,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }

                            Text(text = "•", color = CineTextMuted, fontSize = 12.sp)

                            Text(
                                text = item.duration,
                                color = CineTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons Row (Play, Watchlist, Share)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Play Button
                Button(
                    onClick = { onPlayClick(item, null) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CinePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("details_play_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (item.type == ContentType.SERIES) "Watch S1:E1" else "Play Movie",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Watchlist Toggle Button
                OutlinedButton(
                    onClick = { onToggleWatchlist(item) },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isWatchlisted) CinePrimary.copy(alpha = 0.2f) else CineSurfaceVariant,
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isWatchlisted) CinePrimary else CineBorder
                    ),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("details_watchlist_button")
                ) {
                    Icon(
                        imageVector = if (isWatchlisted) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        tint = if (isWatchlisted) CinePrimary else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isWatchlisted) "In My List" else "Add to List",
                        color = if (isWatchlisted) CinePrimary else Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Genre Tags Row
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                items(item.genres) { genre ->
                    Surface(
                        color = CineSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder)
                    ) {
                        Text(
                            text = genre,
                            color = CineTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Synopsis Overview
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Storyline",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.overview,
                    color = CineTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Director: ${item.director}",
                    color = CineTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        // Cast & Crew Carousel
        if (item.cast.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Cast & Characters",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(item.cast) { castMember ->
                            CastMemberCard(castMember = castMember)
                        }
                    }
                }
            }
        }

        // If Series: Show Season Selector Tabs & Episode List
        if (item.type == ContentType.SERIES && seasons.isNotEmpty()) {
            item {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Text(
                        text = "Episodes",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Season Tabs
                    ScrollableTabRow(
                        selectedTabIndex = selectedSeasonIndex,
                        containerColor = CineBackground,
                        contentColor = CinePrimary,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedSeasonIndex]),
                                color = CinePrimary
                            )
                        }
                    ) {
                        seasons.forEachIndexed { index, season ->
                            Tab(
                                selected = selectedSeasonIndex == index,
                                onClick = { selectedSeasonIndex = index },
                                text = {
                                    Text(
                                        text = season.title,
                                        fontWeight = if (selectedSeasonIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        color = if (selectedSeasonIndex == index) CinePrimary else CineTextMuted,
                                        fontSize = 13.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Episode Items
            activeSeason?.let { season ->
                items(season.episodes, key = { it.id }) { episode ->
                    EpisodeRowItem(
                        episode = episode,
                        onWatchClick = { onPlayClick(item, episode) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }

        // Similar Movies / Series Section
        if (similarItems.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = if (item.type == ContentType.SERIES) "More Like This Series" else "Similar Movies",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        items(similarItems, key = { it.id }) { similar ->
                            PosterCard(
                                item = similar,
                                onClick = { onSimilarClick(similar) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CastMemberCard(
    castMember: CastMember,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(CineSurfaceVariant)
        ) {
            AsyncImage(
                model = castMember.imageUrl,
                contentDescription = castMember.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = castMember.name,
            color = CineTextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = castMember.role,
            color = CineTextMuted,
            fontSize = 10.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EpisodeRowItem(
    episode: Episode,
    onWatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CineCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
        modifier = modifier.clickable(onClick = onWatchClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Episode Thumbnail with Play icon overlay
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CineSurfaceVariant)
            ) {
                AsyncImage(
                    model = episode.thumbnailUrl,
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x44000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Episode",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EP ${episode.episodeNumber}. ${episode.title}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = episode.duration,
                        color = CinePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = episode.overview,
                    color = CineTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
