# Sable Start

Canonical repository for the SableOS launcher and shell.

Android checkout path: `packages/apps/SableStart`.

Sable Start is common product code. Device repositories must not carry copies of this application. Device and Android-substrate differences should be handled through stable interfaces and bounded adapters.

## Current status

The active implementation is being validated on Pixel 7 (`panther`) using GrapheneOS `2026081300` / Android 17.

The canonical source has not yet been migrated into this repository. Migration will happen only after the current R3B compile/type-check gate closes and the complete module tree is captured and recursively hash-verified against the validated workspace.

See `docs/ARCHITECTURE.md` and `docs/MIGRATION_STATUS.md`.
