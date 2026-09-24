# Sable Start R3C-R2 interaction + live-data status — 2026-09-11

> **HISTORICAL R3 EVIDENCE — 2026-09-24 classification:** preserved as exact milestone evidence. It predates the final R9 standalone SableLauncher HOME architecture and must not be used as current launcher-role status.


R3C-R2 exercised the exact installed R3B-R1 APK on Panther under bounded runtime authorization.

## Authorization boundary

```text
SOURCE_MUTATION_AUTHORIZED=NO
BUILD_AUTHORIZED=NO
PACKAGE_INSTALL_AUTHORIZED=NO
DEVICE_CONTACT_AUTHORIZED=YES
UI_INPUT_AUTHORIZED=YES_USER_MEDIATED
PERMISSION_MUTATION_AUTHORIZED=YES_USER_DIALOG_ONLY
HOME_ROLE_MUTATION_AUTHORIZED=NO
DEVICE_REBOOT_AUTHORIZED=NO
DEVICE_ROOT_AUTHORIZED=NO
DEVICE_REMOUNT_AUTHORIZED=NO
PACKAGE_UNINSTALL_AUTHORIZED=NO
USERDATA_OR_METADATA_WIPE_AUTHORIZED=NO
```

Expected / installed APK SHA-256:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

Exact installed artifact re-binding passed.

## Interaction results

The interaction capture produced these externally verifiable activity transitions:

```text
Files -> Open:
  top resumed activity = com.android.gallery3d/.app.GalleryActivity
  result = PASS

Files -> App info:
  top resumed activity = com.android.settings/.spa.SpaActivity
  result = PASS
```

The internal Sable Metro transitions `Start -> Settings` and `Search -> Settings` were captured as screenshots while SableMetroPreviewActivity remained resumed. Their screenshot hashes are preserved, but their visual contents were not independently inspected in the textual gate output; therefore their semantic visual correctness remains pending screenshot review rather than being claimed from activity state alone.

## Permission transition

Before the LIVE permission flow:

```text
READ_CALENDAR       granted=false / app-op ignore
READ_MEDIA_IMAGES   granted=false / app-op ignore
READ_MEDIA_AUDIO    granted=false / app-op ignore
```

After the user completed Sable Metro's normal Android permission dialogs:

```text
READ_CALENDAR       granted=true / app-op allow
READ_MEDIA_IMAGES   granted=true / app-op allow
READ_MEDIA_AUDIO    granted=true / app-op allow
```

No adb `pm grant` was used by the gate.

At the post-grant checkpoint, all three app-ops showed recent access. After the 65-second observation window, Calendar showed another recent access while the media app-op timestamps remained about one minute old. This is evidence of continued runtime activity and is consistent with a periodic refresh path, but it does not by itself prove that all live-data values were refreshed or visually changed.

## Stability and invariants

Sable Start process remained alive with PID 9433 through the sequence. The final error-focus output did not print a `FATAL EXCEPTION` / `AndroidRuntime` match. The default HOME resolver remained:

```text
com.android.launcher3/.uioverrides.QuickstepLauncher
```

Therefore:

```text
SABLE_METRO_R3C_R2_HOME_RESOLVER_UNCHANGED=PASS
```

## Screenshot evidence

```text
00_baseline.png                 46347d0137822dc9c6e942063b63b14fbd9efac25487e8447dadea9f6637fd9d
01_start.png                    ed23eb84f291cf69ff645cd02473136063ca96cfb5a96b7d23ea70c57cccaa4e
02_start_to_settings.png        eebc2827d52e985f51c1e62ab08f2faacfecf6a1dbf1cd53f50790dc5843d9cb
03_search_to_settings.png       e6e7e24cfdd1d77b64d8d4d717e2e18556bc1ad7654beb3f26824529de7452f1
04_files_open.png               12d51b97fd84451c0ef1e453bd1149c11f149b0fd167c68cda962d4657e5eeaa
05_files_app_info.png           2e6c1631c8cfeb8a739f1cdfac323cf16b260b75a2aa1c1c38d95da41120820d
06_live_permissions_denied.png  0f1de2bd286deabe48e5b0900535a89402b1b41d382adecc0acf6cf940105e53
07_live_permissions_granted.png 43bf80a05abdd36a23ddc97cbdf5f19cac1bae7d932466d42a8c8ca206284f74
08_live_after_65s.png            cc718702f113899c3ddc587cf65e8ee0fba7d178ee99b207e29e4d945a4bb87f
```

The distinct hashes for the pre-grant, post-grant, and post-65-second LIVE screenshots establish that the captured frames differ. They do not identify which UI values changed without visual review.

## Evidence seal

```text
EVIDENCE_DIR=/tmp/SABLE_METRO_R3C_R2_INTERACTION_20260911_114302
b053929f6b3516d04c46525f7b5c6254e250ac041aacf234623e328621b4fb84  SHA256SUMS.txt
```

## Current claim boundary

Proven in R3C-R2:

```text
exact installed artifact binding      PASS
Files -> Open external action          PASS
Files -> App info external action      PASS
runtime Calendar permission flow       PASS
runtime media image permission flow    PASS
runtime media audio permission flow    PASS
process survives interaction sequence  PASS
HOME resolver unchanged                PASS
```

Pending direct screenshot review before claiming semantic UI closure:

```text
Start -> Settings visual destination
Search -> Settings visual destination
LIVE denied-state presentation
LIVE granted-state values
specific Calendar / MediaStore values
specific 65-second refresh presentation
```

No source mutation, build, reinstall, reboot, HOME-role change, root/remount, uninstall, or data wipe was performed in this stage.
