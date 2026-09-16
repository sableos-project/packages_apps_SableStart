package org.sableos.start.platform

import android.content.Context
import org.sableos.start.model.AccentPreset
import org.sableos.start.model.AppearanceConfig
import org.sableos.start.model.CornerStyle
import org.sableos.start.model.SurfaceStyle

class AppearanceStore(
    context: Context,
) {
    private val preferences =
        context.applicationContext.getSharedPreferences(
            PREFERENCES_NAME,
            Context.MODE_PRIVATE,
        )

    fun load(): AppearanceConfig =
        AppearanceConfig(
            surfaceStyle =
                SurfaceStyle.fromNativeId(
                    preferences.getInt(
                        KEY_SURFACE_STYLE,
                        AppearanceConfig.Default.surfaceStyle.nativeId,
                    ),
                ),
            accentPreset =
                AccentPreset.fromNativeId(
                    preferences.getInt(
                        KEY_ACCENT_PRESET,
                        AppearanceConfig.Default.accentPreset.nativeId,
                    ),
                ),
            cornerStyle =
                CornerStyle.fromNativeId(
                    preferences.getInt(
                        KEY_CORNER_STYLE,
                        AppearanceConfig.Default.cornerStyle.nativeId,
                    ),
                ),
        )

    fun save(config: AppearanceConfig) {
        preferences
            .edit()
            .putInt(KEY_SURFACE_STYLE, config.surfaceStyle.nativeId)
            .putInt(KEY_ACCENT_PRESET, config.accentPreset.nativeId)
            .putInt(KEY_CORNER_STYLE, config.cornerStyle.nativeId)
            .apply()
    }

    fun reset(): AppearanceConfig {
        preferences
            .edit()
            .remove(KEY_SURFACE_STYLE)
            .remove(KEY_ACCENT_PRESET)
            .remove(KEY_CORNER_STYLE)
            .apply()

        return AppearanceConfig.Default
    }

    private companion object {
        const val PREFERENCES_NAME = "sable_start_appearance"
        const val KEY_SURFACE_STYLE = "surface_style"
        const val KEY_ACCENT_PRESET = "accent_preset"
        const val KEY_CORNER_STYLE = "corner_style"
    }
}
