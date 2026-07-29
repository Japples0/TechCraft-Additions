# TechCraft: Additions KubeJS Prototype

Date: 2026-07-29

This document covers the temporary endgame progression recipe prototype, with ongoing replacements tracked on feature branches such as `feature/oritech-shattered-heart-integration`.

## Scope

Verified implemented:

- Permanent Java item IDs exist for the progression chain.
- Each item has a temporary model, placeholder texture, English name, lore tooltip, and creative-tab access.
- Temporary KubeJS recipes are staged in-repo under `pack/kubejs/server_scripts/techcraft_additions`.
- The temporary shaped Shattered Heart recipe has been replaced by a standard addon datapack recipe for Oritech.
- A sync script copies only the repo-local KubeJS files into the isolated `Dev-TechCraft` instance.

Not implemented:

- Dark World Engine multiblock behavior.
- Custom machine processing.
- Teleportation, chunk loading, AE2 remote access, EMC behavior, or Mekanism/Oritech machine integration.
- Final art or final balance.

## Registered Items

The following IDs are registered by Java, not by KubeJS:

- `techcraft_additions:dark_world_engine_tablet`
- `techcraft_additions:illuminated_world_engine_tablet`
- `techcraft_additions:shattered_heart`
- `techcraft_additions:splintered_echo`
- `techcraft_additions:dimensional_rift`
- `techcraft_additions:overworld_attuned_rift`
- `techcraft_additions:nether_attuned_rift`
- `techcraft_additions:end_attuned_rift`
- `techcraft_additions:otherside_attuned_rift`
- `techcraft_additions:draconic_attuned_rift`
- `techcraft_additions:terrestrial_lattice`
- `techcraft_additions:otherworldly_fragment`
- `techcraft_additions:dimensional_descender`
- `techcraft_additions:loose_strands_of_time`
- `techcraft_additions:finely_woven_time`
- `techcraft_additions:universe_tether`
- `techcraft_additions:sculk_resonance_engine`
- `techcraft_additions:echoes_of_tomorrow`

Existing placeholder block IDs from the bootstrap remain:

- `techcraft_additions:resonance_casing`
- `techcraft_additions:sculk_resonance_controller`

## Recipe Files

All recipe IDs are under `techcraft_additions:development/`.

Exception: `techcraft_additions:oritech/shattered_heart` is now a permanent addon datapack recipe, not a KubeJS development recipe.

| File | Purpose |
| --- | --- |
| `00_development_helpers.js` | Shared namespace helper and development recipe flag. |
| `10_tablet_activation.js` | Creates and illuminates the Dark World Engine Tablet. |
| `20_shattered_heart.js` | Creates Splintered Echo. The old temporary shaped Shattered Heart recipe was removed. |
| `30_dimensional_rifts.js` | Creates base and attuned dimensional rifts. |
| `40_terrestrial_lattice.js` | Combines attuned rifts into Terrestrial Lattice. |
| `50_otherworldly_fragment.js` | Creates Otherworldly Fragment. |
| `60_dimensional_descender.js` | Creates Universe Tether and Dimensional Descender. |
| `70_woven_time.js` | Creates Loose Strands of Time and Finely Woven Time. |
| `80_resonance_engine.js` | Creates Resonance Casing and Sculk Resonance Engine. |
| `90_echoes_of_tomorrow.js` | Creates Echoes of Tomorrow. |

## Development Flag

`00_development_helpers.js` defines:

```js
globalThis.TechCraftAdditions = {
  developmentRecipesEnabled: true,
  item: id => `techcraft_additions:${id}`,
  developmentId: id => `techcraft_additions:development/${id}`
}
```

Set `developmentRecipesEnabled` to `false` when replacing the temporary recipes with real progression recipes. This disables every staged prototype recipe without deleting the files.

## Chain Summary

The visible prototype chain is:

