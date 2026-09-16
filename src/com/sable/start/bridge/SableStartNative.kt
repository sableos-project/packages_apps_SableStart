package org.sableos.start.bridge

import org.sableos.start.model.AccentPreset
import org.sableos.start.model.AppearanceConfig
import org.sableos.start.model.CornerStyle
import org.sableos.start.model.ResolvedAppearance
import org.sableos.start.model.SurfaceStyle

object SableStartNative {
    init {
        System.loadLibrary("sable_start_core_jni")
    }

    external fun nativeCreate(): Long

    external fun nativeDestroy(
        handle: Long,
    )

    fun resolveAppearance(
        handle: Long,
        config: AppearanceConfig,
    ): ResolvedAppearance {
        val token =
            nativeApplyTheme(
                handle = handle,
                surfaceStyle = config.surfaceStyle.nativeId,
                accentPreset = config.accentPreset.nativeId,
                cornerStyle = config.cornerStyle.nativeId,
            )

        val canonicalConfig =
            AppearanceConfig(
                surfaceStyle =
                    SurfaceStyle.fromNativeId(
                        (token and 0xffL).toInt(),
                    ),
                accentPreset =
                    AccentPreset.fromNativeId(
                        ((token ushr 8) and 0xffL).toInt(),
                    ),
                cornerStyle =
                    CornerStyle.fromNativeId(
                        ((token ushr 16) and 0xffL).toInt(),
                    ),
            )

        return ResolvedAppearance(
            config = canonicalConfig,
            token = token,
            primaryArgb = nativeThemeColor(token, COLOR_PRIMARY),
            onPrimaryArgb = nativeThemeColor(token, COLOR_ON_PRIMARY),
            backgroundArgb = nativeThemeColor(token, COLOR_BACKGROUND),
            onBackgroundArgb = nativeThemeColor(token, COLOR_ON_BACKGROUND),
            surfaceArgb = nativeThemeColor(token, COLOR_SURFACE),
            onSurfaceArgb = nativeThemeColor(token, COLOR_ON_SURFACE),
            surfaceVariantArgb = nativeThemeColor(token, COLOR_SURFACE_VARIANT),
            onSurfaceVariantArgb = nativeThemeColor(token, COLOR_ON_SURFACE_VARIANT),
            heroTopArgb = nativeThemeColor(token, COLOR_HERO_TOP),
            heroMiddleArgb = nativeThemeColor(token, COLOR_HERO_MIDDLE),
            cornerDp = nativeThemeCornerDp(token),
        )
    }

    private external fun nativeApplyTheme(
        handle: Long,
        surfaceStyle: Int,
        accentPreset: Int,
        cornerStyle: Int,
    ): Long

    private external fun nativeThemeColor(
        token: Long,
        role: Int,
    ): Int

    private external fun nativeThemeCornerDp(
        token: Long,
    ): Int

    private const val COLOR_PRIMARY = 0
    private const val COLOR_ON_PRIMARY = 1
    private const val COLOR_BACKGROUND = 2
    private const val COLOR_ON_BACKGROUND = 3
    private const val COLOR_SURFACE = 4
    private const val COLOR_ON_SURFACE = 5
    private const val COLOR_SURFACE_VARIANT = 6
    private const val COLOR_ON_SURFACE_VARIANT = 7
    private const val COLOR_HERO_TOP = 8
    private const val COLOR_HERO_MIDDLE = 9
}
