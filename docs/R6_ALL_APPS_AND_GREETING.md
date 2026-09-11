# R6 — All Apps, shared Search inventory, and local-time greeting

Status: **normative implementation requirements for the next functional Sable Start milestone after migrated-source build closure.**

This document is intentionally detailed. Future implementation work should use it as the source of direction instead of inventing new launcher semantics during coding.

R6 is a functional launcher milestone. It is not the theme/customization milestone and it is not a general redesign.

## 1. Goal

Turn Sable Start from a validated preview/shell into a dependable application launcher surface that:

- exposes the complete Android launcher-visible application inventory for accessible profiles;
- shows real labels and icons;
- launches the exact selected component/profile;
- keeps the inventory current as packages change;
- uses the same inventory for All Apps and Search;
- presents an appropriate greeting based on the phone's local time;
- remains bounded to supported launcher/application APIs rather than package-manager internals or device-specific assumptions.

## 2. Dependency

R6 source mutation should begin only after the current source-migration/build gate has established the intended canonical source/repository workflow strongly enough that new feature work will not be developed only in an old workspace copy.

The R6 implementation must be committed to `sableos-project/packages_apps_SableStart`; device repositories must not carry a Panther-only copy.

## 3. Existing implementation baseline

The migrated R3 source already establishes useful baseline behavior:

- a `LauncherAppsRepository` abstraction exists;
- launcher activities are queried through Android `LauncherApps`;
- accessible `UserManager.userProfiles` are considered;
- entries retain component and user/profile identity;
- launch behavior distinguishes current-user launch from cross-profile `LauncherApps.startMainActivity` behavior;
- the current simple Sable Start root can render labels and launch an entry.

R6 should evolve this architecture rather than replace it with an unrelated package scanner.

The current root's one-time remembered load is not sufficient for R6 because it can become stale after package/profile changes.

## 4. Normative definition of "All Apps"

For R6:

> All Apps is the set of enabled activities that Android exposes as launcher activities through the supported launcher API for every user profile Sable Start is permitted to access.

This is intentionally **not** the set of every installed package.

Do not expose implementation-only packages solely because they are installed. Packages that only contain services, content providers, overlays, receivers, libraries, or non-launcher activities do not belong in the primary All Apps catalog.

A future diagnostic/system inventory may expose all packages, but that is a different feature and must not change the R6 launcher semantic.

## 5. Inventory model

### 5.1 Required identity

Each launcher entry must retain enough immutable/stable identity to distinguish launch targets correctly. At minimum:

- user/profile identity (`UserHandle` or a stable internal abstraction preserving it);
- `ComponentName`;
- package name derivable from the component;
- application/activity display label;
- icon source or a safe representation from which the icon can be loaded.

Do not use display label as identity.

### 5.2 Keying

Compose/list keys must distinguish duplicate labels and duplicate package names across profiles/components. A key derived from user/profile plus flattened component identity is acceptable.

### 5.3 Multiple launcher activities

If a package legitimately exposes multiple launcher activities, R6 must follow Android launcher semantics rather than arbitrarily collapsing them by package name. If later UX chooses to collapse/alias such entries, that requires an explicit documented rule and tests.

### 5.4 Multiple profiles

Personal/work/other accessible user-profile entries must remain distinguishable in the model.

The first R6 UI does not need an elaborate profile switcher, but implementation must not throw away profile identity or incorrectly launch a work-profile component as the personal user.

If profile badges are readily available through supported launcher APIs, they may be used, but adding a large profile-management UI is not required for R6.

### 5.5 Disabled/suspended/unavailable state

Use supported Android launcher behavior as the authority. Do not invent a parallel package-state model unless a concrete runtime case requires it.

If Android does not expose an activity as launchable, it should not appear as a normal launchable All Apps entry.

## 6. Ordering

All Apps must be deterministic.

Primary ordering:

1. display label, case-insensitive, locale-aware where practical and stable;
2. package name/component identity as deterministic tie-breaker;
3. profile/user identity as final tie-breaker if otherwise identical.

Do not rely on iteration order returned by framework APIs.

The same logical ordering should be used consistently after refresh so applications do not jump arbitrarily.

## 7. Icons

R6 must show the real icon associated with the launcher entry/application using supported Android APIs.

Requirements:

- no hard-coded per-package icon map;
- no network icon fetching;
- gracefully handle missing/broken drawable cases without crashing the catalog;
- avoid retaining heavyweight drawable state in a way that leaks an Activity/Context;
- icons must not become entry identity;
- work/profile badging should be preserved if the supported launcher API provides a correct mechanism.

Performance optimization/caching is allowed only if it preserves correctness when packages change.

