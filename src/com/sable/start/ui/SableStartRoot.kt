package org.sableos.start.ui

import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.sableos.start.live.LiveAvailability
import org.sableos.start.live.LiveDatum
import org.sableos.start.live.LiveSurfaceSnapshot
import org.sableos.start.model.AppEntry
import org.sableos.start.platform.LiveSurfaceRepository
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class SableStartScreen {
    Start,
    Apps,
    Context,
    Search,
    Pinned,
    Settings,
    Live,
    LockPreview,
}

private val SableBlue = Color(0xFF4D9CFF)
private val SableGreen = Color(0xFF35C66B)
private val SablePurple = Color(0xFF7A42E8)
private val SableSlate = Color(0xFF45515F)
private val SableOrange = Color(0xFFF28C45)
private val SablePanel = Color(0xFF17191D)
private val SablePanelRaised = Color(0xFF22252A)
private val SableMuted = Color(0xFFB8BBC3)

private val SableColors =
    darkColorScheme(
        primary = SableBlue,
        onPrimary = Color.Black,
        background = Color.Black,
        onBackground = Color.White,
        surface = SablePanel,
        onSurface = Color.White,
        surfaceVariant = SablePanelRaised,
        onSurfaceVariant = SableMuted,
    )

private val SableTypography =
    Typography(
        displayLarge =
            TextStyle(
                fontSize = 58.sp,
                lineHeight = 60.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-1.2).sp,
            ),
        displayMedium =
            TextStyle(
                fontSize = 42.sp,
                lineHeight = 46.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = (-0.6).sp,
            ),
        headlineLarge =
            TextStyle(
                fontSize = 34.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Light,
            ),
        titleLarge =
            TextStyle(
                fontSize = 21.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Medium,
            ),
        bodyLarge =
            TextStyle(
                fontSize = 17.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal,
            ),
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
            ),
    )

@Composable
fun SableStartRoot(
    apps: List<AppEntry>,
    pinnedApps: List<AppEntry>,
    recentApps: List<AppEntry>,
    liveSurfaceRepository: LiveSurfaceRepository,
    liveRefreshGeneration: Int,
    onLaunchApp: (AppEntry) -> Unit,
    onTogglePinned: (AppEntry) -> Unit,
    onOpenAppInfo: (AppEntry) -> Unit,
    onRequestLivePermissions: () -> Unit,
) {
    var screen by remember {
        mutableStateOf(SableStartScreen.Start)
    }
    var selectedApp by remember {
        mutableStateOf<AppEntry?>(null)
    }
    var nowEpochMs by remember {
        mutableStateOf(System.currentTimeMillis())
    }
    var liveSnapshot by remember {
        mutableStateOf(liveSurfaceRepository.initialSnapshot())
    }

    LaunchedEffect(Unit) {
        while (true) {
            val now = System.currentTimeMillis()
            nowEpochMs = now

            val untilNextMinute =
                60_000L - (now % 60_000L)

            delay(
                untilNextMinute.coerceAtLeast(1_000L),
            )
        }
    }

    LaunchedEffect(liveRefreshGeneration) {
        while (true) {
            liveSnapshot = liveSurfaceRepository.snapshot()
            delay(60_000L)
        }
    }

    BackHandler(
        enabled = screen != SableStartScreen.Start,
    ) {
        screen =
            if (screen == SableStartScreen.Context) {
                SableStartScreen.Apps
            } else {
                SableStartScreen.Start
            }
    }

    MaterialTheme(
        colorScheme = SableColors,
        typography = SableTypography,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black,
        ) {
            when (screen) {
                SableStartScreen.Start ->
                    StartScreen(
                        apps = apps,
                        pinnedCount = pinnedApps.size,
                        recentCount = recentApps.size,
                        nowEpochMs = nowEpochMs,
                        onAllApps = {
                            screen = SableStartScreen.Apps
                        },
                        onSearch = {
                            screen = SableStartScreen.Search
                        },
                        onPinned = {
                            screen = SableStartScreen.Pinned
                        },
                        onSettings = {
                            screen = SableStartScreen.Settings
                        },
                    )

                SableStartScreen.Apps ->
                    AppsScreen(
                        apps = apps,
                        onSearch = {
                            screen = SableStartScreen.Search
                        },
                        onLaunchApp = onLaunchApp,
                        onOpenContext = { app ->
                            selectedApp = app
                            screen = SableStartScreen.Context
                        },
                    )

                SableStartScreen.Context -> {
                    val app = selectedApp
                    if (app == null) {
                        EmptyState(
                            title = "app unavailable",
                            detail = "Return to All Apps and choose an application again.",
                        )
                    } else {
                        AppContextScreen(
                            app = app,
                            pinned =
                                pinnedApps.any {
                                    it.stableId == app.stableId
                                },
                            onOpen = {
                                onLaunchApp(app)
                            },
                            onAppInfo = {
                                onOpenAppInfo(app)
                            },
                            onTogglePinned = {
                                onTogglePinned(app)
                            },
                        )
                    }
                }

                SableStartScreen.Search ->
                    SearchScreen(
                        apps = apps,
                        onLaunchApp = onLaunchApp,
                        onOpenContext = { app ->
                            selectedApp = app
                            screen = SableStartScreen.Context
                        },
                    )

                SableStartScreen.Pinned ->
                    PinnedRecentScreen(
                        pinnedApps = pinnedApps,
                        recentApps = recentApps,
                        onLaunchApp = onLaunchApp,
                        onOpenContext = { app ->
                            selectedApp = app
                            screen = SableStartScreen.Context
                        },
                    )

                SableStartScreen.Settings ->
                    SableStartSettingsScreen(
                        onLive = {
                            screen = SableStartScreen.Live
                        },
                        onLockPreview = {
                            screen = SableStartScreen.LockPreview
                        },
                    )

                SableStartScreen.Live ->
                    LiveSurfaceScreen(
                        snapshot = liveSnapshot,
                        onRequestPermissions = onRequestLivePermissions,
                    )

                SableStartScreen.LockPreview ->
                    LockPreviewScreen(
                        timeText = liveSnapshot.timeText,
                        dateText = liveSnapshot.dateText,
                    )
            }
        }
    }
}

