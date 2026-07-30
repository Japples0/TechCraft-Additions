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

Commands, in-game observations, measured performance and known limitations will be recorded after
implementation and testing.

