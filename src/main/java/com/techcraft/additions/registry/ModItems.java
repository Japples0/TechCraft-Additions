package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TechCraftAdditions.MOD_ID);

    public static final DeferredHolder<Item, Item> SCULK_RESONANCE_ENGINE =
            ITEMS.register("sculk_resonance_engine", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, Item> FINELY_WOVEN_TIME =
            ITEMS.register("finely_woven_time", () -> new Item(new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