1. `dark_world_engine_tablet`
2. `illuminated_world_engine_tablet`
3. `shattered_heart`
4. `splintered_echo`
5. `dimensional_rift`
6. `overworld_attuned_rift`, `nether_attuned_rift`, `end_attuned_rift`, `otherside_attuned_rift`, `draconic_attuned_rift`
7. `terrestrial_lattice`
8. `otherworldly_fragment`
9. `universe_tether`
10. `dimensional_descender`
11. `loose_strands_of_time`
12. `finely_woven_time`
13. `resonance_casing`
14. `sculk_resonance_engine`
15. `echoes_of_tomorrow`

`shattered_heart` is now obtained through Oritech instead of the development KubeJS shaped recipe:

- Recipe ID: `techcraft_additions:oritech/shattered_heart`
- Type: `oritech:particle_collision`
- Inputs: `deeperdarker:heart_of_the_deep` and `minecraft:amethyst_cluster`
- Output: `techcraft_additions:shattered_heart`
- Collision speed threshold: `18000`

## Verified Dependency IDs Used

These third-party IDs are used by the prototype recipes and were previously verified in `docs/TECHNICAL_AUDIT.md`:

- `deeperdarker:sculk_stone`
- `deeperdarker:heart_of_the_deep`
- `deeperdarker:resonarium`
- `deeperdarker:reinforced_echo_shard`
- `deeperdarker:resonarium_plate`
- `draconicevolution:draconium_dust`
- `projecte:watch_of_flowing_time`

Vanilla IDs are ordinary Minecraft registry IDs.

## Sync Workflow

Use the isolated dev instance only:

```powershell
$env:TECHCRAFT_DEV_INSTANCE = "D:\profiles\Dev-TechCraft"
powershell.exe -NoProfile -ExecutionPolicy Bypass -File .\scripts\Sync-KubeJS.ps1
```

The script:

- Prints source and destination paths.
- Refuses any destination not named `Dev-TechCraft`.
- Refuses `TechCraft (1)`.
- Refuses paths inside a `saves` folder.
- Requires an existing destination `kubejs` folder.
- Backs up overwritten files under `kubejs/_codex_backups/techcraft_additions_sync_<timestamp>/`.

The script was run successfully against `D:\profiles\Dev-TechCraft`. It backed up the previous synced prototype files and copied the updated repo files.

The refusal path was also tested with `D:\profiles\TechCraft (1)` and stopped before copying.

## Verification Results

Commands run:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.11'
$env:TECHCRAFT_LOCAL_MAVEN='D:\meta\libraries'
.\gradlew.bat build --stacktrace
```

Result: `BUILD SUCCESSFUL`.

```powershell
& 'C:\Users\josha\.cache\codex-runtimes\codex-primary-runtime\dependencies\node\bin\node.exe' --check <each KubeJS file>
```

Result: all KubeJS files passed JavaScript syntax checks.

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21.0.11'
$env:TECHCRAFT_LOCAL_MAVEN='D:\meta\libraries'
.\gradlew.bat runClient --stacktrace
```

Result: bounded launch timed out after the client stayed open. `run/logs/latest.log` verified `TechCraft: Additions 0.1.0 (techcraft_additions)` loaded and resources reloaded with `mod/techcraft_additions`. No missing item model, missing block model, missing texture, lang, exception, or addon error was found in the inspected log. The launch entered the local Gradle dev run world `New World`, not a TechCraft profile save.

## Pending In-Pack Verification

Not yet verified in Codex:

- Dev-TechCraft full pack launch after the KubeJS sync.
- KubeJS runtime loading with the full pack.
- EMI visibility for the full recipe chain.
- Survival crafting every chain step in the dev instance.
- Visual inspection of all item sprites inside EMI/inventory.

The built addon JAR was copied into `D:\profiles\Dev-TechCraft\mods` for this next manual/dev-profile verification pass.

## Replacement Priority

Recommended replacement order:

1. Replace `loose_strands_of_time` and `finely_woven_time` first, because they define the core power fantasy and currently depend on a simple shaped recipe around `projecte:watch_of_flowing_time`.
2. Replace rift attunement recipes next, using real dimension/mod progression gates.
3. Replace `sculk_resonance_engine` after the casing and woven-time loops feel good.
4. Leave `echoes_of_tomorrow` as the final prototype capstone until the Dark World Engine has real behavior.