## 8. App count

The All Apps UI must expose a visible inventory count such as `ALL APPS · N` or equivalent.

`N` must refer to the exact same logical entry list being rendered, after any explicitly documented display filtering. It must not be a separate package count.

The count exists partly for user clarity and partly to make completeness validation straightforward.

## 9. Live inventory lifecycle

### 9.1 No permanent one-time snapshot

R6 must not depend solely on `remember { repository.loadApps() }` or an equivalent process-lifetime snapshot.

### 9.2 Required refresh events

The visible model must become current after relevant events including, as supported by Android APIs:

- package added;
- package removed;
- package changed;
- package enabled/disabled state changes affecting launcher visibility;
- profile availability/unavailability changes;
- application label/icon changes;
- Sable Start returning to the foreground/resuming after changes that could have occurred while callbacks were not active.

### 9.3 Preferred mechanism

Use `LauncherApps.Callback` or another supported launcher/package callback mechanism appropriate to the Android target, plus a lifecycle refresh safety net.

Do not implement rapid polling of PackageManager.

### 9.4 State ownership

The inventory should have one clear owner/repository/state-holder boundary. All Apps and Search must observe the same logical state rather than each querying independently and drifting.

The exact Compose architecture (for example state holder/ViewModel/repository observable) may be selected during implementation, but it must satisfy lifecycle correctness and remain testable. Do not introduce a framework solely because it is fashionable.

## 10. Search

### 10.1 Shared data source

Search must operate on the same current inventory as All Apps.

Do not create a second hard-coded preview list or a second package query with different filtering semantics.

### 10.2 Required matching

At minimum support case-insensitive matching by:

- display label;
- package name as a secondary/discovery match.

Component/class-name matching may be included for diagnostic usefulness only if it does not degrade normal user search. It is not a core user requirement.

### 10.3 Empty query

The empty-query behavior should be explicitly simple: either show the ordered inventory or show a neutral prompt according to the existing Sable Start interaction model. Do not add ranking/recommendation logic in R6.

### 10.4 Search ordering

A simple deterministic rule is preferred:

1. label prefix matches;
2. other label substring matches;
3. package-name matches;
4. retain deterministic label/component tie-breaks inside a group.

If implementation uses a different simple rule, document it before closing R6.

### 10.5 No cloud/web search

R6 Search is application inventory search. Cloud search, web search, contacts search, message search, file search federation, and recommendations are out of scope unless separately approved.

## 11. Launch behavior

Selecting an app must launch the exact selected launcher component under the correct profile/user.

Requirements:

- current-user launch uses a supported launcher/main intent path;
- other profiles use the appropriate `LauncherApps` API;
- a failed/stale launch target must not crash Sable Start;
- if an app disappears between rendering and tap, refresh inventory and handle the failure gracefully;
- Sable Start must not silently request/change HOME/default launcher role as part of ordinary R6 testing.

## 12. Local-time greeting

### 12.1 Required text policy

Use the phone's current local clock and time zone:

```text
05:00–11:59  Good morning
12:00–16:59  Good afternoon
17:00–21:59  Good evening
22:00–04:59  Good night
```

No location permission or network service is needed or permitted for determining this greeting.

### 12.2 Time source

Use the device/system local time. Do not derive the greeting from weather location, IP location, GPS, or a fixed developer time zone.

### 12.3 Refresh behavior

The greeting must be recalculated at least when:

- the Start surface is first composed/entered;
- Sable Start returns to the foreground;
- device time changes materially;
- time zone changes;
- the active time bucket boundary is crossed while the surface remains active, if practical without wasteful polling.

A scheduled next-boundary update or lifecycle/broadcast-based approach is preferable to high-frequency polling.

### 12.4 Testability

The greeting decision function should be separable from Android wall-clock acquisition so all four buckets and boundaries can be deterministic unit tested.

Required boundary cases include at least:

- 04:59 -> Good night;
- 05:00 -> Good morning;
- 11:59 -> Good morning;
- 12:00 -> Good afternoon;
- 16:59 -> Good afternoon;
- 17:00 -> Good evening;
- 21:59 -> Good evening;
- 22:00 -> Good night.

### 12.5 Future customization

Greeting text/time buckets may become customizable later. R6 must not introduce a general greeting editor or location-aware greeting system.

## 13. Third-party fixture policy

Keep the currently added Maps and Weather applications installed through the first R6 completeness run if they remain otherwise harmless.

They serve as useful fixtures for:

- non-core app enumeration;
- icon loading;
- alphabetical ordering;
- Search;
- launch behavior;
- dynamic remove/uninstall behavior.

At least one expendable non-core application should be used for the runtime removal test after the pre-removal inventory is captured.

