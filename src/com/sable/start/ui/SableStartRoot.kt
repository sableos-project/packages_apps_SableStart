package org.sableos.start.ui

import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.sableos.start.model.AppEntry
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class SableStartScreen {
    Start,
    Apps,
    Search,
}

private val SableBlue = Color(0xFF4D9CFF)
private val SablePurple = Color(0xFF7A42E8)
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
    )

@Composable
fun SableStartRoot(
    apps: List<AppEntry>,
    onLaunchApp: (AppEntry) -> Unit,
) {
    var screen by remember {
        mutableStateOf(SableStartScreen.Start)
    }

    var nowEpochMs by remember {
        mutableStateOf(System.currentTimeMillis())
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

    BackHandler(
        enabled = screen != SableStartScreen.Start,
    ) {
        screen = SableStartScreen.Start
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
                        nowEpochMs = nowEpochMs,
                        onAllApps = {
                            screen = SableStartScreen.Apps
                        },
                        onSearch = {
                            screen = SableStartScreen.Search
                        },
                    )

                SableStartScreen.Apps ->
                    AppsScreen(
                        apps = apps,
                        onSearch = {
                            screen = SableStartScreen.Search
                        },
                        onLaunchApp = onLaunchApp,
                    )

                SableStartScreen.Search ->
                    SearchScreen(
                        apps = apps,
                        onLaunchApp = onLaunchApp,
                    )
            }
        }
    }
}

@Composable
private fun StartScreen(
    apps: List<AppEntry>,
    nowEpochMs: Long,
    onAllApps: () -> Unit,
    onSearch: () -> Unit,
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
                    modifier =
                        Modifier.align(
                            Alignment.BottomStart,
                        ),
                ) {
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.bodyMedium,
                        color =
                            Color.White.copy(
                                alpha = 0.88f,
                            ),
                    )

                    Spacer(
                        Modifier.height(10.dp),
                    )

                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.displayLarge,
                    )

                    Spacer(
                        Modifier.height(12.dp),
                    )

                    Text(
                        text =
                            "A calmer device\n" +
                                "for a more intentional day.",
                        style = MaterialTheme.typography.bodyMedium,
                        color =
                            Color.White.copy(
                                alpha = 0.82f,
                            ),
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
                    horizontalArrangement =
                        Arrangement.spacedBy(10.dp),
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

                Spacer(
                    Modifier.height(22.dp),
                )

                Text(
                    text = "Sable Start",
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(
                    Modifier.height(6.dp),
                )

                Text(
                    text =
                        "Launcher-visible apps · ${apps.size}",
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
        verticalArrangement =
            Arrangement.SpaceBetween,
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
                color =
                    Color.White.copy(
                        alpha = 0.86f,
                    ),
            )
        }
    }
}

@Composable
private fun AppsScreen(
    apps: List<AppEntry>,
    onSearch: () -> Unit,
    onLaunchApp: (AppEntry) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing,
                )
                .padding(
                    horizontal = 22.dp,
                    vertical = 20.dp,
                ),
    ) {
        MetroHeading(
            "all apps · ${apps.size}",
        )

        Spacer(
            Modifier.height(12.dp),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .clip(
                        RoundedCornerShape(28.dp),
                    )
                    .background(SablePanelRaised)
                    .clickable(onClick = onSearch)
                    .padding(
                        horizontal = 18.dp,
                        vertical = 15.dp,
                    ),
        ) {
            Text(
                text = "⌕   Search apps…",
                style = MaterialTheme.typography.bodyLarge,
                color = SableMuted,
            )
        }

        Spacer(
            Modifier.height(14.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = apps,
                key = {
                    "${it.user}:${it.component.flattenToString()}"
                },
            ) { app ->
                AppRow(
                    app = app,
                    onClick = {
                        onLaunchApp(app)
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
                .windowInsetsPadding(
                    WindowInsets.safeDrawing,
                )
                .padding(
                    horizontal = 22.dp,
                    vertical = 20.dp,
                ),
    ) {
        MetroHeading("search")

        Spacer(
            Modifier.height(14.dp),
        )

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            label = {
                Text(
                    "Search apps",
                )
            },
        )

        Spacer(
            Modifier.height(12.dp),
        )

        Text(
            text = "${results.size} results",
            style = MaterialTheme.typography.bodyMedium,
            color = SableMuted,
        )

        Spacer(
            Modifier.height(8.dp),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(
                items = results,
                key = {
                    "${it.user}:${it.component.flattenToString()}"
                },
            ) { app ->
                AppRow(
                    app = app,
                    onClick = {
                        onLaunchApp(app)
                    },
                )
            }
        }
    }
}

@Composable
private fun AppRow(
    app: AppEntry,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp)
                .clickable(onClick = onClick)
                .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .height(44.dp)
                    .width(44.dp)
                    .clip(
                        RoundedCornerShape(10.dp),
                    )
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

        Spacer(
            Modifier.width(14.dp),
        )

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
