# R7 Sable Start production surfaces

Status: **normative launcher requirements for the R7 production-surface milestone.**

R7 promotes the validated Sable Metro prototype surfaces into the production `SableStartActivity` while preserving the launcher architecture proven during R6:

```text
SableStartActivity
    product HOME / launcher UI

Launcher3 Quickstep
    recents / gesture provider
```

R7 does **not** turn Sable Start into a Quickstep replacement and does not duplicate Android system services or security-critical platform plumbing.

## 1. Goals

R7 must convert the useful prototype surfaces into real launcher behavior without retaining demo data. The production surfaces are:

- Start;
- All Apps;
- App Context;
- Search;
- Pinned & Recent;
- Sable Start Settings;
- Live local data;
- Lock preview.

All production data must come from real Android state or local Sable Start state. Hard-coded demo application inventories, fake notification counts, fake Settings categories, and fake search results are prohibited.

## 2. Privacy and security rules

R7 follows least privilege:

- no network permission is introduced for these launcher surfaces;
- pinned and launcher-local recent state is stored locally in Sable Start preferences;
- "recent" initially means applications successfully launched through Sable Start, not global Usage Stats;
- no Usage Stats permission is required;
- Live data permissions are requested only after an explicit user action;
- Weather and Tasks remain unavailable until explicit providers are designed;
- App info routes to Android Settings rather than duplicating package-management authority;
- uninstall/package mutation is not implemented as a privileged launcher action;
- Lock remains a visual preview only and does not replace Android Keyguard/SystemUI.

## 3. Real application model

The R6 launcher-visible inventory remains the single source of truth for All Apps and Search.

Each `AppEntry` must retain:

```text
label
component
profile/user
profile serial
real launcher icon where available
```

Ordering remains deterministic by label, package, class, then profile serial.

Application identity for local state must use the stable combination of profile serial and exact component identity. Labels are presentation data and must not be used as persistent identity.

## 4. Start

The production Start surface must retain:

- device-local date;
- R6 greeting buckets;
- live launcher-visible app count.

It additionally exposes navigation to:

- All Apps;
- Search;
- Pinned & Recent;
- Sable Start Settings.

Start may summarize pinned/recent state, but it must not fabricate message counts, media recency, Play Store state, or other application content.

## 5. All Apps

All Apps must render the same real launcher-visible inventory proven by R6.

Requirements:

- real label;
- real installed application icon when Android supplies one;
- package name as secondary identity;
- deterministic ordering;
- exact component/profile launch;
- per-row route to App Context;
- Search entry point using the same inventory.

If icon loading fails, a deterministic textual fallback may be rendered, but the normal path must use the Android-provided icon.

## 6. App Context

App Context operates on one exact `AppEntry` and may provide:

- Open;
- App info;
- Pin / Unpin.

`Open` uses the same exact component/profile launch path as All Apps.

`App info` delegates to Android Settings. If an app/profile combination cannot safely be routed to the system app-info UI, Sable Start must fail closed rather than inventing privileged behavior.

Pin state is launcher-local and must not mutate Android package state.

## 7. Search

Search continues to use the exact R6 live inventory. Empty Search returns the full current inventory. Filtering may match:

- label;
- package name.

Search results must retain exact profile/component identity and support the same launch/context behavior as All Apps.

No cloud search, network lookup, recommendation service, or fake Settings result is part of R7.

## 8. Pinned & Recent

Pinned applications are explicitly selected by the user and stored locally.

Recent applications are a privacy-preserving launcher-local history of successful launches performed through Sable Start.

Requirements:

- no Usage Stats permission;
- no global activity-history scraping;
- stale package/component entries disappear from the rendered view when no longer present in the live inventory;
- bounded recent history;
- re-launch uses the exact current `AppEntry` resolved from the live inventory.

## 9. Sable Start Settings

The Settings surface is for **Sable Start-owned behavior only**. It must not visually clone Android Settings or imply ownership of platform networking, telephony, permissions, battery, storage, or system-update plumbing.

Initial R7 Settings provides launcher-local navigation/information for:

- Live local data;
- Lock preview;
- privacy explanation for pinned/recent state and optional Live permissions.

Broader appearance/theme customization remains owned by the R8 design/customization milestone.

## 10. Live local data

The existing local Live surface is promoted from preview to a production-accessible Sable Start surface.

Supported local providers remain:

- Photos count;
- Music count;
- Calendar-today count.

Permission-gated providers must report `permission required` before access and may request the relevant Android permission only after explicit user action.

Weather and Tasks remain `unavailable` until separately specified providers exist. No network permission is added merely to make the prototype look complete.

## 11. Lock preview

The Lock surface remains an explicitly labeled visual preview. It may display current local time/date and Sable visual language, but it must not claim to be the device lock screen and must not alter Keyguard/SystemUI/default security behavior.

Actual lock-screen integration requires a separate platform architecture and security review.

## 12. Runtime state and refresh

Package add/remove/change callbacks and Activity resume continue to refresh the real launcher inventory.

Pinned and recent views are resolved against the latest inventory so removed/disabled applications do not remain launchable through stale local state.

A successful application launch should update launcher-local recent history. Failed launches must not be recorded as successful recents and should trigger an inventory refresh.

## 13. Acceptance direction

R7 production-surface acceptance requires evidence for at least:

```text
production HOME still resolves to SableStartActivity
Launcher3 Quickstep still supplies recents/gesture behavior
All Apps count matches the supported launcher-visible inventory
real application icons render for representative entries
App Context Open launches the exact app/profile
Pin and Unpin persist locally
Recent history updates only after successful Sable Start launches
Search shares the same live inventory
Sable Start Settings contains no fake Android-settings ownership
Live permissions remain explicit and optional
Lock is labeled as preview-only
no new network permission
no Usage Stats permission
no unexpected package/default-role mutation
no Sable Start crash during representative navigation
```

Native Compose UI Test should cover deterministic in-app navigation/state once the expanded storage environment is available. AndroidX UIAutomator should cover cross-package/system boundaries. Shell evidence gates remain the outer artifact/device-binding layer.

## 14. Non-goals

R7 production surfaces do not include:

- Quickstep/Recents replacement;
- folders;
- hidden-app policy;
- cloud search;
- recommendation/ranking services;
- Usage Stats-based global recents;
- privileged silent uninstall;
- replacement Android Settings;
- production lock-screen/Keyguard replacement;
- Weather/Tasks network providers;
- R8 theme editor/customization framework.
