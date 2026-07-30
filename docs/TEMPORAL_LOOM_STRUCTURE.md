# Temporal Loom Structure

## Coordinate system

The greybox Temporal Loom occupies a strict **7 wide x 5 high x 5 deep** volume.
The controller is local coordinate `(0, 0, 0)` at the front-centre of the base.

- Columns run from local right `x=-3` to `x=3`.
- Layers run from `y=0` at the base to `y=4` at the top.
- Rows run from `z=0` at the controller face to `z=4` behind it.
- The controller's `facing` points out of the front. Positive local `z` is the opposite direction.
- Every `.` and `X` is required air. The pattern therefore reserves the renderer volume.

## Legend

| Symbol | Registry ID | Purpose |
| --- | --- | --- |
| `C` | `techcraft_additions:temporal_loom_controller` | Front-centre controller |
| `R` | `techcraft_additions:resonance_casing` | Frame and base |
| `A` | `techcraft_additions:temporal_anchor` | Outer loop stabiliser |
| `S` | `techcraft_additions:time_spindle` | Upper central compressor |
| `P` | `techcraft_additions:amethyst_resonator` | Loop resonance node |
| `D` | `techcraft_additions:descender_mount` | Future Descender upgrade mount |
| `O` | `techcraft_additions:output_shuttle` | Output point below the core |
| `E` | `techcraft_additions:energy_conduit` | Identifiable FE input structure |
| `X` | air | Central singularity/renderer position |
| `.` | air | Reserved renderer and access space |

Each diagram is viewed from above. The first row is the front/controller side.

## Layer y=0

```text
RRRCRRR
RREEERR
RRR.RRR
RRRRRRR
RRRRRRR
```

## Layer y=1

```text
A.....A
.RR.RR.
.R.O.R.
.RR.RR.
A.....A
```

## Layer y=2

```text
R.....R
RP...PR
R..X..R
RP...PR
R.....R
```

## Layer y=3

```text
A.....A
.RR.RR.
.R.S.R.
.RR.RR.
A.....A
```

## Layer y=4

```text
.......
..RRR..
..RDR..
..RRR..
.......
```

## Fixed renderer anchors

Anchor coordinates use horizontal offsets from the controller's centre and vertical distance from
the controller's base. Positive local `z` points behind the controller.

| Anchor | Local coordinate |
| --- | --- |
| Left loop centre | `(-2.0, 2.5, 2.0)` |
| Right loop centre | `(2.0, 2.5, 2.0)` |
| Central singularity | `(0.0, 2.5, 2.0)` |
| Upper Descender | `(0.0, 4.5, 2.0)` |
| Lower output | `(0.0, 1.5, 2.0)` |
| Outer anchors | `x=-3/3`, `y=1.5/3.5`, `z=0/4` |

`TemporalLoomAnchors.toWorld` applies the controller's horizontal rotation. These coordinates are
the stable contract for the next rendering milestone.
