# Sable Start HOME adoption gate

> **HISTORICAL / SUPERSEDED — 2026-09-24:** this document records an earlier SableStart milestone. Final Panther R9 physically accepted standalone `org.sableos.launcher` as HOME, with Launcher3QuickStep Recents-only; the standalone SableStart runtime product is retired. Preserve the body as historical requirements/evidence, not current execution authority.


Status: **HISTORICAL_EVIDENCE / SUPERSEDED CURRENT ROLE.**

Sable Start can be developed and validated as a launcher-capable application without silently becoming the device's default HOME application. Earlier runtime work intentionally preserved Quickstep as HOME while Sable Start preview/activity behavior was validated.

A daily-driver SableOS product will eventually need an explicit decision about which launcher owns HOME. This document prevents that role change from being hidden inside an R6 feature test.

## 1. Separation of claims

These are different claims:

```text
Sable Start can enumerate/search/launch apps
```

and:

```text
Sable Start is selected and reliable as the device HOME launcher
```

R6 `All Apps + Search + greeting` may be proven without changing HOME.

Do not treat R6 functionality as proof of HOME adoption.

## 2. Product decision required before daily-driver closure

Before the Panther configuration is described as a Sable Start daily-driver launcher, explicitly choose one of:

### A. Sable Start is the intended default HOME

Then run a separately authorized HOME-adoption validation gate.

### B. Another launcher remains HOME temporarily

Then document that limitation clearly in R7/daily-driver status. Do not present Sable Start as the active launcher merely because it can be opened manually.

The expected long-term product direction may favor Sable Start, but the role change must remain explicit until validated.

## 3. No silent role mutation

R6 source/runtime tests must not silently:

- issue adb commands that set HOME;
- clear another launcher's default role;
- disable/uninstall Quickstep or another HOME implementation;
- modify role/default-app state;
- change product overlays/configuration that force Sable Start HOME;

unless the specific gate authorizes that operation.

## 4. If Sable Start is selected HOME: required validation

At minimum prove:

- Android recognizes Sable Start as a HOME-capable activity;
- role/default selection succeeds through the chosen supported product/user mechanism;
- pressing/swiping Home reliably reaches Sable Start;
- app launch -> Home returns to Sable Start;
- back behavior does not exit into an unusable state;
- recent-tasks/system navigation remains coherent with the selected system navigation model;
- screen off/on and unlock return to a usable launcher state;
- Sable Start process death/recreation does not leave the device without a usable HOME;
- package update of Sable Start preserves/re-establishes expected HOME behavior according to Android role semantics;
- package crash has an understood recovery path;
- no unexpected privilege is added merely to hold HOME;
- All Apps/Search/greeting continue to satisfy R6 under HOME lifecycle behavior.

## 5. Persistence/reboot

Default-role persistence across reboot should be tested before a strong shipping/daily-driver claim.

Reboot is a separately authorized device operation. If reboot is not authorized for a run, mark persistence-after-reboot untested.

## 6. Quickstep/SystemUI boundary

Do not assume making Sable Start HOME means replacing SystemUI or Recents implementation.

HOME launcher, overview/recents, navigation gestures, SystemUI, and other system surfaces have distinct Android integration boundaries.

Any attempt to replace or deeply integrate Recents/SystemUI requires separate architecture/requirements. Do not pull it into HOME adoption implicitly.

## 7. Recovery

Before changing HOME on a development device, record a recovery path that does not require destructive reset.

Examples may include:

- ability to use Settings/default apps to select the prior HOME;
- separately authorized adb role/default command;
- known prior launcher component identity.

Do not disable/remove the fallback launcher during the first Sable Start HOME qualification.

## 8. Evidence

A HOME adoption evidence run should record:

- exact Sable Start source/build/APK identity;
- HOME/default state before;
- authorized mutation performed;
- HOME/default state after;
- focused activity after Home action;
- process identity across representative interactions;
- relevant runtime errors;
- state after app launch/Home return;
- state after process recreation;
- reboot persistence only if separately authorized/tested;
- fallback/recovery state.

## 9. Relationship to R7

R7 daily-driver qualification must explicitly state the active HOME implementation.

If Sable Start has not yet passed this gate, R7 can still validate telephony/network/basic-app functionality, but the limitation must remain visible in the daily-driver status.

## 10. Definition of HOME adoption done

HOME adoption is done only when:

- the product decision explicitly selects Sable Start as HOME;
- the role/default mutation is separately authorized;
- normal Home/navigation/lifecycle behavior is runtime proven;
- persistence/recovery boundaries are understood;
- R6 launcher functionality remains correct under HOME lifecycle;
- no unapproved SystemUI/Recents replacement is smuggled into the change.

Until then, this document is a pending gate, not permission to mutate HOME.