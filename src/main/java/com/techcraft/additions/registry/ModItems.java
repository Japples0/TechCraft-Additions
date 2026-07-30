package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.item.LoreItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, TechCraftAdditions.MOD_ID);

    public static final DeferredHolder<Item, Item> DARK_WORLD_ENGINE_TABLET =
            registerUnstackableLoreItem("dark_world_engine_tablet");

    public static final DeferredHolder<Item, Item> ILLUMINATED_WORLD_ENGINE_TABLET =
            registerUnstackableLoreItem("illuminated_world_engine_tablet");

    public static final DeferredHolder<Item, Item> SHATTERED_HEART =
            registerLoreItem("shattered_heart");

    public static final DeferredHolder<Item, Item> SPLINTERED_ECHO =
            registerLoreItem("splintered_echo");

    public static final DeferredHolder<Item, Item> DIMENSIONAL_RIFT =
            registerLoreItem("dimensional_rift");

    public static final DeferredHolder<Item, Item> OVERWORLD_ATTUNED_RIFT =
            registerLoreItem("overworld_attuned_rift");

    public static final DeferredHolder<Item, Item> NETHER_ATTUNED_RIFT =
            registerLoreItem("nether_attuned_rift");

    public static final DeferredHolder<Item, Item> END_ATTUNED_RIFT =
            registerLoreItem("end_attuned_rift");

    public static final DeferredHolder<Item, Item> OTHERSIDE_ATTUNED_RIFT =
            registerLoreItem("otherside_attuned_rift");

    public static final DeferredHolder<Item, Item> DRACONIC_ATTUNED_RIFT =
            registerLoreItem("draconic_attuned_rift");

    public static final DeferredHolder<Item, Item> DIMENSIONAL_RIFT_CORE =
            registerLoreItem("dimensional_rift_core");

    public static final DeferredHolder<Item, Item> OVERWORLD_ATTUNED_RIFT_CORE =
            registerLoreItem("overworld_attuned_rift_core");

    public static final DeferredHolder<Item, Item> NETHER_ATTUNED_RIFT_CORE =
            registerLoreItem("nether_attuned_rift_core");

    public static final DeferredHolder<Item, Item> END_ATTUNED_RIFT_CORE =
            registerLoreItem("end_attuned_rift_core");

    public static final DeferredHolder<Item, Item> OTHERSIDE_ATTUNED_RIFT_CORE =
            registerLoreItem("otherside_attuned_rift_core");

    public static final DeferredHolder<Item, Item> DRACONIC_ATTUNED_RIFT_CORE =
            registerLoreItem("draconic_attuned_rift_core");

    public static final DeferredHolder<Item, Item> TERRESTRIAL_LATTICE =
            registerLoreItem("terrestrial_lattice");

    public static final DeferredHolder<Item, Item> OTHERWORLDLY_FRAGMENT =
            registerLoreItem("otherworldly_fragment");

    public static final DeferredHolder<Item, Item> DIMENSIONAL_DESCENDER =
            registerUnstackableLoreItem("dimensional_descender");

    public static final DeferredHolder<Item, Item> LOOSE_STRANDS_OF_TIME =
            registerLoreItem("loose_strands_of_time");

    public static final DeferredHolder<Item, Item> UNIVERSE_TETHER =
            registerUnstackableLoreItem("universe_tether");

    public static final DeferredHolder<Item, Item> ECHOES_OF_TOMORROW =
            registerUnstackableLoreItem("echoes_of_tomorrow");

    public static final DeferredHolder<Item, Item> SCULK_RESONANCE_ENGINE =
            registerUnstackableLoreItem("sculk_resonance_engine");

    public static final DeferredHolder<Item, Item> FINELY_WOVEN_TIME =
            registerLoreItem("finely_woven_time");

    private ModItems() {
    }

    private static DeferredHolder<Item, Item> registerLoreItem(String name) {
        return registerLoreItem(name, new Item.Properties());
    }

    private static DeferredHolder<Item, Item> registerUnstackableLoreItem(String name) {
        return registerLoreItem(name, new Item.Properties().stacksTo(1));
    }

    private static DeferredHolder<Item, Item> registerLoreItem(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new LoreItem(properties, "item.techcraft_additions." + name + ".lore"));
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