@Composable
private fun StartScreen(
    apps: List<AppEntry>,
    pinnedCount: Int,
    recentCount: Int,
    nowEpochMs: Long,
    onAllApps: () -> Unit,
    onSearch: () -> Unit,
    onPinned: () -> Unit,
    onSettings: () -> Unit,
) {
    val context = LocalContext.current

    val calendar =
        remember(nowEpochMs) {
            Calendar.getInstance().apply {
                timeInMillis = nowEpochMs
            }
        }

    val greeting =
        greetingForHour(
            calendar.get(Calendar.HOUR_OF_DAY),
        )

    val dateText =
        remember(nowEpochMs, context) {
            DateFormat
                .getMediumDateFormat(context)
                .format(Date(nowEpochMs))
        }

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        item {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(292.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF6676A8),
                                    Color(0xFF3D536D),
                                    Color.Black,
                                ),
                            ),
                        )
                        .padding(
                            horizontal = 24.dp,
                            vertical = 26.dp,
                        ),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                ) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f),
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "A calmer device\nfor a more intentional day.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.82f),
                    )
                }
            }
        }

        item {
            Column(
                modifier =
                    Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 18.dp,
                    ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    HomeTile(
                        title = "all apps",
                        subtitle = "${apps.size} available",
                        mark = "A",
                        color = SableBlue,
                        modifier = Modifier.weight(1f),
                        onClick = onAllApps,
                    )
                    HomeTile(
                        title = "search",
                        subtitle = "apps on this device",
                        mark = "⌕",
                        color = SablePurple,
                        modifier = Modifier.weight(1f),
                        onClick = onSearch,
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    HomeTile(
                        title = "pinned & recent",
                        subtitle = "$pinnedCount pinned · $recentCount recent",
                        mark = "P",
                        color = SableGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onPinned,
                    )
                    HomeTile(
                        title = "settings",
                        subtitle = "Sable Start",
                        mark = "⚙",
                        color = SableSlate,
                        modifier = Modifier.weight(1f),
                        onClick = onSettings,
                    )
                }

                Spacer(Modifier.height(22.dp))
                Text(
                    text = "Sable Start",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Launcher-visible apps · ${apps.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SableMuted,
                )
            }
        }
    }
}

@Composable
private fun HomeTile(
    title: String,
    subtitle: String,
    mark: String,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .height(142.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color)
                .clickable(onClick = onClick)
                .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = mark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.86f),
            )
        }
    }
}

