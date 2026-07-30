"""Build crisp, animated item sprite sheets for the dimensional rift cores."""

from __future__ import annotations

import json
import math
from pathlib import Path

from PIL import Image


FRAME_SIZE = 64
FRAME_COUNT = 16
FRAME_TIME = 3
TEXTURE_DIR = (
    Path(__file__).parents[1]
    / "src/main/resources/assets/techcraft_additions/textures/item"
)
SOURCE_DIR = Path(__file__).parents[1] / "art/rift_core_sources"
CORE_TEXTURES = (
    "dimensional_rift_core",
    "overworld_attuned_rift_core",
    "nether_attuned_rift_core",
    "end_attuned_rift_core",
    "otherside_attuned_rift_core",
    "draconic_attuned_rift_core",
)
VORTEX_PALETTES = {
    "dimensional_rift_core": ((132, 45, 238), (229, 188, 255)),
    "overworld_attuned_rift_core": ((42, 174, 78), (155, 255, 197)),
    "nether_attuned_rift_core": ((222, 44, 8), (255, 208, 39)),
    "end_attuned_rift_core": ((115, 62, 177), (255, 246, 174)),
    "otherside_attuned_rift_core": ((0, 143, 160), (109, 255, 244)),
    "draconic_attuned_rift_core": ((173, 25, 216), (255, 118, 240)),
}


def first_frame(path: Path) -> Image.Image:
    image = Image.open(path).convert("RGBA")
    if image.width != FRAME_SIZE or image.height < FRAME_SIZE:
        raise ValueError(f"{path.name} must begin with a {FRAME_SIZE}x{FRAME_SIZE} frame")
    return image.crop((0, 0, FRAME_SIZE, FRAME_SIZE))


def clean_alpha(image: Image.Image) -> Image.Image:
    """Remove chroma-key fringe while retaining a narrow intentional glow."""
    cleaned = Image.new("RGBA", image.size)
    source = image.load()
    target = cleaned.load()

    for y in range(image.height):
        for x in range(image.width):
            red, green, blue, alpha = source[x, y]
            if alpha < 72:
                target[x, y] = (0, 0, 0, 0)
            elif alpha < 144:
                target[x, y] = (red, green, blue, 176)
            else:
                target[x, y] = (red, green, blue, 255)

    return cleaned


def restore_overworld_palette(image: Image.Image) -> Image.Image:
    """Restore greens lost when the original green background was keyed out."""
    restored = image.copy()
    pixels = restored.load()

    for y in range(7, 58):
        for x in range(13, 52):
            red, green, blue, alpha = pixels[x, y]
            if alpha == 0:
                continue

            dx = (x - 32.0) / 18.0
            dy = (y - 32.0) / 29.0
            if dx * dx + dy * dy > 1.05:
                continue

            # Preserve the cyan crystal caps and the central dimensional field.
            if blue > red * 1.22 and blue > green * 1.05:
                continue
            if teardrop_weight(x, y) > 0.0:
                continue

            luminance = (red * 3 + green * 5 + blue * 2) / 10
            saturation = max(red, green, blue) - min(red, green, blue)
            strength = 0.72 if saturation < 55 else 0.52
            target = (
                int(12 + luminance * 0.34),
                int(28 + luminance * 0.72),
                int(19 + luminance * 0.31),
            )
            pixels[x, y] = tuple(
                max(0, min(255, round(channel * (1 - strength) + tint * strength)))
                for channel, tint in zip((red, green, blue), target)
            ) + (alpha,)

    return restored


def teardrop_weight(x: int, y: int) -> float:
    """Return a feathered weight for the fixed central teardrop silhouette."""
    vertical = (y - 31.0) / 18.0
    if abs(vertical) >= 1.0:
        return 0.0

    # A slightly fuller upper half and a sharper lower tip match the source art.
    profile = (1.0 - abs(vertical)) ** 0.42
    if vertical > 0:
        profile *= 1.0 - vertical * 0.18
    half_width = 7.4 * profile
    distance = abs(x - 31.5)
    if distance >= half_width:
        return 0.0
    return min(1.0, (half_width - distance) / 1.35)


def animate_frame(base: Image.Image, frame_index: int, name: str) -> Image.Image:
    frame = base.copy()
    source = base.load()
    target = frame.load()
    rotation = math.tau * frame_index / FRAME_COUNT
    primary, highlight = VORTEX_PALETTES[name]

    for y in range(13, 50):
        for x in range(22, 42):
            mask = teardrop_weight(x, y)
            red, green, blue, alpha = source[x, y]
            if mask == 0.0:
                continue

            if alpha == 0:
                if name != "overworld_attuned_rift_core":
                    continue
                # The original Overworld chroma key removed real portal pixels.
                # Reconstruct the fixed teardrop field before animating it.
                red, green, blue = tuple(round(channel * 0.28) for channel in primary)
                alpha = 255

            dx = (x - 31.5) / 7.4
            dy = (y - 31.0) / 18.0
            radius = min(1.0, math.sqrt(dx * dx + dy * dy))
            angle = math.atan2(dy, dx)
            spiral = math.sin((angle + radius * 4.4 - rotation) * 2.0)
            pulse = 0.5 + 0.5 * spiral
            inward = 1.0 - radius

            # Bright arms rotate across a darker field, making the inward spiral
            # legible while preserving the source texture beneath it.
            band = pulse**2.15
            radial_glint = 0.5 + 0.5 * math.sin(radius * 18.0 - rotation * 0.45)
            band = min(1.0, band * 0.84 + radial_glint * inward * 0.16)
            shadow = tuple(round(channel * (0.24 + radius * 0.16)) for channel in primary)
            bright = tuple(
                round(channel * (1.0 - inward * 0.34) + gleam * inward * 0.34)
                for channel, gleam in zip(primary, highlight)
            )
            animated = tuple(
                round(dark * (1.0 - band) + light * band)
                for dark, light in zip(shadow, bright)
            )
            blend = mask * (0.66 + inward * 0.24)
            target[x, y] = tuple(
                round(channel * (1 - blend) + shifted * blend)
                for channel, shifted in zip((red, green, blue), animated)
            ) + (alpha,)

    # A stable dark focal pixel makes the vortex read as depth instead of a pulse.
    center = target[32, 31]
    target[32, 31] = (center[0] // 3, center[1] // 3, center[2] // 3, center[3])
    return frame


def write_animation(name: str) -> None:
    path = TEXTURE_DIR / f"{name}.png"
    base = clean_alpha(first_frame(SOURCE_DIR / f"{name}.png"))
    if name == "overworld_attuned_rift_core":
        base = restore_overworld_palette(base)

    sheet = Image.new("RGBA", (FRAME_SIZE, FRAME_SIZE * FRAME_COUNT))
    for frame_index in range(FRAME_COUNT):
        sheet.paste(animate_frame(base, frame_index, name), (0, frame_index * FRAME_SIZE))
    sheet.save(path, optimize=True)

    metadata = {
        "animation": {
            "frametime": FRAME_TIME,
            "interpolate": True,
        }
    }
    path.with_suffix(".png.mcmeta").write_text(
        json.dumps(metadata, indent=2) + "\n", encoding="ascii"
    )


def main() -> None:
    for name in CORE_TEXTURES:
        write_animation(name)


if __name__ == "__main__":
    main()
