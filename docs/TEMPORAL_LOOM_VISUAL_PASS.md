# Temporal Loom Visual Pass

## Milestone scope

This milestone adds the first polished formed-machine presentation to the working 7x5x5 Temporal
Loom. The placed blocks remain the construction, validation and repair interface. Once the server
has validated the pattern, a client-only block entity renderer layers the infinity loops,
singularity, temporal strands, Descender and output sequence over those blocks.

The renderer never forms the machine, searches the world, consumes energy, owns inventory or
advances production. Breaking the structure changes the synchronized `formed` field and removes all
dynamic geometry immediately.

## Rendering architecture

The implementation uses a procedural hybrid rather than Blockbench:

- Ordinary block models preserve the readable construction view and the existing greybox charm.
- `TemporalLoomRenderer` owns state-driven composition and render-distance checks.
- `TemporalLoomGeometry` emits the reusable procedural band, prism, billboard and ribbon shapes.
- Fixed ellipse samples are calculated once and reused by every Loom and frame.
- Runtime textures are generated reproducibly by `tools/build_temporal_loom_render_textures.py`.
- Existing server particles remain the only particle-spawning path. The renderer emits buffered
  geometry and does not spawn client particles.

No `.bbmodel` is used in this pass. Procedural source is the editable source of truth because the
loops require continuous counter-motion, state-driven highlights and orientation-independent
alignment. A future Blockbench model may be introduced for a genuinely static component without
replacing the procedural loops.

## Dynamic components

The formed renderer is composed in this order:

1. Two dark resonance-metal elliptical frames around the stable left and right loop anchors.
2. Purple amethyst lining on each inner radius, scrolling in opposite directions.
3. Thin gold temporal rails and state-driven travelling highlights.
4. A layered camera-facing black-violet singularity with two counter-rotating corona planes.
5. Seeded gold-white ribbons entering from beyond the machine and curving through the loops.
6. An optional procedural Descender cage and purple core beam when the upgrade is installed.
7. A synthesis beam and successful-output thread routed to the lower output anchor.

All world alignment is derived from the controller facing. No orientation-specific assets are
duplicated.

## Anchor contract

Coordinates are offsets from the controller centre. Positive local `z` points behind the
controller. The renderer consumes the existing constants in `TemporalLoomAnchors`:

| Anchor | Local coordinate |
| --- | --- |
| Left loop centre | `(-2.0, 2.5, 2.0)` |
| Right loop centre | `(2.0, 2.5, 2.0)` |
| Central singularity | `(0.0, 2.5, 2.0)` |
| Upper Descender | `(0.0, 4.5, 2.0)` |
| Lower output | `(0.0, 1.5, 2.0)` |

## State mapping

| Machine state | Visual behaviour |
| --- | --- |
| `UNFORMED` | Dynamic presentation disabled. |
| `DORMANT` | Stable frames, nearly still lining, dim slow singularity pulse, no long strands. |
| `SEARCHING` | Slow counter-motion, three seeded distant strands and a searching core pulse. |
| `HARVESTING` | Fast opposing motion, ten incoming strands, bright gold rails and stronger corona. |
| `SYNTHESIZING` | Loops slow toward lock, eight strands collapse into the core, white-gold compression pulse and output beam. |
| `OUTPUT_BLOCKED` | Stalled motion, backed-up short strands and an amber-red warning flicker. |
| Successful output | One synchronized woven thread travels from the singularity to the output shuttle and decays cleanly. |

The successful-output animation is keyed from a server-authored game time recorded only after the
output inventory accepts the item. It is not inferred from a state transition.

## Materials

Runtime textures live under `assets/techcraft_additions/textures/entity/temporal_loom/`:

- `resonance_metal.png`: charcoal-gunmetal plates with restrained bronze seams.
- `amethyst_lining.png`: faceted sculk-violet inner resonance track.
- `gold_rail.png`: gold-white moving temporal filament.
- `singularity.png`: transparent black-violet radial core and corona.

The metal pass uses world light. Amethyst, temporal filaments, beams and the corona use vanilla
full-bright translucent/emissive render types. Shaders are not required.

## Performance contract

- Maximum configured strands: 10.
- Maximum ribbon segments per strand: 12.
- Dormant/searching/harvesting/synthesizing/blocked strand counts: 0/3/10/8/4.
- Renderer view distance: 48 blocks.
- Fixed loop samples are cached statically; no trigonometric tables grow per frame.
- Strand seeds are static and deterministic from the controller position.
- No renderer-driven network traffic, structure scans, entities or block entities.
- No client particle spawning; existing bounded server particle intervals remain authoritative.
- The custom render bounding box covers the machine and incoming-strand envelope for frustum culling.

## Sound

Sound is deferred from this pass. Adding loosely synchronized ambient loops would dilute the visual
and performance verification. A later sound milestone should use transition-triggered, distance-
attenuated events with an explicit multi-Loom volume cap.

## Verification

Commands run:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-24'
$env:TECHCRAFT_DEV_INSTANCE='D:\profiles\Dev-TechCraft'
python tools\build_temporal_loom_render_textures.py
.\gradlew.bat compileJava processResources --stacktrace
.\gradlew.bat runGameTestServer --stacktrace
.\gradlew.bat clean build --stacktrace
.\gradlew.bat runClient --stacktrace
```

Verified results:

- Clean production build succeeds for Minecraft 1.21.1 and NeoForge 21.1.235.
- The dedicated GameTest server starts on Java 21.0.11 and all six required tests pass.
- The successful-output test proves the visual timestamp is written only after item insertion.
- The output-full test proves blocked production does not arm the output animation.
- A dedicated server starts with the client renderer present, proving distribution isolation.
- The isolated client reaches complete resource initialization with no TechCraft Additions renderer,
  model, texture or class-linkage errors.
- Generated and deployed JAR SHA-256 values match.
- The prior Dev-TechCraft JAR is backed up before deployment.

The full TechCraft in-world visual pass remains a manual checkpoint. The build is deployed to
`D:\profiles\Dev-TechCraft\mods\techcraft-additions-0.1.0.jar`, but no existing development save was
opened automatically. Orientation, active-state appearance, shader compatibility and measured FPS
must be assessed around a formed Loom in a disposable world.

## Performance observations

The implemented budget is bounded rather than inferred from frame rate:

- Dormant Looms draw the two cached-sample rings and singularity but no ribbon strands.
- Harvesting is the maximum load at 10 strands, 12 base segments and at most three highlight
  segments per strand.
- The renderer returns before geometry submission when the Loom is unformed or farther than 48
  blocks from the camera.
- No collections, geometry caches or event maps grow while the renderer runs.
- State profiles, loop samples, strand seeds, textures and render types are static and shared.

One-Loom and two-Loom FPS deltas are not yet measured and are therefore not claimed.

## Known visual limitations

- The ordinary construction blocks remain visible by design; this pass layers the formed
  presentation through and around them rather than replacing their baked models.
- The singularity uses two animated billboard layers instead of a shader-distorted sphere.
- Temporal strands use bounded translucent ribbons and may need width tuning with the full shader
  stack.
- State changes are immediate; cross-fades can be added after the first in-world composition pass.
- Sound is deferred.
- No measured two-Loom performance result exists until the full-pack in-world pass is completed.

## Recommended second polish pass

Use front, quarter-angle and side screenshots from the deployed full pack to tune ring radii,
physical-block intersections, strand curvature, singularity scale and brightness. Measure dormant,
one-active and two-active FPS with shaders disabled and enabled before adding state cross-fades or
transition-triggered sound.
