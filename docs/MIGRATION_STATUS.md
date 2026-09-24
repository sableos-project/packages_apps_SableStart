# Sable Start migration status

> **HISTORICAL / SUPERSEDED — 2026-09-24:** this document records an earlier SableStart milestone. Final Panther R9 physically accepted standalone `org.sableos.launcher` as HOME, with Launcher3QuickStep Recents-only; the standalone SableStart runtime product is retired. Preserve the body as historical requirements/evidence, not current execution authority.


Status: **HISTORICAL_EVIDENCE / SUPERSEDED CURRENT ROLE.**

The organization repository checkout is now proven to build `SableStart` directly at the canonical Android source path and to reproduce the previously validated APK hash on the same Panther substrate/toolchain. The historical Panther workspace remains an important reference until the complete source composition is reconstructible from `platform_manifest` and the source integration boundary is closed.

## Validated source baseline

Authoritative validated working copy used for migration:

```text
/srv/data/sableos_panther_graphene_2026081300_workspace/packages/apps/SableStart
```

Validated R3 source file count:

```text
12
```

Validated preview activity SHA-256:

```text
dde4196f82ea4357cfdef5a78fe0d9824971d93e52ee6886923c6be245f1f249  src/com/sable/start/ui/SableMetroPreviewActivity.kt
```

Previously validated R3B APK SHA-256:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

## R4 recursive capture — PASS

Migration clone:

```text
/srv/data/sableos/repos/packages_apps_SableStart
```

Migration branch:

```text
m1/sablestart-portability-close-20260911
```

The R4 capture gate recursively copied the complete validated module source set and independently verified every migrated source file.

Observed closure:

```text
SABLESTART_R4_SOURCE_FILE_COUNT=12
SABLESTART_MISSING_COUNT=0
SABLESTART_MISMATCH_COUNT=0
SABLESTART_UNEXPECTED_COUNT=0
SABLESTART_PORTABLE_SNAPSHOT=PASS
SABLESTART_R4_CAPTURE_VERIFY=PASS
```

R4 evidence seal:

```text
af3775b45ab231e2100de9267854eb47014ca860c3e2e6aae32f21f73f33ffe1
```

## R4-R1 exact local migration commit — PASS

The 12 migrated files were staged only after their bytes were rebound to the authoritative workspace source set. The committed Git blobs were then verified again and the authoritative workspace was proven unchanged.

Sealed migration identity:

```text
COMMIT_SHA=059d5d23e4186bbd3119180433a5e6206b7d95bd
TREE_SHA=c00fd741c401fdd1421e8971bfb82f01c4b7c7da
PARENT_SHA=0b32d238539cd7d46ea1e0fcb477acc18dcbfbcc
```

R4-R1 evidence seal:

```text
1689008c605a08e83dabb1b680e184e46dabb912e365938fda1e9bd73147573c
```

Key closure:

```text
SABLESTART_R4_R1_STAGED_SET_BINDING=PASS
SABLESTART_R4_R1_STAGED_BYTE_BINDING=PASS
SABLESTART_R4_R1_COMMITTED_BYTE_BINDING=PASS
SABLESTART_R4_R1_POSTCOMMIT_CLEAN=PASS
SABLESTART_R4_R1_SOURCE_UNCHANGED=PASS
SABLESTART_R4_R1_LOCAL_COMMIT_SEAL=PASS
```

## R4-R2 sealed branch push — PASS

The sealed local commit was pushed without rebase, merge, or force-push to:

```text
m1/sablestart-portability-close-20260911
```

Remote branch SHA:

```text
059d5d23e4186bbd3119180433a5e6206b7d95bd
```

Remote `main` at the push gate was:

```text
60bd4c5d6c698784af9470ab5d84bcf4610c70f4
```

GitHub comparison established the source-only migration boundary: one source commit, exactly 12 added source files, 1802 additions, and zero deletions.

R4-R2 evidence seal:

```text
a133f8950a41a4dc2ecbc62a73085f1b1a4ec59d5b8d7e5f6b97f255ad16d7d6
```

Note: one local R4-R2 path-disjointness shell check emitted `comm` sorted-order warnings. That line is not treated as sufficient evidence; the GitHub comparison independently established the source-only diff boundary.

## Pull request — OPEN / UNMERGED

PR #1:

```text
Import validated SableStart R3 source
base: main
head: m1/sablestart-portability-close-20260911
head SHA: 059d5d23e4186bbd3119180433a5e6206b7d95bd
```

The source migration commit has intentionally not been rewritten merely to absorb later documentation/CI changes on `main`.

PR integration remains a separate authorization/acceptance step. An open PR or a successful migrated-checkout build is not itself evidence that the source has been merged.

## R5 sandbox preflights — HOST LIMITATION, NO BUILD

Two bubblewrap-based approaches stopped before compilation because the ThinkPad host policy does not permit the required unprivileged namespace setup.

Observed stops:

```text
bwrap: loopback: Failed RTM_NEWADDR: Operation not permitted
SABLESTART_R5_BWRAP_USERNS_PREFLIGHT=FAIL
```

and:

```text
bwrap: setting up uid map: Permission denied
SABLESTART_R5_R1_BWRAP_MOUNT_PREFLIGHT=FAIL
```

These are sandbox-environment limitations, not source compile failures. No build was attempted in either case, and the host security policy was not weakened to force bubblewrap to work.

## R5-R2 direct migrated-checkout module build — PASS

The exact migrated Git checkout was temporarily presented at the canonical Android source path using an explicitly authorized same-filesystem atomic path swap. Source contents were not edited. The original workspace source directory was held separately and restored after the build; the migrated checkout path was also restored.

