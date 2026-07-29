# Otherside Atmosphere Integration

Date: 2026-07-29

This document covers the vertical slice that replaces the temporary Otherside Attuned Rift shapeless recipe with a Mekanism chemical pipeline.

## Implemented Scope

Verified implemented:

- Registered `techcraft_additions:otherside_atmosphere` as a Mekanism chemical.
- Added a Mekanism chemical texture at `assets/techcraft_additions/textures/chemical/otherside_atmosphere.png`.
- Added `techcraft_additions:otherside_atmosphere` to Mekanism's `mekanism:chemical/gaseous` chemical tag.
- Added English display text for `chemical.techcraft_additions.otherside_atmosphere`.
- Added a Mekanism Chemical Injection Chamber recipe:
  - Recipe ID: `techcraft_additions:mekanism/otherside_attuned_rift`
  - Type: `mekanism:injecting`
  - Item input: `techcraft_additions:dimensional_rift`
  - Chemical input: `1000` mB `techcraft_additions:otherside_atmosphere`
  - Output: `techcraft_additions:otherside_attuned_rift`
  - `per_tick_usage`: `false`
- Removed only the temporary KubeJS shapeless recipe for `techcraft_additions:otherside_attuned_rift`.
- Preserved the existing Overworld collection path for `mekmm:unstable_dimensional_gas`.
- Prevented MekMM's Ambient Gas Collector from producing gas in dimensions other than the Overworld and Otherside.

Not implemented:

- No new item form or bucket form for the chemical.
- No custom GUI or EMI category for the Ambient Gas Collector.
- No changes to Mekanism: MoreMachine JARs.
- No changes to world saves.
- No Dark World Engine multiblock logic.

## Verified Dependency Information

Verified installed JARs:

- `Mekanism-1.21.1-10.7.19.85.jar`
  - Mod ID: `mekanism`
  - Version: `10.7.19`
- `mekmm-1.21.1-1.4.0.jar`
  - Mod ID: `mekmm`
  - Display name: `Mekanism: MoreMachine`
  - Version: `1.4.0`
- `deeperdarker-neoforge-1.21.1-1.4.1.jar`
  - Mod ID: `deeperdarker`
  - Version: `1.4.1`

Verified registry IDs:

- Otherside dimension: `deeperdarker:otherside`
- Existing MekMM collector gas: `mekmm:unstable_dimensional_gas`
- New addon chemical: `techcraft_additions:otherside_atmosphere`
- Base rift item: `techcraft_additions:dimensional_rift`
- Otherside rift output: `techcraft_additions:otherside_attuned_rift`
- Ambient Gas Collector block entity class: `com.jerry.mekmm.common.tile.machine.TileEntityAmbientGasCollector`

## API And Integration Findings

Verified Mekanism API/classes:

- `mekanism.common.registration.impl.ChemicalDeferredRegister` supports registering addon chemicals.
- `mekanism.api.chemical.ChemicalBuilder.builder(ResourceLocation)` supports assigning a chemical icon path.
- `mekanism.api.chemical.ChemicalBuilder.tint(int)` supports tinting the chemical texture.
- `mekanism.api.chemical.Chemical.isGaseous()` checks both its legacy builder flag and the `mekanism:chemical/gaseous` tag. The addon uses the tag path to avoid the deprecated `ChemicalBuilder.gaseous()` method.
- `mekanism:injecting` recipes consume an item plus a chemical and output an item, matching the rift attunement need.

Verified Mekanism: MoreMachine behaviour:

- `TileEntityAmbientGasCollector` has a private `suck(BlockPos)` method.
- The method creates a `ChemicalStack` of `MoreMachineChemicals.UNSTABLE_DIMENSIONAL_GAS`.
- The chemical choice is hard-coded in Java rather than driven by a recipe, tag, config, or datapack.
- The collector emits through Mekanism chemical handling after collection.
- MekMM's config controls collection amount and energy behaviour, but not per-dimension gas output.

Machine choice:

