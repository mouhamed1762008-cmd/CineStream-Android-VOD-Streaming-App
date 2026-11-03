package com.cinestream.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cinestream.app.data.local.ContinueWatchingEntity
import com.cinestream.app.data.local.WatchlistEntity
import com.cinestream.app.data.model.ContentItem
import com.cinestream.app.data.repository.ContentRepository
import com.cinestream.app.ui.components.AVATAR_COLORS
import com.cinestream.app.ui.components.AVATAR_EMOJIS
import com.cinestream.app.ui.components.ContinueWatchingCard
import com.cinestream.app.ui.components.PosterCard
import com.cinestream.app.ui.theme.CineBackground
import com.cinestream.app.ui.theme.CineBorder
import com.cinestream.app.ui.theme.CineCard
import com.cinestream.app.ui.theme.CinePrimary
import com.cinestream.app.ui.theme.CineSurfaceVariant
import com.cinestream.app.ui.theme.CineTextMuted
import com.cinestream.app.ui.theme.CineTextPrimary
import com.cinestream.app.ui.theme.CineTextSecondary

@Composable
fun ProfileScreen(
    userName: String,
    avatarIndex: Int,
    streamQuality: String,
    autoplayNext: Boolean,
    notificationsEnabled: Boolean,
    watchlist: List<WatchlistEntity>,
    continueWatchingList: List<ContinueWatchingEntity>,
    onUpdateUserName: (String) -> Unit,
    onAvatarChange: (Int) -> Unit,
    onQualityChange: (String) -> Unit,
    onAutoplayToggle: (Boolean) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onWatchlistClick: (ContentItem) -> Unit,
    onContinueWatchingClick: (ContinueWatchingEntity) -> Unit,
    onRemoveContinueWatching: (String) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onNotificationsToggle(isGranted)
    }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showQualityDialog by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    var editedName by remember(userName) { mutableStateOf(userName) }

    val avatarColor = AVATAR_COLORS.getOrElse(avatarIndex % AVATAR_COLORS.size) { CinePrimary }
    val avatarEmoji = AVATAR_EMOJIS.getOrElse(avatarIndex % AVATAR_EMOJIS.size) { "🎬" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CineBackground)
            .statusBarsPadding()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // User Profile Header Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Profile & Settings",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = CineCard,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Avatar with emoji & cycle click
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(avatarColor)
                                    .clickable { onAvatarChange((avatarIndex + 1) % AVATAR_COLORS.size) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = avatarEmoji,
                                    fontSize = 32.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userName.ifBlank { "CineStream Viewer" },
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Premium Ultra HD Tier",
                                    color = CinePrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Tap avatar to change emoji",
                                    color = CineTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            IconButton(
                                onClick = { showEditProfileDialog = true },
                                modifier = Modifier.testTag("profile_edit_name_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = CinePrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: My Watchlist
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = CinePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "My Watchlist",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "${watchlist.size} Items",
                        color = CineTextMuted,
                        fontSize = 12.sp
                    )
                }

                if (watchlist.isEmpty()) {
                    Surface(
                        color = CineCard,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Your watchlist is empty. Tap '+ Add to List' on any movie or series to save for later.",
                            color = CineTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        items(watchlist, key = { it.id }) { item ->
                            val content = ContentRepository.getContentById(item.id)
                            if (content != null) {
                                PosterCard(
                                    item = content,
                                    onClick = { onWatchlistClick(content) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section: Continue Watching
        if (continueWatchingList.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = CinePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Continue Watching",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${continueWatchingList.size} in progress",
                            color = CineTextMuted,
                            fontSize = 12.sp
                        )
                    }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        items(continueWatchingList, key = { it.id }) { item ->
                            Box {
                                ContinueWatchingCard(
                                    item = item,
                                    onClick = { onContinueWatchingClick(item) }
                                )

                                IconButton(
                                    onClick = { onRemoveContinueWatching(item.id) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xAA000000))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Playback & App Settings
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Preferences & Settings",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Surface(
                    color = CineCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.HighQuality,
                            title = "Streaming Quality",
                            subtitle = streamQuality,
                            onClick = { showQualityDialog = true }
                        )

                        SettingsSwitchRow(
                            icon = Icons.Default.PlayCircle,
                            title = "Autoplay Next Episode",
                            subtitle = "Automatically chain episodes",
                            checked = autoplayNext,
                            onCheckedChange = onAutoplayToggle
                        )

                        SettingsSwitchRow(
                            icon = Icons.Default.Notifications,
                            title = "Release Notifications",
                            subtitle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                "Alerts on new seasons & blockbusters"
                            } else {
                                "Alerts on new seasons & blockbusters"
                            },
                            checked = notificationsEnabled,
                            onCheckedChange = { enable ->
                                if (enable) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        val hasPermission = ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) == PackageManager.PERMISSION_GRANTED
                                        if (hasPermission) {
                                            onNotificationsToggle(true)
                                        } else {
                                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                        }
                                    } else {
                                        // On Android 7.0 (API 24) to Android 12 (API 32), notifications are enabled without runtime prompt
                                        onNotificationsToggle(true)
                                    }
                                } else {
                                    onNotificationsToggle(false)
                                }
                            }
                        )

                        SettingsRow(
                            icon = Icons.Default.Palette,
                            title = "App Theme",
                            subtitle = "Electric Purple Dark Mode (Active)",
                            onClick = { }
                        )

                        SettingsRow(
                            icon = Icons.Default.Language,
                            title = "Language & Audio",
                            subtitle = "English (United States)",
                            onClick = { }
                        )
                    }
                }
            }
        }

        // Section: About & Legal
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "About & Legal",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Surface(
                    color = CineCard,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CineBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        SettingsRow(
                            icon = Icons.Default.Info,
                            title = "About CineStream",
                            subtitle = "Version 1.0.0 • Modern Android VOD",
                            onClick = { showAboutDialog = true }
                        )

                        SettingsRow(
                            icon = Icons.Default.Security,
                            title = "Privacy Policy & Terms",
                            subtitle = "Authorized open streams and data policy",
                            onClick = { showPrivacyDialog = true }
                        )
                    }
                }
            }
        }

        // Section: Log Out Button
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = { showLogoutConfirm = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626).copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDC2626)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("profile_logout_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reset Profile & Log Out",
                            color = Color(0xFFEF4444),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Edit Profile Name Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = CineCard,
            title = { Text("Edit Display Name", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CinePrimary,
                        unfocusedBorderColor = CineBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editedName.isNotBlank()) {
                            onUpdateUserName(editedName)
                            showEditProfileDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CinePrimary)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = CineTextSecondary)
                }
            }
        )
    }

    // Quality Dialog
    if (showQualityDialog) {
        AlertDialog(
            onDismissRequest = { showQualityDialog = false },
            containerColor = CineCard,
            title = { Text("Playback Quality", color = Color.White) },
            text = {
                Column {
                    listOf("Auto (Adaptive)", "1080p Ultra HD", "4K Ultra HD", "Data Saver (720p)").forEach { q ->
                        TextButton(
                            onClick = {
                                onQualityChange(q)
                                showQualityDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = q,
                                    color = if (streamQuality == q) CinePrimary else CineTextPrimary
                                )
                                if (streamQuality == q) Text("✓", color = CinePrimary)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showQualityDialog = false }) {
                    Text("Close", color = CinePrimary)
                }
            }
        )
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            containerColor = CineCard,
            title = { Text("About CineStream", color = Color.White) },
            text = {
                Text(
                    text = "CineStream is a modern Android Video-on-Demand application built with Jetpack Compose, Material Design 3, Media3 ExoPlayer, and Room persistence.\n\nFeaturing verified open-source cinematics from the Blender Foundation and independent studio productions.",
                    color = CineTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showAboutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CinePrimary)
                ) {
                    Text("Got it")
                }
            }
        )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            containerColor = CineCard,
            title = { Text("Terms & Privacy Policy", color = Color.White) },
            text = {
                Text(
                    text = "All video media stream assets are licensed under Creative Commons and authorized open distribution.\n\nYour viewing history, continue watching positions, and watchlist are securely stored locally on this device using Android Room Database.",
                    color = CineTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CinePrimary)
                ) {
                    Text("Accept")
                }
            }
        )
    }

    // Logout Confirm Dialog
    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            containerColor = CineCard,
            title = { Text("Log Out", color = Color.White) },
            text = {
                Text(
                    text = "Are you sure you want to reset your user profile? You can re-enter your name anytime.",
                    color = CineTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutConfirm = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) {
                    Text("Cancel", color = CineTextSecondary)
                }
            }
        )
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CineSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CinePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = CineTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = CineTextMuted,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CineSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CinePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = CineTextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = CinePrimary,
                uncheckedThumbColor = CineTextSecondary,
                uncheckedTrackColor = CineSurfaceVariant
            )
        )
    }
}
