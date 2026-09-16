//! Native theme/state core for SableStart.
//!
//! The Android/Kotlin layer owns persistence and presentation. This module owns
//! canonical R8 appearance resolution so invalid persisted/UI values cannot
//! produce an undefined launcher palette.

use std::ffi::c_void;

#[derive(Clone, Copy, Debug, Eq, PartialEq)]
struct ThemeConfig {
    surface_style: u8,
    accent_preset: u8,
    corner_style: u8,
}

impl Default for ThemeConfig {
    fn default() -> Self {
        Self {
            surface_style: 0,
            accent_preset: 0,
            corner_style: 1,
        }
    }
}

struct LauncherState {
    generation: u32,
    theme: ThemeConfig,
}

const COLOR_PRIMARY: i32 = 0;
const COLOR_ON_PRIMARY: i32 = 1;
const COLOR_BACKGROUND: i32 = 2;
const COLOR_ON_BACKGROUND: i32 = 3;
const COLOR_SURFACE: i32 = 4;
const COLOR_ON_SURFACE: i32 = 5;
const COLOR_SURFACE_VARIANT: i32 = 6;
const COLOR_ON_SURFACE_VARIANT: i32 = 7;
const COLOR_HERO_TOP: i32 = 8;
const COLOR_HERO_MIDDLE: i32 = 9;

fn normalize_id(value: i32, max_inclusive: u8, default_value: u8) -> u8 {
    if value < 0 || value > i32::from(max_inclusive) {
        default_value
    } else {
        value as u8
    }
}

fn normalized_theme(
    surface_style: i32,
    accent_preset: i32,
    corner_style: i32,
) -> ThemeConfig {
    ThemeConfig {
        surface_style: normalize_id(surface_style, 2, 0),
        accent_preset: normalize_id(accent_preset, 4, 0),
        corner_style: normalize_id(corner_style, 2, 1),
    }
}

fn encode_theme_token(generation: u32, config: ThemeConfig) -> i64 {
    let packed =
        ((generation as u64) << 32)
            | ((config.corner_style as u64) << 16)
            | ((config.accent_preset as u64) << 8)
            | config.surface_style as u64;

    packed as i64
}

fn decode_theme_token(token: i64) -> ThemeConfig {
    let packed = token as u64;

    normalized_theme(
        (packed & 0xff) as i32,
        ((packed >> 8) & 0xff) as i32,
        ((packed >> 16) & 0xff) as i32,
    )
}

fn accent_color(accent_preset: u8) -> u32 {
    match accent_preset {
        1 => 0xff35c66b,
        2 => 0xff9b7bff,
        3 => 0xfff28c45,
        4 => 0xff7e8c9d,
        _ => 0xff4d9cff,
    }
}

fn on_accent_color(_accent_preset: u8) -> u32 {
    // R8 presets are deliberately chosen to keep black text/icons readable.
    0xff000000
}

fn surface_color(config: ThemeConfig, role: i32) -> u32 {
    match config.surface_style {
        1 => match role {
            COLOR_BACKGROUND => 0xff111318,
            COLOR_ON_BACKGROUND => 0xffffffff,
            COLOR_SURFACE => 0xff1b1e24,
            COLOR_ON_SURFACE => 0xffffffff,
            COLOR_SURFACE_VARIANT => 0xff2a2e36,
            COLOR_ON_SURFACE_VARIANT => 0xffc0c4cc,
            COLOR_HERO_TOP => 0xff5f6878,
            COLOR_HERO_MIDDLE => 0xff353b46,
            _ => 0xff111318,
        },
        2 => match role {
            COLOR_BACKGROUND => 0xff000000,
            COLOR_ON_BACKGROUND => 0xffffffff,
            COLOR_SURFACE => 0xff0b0c0e,
            COLOR_ON_SURFACE => 0xffffffff,
            COLOR_SURFACE_VARIANT => 0xff15171a,
            COLOR_ON_SURFACE_VARIANT => 0xffafb4bd,
            COLOR_HERO_TOP => 0xff384a62,
            COLOR_HERO_MIDDLE => 0xff1b2734,
            _ => 0xff000000,
        },
        _ => match role {
            COLOR_BACKGROUND => 0xff000000,
            COLOR_ON_BACKGROUND => 0xffffffff,
            COLOR_SURFACE => 0xff17191d,
            COLOR_ON_SURFACE => 0xffffffff,
            COLOR_SURFACE_VARIANT => 0xff22252a,
            COLOR_ON_SURFACE_VARIANT => 0xffb8bbc3,
            COLOR_HERO_TOP => 0xff6676a8,
            COLOR_HERO_MIDDLE => 0xff3d536d,
            _ => 0xff000000,
        },
    }
}

