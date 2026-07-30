"""Generate deterministic runtime materials for the Temporal Loom renderer."""

from __future__ import annotations

import math
from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).parents[1]
OUTPUT = ROOT / "src/main/resources/assets/techcraft_additions/textures/entity/temporal_loom"


def resonance_metal() -> Image.Image:
    image = Image.new("RGBA", (32, 32), (17, 18, 24, 255))
    draw = ImageDraw.Draw(image)
    for offset in (0, 16):
        draw.rectangle((offset, 0, offset + 15, 31), fill=(24, 25, 33, 255))
        draw.line((offset, 1, offset + 15, 1), fill=(61, 59, 68, 255))
        draw.line((offset, 14, offset + 15, 14), fill=(8, 9, 13, 255))
        draw.line((offset, 16, offset + 15, 16), fill=(73, 48, 20, 255))
        draw.line((offset, 17, offset + 15, 17), fill=(36, 27, 21, 255))
        for x, y in ((offset + 2, 3), (offset + 13, 3), (offset + 2, 12), (offset + 13, 12)):
            draw.rectangle((x, y, x + 1, y + 1), fill=(116, 73, 29, 255))
    draw.line((15, 0, 15, 31), fill=(7, 8, 11, 255))
    draw.line((31, 0, 31, 31), fill=(7, 8, 11, 255))
    return image


def amethyst_lining() -> Image.Image:
    image = Image.new("RGBA", (32, 16), (29, 10, 42, 255))
    draw = ImageDraw.Draw(image)
    facets = ((0, 88, 35, 127), (5, 139, 55, 187), (10, 92, 30, 139), (15, 180, 83, 226),
              (20, 105, 43, 157), (25, 215, 129, 255))
    for x, red, green, blue in facets:
        draw.polygon(((x, 8), (x + 3, 1), (x + 6, 8), (x + 3, 14)), fill=(red, green, blue, 255))
        draw.line((x + 3, 2, x + 3, 13), fill=(222, 151, 255, 255))
    draw.line((0, 0, 31, 0), fill=(70, 232, 224, 170))
    draw.line((0, 15, 31, 15), fill=(20, 4, 31, 255))
    return image


def gold_rail() -> Image.Image:
    image = Image.new("RGBA", (32, 8), (77, 43, 11, 255))
    draw = ImageDraw.Draw(image)
    draw.line((0, 0, 31, 0), fill=(142, 82, 19, 255))
    draw.line((0, 7, 31, 7), fill=(45, 24, 8, 255))
    for x in range(32):
        distance = min((x - 8) % 32, (8 - x) % 32)
        if distance <= 3:
            strength = 1.0 - distance / 4.0
            color = (
                int(210 + 45 * strength),
                int(137 + 102 * strength),
                int(35 + 180 * strength),
                255,
            )
        else:
            color = (180, 107, 24, 255)
        draw.line((x, 2, x, 5), fill=color)
    draw.line((0, 3, 31, 3), fill=(255, 221, 115, 255))
    return image


def singularity() -> Image.Image:
    size = 64
    image = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    pixels = image.load()
    centre = (size - 1) / 2.0
    for y in range(size):
        for x in range(size):
            dx = (x - centre) / centre
            dy = (y - centre) / centre
            radius = math.sqrt(dx * dx + dy * dy)
            if radius > 1.0:
                continue
            angle = math.atan2(dy, dx)
            swirl = 0.5 + 0.5 * math.sin(angle * 5.0 - radius * 19.0)
            if radius < 0.46:
                edge = max(0.0, (radius - 0.27) / 0.19)
                pixels[x, y] = (2 + int(8 * edge), 1, 7 + int(13 * edge), 252)
            else:
                corona = max(0.0, 1.0 - abs(radius - 0.60) / 0.35)
                alpha = int((62 + 150 * swirl) * corona)
                pixels[x, y] = (
                    61 + int(86 * swirl),
                    7 + int(27 * swirl),
                    100 + int(122 * swirl),
                    alpha,
                )
    return image


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    materials = {
        "resonance_metal.png": resonance_metal(),
        "amethyst_lining.png": amethyst_lining(),
        "gold_rail.png": gold_rail(),
        "singularity.png": singularity(),
    }
    for name, image in materials.items():
        image.save(OUTPUT / name, optimize=True)


if __name__ == "__main__":
    main()