- Chemical Infuser was inspected and rejected for this recipe because `mekanism:chemical_infusing` is chemical-to-chemical.
- Chemical Injection Chamber was selected because `mekanism:injecting` is item plus chemical to item.

## Implementation Method

The chemical and rift attunement recipe are normal addon Java/data integration.

The Ambient Gas Collector output requires a narrow mixin because MekMM hard-codes `mekmm:unstable_dimensional_gas` inside private Java behaviour. The mixin:

- Cancels collection outside the Overworld and `deeperdarker:otherside`.
- Leaves the original `mekmm:unstable_dimensional_gas` holder unchanged in the Overworld.
- Replaces only the constructed chemical holder with `techcraft_additions:otherside_atmosphere` in `deeperdarker:otherside`.

This is custom Java behaviour, not a recipe or KubeJS-only integration.

## EMI Expectations

Verified:

- The `mekanism:injecting` recipe is a standard Mekanism recipe type and should be visible through Mekanism's EMI integration where Mekanism injecting recipes are shown.
- MekMM's EMI plugin has an info recipe for `mekmm:unstable_dimensional_gas`.

Not verified in-game by Codex yet:

- The new `techcraft_additions:otherside_atmosphere` chemical visual inside Mekanism tanks, pipes, gauges, and EMI.
- The `techcraft_additions:mekanism/otherside_attuned_rift` recipe's EMI display in the full Dev-TechCraft instance.
- Whether MekMM exposes enough recipe-viewer extension points to show the custom Otherside collection process without an addon EMI plugin.

Assumption:

- The Ambient Gas Collector itself may not show the new Otherside collection process as a machine recipe in EMI because collection is not recipe-backed in MekMM. A future addon EMI info entry may be useful if players need clearer discoverability.

## Recipe Balance Notes

The first vertical slice uses `1000` mB of Otherside Atmosphere per rift with `per_tick_usage: false`. This makes the step easy to reason about during testing: collect one bucket-equivalent chemical amount, inject one Dimensional Rift, receive one Otherside Attuned Rift.

The Ambient Gas Collector's production rate and energy usage remain controlled by MekMM's existing configuration. Per-dimension output is controlled by the addon mixin because MekMM does not expose that as data.

## Verification Results

Commands run:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.11'
$env:TECHCRAFT_LOCAL_MAVEN='D:\meta\libraries'
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
.\gradlew.bat compileJava --stacktrace
```

Result: `BUILD SUCCESSFUL`.

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.11'
$env:TECHCRAFT_LOCAL_MAVEN='D:\meta\libraries'
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
.\gradlew.bat build --stacktrace
```

Result: `BUILD SUCCESSFUL`.

```powershell
& 'C:\Users\josha\.cache\codex-runtimes\codex-primary-runtime\dependencies\node\bin\node.exe' --check pack\kubejs\server_scripts\techcraft_additions\30_dimensional_rifts.js
```

Result: syntax check passed.

```powershell
Get-Content -Raw <resource-json> | ConvertFrom-Json
```

Result: modified JSON resources parsed successfully.

```powershell
jar tf build\libs\techcraft-additions-0.1.0.jar | Select-String -Pattern "otherside_atmosphere|otherside_attuned_rift|mixins|AmbientGasCollector|gaseous.json"
```

Result: the built JAR contains the mixin class, mixin config, chemical texture, gaseous tag, and injecting recipe.

```powershell
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Sync-KubeJS.ps1
```

Result: sync completed into `D:\profiles\Dev-TechCraft\kubejs` with backups.

```powershell
Copy-Item build\libs\techcraft-additions-0.1.0.jar D:\profiles\Dev-TechCraft\mods\
```

Result: installed the built addon JAR into `D:\profiles\Dev-TechCraft\mods` after backing up any previous addon JAR.

No automated full-pack client launch was run by Codex. The `Dev-TechCraft` folder did not expose a clear safe launch script, and the existing logs predated this install.

Pending in-game verification:

- In-game Dev-TechCraft verification of collection by dimension, chemical rendering, EMI recipe display, and Chemical Injection Chamber crafting.
