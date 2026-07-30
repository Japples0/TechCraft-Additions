package com.techcraft.additions.machine.loom;

import com.techcraft.additions.block.TemporalLoomControllerBlock;
import com.techcraft.additions.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

public final class TemporalLoomControllerBlockEntity extends BlockEntity {
    public static final int ENERGY_CAPACITY = 4_000_000;
    public static final int MAX_ENERGY_RECEIVE = 100_000;

    private static final String FORMED_TAG = "Formed";
    private static final String STATE_TAG = "MachineState";
    private static final String PROGRESS_TAG = "Progress";
    private static final String PENDING_OUTPUT_TAG = "PendingOutput";
    private static final String LAST_SUCCESSFUL_OUTPUT_TAG = "LastSuccessfulOutputGameTime";
    private static final String ENERGY_TAG = "Energy";
    private static final String INVENTORY_TAG = "Inventory";

    private final ControllerEnergyStorage energy = new ControllerEnergyStorage();
    private final TemporalLoomInventory inventory = new TemporalLoomInventory(this::inventoryChanged);

    private boolean formed;
    private boolean validationRequested = true;
    private boolean pendingOutput;
    private long lastSuccessfulOutputGameTime = -1L;
    private int progress;
    private TemporalLoomMachineState machineState = TemporalLoomMachineState.UNFORMED;
    private TemporalLoomPattern.ValidationResult lastValidation =
            TemporalLoomPattern.ValidationResult.failure(BlockPos.ZERO, 'C', "unvalidated", net.minecraft.world.level.block.Blocks.AIR);

