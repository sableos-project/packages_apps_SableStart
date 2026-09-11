# Sable Start migration status

Status: **IN PROGRESS — validated source captured locally with recursive byte verification; canonical source commit/push and reconstruction remain open.**

The authoritative validated working copy remains the Panther GrapheneOS 2026081300 workspace until the migrated source is committed, reviewed, and rebuilt through the new repository/manifest structure.

## R4 recursive capture result — 2026-09-11

A clean local clone of `sableos-project/packages_apps_SableStart` was created at:

```text
/srv/data/sableos/repos/packages_apps_SableStart
```

The clone was bound to organization `main` at:

```text
0b32d238539cd7d46ea1e0fcb477acc18dcbfbcc
```

and a dedicated local migration branch was created:

```text
m1/sablestart-portability-close-20260911
```

The R4 capture gate recursively copied the complete SableStart module tree from the validated workspace without deleting destination metadata and then independently verified every captured source file by SHA-256.

Observed results:

```text
SABLESTART_R4_SOURCE_FILE_COUNT=12
SABLESTART_MISSING_COUNT=0
SABLESTART_MISMATCH_COUNT=0
SABLESTART_UNEXPECTED_COUNT=0
SABLESTART_PORTABLE_SNAPSHOT=PASS
SABLESTART_R4_CAPTURE_VERIFY=PASS
```

The validated R3B-R1 preview activity identity was preserved:

```text
dde4196f82ea4357cfdef5a78fe0d9824971d93e52ee6886923c6be245f1f249  src/com/sable/start/ui/SableMetroPreviewActivity.kt
```

The capture added source under these top-level paths in the local migration branch working tree:

```text
Android.bp
AndroidManifest.xml
res/
rust/
src/
```

Local R4 evidence directory:

```text
/tmp/SABLESTART_R4_PORTABILITY_CAPTURE_20260911_121924
```

Evidence seal:

```text
af3775b45ab231e2100de9267854eb47014ca860c3e2e6aae32f21f73f33ffe1  SHA256SUMS.txt
```

No Android workspace mutation, build, device contact, clean/clobber, delete, or network fetch occurred during the R4 capture/verification stage itself. The preceding repository bootstrap used a separately authorized one-time HTTPS clone.

## Migration gate

Before this repository becomes canonical source, migration still must prove:

- source capture is committed on the dedicated migration branch with a sealed commit identity;
- the commit contains the same 12 validated source files with zero byte mismatches;
- module build succeeds with the migrated repository supplying `packages/apps/SableStart`;
- the migrated artifact remains behaviorally equivalent to the already validated R3C runtime artifact where applicable;
- a clean reconstruction through `platform_manifest` succeeds.

## Claim boundary

R4 proves that the validated SableStart module can be captured into the new repository working tree with zero missing files, zero unexpected migrated source files, and zero byte mismatches. It does **not** yet make the organization repository canonical source because the capture has not yet been committed/pushed and no build has yet been performed from the migrated repository checkout.
