package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.example.ui.theme.CineCard
import com.example.ui.theme.CinePrimary
import com.example.ui.theme.CineRatingGold
import com.example.ui.theme.CineSurfaceVariant
import com.example.ui.theme.CineTextMuted
import com.example.ui.theme.CineTextPrimary
import com.example.ui.theme.CineTextSecondary

@Composable
fun PosterCard(
    item: ContentItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Int? = 135,
    cardHeight: Int? = 200
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1.0f, label = "cardScale")

    val columnModifier = if (cardWidth != null) {
        modifier.width(cardWidth.dp)
    } else {
        modifier.fillMaxWidth()
    }

    val boxModifier = if (cardWidth != null && cardHeight != null) {
        Modifier
            .width(cardWidth.dp)
            .height(cardHeight.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .aspectRatio(2f / 3f)
    }

    Column(
        modifier = columnModifier
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .testTag("poster_card_${item.id}")
    ) {
        Box(
            modifier = boxModifier
                .clip(RoundedCornerShape(12.dp))
                .background(CineCard)
        ) {
            AsyncImage(
                model = item.posterUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Subtle gradient overlay at bottom of poster
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xAA080A10)),
                            startY = 180f
                        )
                    )
            )

            // Rating pill in top-right
            Surface(
                color = Color(0xCC080A10),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = CineRatingGold,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "%.1f".format(item.rating),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Type badge if Series
            if (item.type == ContentType.SERIES) {
                Surface(
                    color = CinePrimary,
                    shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 6.dp, bottomEnd = 6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "SERIES",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.title,
            color = CineTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${item.releaseYear} • ${if (item.type == ContentType.SERIES) item.duration else item.genres.firstOrNull() ?: ""}",
                color = CineTextMuted,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ContinueWatchingCard(
    item: ContinueWatchingEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (item.durationMs > 0) {
        (item.positionMs.toFloat() / item.durationMs.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val remainingMinutes = if (item.durationMs > item.positionMs) {
        ((item.durationMs - item.positionMs) / 60000).coerceAtLeast(1)
    } else 0

    Column(
        modifier = modifier
            .width(220.dp)
            .clickable(onClick = onClick)
            .testTag("continue_card_${item.id}")
    ) {
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(124.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CineCard)
        ) {
            AsyncImage(
                model = item.backdropUrl.ifBlank { item.posterUrl },
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Dark tint gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xDD080A10)),
                            startY = 80f
                        )
                    )
            )

            // Play overlay circle button
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0x99000000)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = CinePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Episode or remaining badge at bottom
            if (item.episodeTitle != null) {
                Text(
                    text = "S${item.seasonNumber ?: 1}:E${item.episodeNumber ?: 1} ${item.episodeTitle}",
                    color = CineTextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp, end = 8.dp)
                )
            } else {
                Text(
                    text = "${remainingMinutes}m remaining",
                    color = CineTextSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 8.dp)
                )
            }

            // Progress Bar at the absolute bottom
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.BottomCenter),
                color = CinePrimary,
                trackColor = CineSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            color = CineTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