    public TemporalLoomControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TEMPORAL_LOOM_CONTROLLER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
                                  TemporalLoomControllerBlockEntity controller) {
        if (controller.validationRequested) {
            controller.validateStructure();
        }
        TemporalLoomStateMachine.tick(controller);
        if (level instanceof ServerLevel serverLevel) {
            TemporalLoomParticles.tick(serverLevel, controller);
            if (level.getGameTime() % 20L == 0L && controller.isActive()) {
                controller.syncToClients();
            }
        }
    }

    public void requestValidation() {
        validationRequested = true;
    }

    public TemporalLoomPattern.ValidationResult validateStructure() {
        if (level == null || level.isClientSide) {
            return lastValidation;
        }
        validationRequested = false;
        lastValidation = TemporalLoomPattern.validate(
                level, worldPosition, getBlockState().getValue(TemporalLoomControllerBlock.FACING));
        boolean wasFormed = formed;
        formed = lastValidation.formed();
        if (!formed) {
            progress = 0;
            pendingOutput = false;
            transitionTo(TemporalLoomMachineState.UNFORMED);
        } else if (!wasFormed || machineState == TemporalLoomMachineState.UNFORMED) {
            transitionTo(TemporalLoomMachineState.DORMANT);
        } else {
            markChangedAndSync();
        }
        return lastValidation;
    }

    public void showDiagnostics(Player player) {
        TemporalLoomPattern.ValidationResult validation = validateStructure();
        if (!validation.formed()) {
            String found = BuiltInRegistries.BLOCK.getKey(validation.foundBlock()).toString();
            player.displayClientMessage(Component.translatable(
                    "message.techcraft_additions.temporal_loom.invalid",
                    validation.mismatchPos().toShortString(), validation.expectedName(), found), true);
            return;
        }

        TemporalLoomOperatingProfile profile = operatingProfile();
        int duration = profile.durationFor(machineState);
        int percent = duration == 0 ? 0 : Math.min(100, progress * 100 / duration);
        String outputStatus = machineState == TemporalLoomMachineState.OUTPUT_BLOCKED
                ? "blocked"
                : inventory.outputStack().isEmpty() ? "empty" : inventory.outputStack().getCount() + "/64";
        player.displayClientMessage(Component.translatable(
                "message.techcraft_additions.temporal_loom.status",
                yesNo(formed),
                Component.translatable("state.techcraft_additions.temporal_loom." + machineState.serializedName()),
                energy.getEnergyStored(), energy.getMaxEnergyStored(),
                percent, outputStatus, yesNo(inventory.hasDescender())), true);
    }

    public boolean installDescender(Player player, ItemStack heldStack) {
        if (!inventory.installDescender(heldStack)) {
            return false;
        }
        if (!player.getAbilities().instabuild) {
            heldStack.shrink(1);
        }
        player.displayClientMessage(Component.translatable(
                "message.techcraft_additions.temporal_loom.descender_installed"), true);
        markChangedAndSync();
        return true;
    }

    public boolean removeDescender(Player player) {
        ItemStack removed = inventory.removeDescender();
        if (removed.isEmpty()) {
            return false;
        }
        if (!player.addItem(removed)) {
            player.drop(removed, false);
        }
        player.displayClientMessage(Component.translatable(
                "message.techcraft_additions.temporal_loom.descender_removed"), true);
        markChangedAndSync();
        return true;
    }

    public void dropContents() {
        if (level == null || level.isClientSide) {
            return;
        }
        inventory.dropAll(stack -> Containers.dropItemStack(
                level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5, stack));
    }

    public IEnergyStorage energyCapability() {
        return energy;
    }

    public IItemHandler outputCapability() {
        return inventory.outputCapability();
    }

    public boolean isFormed() {
        return formed;
    }

    public TemporalLoomMachineState machineState() {
        return machineState;
    }

    public int progress() {
        return progress;
    }

    public int currentStateDuration() {
        return operatingProfile().durationFor(machineState);
    }

    public boolean hasDescender() {
        return inventory.hasDescender();
    }

    public long lastSuccessfulOutputGameTime() {
        return lastSuccessfulOutputGameTime;
    }

    TemporalLoomInventory inventory() {
        return inventory;
    }

    TemporalLoomOperatingProfile operatingProfile() {
        return TemporalLoomUpgrades.resolve(inventory.installedDescender());
    }

    int energyStored() {
        return energy.getEnergyStored();
    }

    boolean consumeEnergy(int amount) {
        return energy.consume(amount);
    }

    int incrementProgress() {
        progress++;
        setChanged();
        return progress;
    }

    boolean hasPendingOutput() {
        return pendingOutput;
    }

    void setPendingOutput(boolean pendingOutput) {
        this.pendingOutput = pendingOutput;
        setChanged();
    }

    void recordSuccessfulOutput() {
        if (level != null) {
            lastSuccessfulOutputGameTime = level.getGameTime();
            setChanged();
        }
    }

    void transitionTo(TemporalLoomMachineState newState) {
        if (machineState == newState) {
            return;
        }
        machineState = newState;
        progress = 0;
        updateLitState();
        markChangedAndSync();
    }

    private boolean isActive() {
        return machineState == TemporalLoomMachineState.SEARCHING
                || machineState == TemporalLoomMachineState.HARVESTING
                || machineState == TemporalLoomMachineState.SYNTHESIZING;
    }

    private static Component yesNo(boolean value) {
        return Component.translatable(value ? "gui.yes" : "gui.no");
    }

    private void updateLitState() {
        if (level == null) {
            return;
        }
        BlockState state = getBlockState();
        if (state.hasProperty(TemporalLoomControllerBlock.LIT)
                && state.getValue(TemporalLoomControllerBlock.LIT) != isActive()) {
            level.setBlock(worldPosition, state.setValue(TemporalLoomControllerBlock.LIT, isActive()), Block.UPDATE_CLIENTS);
        }
    }

    private void inventoryChanged() {
        markChangedAndSync();
    }

    private void markChangedAndSync() {
        setChanged();
        syncToClients();
    }

    private void syncToClients() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putBoolean(FORMED_TAG, formed);
        tag.putString(STATE_TAG, machineState.serializedName());
        tag.putInt(PROGRESS_TAG, progress);
        tag.putBoolean(PENDING_OUTPUT_TAG, pendingOutput);
        tag.putLong(LAST_SUCCESSFUL_OUTPUT_TAG, lastSuccessfulOutputGameTime);
        tag.put(ENERGY_TAG, energy.serializeNBT(provider));
        tag.put(INVENTORY_TAG, inventory.serialize(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        formed = tag.getBoolean(FORMED_TAG);
        machineState = TemporalLoomMachineState.byName(tag.getString(STATE_TAG));
        progress = Math.max(0, tag.getInt(PROGRESS_TAG));
        pendingOutput = tag.getBoolean(PENDING_OUTPUT_TAG);
        lastSuccessfulOutputGameTime = tag.contains(LAST_SUCCESSFUL_OUTPUT_TAG)
                ? tag.getLong(LAST_SUCCESSFUL_OUTPUT_TAG)
                : -1L;
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(provider, tag.get(ENERGY_TAG));
        }
        if (tag.contains(INVENTORY_TAG, Tag.TAG_COMPOUND)) {
            inventory.deserialize(provider, tag.getCompound(INVENTORY_TAG));
        }
        validationRequested = true;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private final class ControllerEnergyStorage extends EnergyStorage {
        private ControllerEnergyStorage() {
            super(ENERGY_CAPACITY, MAX_ENERGY_RECEIVE, 0);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) {
                setChanged();
            }
            return received;
        }

        private boolean consume(int amount) {
            if (amount <= 0 || energy < amount) {
                return false;
            }
            energy -= amount;
            setChanged();
            return true;
        }
    }
}
