package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.machine.loom.TemporalLoomControllerBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TechCraftAdditions.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TemporalLoomControllerBlockEntity>>
            TEMPORAL_LOOM_CONTROLLER = BLOCK_ENTITY_TYPES.register(
                    "temporal_loom_controller",
                    () -> BlockEntityType.Builder.of(
                            TemporalLoomControllerBlockEntity::new,
                            ModBlocks.TEMPORAL_LOOM_CONTROLLER.get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
