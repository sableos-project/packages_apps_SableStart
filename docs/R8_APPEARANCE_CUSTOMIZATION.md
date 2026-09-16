# R8 Sable Start appearance/customization

Status: **source-complete build-preparation implementation for the R8 appearance milestone.**

R8 extends the R7 production surfaces with launcher-local appearance customization. It does not change Android's system theme, wallpaper, Keyguard/SystemUI, application themes, permissions, networking, package state, or Quickstep ownership.

## Scope

R8 adds three bounded launcher-owned dimensions:

- surface style: Metro, Graphite, OLED black;
- accent preset: Blue, Green, Purple, Orange, Slate;
- corner style: Compact (4 dp), Metro (8 dp), Rounded (16 dp).

The default remains the R7-compatible Metro + Blue + Metro configuration.

## State and ownership

Kotlin owns user-facing state and persistence through private `SharedPreferences` in `AppearanceStore`.

Rust owns canonical appearance resolution through `libsable_start_core_jni`:

```text
AppearanceConfig (Kotlin)
        |
        v
nativeApplyTheme(...)
        |
        +-- validate/canonicalize enum ids
        +-- update native generation only when the canonical theme changes
        +-- return immutable packed theme token
        |
        v
nativeThemeColor / nativeThemeCornerDp
        |
        v
ResolvedAppearance (Kotlin)
        |
        v
Compose MaterialTheme + launcher surfaces
```

Invalid native IDs fail back to the production defaults rather than producing undefined colors or dimensions.

## UI behavior

Sable Start Settings now exposes `Appearance` ahead of the existing Live local data and Lock preview entries.

The Appearance screen includes:

- live launcher-local preview;
- surface selection;
- accent selection;
- corner selection;
- reset to Sable Start defaults.

Changes apply immediately and persist locally. Reset clears the stored appearance keys and restores the default configuration.

## Preserved R7 boundaries

R8 must preserve:

- real launcher-visible application inventory and icons;
- deterministic app ordering;
- exact component/profile launching;
- App Context and Android-owned App info routing;
- pinned and launcher-local recent state;
- Search over the same live inventory;
- explicit optional Live permissions;
- Weather/Tasks unavailable until provider design exists;
- Lock as preview-only;
- Launcher3 Quickstep as recents/gesture provider;
- no Usage Stats permission;
- no new network permission.

## Build-preparation acceptance

Before runtime acceptance, the migrated build environment should prove:

```text
R8_BRANCH_CHECKOUT=PASS
RUST_FFI_COMPILE=PASS
KOTLIN_COMPOSE_COMPILE=PASS
JNI_SYMBOL_BINDING=PASS
SABLESTART_APK_PACKAGE_INSPECTION=PASS
NO_NEW_NETWORK_PERMISSION=PASS
NO_USAGE_STATS_PERMISSION=PASS
```

Runtime/device acceptance should then prove at least:

```text
APPEARANCE_DEFAULT_MATCHES_R7=PASS
SURFACE_STYLE_APPLIES_IMMEDIATELY=PASS
ACCENT_APPLIES_IMMEDIATELY=PASS
CORNER_STYLE_APPLIES_IMMEDIATELY=PASS
APPEARANCE_PERSISTS_ACROSS_ACTIVITY_RECREATE=PASS
APPEARANCE_RESET_RESTORES_DEFAULT=PASS
R7_APP_LAUNCH_AND_CONTEXT_BEHAVIOR_UNCHANGED=PASS
R7_PINNED_RECENT_BEHAVIOR_UNCHANGED=PASS
R7_LIVE_PERMISSION_BOUNDARY_UNCHANGED=PASS
LOCK_REMAINS_PREVIEW_ONLY=PASS
NO_CRASH_DURING_REPRESENTATIVE_NAVIGATION=PASS
```

The R8 source branch is intended to be built after the expanded build storage is migrated; this document does not claim a completed Android build or device-runtime validation.