Their test-fixture role does not make them required SableOS product packages.

## 14. Runtime acceptance gate

R6 closure must include an independently collected inventory and UI/runtime evidence.

### 14.1 Inventory completeness

Collect Android's authoritative launcher-visible entries using a method independent of the rendered Compose list where practical.

Normalize to a tuple equivalent to:

```text
user/profile | package | component | label
```

Compare with the Sable inventory presented/used by the UI.

Required result:

```text
expected_count = UI logical inventory count
missing_count = 0
unexpected_count = 0
```

If platform APIs legitimately hide details from an external adb comparison, document the limitation rather than fabricating exact equality.

### 14.2 Launch tests

At minimum launch successfully:

- one platform/stock application;
- one separately installed third-party application;
- one additional application with a duplicate/similar label or profile distinction if such a case exists on the test device.

Capture focused activity/component evidence where possible.

### 14.3 Dynamic removal test

Recommended closure sequence:

1. capture baseline inventory and count with a selected expendable test app installed;
2. confirm it is visible/searchable/launchable;
3. uninstall/remove it through a separately authorized normal package operation;
4. keep Sable Start process alive if practical;
5. prove the entry disappears after callback/refresh without restarting the Sable Start process;
6. prove count/search results update consistently;
7. prove no unrelated entries disappeared.

If uninstall is not authorized for the test run, an install/enable/disable change may be used only if it exercises the same update path and the evidence boundary is stated.

### 14.4 Greeting tests

Close with deterministic unit tests of all boundaries plus at least one runtime screenshot/semantic observation showing the greeting matches the device-local time bucket.

Do not manipulate global device time during normal runtime validation unless explicitly authorized. Unit-test/injected clock evidence can cover boundary buckets safely.

### 14.5 Stability

No Sable Start fatal exception should occur during inventory load, scrolling, search, launch, package-change refresh, or greeting refresh.

## 15. Performance expectations

R6 is not a benchmarking milestone, but ordinary usage must remain responsive.

Avoid:

- main-thread repeated full package scans on every recomposition;
- decoding/reloading all icons on every frame;
- polling every second for package or greeting changes;
- unbounded retained Context/Drawable references.

Performance changes must not weaken inventory correctness.

## 16. Privacy/security expectations

R6 requires no new dangerous runtime permission merely to list/launch normal apps or compute the greeting.

Do not add:

- location permission for greeting;
- network permission for greeting/icons;
- broad file/media access for app inventory;
- privileged package-management authority merely to observe launcher apps.

Any new privileged permission must be separately justified and reviewed.

## 17. Accessibility baseline

At minimum:

- app entries expose readable application labels to accessibility services;
- icons do not become unlabeled independent controls;
- touch targets are usable;
- search input has appropriate semantics;
- text remains readable under supported font scaling to a practical baseline;
- selected theme colors in the current implementation do not intentionally reduce contrast.

Full Sable design-system standardization is R8, but R6 must not create inaccessible interaction patterns that R8 then has to undo.

## 18. Explicit R6 non-goals

Do not add merely because the implementation is nearby:

- folders;
- launcher categories;
- AI/recommendation ranking;
- cloud/web search;
- contact/message/file federation;
- hidden-app management;
- icon packs;
- full theme editor;
- arbitrary grid/density customization;
- custom Dialer/Messaging;
- package uninstall/admin UI;
- complete installed-system-package diagnostics;
- device-specific Panther branching in common UI code.

These require separate requirements.

## 19. Source/review expectations

An R6 change should make it easy to review which requirement it satisfies. Prefer bounded commits or clearly separated logical changes for:

- inventory/model lifecycle;
- icon/UI presentation;
- Search sharing/filtering;
- greeting logic;
- tests;
- runtime evidence/docs.

Do not combine unrelated feature experiments into the R6 closure commit simply because the build is already open.

## 20. Definition of R6 done

R6 is done only when all of the following are true:

- canonical Sable Start source builds through the current accepted source-composition workflow;
- All Apps semantic is the documented launcher-visible inventory;
- inventory completeness is proven for the test device with zero unexplained missing/unexpected entries;
- real icons and labels are visible;
- visible count matches rendered logical inventory;
- Search uses the same live inventory;
- package changes refresh inventory without requiring a Sable process restart in the tested supported path;
- representative app launches succeed;
- the local-time greeting policy and boundaries are tested;
- runtime evidence shows no relevant fatal error;
- no unapproved HOME mutation, location dependency, network dependency, theme-system expansion, or custom telephony/messaging work was introduced.

After R6 closes, the next product priority is R7 daily-driver phone qualification, not launcher polish beyond the documented scope.