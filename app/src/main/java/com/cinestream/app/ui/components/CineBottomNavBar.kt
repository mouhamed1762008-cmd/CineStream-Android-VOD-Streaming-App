package com.cinestream.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinestream.app.ui.NavigationTab
import com.cinestream.app.ui.theme.CineAccentGlow
import com.cinestream.app.ui.theme.CineBorder
import com.cinestream.app.ui.theme.CinePrimary
import com.cinestream.app.ui.theme.CineSurface
import com.cinestream.app.ui.theme.CineTextMuted

data class TabItem(
    val tab: NavigationTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun CineBottomNavBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        TabItem(NavigationTab.HOME, Icons.Filled.Home, Icons.Outlined.Home, "bottom_tab_home"),
        TabItem(NavigationTab.MOVIES, Icons.Filled.Movie, Icons.Outlined.Movie, "bottom_tab_movies"),
        TabItem(NavigationTab.SERIES, Icons.Filled.Tv, Icons.Outlined.Tv, "bottom_tab_series"),
        TabItem(NavigationTab.SEARCH, Icons.Filled.Search, Icons.Outlined.Search, "bottom_tab_search"),
        TabItem(NavigationTab.PROFILE, Icons.Filled.Person, Icons.Outlined.Person, "bottom_tab_profile")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = CineSurface.copy(alpha = 0.95f),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tabItem ->
                    val isSelected = tabItem.tab == currentTab
                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else CineTextMuted,
                        label = "iconColor"
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1.0f,
                        label = "tabScale"
                    )

                    val interactionSource = remember { MutableInteractionSource() }

                    Column(
                        modifier = Modifier
                            .scale(scale)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { onTabSelected(tabItem.tab) }
                            )
                            .padding(vertical = 4.dp, horizontal = 10.dp)
                            .testTag(tabItem.testTag),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) {
                                        Brush.linearGradient(listOf(CinePrimary, CineAccentGlow))
                                    } else {
                                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) tabItem.selectedIcon else tabItem.unselectedIcon,
                                contentDescription = tabItem.tab.label,
                                tint = iconColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = tabItem.tab.label,
                            color = if (isSelected) CinePrimary else CineTextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
