package org.sableos.start.model

enum class SurfaceStyle(
    val nativeId: Int,
    val displayName: String,
    val detail: String,
) {
    Metro(
        nativeId = 0,
        displayName = "Metro",
        detail = "Sable's blue-black production surface",
    ),
    Graphite(
        nativeId = 1,
        displayName = "Graphite",
        detail = "Softer charcoal surfaces with lower contrast",
    ),
    Oled(
        nativeId = 2,
        displayName = "OLED black",
        detail = "True-black background with restrained panels",
    ),
    ;

    companion object {
        fun fromNativeId(value: Int): SurfaceStyle =
            entries.firstOrNull { it.nativeId == value } ?: Metro
    }
}

enum class AccentPreset(
    val nativeId: Int,
    val displayName: String,
) {
    Blue(0, "Blue"),
    Green(1, "Green"),
    Purple(2, "Purple"),
    Orange(3, "Orange"),
    Slate(4, "Slate"),
    ;

    companion object {
        fun fromNativeId(value: Int): AccentPreset =
            entries.firstOrNull { it.nativeId == value } ?: Blue
    }
}

enum class CornerStyle(
    val nativeId: Int,
    val displayName: String,
    val detail: String,
) {
    Compact(
        nativeId = 0,
        displayName = "Compact",
        detail = "Sharper 4 dp corners",
    ),
    Metro(
        nativeId = 1,
        displayName = "Metro",
        detail = "Balanced 8 dp corners",
    ),
    Rounded(
        nativeId = 2,
        displayName = "Rounded",
        detail = "Softer 16 dp corners",
    ),
    ;

    companion object {
        fun fromNativeId(value: Int): CornerStyle =
            entries.firstOrNull { it.nativeId == value } ?: Metro
    }
}

data class AppearanceConfig(
    val surfaceStyle: SurfaceStyle = SurfaceStyle.Metro,
    val accentPreset: AccentPreset = AccentPreset.Blue,
    val cornerStyle: CornerStyle = CornerStyle.Metro,
) {
    companion object {
        val Default = AppearanceConfig()
    }
}

data class ResolvedAppearance(
    val config: AppearanceConfig,
    val token: Long,
    val primaryArgb: Int,
    val onPrimaryArgb: Int,
    val backgroundArgb: Int,
    val onBackgroundArgb: Int,
    val surfaceArgb: Int,
    val onSurfaceArgb: Int,
    val surfaceVariantArgb: Int,
    val onSurfaceVariantArgb: Int,
    val heroTopArgb: Int,
    val heroMiddleArgb: Int,
    val cornerDp: Int,
)
