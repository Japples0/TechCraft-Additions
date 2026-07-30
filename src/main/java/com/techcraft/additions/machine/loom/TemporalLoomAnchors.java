package com.techcraft.additions.machine.loom;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public final class TemporalLoomAnchors {
    public static final Vec3 LEFT_LOOP_CENTER = local(-2.0, 2.5, 2.5);
    public static final Vec3 RIGHT_LOOP_CENTER = local(2.0, 2.5, 2.5);
    public static final Vec3 CENTRAL_SINGULARITY = local(0.0, 2.5, 2.5);
    public static final Vec3 UPPER_DESCENDER = local(0.0, 4.5, 2.5);
    public static final Vec3 LOWER_OUTPUT = local(0.0, 1.5, 2.5);
    public static final List<Vec3> OUTER_ANCHORS = List.of(
            local(-3.0, 1.5, 0.5), local(3.0, 1.5, 0.5),
            local(-3.0, 1.5, 4.5), local(3.0, 1.5, 4.5),
            local(-3.0, 3.5, 0.5), local(3.0, 3.5, 0.5),
            local(-3.0, 3.5, 4.5), local(3.0, 3.5, 4.5));

    private TemporalLoomAnchors() {
    }

    public static Vec3 toWorld(BlockPos controllerPos, Direction facing, Vec3 local) {
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        return Vec3.atLowerCornerOf(controllerPos)
                .add(0.5, 0.0, 0.5)
                .add(right.getStepX() * local.x + back.getStepX() * local.z, local.y,
                        right.getStepZ() * local.x + back.getStepZ() * local.z);
    }

    private static Vec3 local(double right, double up, double back) {
        return new Vec3(right, up, back);
    }
}
