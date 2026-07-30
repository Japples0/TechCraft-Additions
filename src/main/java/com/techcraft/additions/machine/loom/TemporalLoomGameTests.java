package com.techcraft.additions.machine.loom;

import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.block.TemporalLoomControllerBlock;
import com.techcraft.additions.registry.ModBlocks;
import com.techcraft.additions.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.capabilities.Capabilities;

@GameTestHolder(TechCraftAdditions.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TemporalLoomGameTests {
    private static final BlockPos CONTROLLER_POS = new BlockPos(7, 1, 7);

    private TemporalLoomGameTests() {
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void formsInEveryHorizontalDirection(GameTestHelper helper) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            clearTestVolume(helper);
            placePattern(helper, direction);
            TemporalLoomPattern.ValidationResult result = TemporalLoomPattern.validate(
                    helper.getLevel(), helper.absolutePos(CONTROLLER_POS), direction);
            helper.assertTrue(result.formed(), "Temporal Loom did not form while facing " + direction);
        }
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void invalidatesAndReforms(GameTestHelper helper) {
        TemporalLoomControllerBlockEntity controller = formedController(helper);
        BlockPos requiredCasing = TemporalLoomPattern.toWorld(CONTROLLER_POS, Direction.NORTH, -3, 0, 0);
        helper.setBlock(requiredCasing, Blocks.AIR);
        helper.runAfterDelay(2, () -> {
            helper.assertFalse(controller.isFormed(), "Broken structure remained formed");
            helper.setBlock(requiredCasing, ModBlocks.RESONANCE_CASING.get());
        });
        helper.runAfterDelay(4, () -> {
            helper.assertTrue(controller.isFormed(), "Repaired structure did not reform");
            helper.succeed();
        });
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void consumesEnergyAndProducesLooseTime(GameTestHelper helper) {
        TemporalLoomControllerBlockEntity controller = formedController(helper);
        helper.assertTrue(controller.lastSuccessfulOutputGameTime() == -1L,
                "Output visual timestamp started armed");
        addEnergy(controller, 500_000);
        for (int tick = 0; tick < 401; tick++) {
            TemporalLoomStateMachine.tick(controller);
        }

        helper.assertTrue(controller.machineState() == TemporalLoomMachineState.DORMANT,
                "Controller did not return to DORMANT");
        helper.assertTrue(controller.energyCapability().getEnergyStored() == 100_000,
                "Expected exactly 400,000 FE to be consumed");
        ItemStack output = controller.outputCapability().getStackInSlot(0);
        helper.assertTrue(output.is(ModItems.LOOSE_STRANDS_OF_TIME.get()) && output.getCount() == 1,
                "Controller did not produce one Loose Strands of Time");
        helper.assertTrue(controller.lastSuccessfulOutputGameTime() == helper.getLevel().getGameTime(),
                "Successful output did not record its server game time");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void blocksSafelyWhenOutputIsFull(GameTestHelper helper) {
        TemporalLoomControllerBlockEntity controller = formedController(helper);
        helper.assertTrue(controller.outputCapability().getStackInSlot(0).isEmpty(), "Output started occupied");
        controller.inventory().insertOutput(new ItemStack(ModItems.LOOSE_STRANDS_OF_TIME.get(), 64));
        TemporalLoomStateMachine.tick(controller);
        helper.assertTrue(controller.machineState() == TemporalLoomMachineState.OUTPUT_BLOCKED,
                "Full output did not block the controller");
        helper.assertTrue(controller.lastSuccessfulOutputGameTime() == -1L,
                "Blocked output incorrectly armed the output visual");

        controller.outputCapability().extractItem(0, 64, false);
        TemporalLoomStateMachine.tick(controller);
        helper.assertTrue(controller.machineState() == TemporalLoomMachineState.DORMANT,
                "Controller did not recover after output was cleared");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void persistsMachineData(GameTestHelper helper) {
        TemporalLoomControllerBlockEntity controller = formedController(helper);
        addEnergy(controller, 250_000);
        controller.inventory().installDescender(new ItemStack(ModItems.DIMENSIONAL_DESCENDER.get()));
        controller.recordSuccessfulOutput();
        TemporalLoomStateMachine.tick(controller);
        for (int tick = 0; tick < 20; tick++) {
            TemporalLoomStateMachine.tick(controller);
        }

        CompoundTag saved = controller.saveCustomOnly(helper.getLevel().registryAccess());
        BlockState state = ModBlocks.TEMPORAL_LOOM_CONTROLLER.get().defaultBlockState()
                .setValue(TemporalLoomControllerBlock.FACING, Direction.NORTH);
        TemporalLoomControllerBlockEntity restored = new TemporalLoomControllerBlockEntity(BlockPos.ZERO, state);
        restored.loadCustomOnly(saved, helper.getLevel().registryAccess());

        helper.assertTrue(restored.isFormed(), "Formed state was not restored");
        helper.assertTrue(restored.machineState() == TemporalLoomMachineState.SEARCHING,
                "Machine state was not restored");
        helper.assertTrue(restored.progress() == 20, "Progress was not restored");
        helper.assertTrue(restored.energyCapability().getEnergyStored() == 230_000,
                "Stored energy was not restored");
        helper.assertTrue(restored.inventory().hasDescender(), "Descender inventory was not restored");
        helper.assertTrue(restored.lastSuccessfulOutputGameTime() == controller.lastSuccessfulOutputGameTime(),
                "Successful output game time was not restored");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 40)
    public static void exposesNeoForgeCapabilities(GameTestHelper helper) {
        formedController(helper);
        BlockPos absoluteController = helper.absolutePos(CONTROLLER_POS);
        var energy = helper.getLevel().getCapability(
                Capabilities.EnergyStorage.BLOCK, absoluteController, Direction.NORTH);
        var output = helper.getLevel().getCapability(
                Capabilities.ItemHandler.BLOCK, absoluteController, Direction.NORTH);
        helper.assertTrue(energy != null && energy.canReceive(), "FE capability was not exposed");
        helper.assertTrue(output != null && output.getSlots() == 1, "Output capability was not exposed");
        helper.succeed();
    }

    private static TemporalLoomControllerBlockEntity formedController(GameTestHelper helper) {
        placePattern(helper, Direction.NORTH);
        TemporalLoomControllerBlockEntity controller = helper.getBlockEntity(CONTROLLER_POS);
        helper.assertTrue(controller.validateStructure().formed(), "Complete Temporal Loom did not form");
        return controller;
    }

    private static void addEnergy(TemporalLoomControllerBlockEntity controller, int amount) {
        int remaining = amount;
        while (remaining > 0) {
            int received = controller.energyCapability().receiveEnergy(remaining, false);
            if (received <= 0) {
                throw new IllegalStateException("Temporal Loom stopped accepting test energy");
            }
            remaining -= received;
        }
    }

    private static void clearTestVolume(GameTestHelper helper) {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 7; y++) {
                for (int z = 0; z < 15; z++) {
                    helper.setBlock(new BlockPos(x, y, z), Blocks.AIR);
                }
            }
        }
    }

    private static void placePattern(GameTestHelper helper, Direction facing) {
        for (int up = 0; up < TemporalLoomPattern.HEIGHT; up++) {
            for (int back = 0; back < TemporalLoomPattern.DEPTH; back++) {
                String row = TemporalLoomPattern.LAYERS[up][back];
                for (int column = 0; column < TemporalLoomPattern.WIDTH; column++) {
                    int right = column - TemporalLoomPattern.WIDTH / 2;
                    char symbol = row.charAt(column);
                    BlockPos pos = TemporalLoomPattern.toWorld(CONTROLLER_POS, facing, right, up, back);
                    BlockState state = stateFor(symbol, facing);
                    helper.setBlock(pos, state);
                }
            }
        }
    }

    private static BlockState stateFor(char symbol, Direction facing) {
        Block block = switch (symbol) {
            case '.', 'X' -> Blocks.AIR;
            case 'C' -> ModBlocks.TEMPORAL_LOOM_CONTROLLER.get();
            case 'R' -> ModBlocks.RESONANCE_CASING.get();
            case 'A' -> ModBlocks.TEMPORAL_ANCHOR.get();
            case 'S' -> ModBlocks.TIME_SPINDLE.get();
            case 'P' -> ModBlocks.AMETHYST_RESONATOR.get();
            case 'D' -> ModBlocks.DESCENDER_MOUNT.get();
            case 'O' -> ModBlocks.OUTPUT_SHUTTLE.get();
            case 'E' -> ModBlocks.ENERGY_CONDUIT.get();
            default -> throw new IllegalArgumentException("Unknown pattern symbol " + symbol);
        };
        BlockState state = block.defaultBlockState();
        return symbol == 'C' ? state.setValue(TemporalLoomControllerBlock.FACING, facing) : state;
    }
}
