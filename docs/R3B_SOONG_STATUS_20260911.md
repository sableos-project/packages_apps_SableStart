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

Evidence directory on the validated build host:

```text
/tmp/SABLE_METRO_R3B_SOONG_20260911_092512
```

Evidence seal reported by the gate:

```text
92041ee86793549ceef22863307d85451a288bef470decc75e73582ecc231c53  SHA256SUMS.txt
```

Interpretation: the revised R3 source is bound and reached Soong/Kotlin compilation, but Android build closure and APK packaging are not proven. The next action is bounded classpath/module forensics in the exact workspace. Do not guess a coroutine module name or fetch a dependency from the network.
