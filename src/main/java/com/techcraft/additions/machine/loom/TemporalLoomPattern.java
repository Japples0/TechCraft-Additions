package com.techcraft.additions.machine.loom;

import com.techcraft.additions.registry.ModBlocks;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class TemporalLoomPattern {
    public static final int WIDTH = 7;
    public static final int HEIGHT = 5;
    public static final int DEPTH = 5;

    // Rows run front-to-back. Columns run left-to-right when looking at the controller face.
    public static final String[][] LAYERS = {
        {
            "RRRCRRR",
            "RREEERR",
            "RRR.RRR",
            "RRRRRRR",
            "RRRRRRR"
        },
        {
            "A.....A",
            ".RR.RR.",
            ".R.O.R.",
            ".RR.RR.",
            "A.....A"
        },
        {
            "R.....R",
            "RP...PR",
            "R..X..R",
            "RP...PR",
            "R.....R"
        },
        {
            "A.....A",
            ".RR.RR.",
            ".R.S.R.",
            ".RR.RR.",
            "A.....A"
        },
        {
            ".......",
            "..RRR..",
            "..RDR..",
            "..RRR..",
            "......."
        }
    };

    private TemporalLoomPattern() {
    }

    public static ValidationResult validate(LevelReader level, BlockPos controllerPos, Direction facing) {
        for (int up = 0; up < HEIGHT; up++) {
            for (int back = 0; back < DEPTH; back++) {
                String row = LAYERS[up][back];
                for (int column = 0; column < WIDTH; column++) {
                    int right = column - WIDTH / 2;
                    char symbol = row.charAt(column);
                    BlockPos worldPos = toWorld(controllerPos, facing, right, up, back);
                    BlockState state = level.getBlockState(worldPos);
                    if (!matches(symbol, state)) {
                        return ValidationResult.failure(worldPos, symbol, expectedName(symbol), state.getBlock());
                    }
                }
            }
        }
        return ValidationResult.success();
    }

    public static BlockPos toWorld(BlockPos controllerPos, Direction facing, int right, int up, int back) {
        Direction rightDirection = facing.getClockWise();
        Direction backDirection = facing.getOpposite();
        return controllerPos
                .relative(rightDirection, right)
                .relative(backDirection, back)
                .above(up);
    }

    private static boolean matches(char symbol, BlockState state) {
        return switch (symbol) {
            case '.', 'X' -> state.isAir();
            case 'C' -> state.is(ModBlocks.TEMPORAL_LOOM_CONTROLLER.get());
            case 'R' -> state.is(ModBlocks.RESONANCE_CASING.get());
            case 'A' -> state.is(ModBlocks.TEMPORAL_ANCHOR.get());
            case 'S' -> state.is(ModBlocks.TIME_SPINDLE.get());
            case 'P' -> state.is(ModBlocks.AMETHYST_RESONATOR.get());
            case 'D' -> state.is(ModBlocks.DESCENDER_MOUNT.get());
            case 'O' -> state.is(ModBlocks.OUTPUT_SHUTTLE.get());
            case 'E' -> state.is(ModBlocks.ENERGY_CONDUIT.get());
            default -> throw new IllegalArgumentException("Unknown Temporal Loom pattern symbol: " + symbol);
        };
    }

    public static String expectedName(char symbol) {
        return switch (symbol) {
            case '.', 'X' -> "air";
            case 'C' -> "temporal_loom_controller";
            case 'R' -> "resonance_casing";
            case 'A' -> "temporal_anchor";
            case 'S' -> "time_spindle";
            case 'P' -> "amethyst_resonator";
            case 'D' -> "descender_mount";
            case 'O' -> "output_shuttle";
            case 'E' -> "energy_conduit";
            default -> "unknown";
        };
    }

    public record ValidationResult(boolean formed, BlockPos mismatchPos, char expectedSymbol,
                                   String expectedName, Block foundBlock) {
        public static ValidationResult success() {
            return new ValidationResult(true, BlockPos.ZERO, ' ', "", Blocks.AIR);
        }

        public static ValidationResult failure(BlockPos pos, char symbol, String name, Block found) {
            return new ValidationResult(false, pos.immutable(), symbol, name, found);
        }
    }
}
