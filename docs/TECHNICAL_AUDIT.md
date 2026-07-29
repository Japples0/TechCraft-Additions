# TechCraft: Additions Technical Audit

Date: 2026-07-29

This audit is for the local TechCraft instance at `D:\profiles\TechCraft (1)`. The live instance was inspected read-only. No saves, original mod JARs, KubeJS scripts, quest files, or live instance files were modified.

## Verified Pack Baseline

| Property | Verified value | Evidence |
| --- | --- | --- |
| Minecraft | `1.21.1` | `logs/latest.log` launch args: `--version 1.21.1`, `--fml.mcVersion 1.21.1` |
| NeoForge | `21.1.235` | `logs/latest.log` launch args: `--fml.neoForgeVersion 21.1.235`; library `D:\meta\libraries\net\neoforged\neoforge\21.1.235\neoforge-21.1.235-universal.jar` |
| Java used by pack | Azul OpenJDK `21.0.9+10-LTS` | `logs/launcher_log.txt`: `JVM identified as Azul Systems, Inc. OpenJDK 64-Bit Server VM 21.0.9+10-LTS` |
| Java used for addon build | `C:\Program Files\Java\jdk-21.0.11` | Local compile JDK; not the same runtime vendor/patch as the pack |

## Relevant Mods

| Mod | JAR filename | Version from metadata | Mod ID |
| --- | --- | --- | --- |
| Deeper and Darker | `deeperdarker-neoforge-1.21.1-1.4.1.jar` | `1.4.1` | `deeperdarker` |
| Oritech | `oritech-neoforge-1.21.1-1.2.9.jar` | `1.2.9` | `oritech` |
| Mekanism | `Mekanism-1.21.1-10.7.19.85.jar` | `10.7.19` | `mekanism` |
| Mekanism More Machines | `mekmm-1.21.1-1.4.0.jar` | `1.4.0` | `mekmm` |
| Applied Energistics 2 | `appliedenergistics2-19.2.17.jar` | `19.2.17` | `ae2` |
| AEInfinityBooster | `aeinfinitybooster-neoforge-1.21.1-1.0.0.58.jar` | `1.21.1-1.0.0.58` | `aeinfinitybooster` |
| AppliedE | `appliede-1.0.8-beta.jar` | `1.0.8-beta` | `appliede` |
| ProjectE | `ProjectE-1.21.1-PE1.1.0.jar` | `1.1.0` | `projecte` |
| Draconic Evolution | `Draconic-Evolution-1.21.1-3.1.4.632.jar` | `3.1.4.632` | `draconicevolution` |
| Re-Avaritia | `Re-Avaritia-neoforge-1.21.1-1.4.1-release.jar` | `1.4.1` | `avaritia` |
| ArPhEx | `ArPhEx-5.0.2-neoforge-1.21.1.jar` | `5.0.2` | `arphex` |
| Iron's Spellbooks | `irons_spellbooks-1.21.1-3.16.2.jar` | `1.21.1-3.16.2` | `irons_spellbooks` |

## Verified Candidate Registry IDs

These IDs were verified from JAR asset/data paths, lang files, or the local EMI cache. Recipe IDs with slashes are recipe/data IDs, not necessarily item IDs.

