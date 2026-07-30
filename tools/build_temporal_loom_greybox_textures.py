"""Generate deterministic 16x16 greybox textures for the Temporal Loom blocks."""

from pathlib import Path

from PIL import Image, ImageDraw


ROOT = Path(__file__).parents[1]
OUTPUT = ROOT / "src/main/resources/assets/techcraft_additions/textures/block"

IRON_DARK = (16, 17, 22, 255)
IRON = (31, 33, 41, 255)
IRON_LIGHT = (57, 59, 68, 255)
BRASS = (126, 84, 30, 255)
GOLD = (232, 171, 61, 255)
PURPLE = (117, 42, 173, 255)
PURPLE_LIGHT = (207, 100, 255, 255)


def base_panel() -> Image.Image:
    image = Image.new("RGBA", (16, 16), IRON)
    draw = ImageDraw.Draw(image)
    draw.rectangle((0, 0, 15, 15), outline=IRON_DARK)
    draw.rectangle((2, 2, 13, 13), outline=IRON_LIGHT)
    draw.line((3, 3, 12, 3), fill=(73, 72, 78, 255))
    for x, y in ((2, 2), (13, 2), (2, 13), (13, 13)):
        draw.point((x, y), fill=BRASS)
    return image


def save(name: str, painter) -> None:
    image = base_panel()
    painter(ImageDraw.Draw(image))
    image.save(OUTPUT / f"{name}.png", optimize=True)


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)

    save("resonance_casing", lambda draw: (
        draw.rectangle((5, 5, 10, 10), fill=IRON_DARK, outline=BRASS),
        draw.rectangle((7, 7, 8, 8), fill=(70, 46, 26, 255))))

    save("temporal_anchor", lambda draw: (
        draw.rectangle((4, 4, 11, 11), fill=IRON_DARK, outline=(76, 67, 84, 255)),
        draw.rectangle((6, 6, 9, 9), fill=PURPLE),
        draw.rectangle((7, 7, 8, 8), fill=PURPLE_LIGHT)))

    save("time_spindle", lambda draw: (
        draw.rectangle((6, 3, 9, 12), fill=BRASS, outline=GOLD),
        draw.rectangle((3, 6, 12, 9), fill=BRASS, outline=GOLD),
        draw.rectangle((7, 7, 8, 8), fill=(255, 232, 153, 255))))

    save("amethyst_resonator", lambda draw: (
        draw.polygon(((8, 3), (11, 7), (9, 12), (6, 12), (4, 7)), fill=PURPLE, outline=PURPLE_LIGHT),
        draw.line((8, 4, 7, 10), fill=(237, 174, 255, 255))))

    save("descender_mount", lambda draw: (
        draw.rectangle((4, 3, 11, 12), fill=IRON_DARK, outline=(82, 75, 88, 255)),
        draw.rectangle((6, 4, 9, 11), fill=PURPLE),
        draw.rectangle((7, 5, 8, 10), fill=PURPLE_LIGHT)))

    save("output_shuttle", lambda draw: (
        draw.rectangle((3, 4, 12, 11), fill=IRON_DARK, outline=BRASS),
        draw.rectangle((5, 6, 10, 10), fill=(105, 65, 22, 255)),
        draw.rectangle((6, 6, 9, 8), fill=GOLD),
        draw.line((6, 9, 9, 9), fill=(255, 225, 139, 255))))

    save("energy_conduit", lambda draw: (
        draw.rectangle((4, 4, 11, 11), fill=IRON_DARK, outline=(91, 83, 98, 255)),
        draw.rectangle((6, 6, 9, 9), fill=PURPLE),
        draw.rectangle((7, 7, 8, 8), fill=(238, 153, 255, 255))))

    save("temporal_loom_controller_side", lambda draw: (
        draw.rectangle((4, 3, 11, 12), fill=IRON_DARK, outline=(77, 62, 88, 255)),
        draw.rectangle((6, 5, 9, 10), fill=(67, 27, 92, 255)),
        draw.line((7, 5, 7, 10), fill=PURPLE)))

    save("temporal_loom_controller_top", lambda draw: (
        draw.rectangle((4, 4, 11, 11), fill=IRON_DARK, outline=PURPLE),
        draw.rectangle((7, 7, 8, 8), fill=PURPLE_LIGHT)))

    save("temporal_loom_controller_front", lambda draw: (
        draw.rectangle((3, 3, 12, 12), fill=IRON_DARK, outline=PURPLE),
        draw.rectangle((5, 5, 10, 10), fill=(45, 18, 61, 255)),
        draw.rectangle((7, 7, 8, 8), fill=PURPLE)))

    save("temporal_loom_controller_front_active", lambda draw: (
        draw.rectangle((3, 3, 12, 12), fill=IRON_DARK, outline=PURPLE_LIGHT),
        draw.rectangle((5, 5, 10, 10), fill=PURPLE),
        draw.rectangle((6, 6, 9, 9), fill=PURPLE_LIGHT),
        draw.rectangle((7, 7, 8, 8), fill=(255, 226, 255, 255))))


if __name__ == "__main__":
    main()
