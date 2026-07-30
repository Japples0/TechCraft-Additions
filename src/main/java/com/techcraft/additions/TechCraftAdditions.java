package com.techcraft.additions;

import com.techcraft.additions.registry.ModBlocks;
import com.techcraft.additions.registry.ModBlockEntities;
import com.techcraft.additions.registry.ModCapabilities;
import com.techcraft.additions.registry.ModChemicals;
import com.techcraft.additions.registry.ModCreativeTabs;
import com.techcraft.additions.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(TechCraftAdditions.MOD_ID)
public final class TechCraftAdditions {
    public static final String MOD_ID = "techcraft_additions";
    public static final Logger LOGGER = LoggerFactory.getLogger("TechCraft Additions");

    public TechCraftAdditions(IEventBus modEventBus) {
        ModChemicals.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        modEventBus.addListener(ModCapabilities::register);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
