# Sable Start R3C-R3 visual closure — 2026-09-11

> **HISTORICAL R3 EVIDENCE — 2026-09-24 classification:** preserved as exact milestone evidence. It predates the final R9 standalone SableLauncher HOME architecture and must not be used as current launcher-role status.


R3C-R3 reviewed the bound screenshot evidence from the sealed R3C-R2 interaction run and closes the remaining visual-semantic claims for the exact installed R3B-R1 artifact.

## Evidence binding

R3C-R3 visual bundle archive:

```text
SABLE_METRO_R3C_R3_VISUAL_CLOSURE_20260911_115828.tar.gz
sha256=b487607d07addb8eac991e6f89082111b7abb9eb000ea11e518984a9a3eec7dd
```

Bundle manifest seal:

```text
48123bf37feae77ef3467b3a1ce28a572cde84cabee4c813855eab0b5a2a0ce4  SHA256SUMS.txt
```

The bundle is bound to the R3C-R2 evidence seal:

```text
b053929f6b3516d04c46525f7b5c6254e250ac041aacf234623e328621b4fb84
```

and to the exact installed APK:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

All five screenshot hashes matched their R3C-R2 recorded identities.

## Visual closure results

### Start -> Settings

`02_start_to_settings.png` visibly shows the Sable Metro `settings` preview (`6 / 8`) after the Start-screen Settings action. The screen contains the expected Settings categories, including Network & internet, Connected devices, Apps, Notifications, Battery, Storage, Security & privacy, and System.

Result:

```text
R3C_R3_START_TO_SETTINGS=PASS
```

### Search -> Settings

`03_search_to_settings.png` visibly shows the same Sable Metro `settings` preview (`6 / 8`) after selecting the Settings result from the Search preview.

Result:

```text
R3C_R3_SEARCH_TO_SETTINGS=PASS
```

### LIVE denied state

`06_live_permissions_denied.png` shows the LIVE preview (`7 / 8`) with:

```text
Photos    permission required
Music     permission required
Calendar  permission required
Weather   provider not configured / unavailable
Tasks     provider not configured / unavailable
```

The screen provides an `Enable local live data` action. It does not present fabricated Photos/Music/Calendar counts while access is denied, and Weather/Tasks are explicitly represented as unavailable pending provider configuration.

Result:

```text
R3C_R3_LIVE_DENIED_STATE=PASS
```

### LIVE granted state

`07_live_permissions_granted.png`, together with R3C-R2 package/app-op evidence, shows the normal permission transition succeeded and LIVE displays:

```text
Photos    3 photos / live
Music     no tracks found / empty
Calendar  no events today / empty
Weather   provider not configured / unavailable
Tasks     provider not configured / unavailable
```

This is consistent with real local provider-backed state rather than placeholders. The accompanying R3C-R2 evidence has all three target permissions granted and app-ops set to `allow`.

Result:

```text
R3C_R3_LIVE_GRANTED_PRESENTATION=PASS
```

### 65-second refresh / stability

`07_live_permissions_granted.png` shows:

```text
11:50 AM
updated 11:50 AM
```

`08_live_after_65s.png` shows:

```text
11:51 AM
updated 11:51 AM
```

while the LIVE screen remains intact and the visible local-data values remain stable (`3 photos`, `no tracks found`, `no events today`). R3C-R2 retained process PID `9433` through the sequence.

The app-op timing evidence after the wait shows a recent Calendar access while image/audio access timestamps remain older. Therefore this evidence proves a visible periodic LIVE refresh and a Calendar re-read, but it does not independently prove that every MediaStore source was re-queried during that exact refresh cycle.

Result:

```text
R3C_R3_LIVE_PERIODIC_REFRESH=PASS
R3C_R3_LIVE_65S_STABILITY=PASS
```

## Combined R3C interaction closure

R3C-R2 already established:

```text
exact installed APK binding                    PASS
Files -> Open                                  PASS
Files -> App info                              PASS
READ_CALENDAR permission flow                  PASS
READ_MEDIA_IMAGES permission flow              PASS
READ_MEDIA_AUDIO permission flow               PASS
process survival through sequence              PASS
HOME resolver unchanged                        PASS
```

R3C-R3 now establishes the remaining visual claims:

```text
Start -> Settings                              PASS
Search -> Settings                             PASS
LIVE denied-state presentation                 PASS
LIVE granted local-data presentation           PASS
LIVE visible periodic refresh                  PASS
LIVE stability after 65-second observation     PASS
```

Therefore:

```text
SABLE_METRO_R3C_RUNTIME_INTERACTION_CLOSURE=PASS
```

## Claim boundary

This closes the targeted R3 interaction/runtime defects and live-data presentation on the observed Panther / Android 17 test device for the exact installed APK above.

It does **not** establish HOME adoption, production performance, long-duration soak stability, correctness on other devices/substrates, or portable-source reconstruction from the new organization repositories.

The next project gate is the complete recursive SableStart source capture and byte-for-byte portability verification into `sableos-project/packages_apps_SableStart`, followed by a build from the migrated checkout and later clean manifest-driven reconstruction.