| Area | Verified IDs |
| --- | --- |
| Deeper and Darker sculk/resonance | `deeperdarker:sculk_stone`, `deeperdarker:sculk_transmitter`, `deeperdarker:reinforced_echo_shard`, `deeperdarker:ancient_compass`, `deeperdarker:resonarium`, `deeperdarker:resonarium_plate` |
| Oritech machines/materials | `oritech:accelerator_controller`, `oritech:particle_collector_block`, `oritech:advanced_augment_station`, `oritech:arcane_augment_station`, `oritech:machine_core_7`, `oritech:reactor_controller`, `oritech:unstable_container`, `oritech:sculk_paint` |
| Mekanism machines/items | `mekanism:dimensional_stabilizer`, `mekanism:quantum_entangloporter`, `mekanism:teleporter`, `mekanism:teleporter_frame`, `mekanism:portable_teleporter`, `mekanism:qio_dashboard`, `mekanism:qio_drive_array`, `mekanism:qio_drive_time_dilating`, `mekanism:ultimate_control_circuit` |
| Mekanism More Machines | `mekmm:chemical_replicator`, `mekmm:fluid_replicator`, `mekmm:replicator`, `mekmm:recycler`, `mekmm:cnc_stamper`, `mekmm:cnc_lathe`, `mekmm:cnc_rolling_mill`, `mekmm:ultimate_liquifying_factory`, chemical `mekmm:unstable_dimensional_gas` |
| AE2 network/wireless/spatial | `ae2:crystal_resonance_generator`, `ae2:wireless_access_point`, `ae2:wireless_booster`, `ae2:quantum_link`, `ae2:quantum_ring`, `ae2:singularity`, `ae2:quantum_entangled_singularity`, `ae2:spatial_cell_component_2`, `ae2:spatial_cell_component_16`, `ae2:spatial_cell_component_128` |
| AEInfinityBooster | `aeinfinitybooster:infinity_card`, `aeinfinitybooster:dimension_card` |
| AppliedE | `appliede:emc_interface`, `appliede:emc_interface_part`, `appliede:emc_export_bus`, `appliede:emc_import_bus`, `appliede:emc_module`, `appliede:transmutation_terminal`, `appliede:wireless_transmutation_terminal` |
| ProjectE | `projecte:watch_of_flowing_time`, `projecte:transmutation_table`, `projecte:transmutation_tablet`, `projecte:klein_star_omega`, `projecte:collector_mk3`, `projecte:relay_mk3`, `projecte:condenser_mk2`, `projecte:dm_pedestal` |
| Draconic Evolution | `draconicevolution:crafting_core`, `draconicevolution:energy_core`, `draconicevolution:energy_core_stabilizer`, `draconicevolution:wyvern_core`, `draconicevolution:awakened_core` display name "Draconic Core", `draconicevolution:chaotic_core`, `draconicevolution:advanced_dislocator` |
| Re-Avaritia | `avaritia:neutron_pile`, `avaritia:neutron_nugget`, `avaritia:neutron_ingot`, `avaritia:crystal_matrix_ingot`, `avaritia:infinity_catalyst`, `avaritia:infinity_ingot`, `avaritia:sculk_crafting_table`, `avaritia:neutron_collector`, `avaritia:dense_neutron_collector`, `avaritia:denser_neutron_collector`, `avaritia:densest_neutron_collector`, `avaritia:infinity_upgrade` |
| ArPhEx | `arphex:spacetime_shard`, `arphex:spacetime_ingot`, `arphex:time_prism`, `arphex:time_prism_shard`, `arphex:block_of_time_prism`, `arphex:core_of_eternal_suffering`, `arphex:time_syrup`, `arphex:singularity_satchel` |
| Iron's Spellbooks | `irons_spellbooks:arcane_essence`, `irons_spellbooks:amethyst_resonance_charm`, `irons_spellbooks:ruined_book`, `irons_spellbooks:ancient_knowledge_fragment`, `irons_spellbooks:arcane_ingot`, `irons_spellbooks:arcane_anvil`, `irons_spellbooks:inscription_table`, `irons_spellbooks:scroll_forge`; spell/icon evidence for `irons_spellbooks:sonic_boom` and `irons_spellbooks:sculk_tentacles` |

No existing `finely_woven_time`, `sculk_resonance_engine`, `resonance_casing`, or `sculk_resonance_controller` IDs were found in the inspected pack files.

## Integration Surfaces

