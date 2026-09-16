# Sable Start

Canonical SableOS launcher/shell repository.

Android checkout path: `packages/apps/SableStart` when integrated into an Android source tree.

Sable Start is common product code. Device repositories must not carry copies of this application; Android/device differences belong behind bounded platform/device adapters.

## Current repository/source status

The default `main` branch still primarily reflects the organization migration/documentation baseline. The active validated/production source lineage has continued on development branches while the original migration/integration PR remains separate.

Important source history:

```text
m1/sablestart-portability-close-20260911
  sealed migration baseline:
  commit 059d5d23e4186bbd3119180433a5e6206b7d95bd
  tree   c00fd741c401fdd1421e8971bfb82f01c4b7c7da

R7 production-surface work
  merged into the active development lineage as PR #9
  merge commit fcd1c41d416b17030da683cb66ed077e37b92828

R8 appearance/customization prototype
  PR #10 / r8/sablestart-customization-20260916
  DRAFT — requires scope correction before merge
```

PR #1 remains the original migration/source-integration boundary; do not describe `main` as containing every later production source commit until that integration actually occurs.

See [`docs/CURRENT_STATUS.md`](docs/CURRENT_STATUS.md) for the current branch/PR status and [`docs/MIGRATION_STATUS.md`](docs/MIGRATION_STATUS.md) for preserved R3–R5 migration/build evidence.

## Established launcher behavior

The active development lineage has moved beyond the old "next R6" description. Sable Start architecture now includes the production direction for:

- real launcher-visible application inventory and icons;
- shared live Search inventory;
- exact component/profile launch behavior;
- Start greeting;
- Start / All Apps / App Context / Search;
- local Pinned & Recent state;
- Sable Start Settings;
- bounded local Live data;
- Lock preview only;
- preservation of Launcher3 Quickstep as recents/gesture provider.

Historical R6 requirements remain in [`docs/R6_ALL_APPS_AND_GREETING.md`](docs/R6_ALL_APPS_AND_GREETING.md). Historical requirement/evidence files are preserved rather than rewritten into current R8 documents.

## Current R8 role

Sable Start is a **consumer of the shared R8-A design contract**, not the owner of an independent theme system.

Authoritative first-R8 customization remains:

```text
Follow system
Light
Dark
bounded accent
reset/default
shared semantic design roles
```

The current draft PR #10 prototype contains broader Metro/Graphite/OLED surface modes and user-selectable corner styles. Those choices are **not current first-R8 requirements** and must be reconciled before that PR can become accepted R8 source.

Do not add grid/density editors, icon packs, theme stores, wallpaper editors or unrelated launcher customization merely because the appearance screen exists.

## HOME/default launcher is a separate gate

Feature correctness does not by itself authorize or prove default HOME adoption.

Read:

- [`docs/HOME_ADOPTION_GATE.md`](docs/HOME_ADOPTION_GATE.md)

Keep launcher feature testing, HOME/default role state, navigation/lifecycle, reboot persistence and fallback/recovery as separate claims.

## Architecture

Read:

- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
- [`docs/CURRENT_STATUS.md`](docs/CURRENT_STATUS.md)
- [`docs/HOME_ADOPTION_GATE.md`](docs/HOME_ADOPTION_GATE.md)
- [`docs/MIGRATION_STATUS.md`](docs/MIGRATION_STATUS.md)
- historical [`docs/R6_ALL_APPS_AND_GREETING.md`](docs/R6_ALL_APPS_AND_GREETING.md)

Organization-wide current direction lives in `sableos-project/.github/docs/DEVELOPMENT_RELEASE_PLAN.md`; the detailed R8 shared application/design architecture lives in `sableos-project/platform_sable`.

## Integration rule

A successful Sable Start module/app build is not sufficient product-image evidence. Product inclusion must separately prove product selection, concrete PRODUCT_OUT install identity, target-files/image membership and runtime package/HOME behavior as required by the claim.