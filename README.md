# TechCraft: Additions

TechCraft: Additions is a NeoForge addon for the local TechCraft Minecraft modpack. It exists to prototype and then harden original endgame content that bridges the pack's major technology, magic, storage, EMC, and dimensional systems.

The flagship feature is the Dark World Engine: a future 3x3x3 multiblock powered by an installable Sculk Resonance Engine core. The intended long-term role is dimensional travel, remote network access, and controlled chunk loading powered by Finely Woven Time.

## Versions

- Minecraft: `1.21.1`
- NeoForge: `21.1.235`
- Java target: `21`

## Current Status

This branch contains a visible prototype of the endgame progression chain:

- Permanent Java-registered item IDs for the progression components.
- Temporary placeholder item textures, models, names, lore tooltips, and creative-tab access.
- Temporary KubeJS shaped/shapeless recipes staged under `pack/kubejs`, except where a prototype recipe has already been replaced by a real integration.
- A real Oritech Particle Accelerator recipe for `techcraft_additions:shattered_heart`.

No Dark World Engine multiblock logic, teleportation, chunk loading, custom gases, custom rendering, or third-party machine behavior is implemented yet.

## Build

Use JDK 21.

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.11"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build
```

If you have a local Minecraft/NeoForge Maven cache, configure it outside the repository:

```powershell
$env:TECHCRAFT_LOCAL_MAVEN = "<path-to-local-maven-cache>"
```

or:

```powershell
.\gradlew.bat build -Ptechcraft.localMaven="<path-to-local-maven-cache>"
```

Do not commit machine-specific paths.

## Development Instance Sync

The live `TechCraft (1)` profile is not the development target. Use the isolated `Dev-TechCraft` instance.

To sync the staged KubeJS prototype scripts:

```powershell
$env:TECHCRAFT_DEV_INSTANCE = "<path-to-Dev-TechCraft>"
.\scripts\Sync-KubeJS.ps1
```

The script prints the source and destination, refuses destinations that do not look like the isolated dev instance, and backs up any overwritten files.

For full in-pack testing, install the built addon JAR from `build/libs/` into the development instance's `mods` folder, then launch the dev instance normally.

## Prototype Warning

Current recipes and textures are intentionally temporary. The recipe chain is designed to make the whole journey visible in EMI and craftable during development. Later milestones should replace one placeholder recipe at a time with real integrations from Oritech, Mekanism, AE2, ProjectE, Draconic Evolution, Re-Avaritia, Deeper and Darker, ArPhEx, Iron's Spellbooks, and related pack systems.

The first real integration is documented in `docs/ORITECH_SHATTERED_HEART_INTEGRATION.md`: `deeperdarker:heart_of_the_deep` collides with `minecraft:amethyst_cluster` in Oritech's Particle Accelerator at `18000` collision speed to create `techcraft_additions:shattered_heart`.
