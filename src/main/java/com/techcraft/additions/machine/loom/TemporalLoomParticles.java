package com.techcraft.additions.machine.loom;

import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

public final class TemporalLoomParticles {
    private TemporalLoomParticles() {
    }

    public static void tick(ServerLevel level, TemporalLoomControllerBlockEntity controller) {
        TemporalLoomMachineState state = controller.machineState();
        int interval = switch (state) {
            case SEARCHING -> 8;
            case HARVESTING -> 4;
            case SYNTHESIZING -> 2;
            default -> 0;
        };
        if (interval == 0 || level.getGameTime() % interval != 0) {
            return;
        }

        Direction facing = controller.getBlockState().getValue(com.techcraft.additions.block.TemporalLoomControllerBlock.FACING);
        double phase = (level.getGameTime() % 120L) / 120.0 * Math.PI * 2.0;
        ParticleOptions loopParticle = state == TemporalLoomMachineState.HARVESTING
                ? ParticleTypes.ENCHANT
                : ParticleTypes.WITCH;

        emitLoopPoint(level, controller, facing, TemporalLoomAnchors.LEFT_LOOP_CENTER, phase, loopParticle);
        emitLoopPoint(level, controller, facing, TemporalLoomAnchors.RIGHT_LOOP_CENTER, -phase, loopParticle);

        Vec3 singularity = TemporalLoomAnchors.toWorld(
                controller.getBlockPos(), facing, TemporalLoomAnchors.CENTRAL_SINGULARITY);
        ParticleOptions coreParticle = state == TemporalLoomMachineState.SYNTHESIZING
                ? ParticleTypes.END_ROD
                : ParticleTypes.PORTAL;
        level.sendParticles(coreParticle, singularity.x, singularity.y, singularity.z,
                state == TemporalLoomMachineState.SYNTHESIZING ? 3 : 1, 0.18, 0.18, 0.18, 0.02);
    }

    private static void emitLoopPoint(ServerLevel level, TemporalLoomControllerBlockEntity controller,
                                      Direction facing, Vec3 center, double phase, ParticleOptions particle) {
        Vec3 localPoint = center.add(Math.cos(phase) * 0.95, Math.sin(phase) * 1.15, Math.sin(phase * 2.0) * 0.18);
        Vec3 worldPoint = TemporalLoomAnchors.toWorld(controller.getBlockPos(), facing, localPoint);
        level.sendParticles(particle, worldPoint.x, worldPoint.y, worldPoint.z, 1, 0.04, 0.04, 0.04, 0.0);
    }
}