| Mod | Verified APIs/classes | Likely implementation route |
| --- | --- | --- |
| Deeper and Darker | No `api`, `integration`, or `compat` package classes found. Classes include `com.kyanite.deeperdarker.content.DDItems`, `DDBlocks`, `DDRecipes`, `world.otherside.OthersideDimension`, `OthersideTeleporter`. | Recipes/data and normal registry references are safe. Dimension travel behavior may require custom Java using vanilla/NeoForge dimension APIs; direct compile-time dependency should be avoided unless source is reviewed. |
| Oritech | `rearth.oritech.api.energy.EnergyApi`, `rearth.oritech.api.fluid.FluidApi`, `rearth.oritech.api.item.ItemApi`, `rearth.oritech.api.recipe.*RecipeBuilder`, `rearth.oritech.api.networking.NetworkManager`. | Recipes/data for crafting and Oritech processing; direct API integration is available for energy/fluid/item/networking behavior. |
| Mekanism | `mekanism.api.*` packages exist, plus `mekanism.common.integration.crafttweaker.*`, `mekanism.common.integration.computer.*`, recipe-viewer recipe classes. | Recipes/data for machine recipes and crafting. Direct API is appropriate for chemicals, energy containers, or capabilities. Custom Java is required for Dark World Engine behavior that mimics dimensional stabilizer, teleporter, QIO, or quantum entangloporter semantics. |
| Mekanism More Machines | `com.jerry.mekmm.api.recipes.MoreMachineRecipeTypes`, `MoreMachineRecipeSerializers`, `com.jerry.mekmm.api.datamaps.*`, `com.jerry.mekmm.api.datagen.recipe.builder.*`; chemical registry class `com.jerry.mekmm.common.registries.MoreMachineChemicals`. | Recipes/data maps for replicator and processing recipes. Direct API possible for recipes/datamaps; chemical `mekmm:unstable_dimensional_gas` should be referenced through Mekanism chemical registry APIs if used in Java. |
| AE2 | Extensive `appeng.api.*`, including `IWirelessAccessPoint`, `IAEItemPowerStorage`, `ISpatialStorageCell`, storage/grid APIs, crafting APIs. Internal classes include `QuantumBridgeBlockEntity`, `WirelessAccessPointBlockEntity`, `SpatialStorageHelper`. | Direct AE2 API integration is the right path for remote ME access and wireless/grid interaction. Recipes/data can add crafting. Avoid depending on AE2 internals unless no API path exists. |
| AEInfinityBooster | No API package found. Classes include `InfinityCard`, `DimensionCard`, multiple AE2 mixins, `AEInfinityBoosterConfig`. | Use recipes/data and registry/item checks. For behavior, prefer compatibility by consuming the card items or reflecting only if unavoidable. |
| AppliedE | Integration classes under `gripe._90.appliede.integration.*`; core classes include `EMCInterfaceLogic`, `EMCStorage`, `KnowledgeService`, `EMCKey`, `TransmutationTerminalHost`. | Recipes/data can reference blocks/items. Direct compile-time integration needs source review because no public `api` package was found; reflection or optional Java integration may be safer. |
| ProjectE | `moze_intel.projecte.api.proxy.IEMCProxy`, `ITransmutationProxy`, `api.capabilities.block_entity.IEmcStorage`, `api.conversion.*`, `api.mapper.*`, `ProjectERegistries`, `EMCRemapEvent`. | Direct API integration is available for EMC values, conversion mappings, and EMC storage. Recipes/data can use ProjectE items/blocks. |
| Draconic Evolution | `com.brandon3055.draconicevolution.api.DraconicAPI`, energy/module/capability APIs, `api.crafting.IFusionRecipe`; integration classes include `AE2Compat`, `integration.computers.*`. | Recipes/data for fusion crafting and components; direct API for modules/energy/fusion recipes. Custom Java for high-tier energy storage or dislocator-like travel. |
| Re-Avaritia | Many `committee.nova.mods.avaritia.api.*` utility classes. Verified recipes under `data/avaritia/recipe/*`; crafting table block `avaritia:sculk_crafting_table`. | Recipes/data for extreme crafting chain references. Direct API may be useful for Avaritia-specific crafting, but source review is recommended before compile-time coupling. |
| ArPhEx | No API package found. Classes include `net.arphex.init.ArphexModItems`, `ArphexModBlocks`, dimension classes `TheCrawlingDimension`, `TheWebroomsDimension`, and teleport procedures. | Recipes/data and registry references only for now. Custom Java for any dimension interaction; avoid direct coupling until source is reviewed. |
| Iron's Spellbooks | `io.redspace.ironsspellbooks.api.*`, including events `SpellTeleportEvent`, `SpellOnCastEvent`, `SpellPreCastEvent`, spell registry classes, magic/item APIs. | Direct API integration is available for spell/magic events. Recipes/data for magic ingredients and machines. |

## Assumptions and Open Verification

- The first addon mod ID is assumed to be `techcraft_additions`; no conflicting ID was found locally.
- The initial placeholders intentionally have no recipes and no functional block entities.
- The Dark World Engine should start as a standalone NeoForge multiblock and only add optional direct integrations after each dependency's source/API stability is reviewed.
- The exact behavior of `mekmm:unstable_dimensional_gas` was verified by lang/EMI and MekMM classes, but not by a JSON asset path; it appears to be a Mekanism chemical registration.
- Client registration was not verified in-game during this audit document write-up; the Gradle build compiles the mod and processes assets successfully.
