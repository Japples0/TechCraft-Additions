package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, TechCraftAdditions.MOD_ID);

    public static final DeferredHolder<Block, Block> RESONANCE_CASING = registerBlock(
            "resonance_casing",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK).strength(3.0F, 6.0F).sound(SoundType.SCULK)));

    public static final DeferredHolder<Block, Block> SCULK_RESONANCE_CONTROLLER = registerBlock(
            "sculk_resonance_controller",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_CATALYST).strength(4.0F, 9.0F).sound(SoundType.SCULK_CATALYST)));

    private ModBlocks() {
    }

    private static DeferredHolder<Block, Block> registerBlock(String name, Supplier<Block> block) {
        DeferredHolder<Block, Block> registeredBlock = BLOCKS.register(name, block);
        ModBlockItems.registerBlockItem(name, registeredBlock);
        return registeredBlock;
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ModBlockItems.register(modEventBus);
    }

    public static final class ModBlockItems {
        private static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(Registries.ITEM, TechCraftAdditions.MOD_ID);

        private ModBlockItems() {
        }

        private static void registerBlockItem(String name, Supplier<? extends Block> block) {
            BLOCK_ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }

        private static void register(IEventBus modEventBus) {
            BLOCK_ITEMS.register(modEventBus);
        }
    }
}
