# Sable Start repository current status

Status date: **2026-09-24**

## Current role

```text
shipping SableOS HOME package      org.sableos.launcher
Quickstep role                     Recents/Overview/task substrate
standalone org.sableos.start       retired from product
this repository                    historical/presentation reference
Panther                             frozen accepted R9 reference
active launcher design             keyboard-first common SableLauncher profile
```

The prior 2026-09-20 architecture in which Launcher3/Quickstep hosted
SableStartScreen as the NORMAL state was superseded before final Panther R9
physical acceptance.

The final accepted architecture moved HOME ownership back into a standalone
Sable-owned `org.sableos.launcher` package while retaining Quickstep privately
for Recents/task/gesture substrate.

## Historical evidence

The repository still preserves:

- R3 Soong/install/runtime evidence;
- R5 source migration/reconstruction evidence;
- R6 All Apps/Search/greeting requirements;
- historical HOME-adoption gate;
- early R8 appearance/presentation work.

These files are audit/history records, not current execution authority.

## Current design handoff

Future reusable presentation work should feed the public SableLauncher/common
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