Build identity:

```text
MIGRATED_COMMIT=059d5d23e4186bbd3119180433a5e6206b7d95bd
MIGRATED_TREE=c00fd741c401fdd1421e8971bfb82f01c4b7c7da
source_count=12
```

The gate proved before build:

```text
SABLESTART_R5_R2_COMMIT_BINDING=PASS
SABLESTART_R5_R2_TREE_BINDING=PASS
SABLESTART_R5_R2_PARENT_BINDING=PASS
SABLESTART_R5_R2_MIGRATED_REPO_CLEAN=PASS
SABLESTART_R5_R2_SOURCE_COUNT_BINDING=PASS
SABLESTART_R5_R2_PRE_SWAP_BYTE_BINDING=PASS
SABLESTART_R5_R2_SAME_FILESYSTEM_RENAME_PRECONDITION=PASS
SABLESTART_R5_R2_DIRECT_CHECKOUT_HEAD_AT_BUILD_PATH=PASS
SABLESTART_R5_R2_DIRECT_CHECKOUT_TREE_AT_BUILD_PATH=PASS
SABLESTART_R5_R2_DIRECT_CHECKOUT_INODE_IDENTITY=PASS
SABLESTART_R5_R2_HELD_BASELINE_INODE_IDENTITY=PASS
```

### Network boundary

The build did not use a network namespace because the host blocks the required unprivileged namespace setup. Instead, the approved R5-R2 gate used a seccomp execution wrapper that denied creation of non-`AF_UNIX` sockets.

Preflight proved:

```text
AF_INET_SOCKET_DENIED=PASS
AF_UNIX_SOCKET_ALLOWED=PASS
SECCOMP_NETWORK_DENIAL_PROBE=PASS
SABLESTART_R5_R2_SECCOMP_NETWORK_DENIAL=PASS
```

The exact claim is therefore **seccomp non-AF_UNIX socket-creation denial during the build**, not network-namespace isolation.

### Fresh isolated output

The build used a fresh isolated output directory without clean/clobber/delete:

```text
/srv/data/sableos_panther_graphene_2026081300_workspace/out_r5_migrated_20260911_142602
```

Observed artifact closure:

```text
SABLESTART_R5_R2_APK_STRUCTURE=PASS
SABLESTART_R5_R2_AAPT2_PARSE=PASS
package: name='org.sableos.start' versionCode='38' versionName='17'
targetSdkVersion:'37'
application-label:'Sable Start'
SABLESTART_R5_R2_DIRECT_MIGRATED_CHECKOUT_BUILD=PASS
```

Produced APK SHA-256:

```text
1b35cd8a6ee90bfac6108babce160a040c5315dec88e7b0c6ae9c9b968b757ef
```

This exactly matches the previously validated R3B SableStart APK hash on the same Panther build substrate/toolchain. This is strong artifact-equivalence evidence for the migration, but it is not yet a claim of cross-host or general bit-for-bit reproducibility.

Evidence directory:

```text
/tmp/SABLESTART_R5_R2_DIRECT_BUILD_20260911_142602
```

Evidence checksum-file seal:

```text
f9cca514cdb02b002710084e2ea8077c2fd19089bd62bdb3a9ecceee1e8a0b26  SHA256SUMS.txt
```

Restoration/side-effect closure:

```text
WORKSPACE_SOURCE_PATH_RESTORED=YES
MIGRATED_REPO_PATH_RESTORED=YES
DELETE_OR_CLEAN_PERFORMED=NO
```

The isolated output directory remains present; no cleanup authorization is implied by this gate.

## Remaining R5 migration closure

The direct migrated-checkout build portion is closed. Full R5 migration/reconstruction closure still requires, at minimum:

1. **revision-pinned source composition** — `platform_manifest` must describe the organization repository checkout at the intended canonical Android path without a manual source swap;
2. **fresh reconstruction/build gate** — reconstruct from the manifest into a controlled fresh workspace and prove the organization repositories plus pinned upstream inputs are sufficient for the build claim;
3. **runtime equivalence/smoke if required by the acceptance boundary** — bind any runtime check to the migrated/reconstructed artifact rather than relying only on the historical R3C install;
4. **PR integration** — merge/source-integrate only when the chosen acceptance boundary is satisfied and merge authorization is explicit.

The direct-build PASS removes the earlier uncertainty about whether the captured organization checkout itself can compile. The remaining uncertainty is source-composition reconstruction/integration, not SableStart module buildability.

## Next feature milestone after migration closure

The next functional Sable Start milestone is documented in:

```text
docs/R6_ALL_APPS_AND_GREETING.md
```

R6 covers complete launcher-visible All Apps inventory, real icons/count, live package refresh, Search backed by the same inventory, exact launch behavior, and a local-time `Good morning`/`Good afternoon`/`Good evening`/`Good night` greeting.

R6 should not be developed as a Panther-only workspace fork.

## Claim boundary

Current evidence proves that the validated R3 Sable Start source was captured into the organization repository with exact byte fidelity, committed under a sealed Git identity, pushed without rewriting that source commit, and **successfully built directly from the exact migrated Git checkout at the canonical Android source path**. The fresh isolated build produced a structurally valid APK whose SHA-256 exactly matches the previously validated R3B artifact on the same Panther substrate/toolchain, and both workspace/migrated source paths were restored without clean/delete.

It does **not yet prove** a fresh `platform_manifest` reconstruction, cross-host reproducibility, runtime behavior of a separately reconstructed artifact, or that PR #1 has been merged. Those remain separate gates.
