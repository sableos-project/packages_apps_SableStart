# Sable Start current status

Status date: **2026-09-16**.

This file is the current-status entry point. Historical R3/R5/R6 requirement/evidence documents remain preserved separately.

## Source lineage

### Migration baseline

```text
branch: m1/sablestart-portability-close-20260911
commit: 059d5d23e4186bbd3119180433a5e6206b7d95bd
tree:   c00fd741c401fdd1421e8971bfb82f01c4b7c7da
```

The organization migration work established the canonical repository source baseline and direct migrated-checkout build evidence. `MIGRATION_STATUS.md` preserves the detailed claim/evidence history.

### R7 production surfaces

PR #9 was merged into the active SableStart development lineage as:

```text
merge commit: fcd1c41d416b17030da683cb66ed077e37b92828
```

That line advances Sable Start beyond the old "next R6" documentation into the production Start/All Apps/App Context/Search/Pinned+Recent/Settings/Live/Lock-preview model while retaining the established Android boundaries.

This does **not** mean every later development commit is already present on the repository's default `main` branch. PR #1 remains the original migration/source-integration boundary.

## R8 appearance work

Current draft:

```text
branch: r8/sablestart-customization-20260916
PR: #10
status: DRAFT / REQUIRES CONTRACT ALIGNMENT
```

The first prototype added broader surface/corner customization. The authoritative R8-A design contract now limits first-R8 user-facing customization to:

```text
Follow system
Light
Dark
bounded accent
reset/default
```

Therefore PR #10 must be revised before merge. Specifically, Metro/Graphite/OLED and user-selectable corner/density concepts must not become first-R8 behavior unless `platform_sable` requirements change explicitly.

## R8 SableStart acceptance target

For Sable Start, R8 should prove:

- shared design contract consumption rather than a divergent local theme system;
- persisted appearance mode/accent using the accepted shared schema/adapter;
- correct Follow system behavior;
- Light and Dark override behavior;
- invalid/corrupt preference fallback;
- reset/default behavior;
- representative production surfaces responding consistently;
- accessibility/font/contrast review;
- no privilege/network expansion solely for appearance;
- existing real launcher/Search/pinned/recent/Settings/Live boundaries preserved.

## Product/image boundary

Sable Start source/app correctness remains separate from product inclusion.

Required product/image proof continues to distinguish:

```text
module/source discovery
 -> compile
 -> product selection
 -> PRODUCT_OUT
 -> target-files/image
 -> runtime package/HOME behavior
```

Do not use a full Panther image build merely to discover ordinary SableStart Kotlin/Rust errors that can be caught with narrower source/application gates.

## Historical documents

Preserve as historical requirement/evidence records:

- `MIGRATION_STATUS.md` — R3–R5 migration/build/reconstruction state;
- `R6_ALL_APPS_AND_GREETING.md` — R6 launcher requirements;
- R3/R6 gate/result files — historical evidence snapshots.

Current forward architecture is defined by:

- this file;
- `ARCHITECTURE.md`;
- organization `DEVELOPMENT_RELEASE_PLAN.md`;
- `platform_sable` R8-A and application-reuse documents.