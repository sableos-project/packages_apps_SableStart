# Sable Start R3B Soong status — 2026-09-11

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

### Current claim boundary

The R3B-R1 source has now passed the actual Soong module build. This establishes Kotlin/Android compile closure for the module build invocation and proves that an APK path was emitted. The overall gate remains failed because the gate's post-build APK-content checks reported no manifest and no DEX entry.

Those two APK-content failures must be investigated before treating packaging as closed. They should not be interpreted as a source compile failure. In particular, the installed system APK may differ from an intermediate unstripped APK because Android dexpreopt can change the installed artifact, while `AndroidManifest.xml` should still be independently verified with archive listing and `aapt2` inspection.

No source mutation, clean/clobber, network fetch, device contact, or package install is justified by the current evidence. The next action is read-only APK/post-build forensics and review of the gate's archive-member detection logic.

The original failed-build evidence remains:

```text
/tmp/SABLE_METRO_R3B_SOONG_20260911_092512
92041ee86793549ceef22863307d85451a288bef470decc75e73582ecc231c53  SHA256SUMS.txt
```
