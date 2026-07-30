package com.techcraft.additions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** Deferred controller lookup. The block entity implementation supplies the callback in the runtime milestone. */
public final class TemporalLoomValidationNotifier {
    private static final int HORIZONTAL_RANGE = 7;
    private static final int VERTICAL_RANGE = 5;

    private TemporalLoomValidationNotifier() {
    }

    public static void requestNearby(Level level, BlockPos changedPos) {
        // Controller discovery is installed with the block entity runtime. Keeping this
        // event hook on every permanent part avoids changing block classes later.
    }
}
