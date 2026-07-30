package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.block.TemporalLoomControllerBlock;
import com.techcraft.additions.block.TemporalLoomPartBlock;
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
            () -> new TemporalLoomPartBlock(loomProperties()));

    public static final DeferredHolder<Block, Block> SCULK_RESONANCE_CONTROLLER = registerBlock(
            "sculk_resonance_controller",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_CATALYST).strength(4.0F, 9.0F).sound(SoundType.SCULK_CATALYST)));

    public static final DeferredHolder<Block, TemporalLoomControllerBlock> TEMPORAL_LOOM_CONTROLLER = registerBlock(
            "temporal_loom_controller",
            () -> new TemporalLoomControllerBlock(loomProperties().lightLevel(state -> state.getValue(TemporalLoomControllerBlock.LIT) ? 10 : 2)));

    public static final DeferredHolder<Block, Block> TEMPORAL_ANCHOR = registerLoomPart("temporal_anchor", 5);
    public static final DeferredHolder<Block, Block> TIME_SPINDLE = registerLoomPart("time_spindle", 8);
    public static final DeferredHolder<Block, Block> AMETHYST_RESONATOR = registerLoomPart("amethyst_resonator", 9);
    public static final DeferredHolder<Block, Block> DESCENDER_MOUNT = registerLoomPart("descender_mount", 4);
    public static final DeferredHolder<Block, Block> OUTPUT_SHUTTLE = registerLoomPart("output_shuttle", 7);
    public static final DeferredHolder<Block, Block> ENERGY_CONDUIT = registerLoomPart("energy_conduit", 6);

    private ModBlocks() {
    }

    private static BlockBehaviour.Properties loomProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_TILES)
                .strength(4.0F, 12.0F)
                .sound(SoundType.NETHERITE_BLOCK);
    }

    private static DeferredHolder<Block, Block> registerLoomPart(String name, int lightLevel) {
        return registerBlock(name, () -> new TemporalLoomPartBlock(loomProperties().lightLevel(state -> lightLevel)));
    }

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> registeredBlock = BLOCKS.register(name, block);
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
