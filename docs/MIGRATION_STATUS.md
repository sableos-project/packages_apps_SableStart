# Sable Start migration status

Status: **IN PROGRESS — source capture, exact local commit seal, remote branch push, and PR diff boundary are closed; direct build/reconstruction from the migrated checkout remains open.**

The authoritative validated Panther workspace remains an important reference until the migrated repository checkout has been rebuilt and the complete source composition is reproducible through `platform_manifest`. New feature development should move to the organization repository only after the migrated-source build gate closes.

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

The later `main` commit relative to the migration parent changed documentation only. GitHub's independent comparison of the migration branch against current `main` showed exactly the intended 12 source additions, 1802 additions, and zero deletions.

R4-R2 evidence seal:

```text
a133f8950a41a4dc2ecbc62a73085f1b1a4ec59d5b8d7e5f6b97f255ad16d7d6
```

Note: the local R4-R2 shell output emitted `comm` sorted-order warnings during one path-disjointness check, so that specific local line is not treated as sufficient evidence. The GitHub comparison/PR file set independently established the source-only diff boundary.

## Pull request — OPEN

PR #1:

```text
Import validated SableStart R3 source
base: main
head: m1/sablestart-portability-close-20260911
head SHA: 059d5d23e4186bbd3119180433a5e6206b7d95bd
```

Observed PR boundary when created:

```text
commits:       1
changed files: 12
additions:     1802
deletions:     0
mergeable:     true
```

The source migration commit has intentionally not been rewritten merely to incorporate documentation-only `main` changes.

## R5 migrated-checkout build — IN PROGRESS

The next migration proof is a build where the exact migrated Git checkout supplies `packages/apps/SableStart`.

Two attempted bubblewrap-based sandbox approaches stopped before the build because the ThinkPad host policy does not permit the required unprivileged user/mount/network namespace setup. These stops did not constitute source/build failures and did not mutate source.

The current R5 strategy is a separately authorized bounded direct-checkout build method that must prove:

- build input is exact commit `059d5d23e4186bbd3119180433a5e6206b7d95bd` / tree `c00fd741c401fdd1421e8971bfb82f01c4b7c7da`;
- no source-content mutation;
- network boundary according to the approved gate;
- successful Soong `SableStart` module build;
- package/artifact structure and hash;
- restoration/integrity of any temporary host path manipulation used solely to present the checkout at the canonical Android path;
- migrated Git checkout remains clean;
- authoritative workspace source remains byte-identical.

Do not mark R5 `PASS` until the actual gate output is captured and reviewed.

## Remaining migration closure

Before this repository is treated as fully canonical source for continuing product development, the project still needs to close, at minimum:

1. **migrated-checkout module build** — build `SableStart` with the exact organization-repository checkout supplying the canonical Android source path;
2. **artifact/package validation** — bind the produced APK to the migrated source/build identity;
3. **runtime equivalence/smoke as required** — prove no migration-induced functional regression relative to the already validated R3C behavior;
4. **revision-pinned source composition** — make `platform_manifest` reconstruct the expected source checkout without local/manual source substitution;
5. **fresh reconstruction/build gate** — prove the organization repositories, not a historical local workspace arrangement, are sufficient to reproduce the build claim;
6. **PR integration** — merge/source-integrate only when the chosen migration acceptance boundary is satisfied.

## Next feature milestone after migration closure

The next functional Sable Start milestone is documented in:

```text
docs/R6_ALL_APPS_AND_GREETING.md
```

R6 covers complete launcher-visible All Apps inventory, real icons/count, live package refresh, Search backed by the same inventory, exact launch behavior, and a local-time `Good morning`/`Good afternoon`/`Good evening`/`Good night` greeting.

R6 should not be developed as a Panther-only workspace fork.

## Claim boundary

Current evidence proves that the validated R3 Sable Start source was captured into the organization repository with exact byte fidelity, committed under a sealed Git identity, pushed without rewriting that source commit, and exposed through a source-only pull request boundary.

It does **not yet prove** a successful build from the migrated checkout, a fresh `platform_manifest` reconstruction, or that PR #1 has been merged. Those remain separate gates.