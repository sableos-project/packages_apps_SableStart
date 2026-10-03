# Sable Start presentation/history repository

Status: **historical/common presentation reference; active keyboard-first handoff is P1 — 2026-10-02**

This repository is **not** the current private integration release authority and
is not the place to infer the exact accepted Panther image source.

Current organization-level status:

```text
R9_PANTHER_HUB_V1_CLOSURE=MERGED_PR_110
R9_PANTHER_ACCEPTED_SOURCE=edf62e5bb08372a1395841d6cc5d78d3148a7695
R9_PANTHER_TARGET_FILES_SHA256=a0b359613c4f30e9a834fba212e0b044a97d63ed0537c59471c31b99b627d285
R10_KEYBOARD_FIRST_DESIGN_V1=MERGED_PR_108
PANTHER_ROLE=FROZEN_TOUCH_FIRST_REFERENCE
TITAN2_ROLE=ACTIVE_KEYBOARD_FIRST_N1D_C3B_TARGET
```

Historical branches and R3-R8 documents in this repository remain valuable for
presentation semantics, migration evidence and launcher requirement history.
They must not be read as current HOME ownership, current execution status or
current release evidence.

Current first-party HOME architecture:

```text
SABLE_FIRST_PARTY_HOME_OWNER=Launcher3QuickStep
SABLESTART_ROLE=PRESENTATION_AND_STATE_SOURCE_HOSTED_IN_LAUNCHER3
STANDALONE_SABLELAUNCHER_RUNTIME=RETIRED
THIRD_PARTY_HOME_SELECTION_ALLOWED=YES
FORCE_SABLE_HOME_AFTER_USER_SELECTION=NO
```

This repository remains presentation/history source; it is not a standalone
shipping HOME APK. Android user selection of another installed launcher remains
supported. A third-party launcher does not automatically inherit Sable's
Quickstep/SystemUI/Private-Space integration.

## P1 current assignment

The active Titan product-source task is **P1** in the private
`aimindseye/titan2-temp` lane: prepare reusable keyboard-first
interaction/focus/type-to-search behavior and tests for canonical
Launcher3/Launcher3QuickStep-hosted Sable Start.

```text
P1_STANDALONE_HOME_ALLOWED=NO
P1_CANONICAL_HOME_OWNER=Launcher3QuickStep
P1_FIRST_CHARACTER_PRESERVATION=REQUIRED
P1_DETERMINISTIC_FOCUS=REQUIRED
P1_DEVICE_MODEL_BRANCHING=FORBIDDEN
P1_CANONICAL_RUNTIME_INTEGRATION=PENDING
```

P1 is source/handoff work only. Canonical Launcher3 Android-tree integration
and device qualification remain owned by `aimindseye/sableos`.

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

Reusable launcher presentation should converge on a clearly owned public Sable
launcher/application repository during the source-publication transition. This
repository remains history/reference until that migration is complete.

Remaining open launcher/appearance/polish issues are intentionally kept open
until the Titan 2 SableOS install path proves or supersedes them.
