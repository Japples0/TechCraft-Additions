package com.techcraft.additions.registry;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public final class ModCapabilities {
    private ModCapabilities() {
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.TEMPORAL_LOOM_CONTROLLER.get(),
                (controller, side) -> controller.energyCapability());
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.TEMPORAL_LOOM_CONTROLLER.get(),
                (controller, side) -> controller.outputCapability());
    }
}
