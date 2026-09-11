# Sable Start migration status

Status: **OPEN — source migration intentionally deferred until current R3B build evidence closes.**

The validated working copy currently lives in the Panther GrapheneOS 2026081300 workspace. The organization repository is documentation-only until the exact module tree is transferred and recursively verified.

## Migration gate

Before this repository becomes canonical source, the migration must prove:

- complete module-tree capture, including Kotlin, XML, Rust, resources, and future non-text assets;
- zero missing files;
- zero unexpected source files;
- zero byte mismatches against the validated workspace;
- current R3 source identities preserved;
- module build succeeds from the migrated checkout;
- clean reconstruction through `platform_manifest` succeeds.

## Current R3 identities already established

The active R3 source delta includes the revised preview activity, live-surface repository/model, and manifest changes already applied in the validated workspace. Exact hashes remain recorded in the existing SableOS validation documentation and will be imported here with the source-transfer evidence.

## Claim boundary

Repository creation does not prove portability. Portability closes only when another checkout can reconstruct the same Sable Start source and successfully pass the corresponding build gate.
