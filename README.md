# Sable Start / launcher presentation history

## Current product role

This repository preserves Sable Start presentation/source history and common
launcher requirements. It is **not** the current Android HOME package authority.

The accepted R9 product architecture is:

```text
org.sableos.launcher / SableLauncher
    user-facing HOME
    Start
    All Apps
    Search / Command
    Peek
    app context / permission summaries

Launcher3QuickStep
    private Recents / Overview / task/gesture substrate
    HOME eligibility removed
```

Historical branches and documents describing a Launcher3-hosted
`SableQuickstepLauncher` as the product HOME are superseded by the physically
accepted R9 architecture.

## Active design direction

Panther is frozen as the touch-first reference. Launcher/product work now
focuses on a common keyboard-first interaction profile for Titan 2, Titan 2
Elite and future Q27:

- deterministic visible focus;
- arrows/D-pad movement;
- Enter/Space activation;
- Back/Escape;
- printable-key type-to-search;
- shortcut/command palette;
- stable focus restoration;
- touch as a secondary path;
- square/near-square responsive layouts.

Do not fork launcher semantics by model name. Device/input differences belong
behind interaction/device adapters.

## Ownership

Reusable launcher presentation should converge on a clearly owned public
SableLauncher/application repository during the source-publication transition.
This repository remains useful history/reference until that migration is
complete.
