package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TechCraftAdditions.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TECHCRAFT_ADDITIONS = CREATIVE_TABS.register(
            "techcraft_additions",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.techcraft_additions"))
                    .icon(() -> new ItemStack(ModItems.SCULK_RESONANCE_ENGINE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DARK_WORLD_ENGINE_TABLET.get());
                        output.accept(ModItems.ILLUMINATED_WORLD_ENGINE_TABLET.get());
                        output.accept(ModItems.SHATTERED_HEART.get());
                        output.accept(ModItems.SPLINTERED_ECHO.get());
                        output.accept(ModItems.DIMENSIONAL_RIFT.get());
                        output.accept(ModItems.OVERWORLD_ATTUNED_RIFT.get());
                        output.accept(ModItems.NETHER_ATTUNED_RIFT.get());
                        output.accept(ModItems.END_ATTUNED_RIFT.get());
                        output.accept(ModItems.OTHERSIDE_ATTUNED_RIFT.get());
                        output.accept(ModItems.DRACONIC_ATTUNED_RIFT.get());
                        output.accept(ModItems.DIMENSIONAL_RIFT_CORE.get());
                        output.accept(ModItems.OVERWORLD_ATTUNED_RIFT_CORE.get());
                        output.accept(ModItems.NETHER_ATTUNED_RIFT_CORE.get());
                        output.accept(ModItems.END_ATTUNED_RIFT_CORE.get());
                        output.accept(ModItems.OTHERSIDE_ATTUNED_RIFT_CORE.get());
                        output.accept(ModItems.DRACONIC_ATTUNED_RIFT_CORE.get());
                        output.accept(ModItems.TERRESTRIAL_LATTICE.get());
                        output.accept(ModItems.OTHERWORLDLY_FRAGMENT.get());
                        output.accept(ModItems.DIMENSIONAL_DESCENDER.get());
                        output.accept(ModItems.LOOSE_STRANDS_OF_TIME.get());
                        output.accept(ModItems.FINELY_WOVEN_TIME.get());
                        output.accept(ModItems.UNIVERSE_TETHER.get());
                        output.accept(ModItems.SCULK_RESONANCE_ENGINE.get());
                        output.accept(ModItems.ECHOES_OF_TOMORROW.get());
                        output.accept(ModBlocks.RESONANCE_CASING.get());
                        output.accept(ModBlocks.SCULK_RESONANCE_CONTROLLER.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
}