@Composable
private fun AppsScreen(
    apps: List<AppEntry>,
    onSearch: () -> Unit,
    onLaunchApp: (AppEntry) -> Unit,
    onOpenContext: (AppEntry) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        MetroHeading("all apps · ${apps.size}")
        Spacer(Modifier.height(12.dp))

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(SablePanelRaised)
                    .clickable(onClick = onSearch)
                    .padding(horizontal = 18.dp, vertical = 15.dp),
        ) {
            Text(
                text = "⌕   Search apps…",
                style = MaterialTheme.typography.bodyLarge,
                color = SableMuted,
            )
        }

        Spacer(Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = apps,
                key = { it.stableId },
            ) { app ->
                AppRow(
                    app = app,
                    onLaunch = {
                        onLaunchApp(app)
                    },
                    onContext = {
                        onOpenContext(app)
                    },
                )
            }
        }
    }
}

@Composable
private fun SearchScreen(
    apps: List<AppEntry>,
    onLaunchApp: (AppEntry) -> Unit,
    onOpenContext: (AppEntry) -> Unit,
) {
    var query by remember {
        mutableStateOf("")
    }

    val results =
        remember(apps, query) {
            searchApps(
                apps = apps,
                rawQuery = query,
            )
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        MetroHeading("search")
        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            label = {
                Text("Search apps")
            },
        )

        Spacer(Modifier.height(12.dp))
        Text(
            text = "${results.size} results",
            style = MaterialTheme.typography.bodyMedium,
            color = SableMuted,
        )
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = results,
                key = { it.stableId },
            ) { app ->
                AppRow(
                    app = app,
                    onLaunch = {
                        onLaunchApp(app)
                    },
                    onContext = {
                        onOpenContext(app)
                    },
                )
            }
        }
    }
}

@Composable
private fun AppRow(
    app: AppEntry,
    onLaunch: () -> Unit,
    onContext: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .clickable(onClick = onLaunch)
                .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIcon(
            app = app,
            modifier = Modifier.size(44.dp),
        )
        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = app.label,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = app.component.packageName,
                style = MaterialTheme.typography.bodyMedium,
                color = SableMuted,
            )
        }

        Text(
            text = "⋯",
            modifier =
                Modifier
                    .heightIn(min = 48.dp)
                    .clickable(onClick = onContext)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
            fontSize = 24.sp,
            color = SableMuted,
        )
    }
}

@Composable
private fun AppIcon(
    app: AppEntry,
    modifier: Modifier,
) {
    val icon = app.icon

    if (icon != null) {
        Image(
            bitmap = icon.asImageBitmap(),
            contentDescription = "${app.label} icon",
            modifier =
                modifier.clip(
                    RoundedCornerShape(10.dp),
                ),
        )
    } else {
        Box(
            modifier =
                modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SablePanelRaised),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text =
                    app.label
                        .trim()
                        .take(1)
                        .uppercase(),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun AppContextScreen(
    app: AppEntry,
    pinned: Boolean,
    onOpen: () -> Unit,
    onAppInfo: () -> Unit,
    onTogglePinned: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp, vertical = 20.dp),
    ) {
        MetroHeading(app.label)
        Spacer(Modifier.height(18.dp))

        AppIcon(
            app = app,
            modifier = Modifier.size(78.dp),
        )

        Spacer(Modifier.height(18.dp))
        Text(
            text = app.component.packageName,
            style = MaterialTheme.typography.bodyLarge,
            color = SableMuted,
        )
        Text(
            text = "profile ${app.profileSerial}",
            style = MaterialTheme.typography.bodyMedium,
            color = SableMuted,
        )

        Spacer(Modifier.height(22.dp))

        Button(
            onClick = onOpen,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9BCBFF),
                    contentColor = Color.Black,
                ),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Open", style = MaterialTheme.typography.bodyLarge)
                Text("→", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(Modifier.height(18.dp))
        ContextAction(
            mark = "ⓘ",
            label = "App info",
            onClick = onAppInfo,
        )
        ContextAction(
            mark = if (pinned) "−" else "+",
            label = if (pinned) "Unpin from Start" else "Pin to Start",
            onClick = onTogglePinned,
        )

        Spacer(Modifier.height(18.dp))
        Text(
            text =
                "Application management remains owned by Android. " +
                    "Sable Start does not silently uninstall or change package state.",
            style = MaterialTheme.typography.bodyMedium,
            color = SableMuted,
        )
    }
}

