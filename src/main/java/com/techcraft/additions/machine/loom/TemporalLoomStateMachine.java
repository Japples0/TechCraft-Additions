package com.techcraft.additions.machine.loom;

import com.techcraft.additions.registry.ModItems;
import net.minecraft.world.item.ItemStack;

public final class TemporalLoomStateMachine {
    private TemporalLoomStateMachine() {
    }

    public static void tick(TemporalLoomControllerBlockEntity controller) {
        if (!controller.isFormed()) {
            controller.transitionTo(TemporalLoomMachineState.UNFORMED);
            return;
        }

        TemporalLoomOperatingProfile profile = controller.operatingProfile();
        switch (controller.machineState()) {
            case UNFORMED -> controller.transitionTo(TemporalLoomMachineState.DORMANT);
            case DORMANT -> beginCycleIfPossible(controller, profile);
            case SEARCHING -> advance(controller, profile, TemporalLoomMachineState.HARVESTING);
            case HARVESTING -> advance(controller, profile, TemporalLoomMachineState.SYNTHESIZING);
            case SYNTHESIZING -> synthesize(controller, profile);
            case OUTPUT_BLOCKED -> retryBlockedOutput(controller, profile);
        }
    }

    private static void beginCycleIfPossible(TemporalLoomControllerBlockEntity controller,
                                             TemporalLoomOperatingProfile profile) {
        ItemStack result = output(profile);
        if (!controller.inventory().canAcceptOutput(result)) {
            controller.setPendingOutput(false);
            controller.transitionTo(TemporalLoomMachineState.OUTPUT_BLOCKED);
        } else if (controller.energyStored() >= profile.fePerTick()) {
            controller.transitionTo(TemporalLoomMachineState.SEARCHING);
        }
    }

    private static void advance(TemporalLoomControllerBlockEntity controller,
                                TemporalLoomOperatingProfile profile,
                                TemporalLoomMachineState nextState) {
        if (!controller.consumeEnergy(profile.fePerTick())) {
            return;
        }
        if (controller.incrementProgress() >= profile.durationFor(controller.machineState())) {
            controller.transitionTo(nextState);
        }
    }

    private static void synthesize(TemporalLoomControllerBlockEntity controller,
                                   TemporalLoomOperatingProfile profile) {
        if (!controller.consumeEnergy(profile.fePerTick())) {
            return;
        }
        if (controller.incrementProgress() < profile.synthesizingTicks()) {
            return;
        }

        if (controller.inventory().insertOutput(output(profile))) {
            controller.setPendingOutput(false);
            controller.transitionTo(TemporalLoomMachineState.DORMANT);
        } else {
            controller.setPendingOutput(true);
            controller.transitionTo(TemporalLoomMachineState.OUTPUT_BLOCKED);
        }
    }

    private static void retryBlockedOutput(TemporalLoomControllerBlockEntity controller,
                                           TemporalLoomOperatingProfile profile) {
        ItemStack result = output(profile);
        if (controller.hasPendingOutput()) {
            if (controller.inventory().insertOutput(result)) {
                controller.setPendingOutput(false);
                controller.transitionTo(TemporalLoomMachineState.DORMANT);
            }
        } else if (controller.inventory().canAcceptOutput(result)) {
            controller.transitionTo(TemporalLoomMachineState.DORMANT);
        }
    }

    private static ItemStack output(TemporalLoomOperatingProfile profile) {
        return new ItemStack(ModItems.LOOSE_STRANDS_OF_TIME.get(), profile.outputAmount());
    }
}
