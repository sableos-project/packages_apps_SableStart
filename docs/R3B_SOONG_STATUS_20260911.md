# Sable Start R3B Soong status — 2026-09-11

> **HISTORICAL R3 EVIDENCE — 2026-09-24 classification:** preserved as exact milestone evidence. It predates the final R9 standalone SableLauncher HOME architecture and must not be used as current launcher-role status.


The first exact module-scoped Soong compile/type-check gate reached the real Android build graph but did not close.

Validated pre-build facts from the first gate:

```text
SOURCE_MUTATION_AUTHORIZED=NO
WORKSPACE_OUTPUT_MUTATION_AUTHORIZED=YES_BUILD_OUTPUTS_ONLY
BUILD_AUTHORIZED=YES_MODULE_ONLY
MODULE=SableStart
NETWORK_FETCH_AUTHORIZED=NO
DEVICE_CONTACT_AUTHORIZED=NO
PACKAGE_INSTALL_AUTHORIZED=NO
```

Source and environment preflight passed:

```text
SABLE_METRO_R3B_APP_DIR_PRESENT=PASS
SABLE_METRO_R3B_EXACT_SOURCE_BINDING=PASS
SABLE_METRO_R3B_BUILD_SOURCE_INVENTORY_COUNT=11
SABLE_METRO_R3B_EXPECTED_SOURCE_INVENTORY=PASS
SABLE_METRO_R3B_MANIFEST_XML=PASS
SABLE_METRO_R3B_APPLIED_SOURCE_PRECHECK=PASS
SABLE_METRO_R3B_ENVSETUP=PASS
TARGET_PRODUCT=panther
TARGET_BUILD_VARIANT=user
TARGET_RELEASE=cur
```

The first build failed after reaching concrete Kotlin compiler diagnostics. The gate initially classified the failure as `COROUTINES_CLASSPATH`, but follow-up evidence showed that `kotlinx_coroutines_android` built successfully and the actual SableStart errors were:

```text
line 151: 'onRequestPermissionsResult' overrides nothing
line 156: argument type mismatch: Array<out String> vs Array<String>
```

No `Android.bp` coroutine dependency change was justified by that evidence.

## R3B-R1 minimal source correction

The exact pre-fix file identity was confirmed before mutation:

```text
12db1f5a6fc6584dd462395d5f30f44d8980c4edd92b901bb52f9cb921eca1a2  SableMetroPreviewActivity.kt
```

The single matching callback parameter was changed from:

```kotlin
permissions: Array<out String>,
```

to:

```kotlin
permissions: Array<String>,
```

The post-fix source identity is:

```text
dde4196f82ea4357cfdef5a78fe0d9824971d93e52ee6886923c6be245f1f249  SableMetroPreviewActivity.kt
```

Observed mutation gate facts:

```text
old_signature_match_count=1
SABLE_METRO_R3B_R1_SOURCE_MUTATION=PASS
```

No other source or build-system change was implied by this correction.

## R3B-R1 rerun

The exact-source-bound rerun completed with the same authorization boundaries and produced:

```text
EVIDENCE_DIR=/tmp/SABLE_METRO_R3B_SOONG_20260911_105652
SABLE_METRO_R3B_EXACT_SOURCE_BINDING=PASS
SABLE_METRO_R3B_BUILD_SOURCE_INVENTORY_COUNT=11
SABLE_METRO_R3B_EXPECTED_SOURCE_INVENTORY=PASS
SABLE_METRO_R3B_MANIFEST_XML=PASS
SABLE_METRO_R3B_APPLIED_SOURCE_PRECHECK=PASS
SABLE_METRO_R3B_ENVSETUP=PASS
TARGET_PRODUCT=panther
TARGET_BUILD_VARIANT=user
TARGET_RELEASE=cur
SABLE_METRO_R3B_BUILD_ELAPSED_SECONDS=726
SABLE_METRO_R3B_SOONG_MODULE_BUILD=PASS
SABLE_METRO_R3B_APK_LOCATED=PASS
SABLE_METRO_R3B_APK_PATH=/srv/data/sableos_panther_graphene_2026081300_workspace/out/target/product/panther/system/app/SableStart/SableStart.apk
SABLE_METRO_R3B_APK_HAS_MANIFEST=FAIL
SABLE_METRO_R3B_APK_HAS_DEX=FAIL
SABLE_METRO_R3B_AAPT_TOOL=/srv/data/sableos_panther_graphene_2026081300_workspace/out/host/linux-x86/bin/aapt2
SABLE_METRO_R3B_APK_BADGING=INSPECTED
SABLE_METRO_R3B_SOONG_COMPILE_TYPECHECK_GATE=FAIL
```