fn color_for_token(token: i64, role: i32) -> i32 {
    let config = decode_theme_token(token);

    let color = match role {
        COLOR_PRIMARY => accent_color(config.accent_preset),
        COLOR_ON_PRIMARY => on_accent_color(config.accent_preset),
        COLOR_BACKGROUND
        | COLOR_ON_BACKGROUND
        | COLOR_SURFACE
        | COLOR_ON_SURFACE
        | COLOR_SURFACE_VARIANT
        | COLOR_ON_SURFACE_VARIANT
        | COLOR_HERO_TOP
        | COLOR_HERO_MIDDLE => surface_color(config, role),
        _ => 0xffff00ff,
    };

    color as i32
}

fn corner_dp_for_token(token: i64) -> i32 {
    match decode_theme_token(token).corner_style {
        0 => 4,
        2 => 16,
        _ => 8,
    }
}

/// Creates the native SableStart launcher state and returns its opaque handle.
#[no_mangle]
pub extern "C" fn
Java_org_sableos_start_bridge_SableStartNative_nativeCreate(
    _env: *mut c_void,
    _object: *mut c_void,
) -> i64 {
    let state = Box::new(LauncherState {
        generation: 1,
        theme: ThemeConfig::default(),
    });

    Box::into_raw(state) as i64
}

/// Applies and canonicalizes an R8 appearance configuration.
///
/// The returned token is immutable and can be used by the stateless color and
/// corner resolvers below. Generation advances only when the canonical theme
/// actually changes.
#[no_mangle]
pub extern "C" fn
Java_org_sableos_start_bridge_SableStartNative_nativeApplyTheme(
    _env: *mut c_void,
    _object: *mut c_void,
    handle: i64,
    surface_style: i32,
    accent_preset: i32,
    corner_style: i32,
) -> i64 {
    let config = normalized_theme(
        surface_style,
        accent_preset,
        corner_style,
    );

    if handle == 0 {
        return encode_theme_token(0, config);
    }

    // SAFETY: handles are created by `nativeCreate`, remain Activity-owned, and
    // are consumed once by `nativeDestroy`. Theme calls occur before destroy.
    let state = unsafe { &mut *(handle as *mut LauncherState) };

    if state.theme != config {
        state.theme = config;
        state.generation = state.generation.wrapping_add(1).max(1);
    }

    encode_theme_token(state.generation, state.theme)
}

/// Resolves one ARGB color role from a canonical theme token.
#[no_mangle]
pub extern "C" fn
Java_org_sableos_start_bridge_SableStartNative_nativeThemeColor(
    _env: *mut c_void,
    _object: *mut c_void,
    token: i64,
    role: i32,
) -> i32 {
    color_for_token(token, role)
}

/// Resolves the theme's canonical corner radius in density-independent pixels.
#[no_mangle]
pub extern "C" fn
Java_org_sableos_start_bridge_SableStartNative_nativeThemeCornerDp(
    _env: *mut c_void,
    _object: *mut c_void,
    token: i64,
) -> i32 {
    corner_dp_for_token(token)
}

/// Destroys a native launcher state previously returned by `nativeCreate`.
#[no_mangle]
pub extern "C" fn
Java_org_sableos_start_bridge_SableStartNative_nativeDestroy(
    _env: *mut c_void,
    _object: *mut c_void,
    handle: i64,
) {
    if handle == 0 {
        return;
    }

    // SAFETY: `handle` was created by `Box::into_raw` in `nativeCreate` and is
    // consumed exactly once here by the Activity lifecycle owner.
    unsafe {
        drop(Box::from_raw(handle as *mut LauncherState));
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn invalid_theme_ids_fall_back_to_defaults() {
        assert_eq!(
            normalized_theme(-1, 99, 42),
            ThemeConfig::default(),
        );
    }

    #[test]
    fn token_round_trip_preserves_canonical_theme() {
        let config = normalized_theme(2, 3, 0);
        let token = encode_theme_token(17, config);
        assert_eq!(decode_theme_token(token), config);
        assert_eq!(corner_dp_for_token(token), 4);
    }

    #[test]
    fn accent_is_independent_from_surface_style() {
        let metro = encode_theme_token(1, normalized_theme(0, 1, 1));
        let oled = encode_theme_token(1, normalized_theme(2, 1, 1));
        assert_eq!(
            color_for_token(metro, COLOR_PRIMARY),
            color_for_token(oled, COLOR_PRIMARY),
        );
        assert_ne!(
            color_for_token(metro, COLOR_SURFACE),
            color_for_token(oled, COLOR_SURFACE),
        );
    }
}
