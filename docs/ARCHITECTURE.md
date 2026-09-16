# Sable Start architecture

Status: **normative launcher-specific architecture.**

Sable Start is a portable SableOS application surface, not a per-device launcher fork and not a replacement for every Android system UI component.

## Ownership boundary

Sable Start owns:

- launcher presentation/navigation;
- launcher-visible application discovery UX;
- Search over the same live inventory;
- exact component/profile launch requests through supported Android APIs;
- local pinned state;
- SableStart-observed recent-launch state where required;
- launcher-local Settings;
- bounded local Live presentation;
- Start greeting;
- Lock **preview** presentation;
- consumption of the shared Sable design contract.

It does **not** own:

- device HAL/vendor/firmware behavior;
- raw hardware policy;
- Android Settings plumbing;
- Keyguard/SystemUI replacement merely because a Lock preview exists;
- Launcher3 Quickstep recents/gesture ownership;
- global Usage Stats simply to manufacture richer recents;
- common design semantics independently from `platform_sable`.

## Launcher inventory boundary

All Apps and Search use the same real launcher-visible Android inventory for accessible profiles.

Stable identity should preserve at least:

```text
user/profile
package
component
```

Labels/icons are presentation data, not the sole identity key.

Do not reintroduce hard-coded/fake app catalogs, fake search results or demo notification/application state into the production HOME path.

## Launch and state boundary

Launch the exact component for the correct profile/user through the supported platform APIs.

Pinned state is explicit SableStart-local user state.

Recent state in the first production model is limited to launches successfully observed through Sable Start unless a future requirement explicitly justifies broader platform authority. Do not add Usage Stats permission by default.

## Data boundary

UI code should not perform blocking provider I/O on the main thread. Provider-backed/local data belongs behind explicit states such as:

```text
loading
live
permission-required
unavailable
error
```

Live data should use local Android/platform sources where useful. Do not add network/account dependencies simply to populate a launcher surface.

## Android system boundaries

- **Quickstep:** Launcher3 Quickstep remains the recents/gesture provider unless a separately approved architecture changes this.
- **Keyguard:** Lock remains preview/presentation only until a separately designed security/system architecture exists.
- **Settings:** launcher Settings owns launcher-local behavior; delegate platform settings to Android.
- **Notifications:** do not invent counts/state the app is not authorized to observe.

## R8-A design boundary

Sable Start is one consumer of the shared R8-A design contract in `platform_sable`.

Authoritative first-R8 appearance behavior:

```text
Follow system
Light
Dark
bounded accent selection
reset/default
shared semantic colors/typography/spacing/shapes
```

SableStart must not independently define a conflicting theme taxonomy or persistent schema.

The existing R8 draft prototype with Metro/Graphite/OLED surface modes and user-selectable corner styles is not accepted first-R8 behavior until/unless the shared platform requirements are explicitly changed.

## Rust / JNI boundary

Rust may own deterministic launcher-local state/logic where it materially helps correctness and testability. Kotlin/Android continues to own LauncherApps/package/profile APIs, Activity lifecycle, permissions and Compose UI.

Any JNI boundary must remain narrow and be tested end-to-end. A passing host Rust test is not proof that the Android native library is packaged/loaded/called correctly.

## Device portability

Panther, Bramble and future device targets should consume the same Sable Start source. A platform/device adapter is justified only when evidence identifies a real Android-version/device difference.

A bug observed on Panther is not sufficient reason to move common launcher semantics into `device_sable_panther`.

## HOME adoption

Launcher feature completeness and default HOME adoption are separate gates. See `HOME_ADOPTION_GATE.md`.

Testing Sable Start must not silently mutate the device default launcher merely because Sable Start is launcher-capable.

## Validation layers

Use layered evidence:

```text
requirements/source identity
 -> pure/model/Rust tests
 -> Kotlin/JNI/native tests as applicable
 -> app/module compile
 -> APK/package inspection
 -> product selection/install/image evidence
 -> controlled device launch/inventory/UI evidence
 -> HOME/default role validation only when separately authorized
```

No layer implies the next one automatically.