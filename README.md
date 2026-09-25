# Sable Start presentation/history repository

Status: **historical/common presentation reference — 2026-09-25**

This repository is **not** the current private integration release authority and
is not the place to infer the exact accepted Panther image source.

Current organization-level status:

```text
R9_PANTHER_HUB_V1_CLOSURE=MERGED_PR_110
R9_PANTHER_ACCEPTED_SOURCE=edf62e5bb08372a1395841d6cc5d78d3148a7695
R9_PANTHER_TARGET_FILES_SHA256=a0b359613c4f30e9a834fba212e0b044a97d63ed0537c59471c31b99b627d285
R10_KEYBOARD_FIRST_DESIGN_V1=MERGED_PR_108
PANTHER_ROLE=FROZEN_TOUCH_FIRST_REFERENCE
TITAN2_ROLE=ACTIVE_KEYBOARD_FIRST_N0_TARGET
```

Historical branches and R3-R8 documents in this repository remain valuable for
presentation semantics, migration evidence and launcher requirement history.
They must not be read as current HOME ownership, current execution status or
current release evidence.

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
