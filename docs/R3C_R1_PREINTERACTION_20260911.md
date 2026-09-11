# Sable Start R3C-R1 pre-interaction runtime inventory — 2026-09-11

This document records the read-only pre-interaction state of the exact R3C-installed Sable Start artifact on Panther before any new UI input or runtime permission mutation.

## Authorization boundary

```text
DEVICE_CONTACT_AUTHORIZED=YES
SOURCE_MUTATION_AUTHORIZED=NO
BUILD_AUTHORIZED=NO
PACKAGE_INSTALL_AUTHORIZED=NO_ADDITIONAL_INSTALL
PERMISSION_MUTATION_AUTHORIZED=NO_THIS_STAGE
UI_INPUT_AUTHORIZED=NO_THIS_STAGE
```

Evidence directory:

```text
/tmp/SABLE_METRO_R3C_R1_PREINTERACTION_20260911_113907
```

## Installed package state

The package remained installed at the post-R3C code path:

```text
/data/app/~~5GMQl8Z7FXW9bRh3LjMxRA==/org.sableos.start-Wr8XafikZHanjenryukUnw==/base.apk
```

Observed package version:

```text
versionCode=38
versionName=17
minSdk=37
targetSdk=37
```

## Runtime permission baseline

The live-data permissions were present in the package but not granted at this stage:

```text
android.permission.READ_CALENDAR: granted=false
android.permission.READ_MEDIA_IMAGES: granted=false
android.permission.READ_MEDIA_AUDIO: granted=false
android.permission.READ_MEDIA_VISUAL_USER_SELECTED: granted=false
```

App-ops likewise reported the relevant operations as `ignore`:

```text
READ_CALENDAR: ignore
READ_MEDIA_IMAGES: ignore
READ_MEDIA_AUDIO: ignore
READ_MEDIA_VISUAL_USER_SELECTED: ignore
```

This establishes a clean denied/ungranted baseline for the subsequent user-mediated runtime permission flow. No permission mutation occurred in this inventory stage.

`android.permission.OTHER_SENSORS` was reported granted, but it is not part of the live Calendar/MediaStore validation target for this gate.

## Activity/process state

The preview activity remained the resumed/focused activity and the Sable Start process remained alive:

```text
process pid=9433
ResumedActivity=org.sableos.start/.ui.SableMetroPreviewActivity
mFocusedApp=org.sableos.start/.ui.SableMetroPreviewActivity
```

A pre-interaction screenshot was captured with SHA-256:

```text
46347d0137822dc9c6e942063b63b14fbd9efac25487e8447dadea9f6637fd9d
```

## Accessibility/UI hierarchy observation

`uiautomator dump /dev/tty` returned exit code 0, but the extracted output contained no text/content-description/clickable nodes matching the inventory probe. Therefore this stage does not establish a usable accessibility-semantic tree for automating Sable Start interactions. This is an observation about available evidence, not a failure of the rendered UI.

The next interaction gate should not assume that semantic node lookup is available; it should use user-mediated interaction and screenshot/activity/log evidence unless a richer accessibility tree is independently demonstrated.

## Runtime log review

The focused log extract contains normal package-update, process-start, window-management, IME, and ADB activity for `org.sableos.start`. It does not show a `FATAL EXCEPTION` attributed to the Sable Start process in the supplied evidence.

Several `SecurityException` / permission-denial lines in the same broad log extract belong to other packages such as Google Play services (`com.google.android.gms`, uid 10196) and must not be attributed to Sable Start.

The package replacement log confirms that Android retained the application data while moving to the new code path and force-stopped the previous Sable Start process as part of normal package update handling.

## Evidence seal

```text
7a014db760cad0a11b6d3c0283eed9de0fb14bb544060fa49546a420ed70ea8f  SHA256SUMS.txt
```

## Claim boundary

R3C-R1 establishes the exact pre-interaction state immediately before the next runtime interaction gate:

```text
exact installed package remains present        PASS
version 38 / version name 17                   OBSERVED
preview activity resumed/focused               PASS
Sable Start process alive                      PASS
pre-interaction screenshot captured            PASS
READ_CALENDAR granted                          NO
READ_MEDIA_IMAGES granted                      NO
READ_MEDIA_AUDIO granted                       NO
relevant app-ops                               IGNORE
permission mutation during this stage          NONE
UI input during this stage                     NONE
Sable Start fatal exception in supplied focus  NOT OBSERVED
usable UIAutomator semantic tree                NOT ESTABLISHED
```

The next stage may validate the previously failing Settings/Files actions and the new Calendar/MediaStore permission/live-data behavior while remaining bound to the exact installed R3C artifact.
