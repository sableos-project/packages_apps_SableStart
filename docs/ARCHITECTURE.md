# Sable Start architecture

Sable Start is a portable SableOS application surface, not a per-device launcher fork.

## Ownership boundary

Sable Start owns presentation, navigation, application discovery UX, live-surface presentation, and user-facing interaction semantics.

It should consume Android and Sable platform capabilities through stable APIs. It should not own device HAL integration, vendor protocols, firmware behavior, or raw hardware policy.

## Data boundary

UI code should not perform blocking provider I/O on the main thread. Provider-backed data belongs behind repository/service boundaries with explicit states such as loading, live, permission-required, unavailable, and error.

The current R3 direction uses local platform sources for date/time, media counts, and calendar data. Weather and tasks remain explicit provider-contract gaps rather than silent network/account dependencies.

## Device portability

Panther, Bramble, and future MediaTek/QWERTY targets should consume the same Sable Start source. Platform-specific behavior belongs below the application boundary unless a product-level interaction profile genuinely differs.

## Validation layers

Sable Start changes should progress through source guards, host/pure-model tests, module build/type-check, package inspection, controlled install, interaction semantics, provider correctness, and device/runtime acceptance.