Evidence seal:

```text
ce9b4fd306bc03990f3e99376d8fb390f59cbf11bbb919d82f5704b28e75eedd  SHA256SUMS.txt
```

## Independent APK forensics

A read-only follow-up inspected the exact APK emitted at the gate path. The artifact identity was:

```text
path=/srv/data/sableos_panther_graphene_2026081300_workspace/out/target/product/panther/system/app/SableStart/SableStart.apk
size=4890816
sha256=1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
file=Android package (APK), with APK Signing Block
```

Python `zipfile` inspection independently established:

```text
is_zipfile=True
member_count=173
manifest_exact_count=1
dex_member_count=1
dex_members=['classes.dex']
```

The archive member list explicitly contained both:

```text
classes.dex
AndroidManifest.xml
```

`aapt2 dump badging` also successfully parsed the package and reported:

```text
package: name='org.sableos.start' versionCode='38' versionName='17' platformBuildVersionName='17' platformBuildVersionCode='37' compileSdkVersion='37' compileSdkVersionCodename='17'
minSdkVersion:'37'
targetSdkVersion:'37'
application-label:'Sable Start'
```

The APK declares the expected live-data permissions, including `READ_CALENDAR`, `READ_MEDIA_IMAGES`, `READ_MEDIA_AUDIO`, and legacy `READ_EXTERNAL_STORAGE` with max SDK 32.

## Gate implementation forensics

The post-build checks in the R3B-R1 gate were confirmed to use early-exit `grep -q` pipelines under global `set -o pipefail` semantics:

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

A read-only reproduction demonstrated:

```text
manifest_pipeline_rc=141
manifest_control_rc=0
dex_control_rc=0
```

Return code 141 is SIGPIPE (128 + 13). This proves that the manifest check can report failure even when the manifest is present: `grep -q` exits after finding its match, the upstream `unzip` receives SIGPIPE, and `pipefail` promotes that upstream failure to the pipeline status.

The DEX member is independently proven present by Python `zipfile`, and a non-early-exit control also succeeds. The exact DEX false-negative mechanism is not yet isolated because the first reproduction used `unzip -Z1` rather than the gate's exact `unzip -l` command. The packaging claim does not depend on resolving that shell-level detail.

The build-log failure classifier also contains an over-broad coroutine branch:

```bash
if grep -qiE 'unresolved reference.*(kotlinx|Dispatchers|withContext|delay)|kotlinx\.coroutines' "$BUILD_LOG"; then
    record SABLE_METRO_R3B_FAILURE_CLASS COROUTINES_CLASSPATH
```

Because any normal `kotlinx.coroutines` build activity satisfies the second alternative, concrete Kotlin type errors can be misclassified. A corrected gate should classify concrete compiler diagnostics before generic dependency-name matches and should use a direct archive API such as Python `zipfile` for APK member checks.

### Corrected claim boundary

The R3B-R1 source has passed the actual Soong module build, and the exact emitted APK has independently passed archive-structure verification for both `AndroidManifest.xml` and `classes.dex`. The package also parses successfully with `aapt2` and has an APK Signing Block.

Therefore, the gate's `APK_HAS_MANIFEST=FAIL` and `APK_HAS_DEX=FAIL` results are false negatives in the post-build inspection logic, not properties of the APK. The aggregate gate remains mechanically reported as `FAIL`, but the underlying compile and APK-structure evidence is positive.

No additional source mutation or rebuild is justified by these two false-negative checks. The gate implementation should be corrected separately, preserving the original evidence.

No clean/clobber, network fetch, device contact, or package install occurred in these compile and forensic stages.

The original failed-build evidence remains:

```text
/tmp/SABLE_METRO_R3B_SOONG_20260911_092512
92041ee86793549ceef22863307d85451a288bef470decc75e73582ecc231c53  SHA256SUMS.txt
```
