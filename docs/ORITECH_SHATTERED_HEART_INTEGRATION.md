# Oritech Shattered Heart Integration

Date: 2026-07-29

Branch: `feature/oritech-shattered-heart-integration`

## Scope

This milestone replaces the temporary shaped Shattered Heart recipe with a real Oritech Particle Accelerator collision recipe. It does not implement the Dark World Engine multiblock, custom accelerator behavior, teleportation, chunk loading, remote AE2 access, or final balance.

## Verified Facts

### Installed mod versions

| Mod | Installed file | Mod ID | Version |
| --- | --- | --- | --- |
| Oritech | `oritech-neoforge-1.21.1-1.2.9.jar` | `oritech` | `1.2.9` |
| Oritech Things | `oritechthings-0.0.45.jar` | `oritechthings` | `0.0.45` |
| Deeper and Darker | `deeperdarker-neoforge-1.21.1-1.4.1.jar` | `deeperdarker` | `1.4.1` from filename |

Oritech metadata declares NeoForge dependency range `[21.1.200,)` and Minecraft dependency range `[1.21,)`. Oritech Things metadata declares Oritech `[1.2.0,)`, NeoForge `[21.1.0,)`, and Minecraft `[1.21.1, 1.22)`.

### Candidate heart ingredient

Verified registry ID: `deeperdarker:heart_of_the_deep`

Evidence:

- Deeper and Darker ships `assets/deeperdarker/models/item/heart_of_the_deep.json`.
- Deeper and Darker English lang maps `item.deeperdarker.heart_of_the_deep` to `Heart of the Deep`.
- `com.kyanite.deeperdarker.content.DDItems` contains static field `HEART_OF_THE_DEEP`.
- `data/deeperdarker/loot_modifiers/warden_heart_from_warden.json` adds `deeperdarker:heart_of_the_deep` to `minecraft:entities/warden`.
- `data/deeperdarker/advancement/main/kill_warden.json` uses `deeperdarker:heart_of_the_deep` as the Warden kill advancement icon.

Rejected fallback: `minecraft:heart_of_the_sea` was not used because it is not Warden-related and the pack has a verified Deeper and Darker Warden heart item.

### Oritech particle collision recipe format

Verified built-in examples are located in `data/oritech/recipe/particle/*.json`. Example fields:

- `type`: `oritech:particle_collision`
- `ingredients`: list of two `Ingredient` objects, using either `item` or `tag`
- `results`: list containing one item stack object with `id` and `count`
- `time`: integer collision speed threshold

Verified class inspection:

- `rearth.oritech.init.recipes.RecipeContent` contains `PARTICLE_COLLISION`.
- `rearth.oritech.api.recipe.ParticleCollisionRecipeBuilder` uses `RecipeContent.PARTICLE_COLLISION` and validates exactly two inputs and one result.
- `rearth.oritech.init.recipes.OritechRecipeType` decodes `time`, `ingredients`, `results`, `type`, optional `fluidInput`, and optional `fluidOutputs`.
- `rearth.oritech.block.entity.accelerator.AcceleratorControllerBlockEntity` calls `tryCraftResult(long speed, ItemStack first, ItemStack second)` on collision.
- `tryCraftResult` looks up `RecipeContent.PARTICLE_COLLISION`, retries with reversed input order if needed, compares collision speed to `OritechRecipe.getTime()`, and writes the first recipe result to the accelerator output slot.
- `rearth.oritech.init.compat.emi.OritechEMIParticleCollisionRecipe` displays `recipe.getTime()` through translation key `emi.title.oritech.collisionspeed`.

### Oritech Things compatibility

Verified from `oritechthings.mixins.json` and class inspection:

- `AcceleratorControllerBlockEntityMixin` stores linked magnetic field positions.
- `AcceleratorParticleLogicMixin` assists bend distance using magnetic fields and consumes magnetic field energy.
- No inspected Oritech Things mixin replaced the Oritech particle collision recipe serializer, JSON fields, or `tryCraftResult` lookup path.

## Implemented Recipe

File: `src/main/resources/data/techcraft_additions/recipe/oritech/shattered_heart.json`

Recipe ID: `techcraft_additions:oritech/shattered_heart`

```json
{
  "type": "oritech:particle_collision",
  "ingredients": [
    {
      "item": "deeperdarker:heart_of_the_deep"
    },
    {
      "item": "minecraft:amethyst_cluster"
    }
  ],
  "results": [
    {
      "count": 1,
      "id": "techcraft_additions:shattered_heart"
    }
  ],
  "time": 18000
}
```

This is a standard datapack recipe bundled in the addon jar. No KubeJS custom JSON, direct Java API integration, reflection, or mixin was needed.

## Temporary Recipe Removed

The temporary shaped recipe for `techcraft_additions:shattered_heart` was removed from `pack/kubejs/server_scripts/techcraft_additions/20_shattered_heart.js`.

The Splintered Echo prototype recipe remains in that file and is still gated by `TechCraftAdditions.developmentRecipesEnabled`.

## Assumptions And Risks

- Assumption: The requested `18000 J` maps to Oritech's particle collision `time`/speed threshold, because Oritech's collision recipe and EMI code expose this value as collision speed rather than as an energy field named `energy` or `joules`.
- Assumption: Bundling the recipe in the addon jar is acceptable because TechCraft: Additions is a pack addon and the target pack includes Oritech and Deeper and Darker.
- Risk: A standalone Gradle dev client without Oritech installed may report an unknown recipe type during resource loading. Full-pack testing should be done in `Dev-TechCraft`.
- Verified by user testing: `18000` is currently reachable in the TechCraft testing environment.

## Recommended Verification

1. Build the addon jar.
2. Install it in `Dev-TechCraft`.
3. Launch the full dev profile.
4. Confirm EMI shows `techcraft_additions:oritech/shattered_heart` as an Oritech Particle Accelerator recipe.
5. Confirm the temporary shaped Shattered Heart recipe no longer appears.
6. Confirm colliding `deeperdarker:heart_of_the_deep` with `minecraft:amethyst_cluster` at or above `18000` speed outputs `techcraft_additions:shattered_heart`.