@Composable
private fun ContextAction(
    mark: String,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clickable(onClick = onClick)
                .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(mark, fontSize = 22.sp)
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun PinnedRecentScreen(
    pinnedApps: List<AppEntry>,
    recentApps: List<AppEntry>,
    onLaunchApp: (AppEntry) -> Unit,
    onOpenContext: (AppEntry) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        item {
            MetroHeading("pinned")
            Spacer(Modifier.height(12.dp))
            if (pinnedApps.isEmpty()) {
                Text(
                    text = "Nothing pinned yet. Use an app's context menu to pin it.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SableMuted,
                )
                Spacer(Modifier.height(14.dp))
            }
        }

        items(
            items = pinnedApps,
            key = { "pinned:${it.stableId}" },
        ) { app ->
            AppRow(
                app = app,
                onLaunch = {
                    onLaunchApp(app)
                },
                onContext = {
                    onOpenContext(app)
                },
            )
        }

        item {
            Spacer(Modifier.height(26.dp))
            MetroHeading("recent")
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Only successful launches made through Sable Start are recorded.",
                style = MaterialTheme.typography.bodyMedium,
                color = SableMuted,
            )
            Spacer(Modifier.height(8.dp))
            if (recentApps.isEmpty()) {
                Text(
                    text = "No launcher-local recent apps yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SableMuted,
                )
            }
        }

        items(
            items = recentApps,
            key = { "recent:${it.stableId}" },
        ) { app ->
            AppRow(
                app = app,
                onLaunch = {
                    onLaunchApp(app)
                },
                onContext = {
                    onOpenContext(app)
                },
            )
        }
    }
}

@Composable
private fun SableStartSettingsScreen(
    onLive: () -> Unit,
    onLockPreview: () -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 22.dp, vertical = 20.dp),
    ) {
        item {
            MetroHeading("settings")
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Sable Start settings",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text =
                    "Launcher-local controls only. Android system settings, " +
                        "permissions, networking, telephony, storage, and updates remain platform-owned.",
                style = MaterialTheme.typography.bodyMedium,
                color = SableMuted,
            )
            Spacer(Modifier.height(20.dp))
        }

        item {
            SettingsRow(
                mark = "◎",
                title = "Live local data",
                detail = "Photos, music, and calendar — opt in",
                onClick = onLive,
            )
            SettingsRow(
                mark = "▣",
                title = "Lock preview",
                detail = "Visual concept only; does not replace Android lock screen",
                onClick = onLockPreview,
            )
            Spacer(Modifier.height(22.dp))
            Text(
                text = "Privacy",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text =
                    "Pinned apps and launcher-local recents stay in Sable Start's private local storage. " +
                        "No Usage Stats or network access is required for these surfaces.",
                style = MaterialTheme.typography.bodyMedium,
                color = SableMuted,
            )
        }
    }
}

@Composable
private fun SettingsRow(
    mark: String,
    title: String,
    detail: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = mark,
            fontSize = 24.sp,
            color = SableBlue,
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = detail,
                style = MaterialTheme.typography.bodyMedium,
                color = SableMuted,
            )
        }
        Text(
            text = "›",
            color = SableMuted,
            fontSize = 24.sp,
        )
    }
}

