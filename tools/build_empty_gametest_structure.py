"""Write the empty 15x7x15 GameTest structure used by Temporal Loom tests."""

from __future__ import annotations

import gzip
import struct
from pathlib import Path


OUTPUT = (
    Path(__file__).parents[1]
    / "src/main/resources/data/techcraft_additions/structure/empty.nbt"
)


def name(value: str) -> bytes:
    encoded = value.encode("utf-8")
    return struct.pack(">H", len(encoded)) + encoded


def named_int(key: str, value: int) -> bytes:
    return b"\x03" + name(key) + struct.pack(">i", value)


def named_string(key: str, value: str) -> bytes:
    encoded = value.encode("utf-8")
    return b"\x08" + name(key) + struct.pack(">H", len(encoded)) + encoded


def named_int_list(key: str, values: list[int]) -> bytes:
    return b"\x09" + name(key) + b"\x03" + struct.pack(">i", len(values)) + b"".join(
        struct.pack(">i", value) for value in values
    )


def named_compound_list(key: str, compounds: list[bytes]) -> bytes:
    return b"\x09" + name(key) + b"\x0a" + struct.pack(">i", len(compounds)) + b"".join(
        compound + b"\x00" for compound in compounds
    )


def main() -> None:
    root = (
        b"\x0a\x00\x00"
        + named_int("DataVersion", 3955)
        + named_int_list("size", [15, 7, 15])
        + named_compound_list("palette", [named_string("Name", "minecraft:air")])
        + named_compound_list("blocks", [])
        + named_compound_list("entities", [])
        + b"\x00"
    )
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    with gzip.GzipFile(filename=OUTPUT, mode="wb", mtime=0) as target:
        target.write(root)


if __name__ == "__main__":
    main()
