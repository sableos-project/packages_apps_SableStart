# Sable Start R3B Soong status — 2026-09-11

The first exact module-scoped Soong compile/type-check gate reached the real Android build graph but did not close.

Validated pre-build facts from the gate:

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

Build result:

```text
SABLE_METRO_R3B_BUILD_ELAPSED_SECONDS=4123
SABLE_METRO_R3B_SOONG_MODULE_BUILD=FAIL
SABLE_METRO_R3B_FAILURE_CLASS=COROUTINES_CLASSPATH
```

The gate-reported failure class is now known to be a classifier false positive. Follow-up evidence showed that `kotlinx_coroutines_android` built successfully in the same graph and the SableStart compiler reached two concrete Kotlin errors in `SableMetroPreviewActivity.kt`:

```text
line 151: 'onRequestPermissionsResult' overrides nothing
line 156: argument type mismatch: Array<out String> vs Array<String>
```

No `Android.bp` coroutine dependency change is justified by this evidence.

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

No other source or build-system change is implied by this correction.

Evidence directory for the original failed build on the validated build host:

```text
/tmp/SABLE_METRO_R3B_SOONG_20260911_092512
```

Evidence seal reported by the original gate:

```text
92041ee86793549ceef22863307d85451a288bef470decc75e73582ecc231c53  SHA256SUMS.txt
```

Interpretation: revised R3 source reached actual Soong/Kotlin compilation. Android build closure and APK packaging remain unproven until the module gate is rerun against the new exact source hash. The gate failure classifier should also be narrowed so normal coroutine build-log lines do not override concrete compiler diagnostics.