@Composable
private fun LiveSurfaceScreen(
    snapshot: LiveSurfaceSnapshot,
    onRequestPermissions: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF8C4E34),
                            Color(0xFF314238),
                            Color(0xFF121413),
                        ),
                    ),
                )
                .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 28.dp),
        ) {
            item {
                Text(
                    text = snapshot.dateText,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = snapshot.timeText,
                    style = MaterialTheme.typography.displayLarge,
                )
                Text(
                    text = "live local data",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "updated ${snapshot.timeText}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.82f),
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                LiveCard("▣", snapshot.photos)
                Spacer(Modifier.height(10.dp))
                LiveCard("♪", snapshot.music)
                Spacer(Modifier.height(10.dp))
                LiveCard("17", snapshot.calendar)
                Spacer(Modifier.height(10.dp))
                LiveCard("☁", snapshot.weather)
                Spacer(Modifier.height(10.dp))
                LiveCard("✓", snapshot.tasks)
                Spacer(Modifier.height(16.dp))
            }

            if (
                snapshot.photos.availability == LiveAvailability.PERMISSION_REQUIRED ||
                    snapshot.music.availability == LiveAvailability.PERMISSION_REQUIRED ||
                    snapshot.calendar.availability == LiveAvailability.PERMISSION_REQUIRED
            ) {
                item {
                    Button(
                        onClick = onRequestPermissions,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Enable local live data")
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            item {
                Text(
                    text =
                        "Weather and Tasks remain unavailable until explicit providers are configured. " +
                            "Sable Start does not enable network access just to populate this surface.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.70f),
                )
            }
        }
    }
}

@Composable
private fun LiveCard(
    mark: String,
    datum: LiveDatum,
) {
    val status =
        when (datum.availability) {
            LiveAvailability.LOADING -> "loading"
            LiveAvailability.LIVE -> "live"
            LiveAvailability.EMPTY -> "empty"
            LiveAvailability.PERMISSION_REQUIRED -> "permission required"
            LiveAvailability.UNAVAILABLE -> "unavailable"
            LiveAvailability.ERROR -> "error"
        }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.55f))
                .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(mark, fontSize = 26.sp)
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(datum.title, style = MaterialTheme.typography.bodyLarge)
            Text(
                datum.detail,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.76f),
            )
            Text(
                status,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.58f),
            )
        }
    }
}

@Composable
private fun LockPreviewScreen(
    timeText: String,
    dateText: String,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF46608F),
                            Color(0xFF9C7190),
                            Color(0xFF1B3045),
                        ),
                    ),
                )
                .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val ridge =
                Path().apply {
                    moveTo(0f, size.height * 0.74f)
                    lineTo(size.width * 0.17f, size.height * 0.66f)
                    lineTo(size.width * 0.31f, size.height * 0.70f)
                    lineTo(size.width * 0.53f, size.height * 0.54f)
                    lineTo(size.width * 0.68f, size.height * 0.68f)
                    lineTo(size.width, size.height * 0.60f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
            drawPath(
                path = ridge,
                color = Color(0xCC102334),
            )
        }

        Column(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 86.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = timeText,
                fontSize = 72.sp,
                lineHeight = 78.sp,
                fontWeight = FontWeight.Light,
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 34.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("⌃", fontSize = 26.sp)
            Text(
                text = "Sable lock preview",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text =
                    "Visual concept only — Android Keyguard/SystemUI remains the real security boundary.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.70f),
            )
        }
    }
}

@Composable
private fun EmptyState(
    title: String,
    detail: String,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(24.dp),
    ) {
        MetroHeading(title)
        Spacer(Modifier.height(12.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodyMedium,
            color = SableMuted,
        )
    }
}

@Composable
private fun MetroHeading(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Light,
        modifier = Modifier.semantics { heading() },
    )
}

internal fun greetingForHour(
    hour: Int,
): String {
    require(hour in 0..23)

    return when (hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..21 -> "Good evening"
        else -> "Good night"
    }
}

private fun searchApps(
    apps: List<AppEntry>,
    rawQuery: String,
): List<AppEntry> {
    val query =
        rawQuery
            .trim()
            .lowercase(Locale.getDefault())

    if (query.isEmpty()) {
        return apps
    }

    return apps
        .mapNotNull { app ->
            val label =
                app.label.lowercase(
                    Locale.getDefault(),
                )
            val packageName =
                app.component.packageName.lowercase(
                    Locale.ROOT,
                )

            val rank =
                when {
                    label.startsWith(query) -> 0
                    label.contains(query) -> 1
                    packageName.contains(query) -> 2
                    else -> null
                }

            rank?.let {
                SearchMatch(
                    rank = it,
                    app = app,
                )
            }
        }
        .sortedWith(
            compareBy<SearchMatch> {
                it.rank
            }
                .thenBy {
                    it.app.label.lowercase(
                        Locale.getDefault(),
                    )
                }
                .thenBy {
                    it.app.component.flattenToString()
                }
                .thenBy {
                    it.app.profileSerial
                },
        )
        .map {
            it.app
        }
}

private data class SearchMatch(
    val rank: Int,
    val app: AppEntry,
)
