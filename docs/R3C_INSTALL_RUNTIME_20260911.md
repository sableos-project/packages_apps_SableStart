# Sable Start R3C exact APK install + smoke runtime — 2026-09-11

> **HISTORICAL R3 EVIDENCE — 2026-09-24 classification:** preserved as exact milestone evidence. It predates the final R9 standalone SableLauncher HOME architecture and must not be used as current launcher-role status.


R3C validated installation and smoke runtime behavior for the exact R3B-R1 APK on the Panther test device.

## Authorization boundary

```text
SOURCE_MUTATION_AUTHORIZED=NO
BUILD_AUTHORIZED=NO
CLEAN_AUTHORIZED=NO
CLOBBER_AUTHORIZED=NO
NETWORK_FETCH_AUTHORIZED=NO
DEVICE_CONTACT_AUTHORIZED=YES
PACKAGE_INSTALL_AUTHORIZED=YES
DEVICE_REBOOT_AUTHORIZED=NO
DEVICE_ROOT_AUTHORIZED=NO
DEVICE_REMOUNT_AUTHORIZED=NO
DEVICE_SLOT_CHANGE_AUTHORIZED=NO
PACKAGE_UNINSTALL_AUTHORIZED=NO
USERDATA_OR_METADATA_WIPE_AUTHORIZED=NO
HOME_ROLE_MUTATION_AUTHORIZED=NO
```

## Exact artifact binding

Host APK:

```text
/srv/data/sableos_panther_graphene_2026081300_workspace/out/target/product/panther/system/app/SableStart/SableStart.apk
```

SHA-256:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

The gate independently verified:

```text
SABLE_METRO_R3C_APK_PRESENT=PASS
SABLE_METRO_R3C_EXACT_APK_BINDING=PASS
SABLE_METRO_R3C_ARCHIVE_PACKAGE=org.sableos.start
SABLE_METRO_R3C_ARCHIVE_PACKAGE_BINDING=PASS
SABLE_METRO_R3C_APK_STRUCTURE=PASS
manifest_present=True
dex_present=True
member_count=173
```

## Device binding

Device serial:

```text
2C160DLH20007H
```

Observed device identity:

```text
product=panther
model=Pixel_7
device=panther
ro.product.device=panther
ro.build.fingerprint=google/panther/panther:17/CP2A.260705.006/2026090500:userdebug/test-keys
ro.build.version.release=17
ro.build.version.security_patch=2026-08-05
```

The runtime device is therefore Panther / Android 17 on a `userdebug/test-keys` build. This is distinct from the module build's `TARGET_BUILD_VARIANT=user`; the R3C claim is specifically about installation and runtime on the observed device build above.

## Pre-install state

Sable Start was already installed as version 38 / version name 17. The current HOME resolver before installation was:

```text
com.android.launcher3/.uioverrides.QuickstepLauncher
```

No HOME-role mutation was authorized or performed.

## Installation result

The exact APK was installed with package replacement semantics. ADB reported:

```text
Performing Incremental Install
Success
Install command complete in 380 ms
SABLE_METRO_R3C_INSTALL_RC=0
SABLE_METRO_R3C_PACKAGE_INSTALL=PASS
```

Post-install package state remained:

```text
versionCode=38
versionName=17
```

The installed device APK path was:

```text
/data/app/~~5GMQl8Z7FXW9bRh3LjMxRA==/org.sableos.start-Wr8XafikZHanjenryukUnw==/base.apk
```

The device-side APK SHA-256 exactly matched the host artifact:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

Therefore:

```text
SABLE_METRO_R3C_INSTALLED_ARTIFACT_BINDING=PASS
```

## Smoke launch

The preview activity launch command succeeded:

```text
org.sableos.start/.ui.SableMetroPreviewActivity
Status: ok
LaunchState: UNKNOWN (0)
TotalTime: 0
WaitTime: 4
```

ADB also reported:

```text
Warning: Activity not started, intent has been delivered to currently running top-most instance.
```

Accordingly, this run proves successful intent delivery, resumed/top activity state, and live process state after launch, but does not establish a cold-launch timing result.

Observed post-launch process/activity evidence included:

```text
pid=9433
ResumedActivity=org.sableos.start/.ui.SableMetroPreviewActivity
mFocusedApp=org.sableos.start/.ui.SableMetroPreviewActivity
baseDir=/data/app/.../org.sableos.start.../base.apk
```

Gate results:

```text
SABLE_METRO_R3C_PREVIEW_LAUNCH=PASS
SABLE_METRO_R3C_PROCESS_ALIVE_AFTER_LAUNCH=PASS
SABLE_METRO_R3C_SCREENSHOT_CAPTURED=PASS
```

The captured screenshot and logcat evidence are preserved in the local evidence directory; no claim of visual correctness or absence of all runtime errors should be made from these summary lines alone.

## HOME resolver

The default HOME resolver after installation/launch remained:

```text
com.android.launcher3/.uioverrides.QuickstepLauncher
```

Therefore:

```text
SABLE_METRO_R3C_HOME_RESOLVER_UNCHANGED=PASS
```

This does not prove or disprove Sable Start's HOME capability in the manifest; it proves only that R3C did not change the currently selected/default HOME resolver.

## Evidence seal

Local evidence directory:

```text
/tmp/SABLE_METRO_R3C_INSTALL_RUNTIME_20260911_113703
```

Evidence seal:

```text
704ddc962c35ba9472dd78642982ffc05efc337195e3c1c1ffc44a8a4d004aa8  SHA256SUMS.txt
```

## Claim boundary

R3C establishes all of the following for the exact APK SHA-256 above:

```text
host artifact binding                 PASS
Panther device reachability           PASS
Panther product binding               PASS
package replacement installation      PASS
installed device artifact hash match  PASS
preview intent/activity reachability   PASS
process alive after launch            PASS
screenshot capture                    PASS
HOME resolver unchanged               PASS
```

R3C does not yet establish cold-launch timing, runtime permission flows, Calendar/MediaStore data correctness, Settings navigation, Files Open/App-info actions, Search-to-Settings behavior, broader interaction semantics, sustained runtime stability, performance, or HOME adoption.

The next gate should validate those interaction and live-data behaviors without source mutation or rebuild and should remain bound to the exact installed APK SHA-256.
