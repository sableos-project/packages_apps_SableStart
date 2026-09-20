# Sable Start

![Local CI](https://img.shields.io/badge/CI-local%20direct-active-2ea44f)
![R9 Launcher](https://img.shields.io/badge/R9%20launcher%20visual-PASS-2ea44f)
![Fresh Panther](https://img.shields.io/badge/fresh%20Panther%20build-IN%20PROGRESS-f0ad4e)
![Pixel 7](https://img.shields.io/badge/Pixel%207%20physical-PENDING-lightgrey)
![Titan 2](https://img.shields.io/badge/Titan%202-keyboard--first%20QUEUED-6f42c1)

## Current R9 product status

Sable Start is now the **Sable presentation layer hosted by Launcher3/Quickstep**, not a standalone HOME APK. The product HOME package is `com.android.launcher3`, with `com.android.launcher3.sable.SableQuickstepLauncher` owning the Android HOME/Quickstep lifecycle while `SableStartScreen` renders the Sable NORMAL-state experience.

The Pixel 7 visual set is accepted for Start, All Apps/Sable Rail, Search/Command, Peek, Local Context, Appearance, light/dark system bars and alternate accent. A fresh full Panther build and physical HOME/Overview/Recents runtime acceptance are the remaining R9 closure steps.

The public repository preserves canonical SableStart source/history; integration authority for the active R9 product line is tracked in the private integration repository until synchronized here.

Canonical SableOS launcher/shell repository.

Android checkout path: `packages/apps/SableStart` when integrated into an Android source tree.

Sable Start is common product code. Device repositories must not carry copies of this application; Android/device differences belong behind bounded platform/device adapters.

Organization-wide security, quality, coverage, fuzzing, provenance and performance policy is defined in `sableos-project/.github/docs/SECURITY_QUALITY_ENGINEERING.md`.

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

## Current launcher role

Sable Start is evolving from a launcher prototype into the primary Sable-owned Start/Home/app-list/search experience, while mature Android/Launcher3 Quickstep behavior remains underneath where it provides proven recents/gesture/platform capability.

The product direction is Metro-influenced but Sable-owned: large typography, information-first surfaces, low chrome, logo-derived semantic color roles, real launcher icons, a clean app-name list without exposing package names in ordinary UI, and consistent Start/All Apps/Search/Peek/Local Context behavior. Launcher3/Quickstep remains authoritative for HOME lifecycle, Overview/Recents and task/gesture integration.

Sable Start consumes the shared Sable design/security/accessibility/localization contract; it must not invent an incompatible private theme or privilege model.

Do not copy proprietary Microsoft assets/fonts/branding. The influence is interaction/design language, not source or asset reuse.

## Security and privacy contract

A launcher can observe substantial application/profile state, so its authority must remain narrower than its visual prominence might imply.

Requirements include:

- use Android PackageManager/LauncherApps/profile APIs rather than scraping private application data;
- no new shared UID or privileged/system authority merely for convenience;
- exported components and intent filters are explicit and reviewed;
- local Pinned/Recent/Search state stores only the data required for the feature;
- no analytics/tracking SDK by default;
- no network permission unless a separately approved Sable Start capability requires it;
- shell UI may request supported actions, but privileged services/frameworks remain the enforcement authority;
- any Rust core remains narrow and deterministic; Android lifecycle, PackageManager, launcher/profile integration and accessibility stay platform/Kotlin-owned.

OWASP MASVS/MASTG platform/privacy/code controls are mapped at the organization assurance layer where applicable.

## Test and code-quality contract

Sable Start changes should converge on layered evidence rather than one compile check:

```text
Kotlin/JVM tests
Android Lint + detekt + ktlint
CodeQL Java/Kotlin
MobSF/mobsfscan
secret/workflow/dependency policy
Kover coverage reporting + ratchet
Compose UI semantics tests
UIAutomator/instrumentation for launcher/system boundaries
package/permission/component audit
physical Panther/Titan launcher/runtime regression
```

If Rust remains part of Sable Start, the relevant Rust fmt/Clippy/tests/advisory/dependency/coverage/fuzz/unsafe-review controls also apply.

A coverage percentage is not accepted as proof that HOME lifecycle, accessibility, multi-profile behavior, recents/gesture integration or fallback/recovery are correct.

## Performance contract

Launcher performance is product behavior. Relevant measurements include cold/warm Start launch, app-list/search responsiveness, scroll/frame jank, memory, package-query/update cost and battery/background impact where applicable.

Performance is measured on exact builds/devices. Panther results are not silently generalized to Titan 2, and optimization does not justify bypassing Android security/accessibility boundaries.

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

Organization-wide current direction lives in `sableos-project/.github/docs/DEVELOPMENT_RELEASE_PLAN.md`; the shared engineering-assurance policy lives in `sableos-project/.github/docs/SECURITY_QUALITY_ENGINEERING.md`; detailed R8 shared application/design architecture lives in `sableos-project/platform_sable`.

## Integration rule

A successful Sable Start module/app build is not sufficient product-image evidence. Product inclusion must separately prove product selection, concrete PRODUCT_OUT install identity, target-files/image membership and runtime package/HOME behavior as required by the claim.

Likewise, a green scanner or UI test does not replace trusted artifact/image/runtime evidence, and a successful Panther run does not establish Titan 2 portability automatically.
