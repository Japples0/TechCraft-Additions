package com.techcraft.additions.block;

import com.techcraft.additions.machine.loom.TemporalLoomControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class TemporalLoomValidationNotifier {
    private static final int HORIZONTAL_RANGE = 7;
    private static final int VERTICAL_RANGE = 5;

    private TemporalLoomValidationNotifier() {
    }

    public static void requestNearby(Level level, BlockPos changedPos) {
        if (level.isClientSide) {
            return;
        }
        BlockPos min = changedPos.offset(-HORIZONTAL_RANGE, -VERTICAL_RANGE, -HORIZONTAL_RANGE);
        BlockPos max = changedPos.offset(HORIZONTAL_RANGE, VERTICAL_RANGE, HORIZONTAL_RANGE);
        for (BlockPos candidate : BlockPos.betweenClosed(min, max)) {
            if (level.getBlockEntity(candidate) instanceof TemporalLoomControllerBlockEntity controller) {
                controller.requestValidation();
            }
        }
    }
}
