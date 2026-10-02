# Sable Start repository current status

Status date: **2026-10-02**

## Current role

```text
Sable first-party HOME runtime     packages/apps/Launcher3 / Launcher3QuickStep
Sable Start role                   presentation/state source hosted in Launcher3
standalone org.sableos.launcher    retired from current product architecture
standalone org.sableos.start       retired from product
this repository                    historical/presentation reference
Panther                             frozen accepted touch-first reference
active launcher design             keyboard-first Launcher3-hosted Sable Start
third-party HOME selection         allowed through Android user choice
```

The R9L8 cutover is current authority: Launcher3/Launcher3QuickStep hosts Sable
Start and remains the first-party HOME plus Recents/Overview/task/gesture
substrate. Historical standalone-SableLauncher acceptance notes remain evidence
for their milestone but are not current architecture.

SableOS must not force Sable Start back after an explicit user-selected
third-party HOME.

## Historical evidence

The repository still preserves:

- R3 Soong/install/runtime evidence;
- R5 source migration/reconstruction evidence;
- R6 All Apps/Search/greeting requirements;
- historical HOME-adoption gate;
- early R8 appearance/presentation work.

These files are audit/history records, not current execution authority.

## Current design handoff

Future reusable presentation work should feed the Launcher3-hosted Sable Start/common
design architecture, especially the keyboard-first profile:

- visible deterministic focus;
- type-to-search;
- command shortcuts;
- square-display layout;
- app-context/permission summaries;
- accessibility;
- touch-secondary operation.

No Panther-specific or Titan-specific copy of common launcher code should be
created.
