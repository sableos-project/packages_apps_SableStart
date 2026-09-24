# Sable Start / launcher presentation architecture

Status: **current repository-role architecture — 2026-09-24**

This document defines what remains reusable from SableStart history after the
accepted R9 launcher architecture change.

## Current product boundary

```text
SableLauncher (org.sableos.launcher)
    owns HOME and user-facing launcher semantics

Launcher3QuickStep
    owns Recents/Overview/task/gesture substrate
    is not HOME eligible

SableStart repository
    presentation/history/reference source
    not a shipping HOME package authority
```

## Reusable semantic concepts

The following Sable presentation concepts remain valid and belong in the common
launcher product architecture where still useful:

- Start surface;
- complete real launcher inventory;
- All Apps;
- Search/type-to-search;
- Peek/live summaries;
- pinned/recent state;
- app context;
- privacy/security-impacting permission summaries;
- local-time greeting where retained by current product requirements;
- real package/profile/icon/launch semantics;
- no fabricated application or notification data.

## Android boundaries

Common launcher presentation must continue to respect Android ownership:

- package/profile discovery through supported launcher/package APIs;
- Recents/task/gesture behavior through Quickstep/platform substrate;
- keyguard/security through Android platform ownership;
- Settings/default-app/app-info flows through supported intents/APIs.

Do not recreate platform security or task-management services inside the launcher
for visual convenience.

## Appearance

Settings is the global appearance authority. Launcher presentation consumes the
same Follow-system/Light/Dark and semantic design roles as other Sable apps.

The accepted Panther R9 physically proved global Light/Dark propagation.

## Keyboard-first direction

Keyboard-first is a common interaction profile, not a Titan-specific launcher
fork.

Required launcher behavior includes:

```text
deterministic visible focus
arrow/D-pad navigation
Enter/Space activation
Back/Escape
type-to-search
shortcut/command palette
stable focus restoration
no focus traps
square/near-square responsive layout
touch as secondary input
```

Device-specific scan codes, Fn/Sym behavior and physical-key quirks do not belong
in common launcher semantics.

## Historical documents

R3/R5/R6 migration, runtime and HOME-adoption documents remain preserved as
historical evidence. Their pending/current wording is superseded by the accepted
SableLauncher architecture.
