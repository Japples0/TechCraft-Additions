# Dimensional Atmospheres

Date: 2026-07-29

This document describes the data-driven Ambient Gas Collector integration for dimension-specific Mekanism chemicals.

## Implemented Atmospheres

Verified implemented in the addon:

- `minecraft:overworld` -> `mekmm:unstable_dimensional_gas`
- `minecraft:the_nether` -> `techcraft_additions:nether_atmosphere`
- `minecraft:the_end` -> `techcraft_additions:end_atmosphere`
- `deeperdarker:otherside` -> `techcraft_additions:otherside_atmosphere`

Unmapped dimensions produce no gas from MekMM's Ambient Gas Collector.

## Data Format

Atmosphere mappings are loaded from:

```text
data/*/dimensional_atmospheres/*.json
```

Each file uses:

```json
{
  "dimension": "minecraft:the_nether",
  "chemical": "techcraft_additions:nether_atmosphere"
}
```

The mixin reads these files through the server resource manager and resolves the configured chemical through Mekanism's chemical registry. This keeps the collector behaviour table-driven instead of hard-coding dimension conditionals.

## Chemicals

Addon chemicals:

- `techcraft_additions:nether_atmosphere`
- `techcraft_additions:end_atmosphere`
- `techcraft_additions:otherside_atmosphere`

All addon atmosphere chemicals:

- Are registered as Mekanism chemicals.
- Are tagged in `mekanism:chemical/gaseous`.
- Use the legacy Mekanism `ChemicalBuilder.gaseous()` flag for current compatibility.
- Have animated `assets/techcraft_additions/textures/liquid/*_atmosphere.png` textures.
- Are stitched into the block atlas through `assets/minecraft/atlases/blocks.json`.

The Overworld intentionally keeps `mekmm:unstable_dimensional_gas` so MekMM's existing UU matter path remains available.

## Rift Recipes

Permanent Mekanism Chemical Injection Chamber recipes:

- `techcraft_additions:mekanism/nether_attuned_rift`
  - Item input: `techcraft_additions:dimensional_rift`
  - Chemical input: `1` mB/tick `techcraft_additions:nether_atmosphere`
  - Output: `techcraft_additions:nether_attuned_rift`
- `techcraft_additions:mekanism/end_attuned_rift`
  - Item input: `techcraft_additions:dimensional_rift`
  - Chemical input: `1` mB/tick `techcraft_additions:end_atmosphere`
  - Output: `techcraft_additions:end_attuned_rift`
- `techcraft_additions:mekanism/otherside_attuned_rift`
  - Item input: `techcraft_additions:dimensional_rift`
  - Chemical input: `1` mB/tick `techcraft_additions:otherside_atmosphere`
  - Output: `techcraft_additions:otherside_attuned_rift`

The temporary KubeJS shapeless recipes for Nether, End and Otherside attunement have been removed. Overworld and Draconic attunement remain temporary KubeJS recipes for now.

## Verification

Commands run:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.11'
$env:TECHCRAFT_LOCAL_MAVEN='D:\meta\libraries'
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
.\gradlew.bat build --stacktrace
```

Result: `BUILD SUCCESSFUL`.

Known warning:

- Mekanism `10.7.19` warns that `ChemicalBuilder.gaseous()` is deprecated. The flag is intentionally kept because full-pack testing showed tag-only gas registration was not enough for this integration path.

Pending in-game verification:

- Ambient Gas Collector output in the Nether.
- Ambient Gas Collector output in the End.
- No gas output in other unmapped dimensions.
- EMI display and machine processing for Nether and End attuned rift recipes.

