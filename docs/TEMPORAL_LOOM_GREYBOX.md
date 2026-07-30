# Temporal Loom Greybox

## Milestone scope

This milestone implements the first functional server-side Temporal Loom. It proves the permanent
block vocabulary, 7x5x5 formation rules, persistent controller, FE/output capabilities, production
state machine, diagnostics, temporary particles and stable future-renderer anchors.

It intentionally does not implement the final infinity-loop renderer, shaders, final balance,
Finely Woven Time production, environmental requirements or functional upgrades.

## Registered blocks

| Registry ID | Role |
| --- | --- |
| `techcraft_additions:temporal_loom_controller` | Controller, FE input and output capability host |
| `techcraft_additions:resonance_casing` | Shared structural frame; existing ID preserved |
| `techcraft_additions:temporal_anchor` | Outer loop stabiliser |
| `techcraft_additions:time_spindle` | Central compression spine |
| `techcraft_additions:amethyst_resonator` | Loop resonance node |
| `techcraft_additions:descender_mount` | Structural future-upgrade mount |
| `techcraft_additions:output_shuttle` | Structural output assembly |
| `techcraft_additions:energy_conduit` | Visually identifiable energy-input structure |

All blocks have blockstates, block/item models, 16x16 temporary textures, translations, creative-tab
entries and self-drop loot tables.

## Exact pattern

Dimensions are **7 wide x 5 high x 5 deep**. The controller is the centre character of the first
row in `y=0`. Rows point from the controller face toward the rear of the machine.

```text
y=0       y=1       y=2       y=3       y=4
RRRCRRR   A.....A   R.....R   A.....A   .......
RREEERR   .RR.RR.   RP...PR   .RR.RR.   ..RRR..
RRR.RRR   .R.O.R.   R..X..R   .R.S.R.   ..RDR..
RRRRRRR   .RR.RR.   RP...PR   .RR.RR.   ..RRR..
RRRRRRR   A.....A   R.....R   A.....A   .......
```

Legend: `C` controller, `R` casing, `A` anchor, `S` spindle, `P` resonator, `D` Descender
mount, `O` output shuttle, `E` energy conduit, `X` singularity air and `.` reserved air.
Every air position is strict so renderer space cannot be obstructed. Full coordinate conventions
are in [TEMPORAL_LOOM_STRUCTURE.md](TEMPORAL_LOOM_STRUCTURE.md).

## Orientation and validation

The controller faces outward. The pattern extends opposite its facing direction and rotates for
north, east, south and west. Mirroring is not implemented.

Validation runs:

- after controller placement;
- when the player activates the controller;
- after a nearby permanent Loom part is placed or removed;
- once after block-entity loading.

Part changes only set a deferred validation flag. The complete 175-position volume is scanned on
the next controller tick, never continuously. A failed validation stores the first mismatch and
reports its world coordinate, expected symbol/ID and found block through the action bar.

## State machine

```text
UNFORMED -> DORMANT -> SEARCHING -> HARVESTING -> SYNTHESIZING -> DORMANT
                        |              |                |
                        +------ insufficient FE: pause progress

DORMANT or SYNTHESIZING -> OUTPUT_BLOCKED -> DORMANT when space is available
```

Greybox operating constants live in `TemporalLoomOperatingProfile.GREYBOX_BASE`:

| Setting | Value |
| --- | ---: |
| Energy capacity | 4,000,000 FE |
| Maximum receive per call | 100,000 FE |
| FE per active tick | 1,000 FE |
| Searching | 100 ticks |
| Harvesting | 200 ticks |
| Synthesizing | 100 ticks |
| Total per output | 400 ticks / 400,000 FE |
| Output | 1 `techcraft_additions:loose_strands_of_time` |

The base Loom does not produce `finely_woven_time`.

## Inventory and capabilities

The controller owns two one-slot handlers:

- Output: accepts products internally, rejects external insertion and permits automation extraction.
- Upgrade: accepts one `techcraft_additions:dimensional_descender` through player interaction and is
  not exposed to automation.

NeoForge block capabilities are exposed on the controller for FE input and output-item extraction.
The structural Energy Conduits and Output Shuttle identify those systems physically; proxy
capabilities on those part blocks are deferred.

Right-click with a Dimensional Descender to install it. Sneak-right-click with an empty hand to
remove it. `TemporalLoomUpgrades.resolve` is the isolated future extension point; the installed
Descender deliberately has no greybox operating effect.

## Saved and synchronized data

The block entity persists:

- formed state;
- machine state;
- current state progress;
- pending blocked output;
- stored FE;
- output inventory;
- installed Descender.

State transitions, inventory changes and validation changes send block updates. Active progress is
synchronized every 20 ticks. The controller `lit` blockstate provides an immediate active texture
and light change. No client-only classes are referenced by server machine code.

## Temporary feedback

Activating the controller reports formation, state, FE, progress, output status and Descender status
in the action bar. Searching, Harvesting and Synthesizing emit progressively denser server-driven
particles around the two counter-rotating loop paths and central singularity.

## Renderer anchor contract

Horizontal coordinates are offsets from the controller centre; vertical coordinates start at the
controller base. Positive local `z` points behind the controller.

| Anchor | Local coordinate |
| --- | --- |
| Left loop centre | `(-2.0, 2.5, 2.0)` |
| Right loop centre | `(2.0, 2.5, 2.0)` |
| Central singularity | `(0.0, 2.5, 2.0)` |
| Upper Descender | `(0.0, 4.5, 2.0)` |
| Lower output | `(0.0, 1.5, 2.0)` |
| Outer anchors | `x=-3/3`, `y=1.5/3.5`, `z=0/4` |

`TemporalLoomAnchors.toWorld` rotates these coordinates with the controller.

## Verification

Commands used:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-24'
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
.\gradlew.bat compileJava --stacktrace
.\gradlew.bat runGameTestServer --stacktrace
.\gradlew.bat build --stacktrace
```

The isolated GameTest server uses Minecraft 1.21.1, NeoForge 21.1.235, Java 21 and the exact installed
Mekanism 10.7.19.85, MekMM 1.4.0 and Deeper and Darker 1.4.1 JARs. Six required tests pass:

1. Formation in all four horizontal orientations.
2. Automatic invalidation after a required block breaks and automatic reformation after replacement.
3. Exact FE consumption and one Loose Strands of Time after a complete cycle.
4. Safe `OUTPUT_BLOCKED` behavior and recovery.
5. NBT round-trip for formation, state, progress, FE and Descender inventory.
6. NeoForge FE and item capability exposure.

The dedicated server starts and stops cleanly. The isolated dependency set reports pre-existing
MekMM missing-item loot warnings and cannot deserialize the Oritech Shattered Heart recipe because
Oritech is intentionally absent from that small runtime. These warnings do not fail the six tests.

The complete graphical Dev-TechCraft client still requires an in-game player pass after deployment;
it was not launched automatically from Gradle because the isolated run does not contain the full
client modpack.

## Known limitations

- Temporary cube models and particles only; no final loops, singularity or gold-fibre renderer.
- Descender installation is persisted but has no functional modifier.
- Energy and output capabilities are hosted on the controller, not proxied through structural parts.
- No GUI, catalyst slot, environmental requirement, redstone control or final recipe balance.
- Output is Loose Strands of Time only.
- No mirrored structure variant.

## Recommended rendering milestone

Implement a client-only Temporal Loom renderer that consumes the stable anchor contract and synced
machine state. Start with two counter-rotating loop meshes and state-scaled gold temporal strands,
then add the central singularity and Descender beam. Keep all geometry generation and animation out
of the block entity and production state machine.
