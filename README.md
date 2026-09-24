# Sable Start presentation/history repository

Status: **historical/common presentation reference — 2026-09-24**

This repository is **not** the current SableOS HOME package authority.

The physically accepted R9 architecture is:

```text
org.sableos.launcher / SableLauncher
    HOME
    Start
    All Apps
    Search / Command
    Peek
    app context / permission summaries

Launcher3QuickStep
    Recents / Overview / task/gesture substrate
    not HOME eligible

org.sableos.start / SableStart
    retired standalone runtime product
    historical presentation/source reference
```

Historical branches and R3-R8 documents in this repository remain valuable for
presentation semantics, migration evidence and launcher requirement history.
They must not be read as current HOME ownership or current execution status.

## Active design handoff

Panther is frozen as the accepted touch-first reference.

Current launcher/product design work is the common keyboard-first profile for
Titan 2, Titan 2 Elite and future Q27:

- deterministic visible focus;
- arrows/D-pad movement;
- Enter/Space activation;
- Back/Escape;
- printable-key type-to-search;
- command/shortcut navigation;
- stable focus restoration;
- square/near-square responsive layouts;
- accessibility and keyboard-only operation;
- touch retained as a secondary path.

Do not fork launcher semantics by device model.

## Repository future

Reusable launcher presentation should converge on a clearly owned public
SableLauncher/application repository during the source-publication transition.
This repository remains history/reference until that migration is complete.
