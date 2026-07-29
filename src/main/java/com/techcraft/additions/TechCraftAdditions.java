package com.techcraft.additions;

import com.techcraft.additions.registry.ModBlocks;
import com.techcraft.additions.registry.ModCreativeTabs;
import com.techcraft.additions.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(TechCraftAdditions.MOD_ID)
public final class TechCraftAdditions {
    public static final String MOD_ID = "techcraft_additions";

    public TechCraftAdditions(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
