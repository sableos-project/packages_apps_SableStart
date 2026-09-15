package org.sableos.start.platform

import android.content.Context
import org.sableos.start.model.AppEntry

class LauncherStateStore(
    context: Context,
) {
    private val preferences =
        context.applicationContext.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE,
        )

    fun isPinned(entry: AppEntry): Boolean =
        preferences
            .getStringSet(KEY_PINNED, emptySet())
            .orEmpty()
            .contains(entry.stableId)

    fun togglePinned(entry: AppEntry): Boolean {
        val pinned =
            preferences
                .getStringSet(KEY_PINNED, emptySet())
                .orEmpty()
                .toMutableSet()

        val nowPinned =
            if (pinned.remove(entry.stableId)) {
                false
            } else {
                pinned.add(entry.stableId)
                true
            }

        preferences
            .edit()
            .putStringSet(KEY_PINNED, pinned)
            .apply()

        return nowPinned
    }

    fun recordSuccessfulLaunch(entry: AppEntry) {
        val recent =
            preferences
                .getString(KEY_RECENT, "")
                .orEmpty()
                .lineSequence()
                .filter { it.isNotBlank() }
                .filterNot { it == entry.stableId }
                .toMutableList()

        recent.add(0, entry.stableId)

        preferences
            .edit()
            .putString(
                KEY_RECENT,
                recent
                    .take(MAX_RECENT)
                    .joinToString("\n"),
            )
            .apply()
    }

    fun resolvePinned(apps: List<AppEntry>): List<AppEntry> {
        val pinnedIds =
            preferences
                .getStringSet(KEY_PINNED, emptySet())
                .orEmpty()

        return apps.filter { it.stableId in pinnedIds }
    }

    fun resolveRecent(apps: List<AppEntry>): List<AppEntry> {
        val byId = apps.associateBy { it.stableId }

        return preferences
            .getString(KEY_RECENT, "")
            .orEmpty()
            .lineSequence()
            .filter { it.isNotBlank() }
            .mapNotNull(byId::get)
            .take(MAX_RECENT)
            .toList()
    }

    private companion object {
        const val PREFERENCES_NAME = "sable_start_launcher_state"
        const val KEY_PINNED = "pinned_components"
        const val KEY_RECENT = "recent_components"
        const val MAX_RECENT = 8
    }
}
