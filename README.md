# Sable Start

Canonical repository for the SableOS launcher and shell.

Android checkout path: `packages/apps/SableStart`.

Sable Start is common product code. Device repositories must not carry copies of this application. Device and Android-substrate differences should be handled through stable interfaces and bounded adapters.

## Current source migration status

The validated R3 source has been recursively captured, byte-verified, committed, and pushed on the migration branch:

```text
m1/sablestart-portability-close-20260911
commit 059d5d23e4186bbd3119180433a5e6206b7d95bd
tree   c00fd741c401fdd1421e8971bfb82f01c4b7c7da
```

PR #1 imports exactly the 12 validated source files. The migration commit has intentionally not been rewritten merely to follow later documentation commits on `main`.

A direct build/reconstruction proof from the migrated checkout is a separate gate and must close before the repository is treated as fully canonical for new feature development.

See `docs/MIGRATION_STATUS.md` for the migration evidence/claim boundary.

## Next functional milestone: R6

After migrated-source build closure, the next Sable Start milestone is **R6 — All Apps, shared Search inventory, and local-time greeting**.

The normative requirements are in:

- [`docs/R6_ALL_APPS_AND_GREETING.md`](docs/R6_ALL_APPS_AND_GREETING.md)

R6 requires:

- complete Android launcher-visible application inventory for accessible profiles;
- real app labels/icons and deterministic ordering;
- visible app count;
- live refresh on relevant package/profile changes;
- Search backed by the same live inventory;
- correct component/profile launch behavior;
- a device-local time-based greeting (`Good morning`, `Good afternoon`, `Good evening`, `Good night`);
- runtime completeness/launch/dynamic-removal evidence.

R6 does **not** include a general theme editor, launcher folders/categories, cloud search, recommendation ranking, or custom Phone/Messaging work.

## Architecture

See:

- [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)
- [`docs/R6_ALL_APPS_AND_GREETING.md`](docs/R6_ALL_APPS_AND_GREETING.md)
- [`docs/MIGRATION_STATUS.md`](docs/MIGRATION_STATUS.md)

Organization-wide product direction is maintained by `sableos-project/.github/docs/DEVELOPMENT_RELEASE_PLAN.md` until the central `sableos` project repository transition is complete.