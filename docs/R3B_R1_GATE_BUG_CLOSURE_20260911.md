# Sable Start R3B-R1 gate false-negative closure — 2026-09-11

The R3B-R1 rerun passed the exact-source-bound Soong module build and emitted a valid `SableStart.apk`, but the gate mechanically reported `FAIL` because its post-build archive checks returned false negatives.

## Proven artifact state

The exact emitted APK is:

```text
/srv/data/sableos_panther_graphene_2026081300_workspace/out/target/product/panther/system/app/SableStart/SableStart.apk
```

Artifact identity:

```text
sha256=1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
size=4890816
file=Android package (APK), with APK Signing Block
```

Independent Python `zipfile` inspection established:

```text
is_zipfile=True
member_count=173
manifest_exact_count=1
dex_member_count=1
dex_members=['classes.dex']
```

`aapt2 dump badging` successfully parsed package `org.sableos.start`.

## Exact gate implementation

The failing checks were:

```bash
if unzip -l "$APK" | grep -q 'AndroidManifest.xml'; then
    pass SABLE_METRO_R3B_APK_HAS_MANIFEST
else
    fail SABLE_METRO_R3B_APK_HAS_MANIFEST
fi

if unzip -l "$APK" | grep -qE 'classes([0-9]+)?\.dex'; then
    pass SABLE_METRO_R3B_APK_HAS_DEX
else
    fail SABLE_METRO_R3B_APK_HAS_DEX
fi
```

The script runs with `set -o pipefail` semantics.

An exact reproduction using the same pipelines returned:

```text
manifest_exact_gate_rc=141
dex_exact_gate_rc=141
```

Return code 141 is SIGPIPE (`128 + 13`). In both cases `grep -q` exits immediately once it finds the requested archive member, the upstream `unzip -l` process receives SIGPIPE, and `pipefail` promotes the upstream non-zero status to the entire pipeline. The gate therefore records `FAIL` despite the requested archive member being present.

This precisely explains both post-build false negatives.

## R3B-R1 closure

The underlying build/package facts are therefore:

```text
EXACT_SOURCE_BINDING=PASS
SOONG_MODULE_BUILD=PASS
APK_LOCATED=PASS
APK_IS_VALID_ZIP=PASS
APK_HAS_ANDROIDMANIFEST=PASS
APK_HAS_CLASSES_DEX=PASS
AAPT2_PACKAGE_PARSE=PASS
APK_SIGNING_BLOCK=PRESENT
```

The historical aggregate gate result remains mechanically `FAIL` and must not be rewritten. The correct interpretation is that compile/package closure succeeded and the aggregate failure was caused by defective post-build check implementation.

No rebuild is required to establish those facts.

## Required gate correction

Future gates should avoid early-exit archive pipelines under `pipefail`. Preferred implementation is a direct archive API, for example Python `zipfile`, with exact membership checks.

The build-log failure classifier should also be corrected separately: the broad `|kotlinx\.coroutines` alternative can classify ordinary successful coroutine build activity as `COROUTINES_CLASSPATH`. Concrete compiler diagnostics should take precedence over generic dependency-name matches.

## Claim boundary

Proven: exact R3B-R1 source identity, successful module-scoped Soong/Kotlin/Android build, emitted APK identity, archive structure, manifest presence, DEX presence, package parsing, and signing-block presence.

Not proven by this closure: package installation, runtime permission behavior, provider data correctness, live-tile semantics, interaction behavior on the device, HOME adoption, or performance on Panther.
