package com.techcraft.additions.machine.loom;

import com.techcraft.additions.registry.ModItems;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public final class TemporalLoomInventory {
    private static final String OUTPUT_TAG = "Output";
    private static final String UPGRADES_TAG = "Upgrades";

    private final Runnable changedCallback;
    private final OutputHandler output = new OutputHandler();
    private final UpgradeHandler upgrades = new UpgradeHandler();

    public TemporalLoomInventory(Runnable changedCallback) {
        this.changedCallback = changedCallback;
    }

    public IItemHandler outputCapability() {
        return output;
    }

    public ItemStack outputStack() {
        return output.getStackInSlot(0);
    }

    public ItemStack installedDescender() {
        return upgrades.getStackInSlot(0);
    }

    public boolean hasDescender() {
        return !installedDescender().isEmpty();
    }

    public boolean installDescender(ItemStack stack) {
        if (!stack.is(ModItems.DIMENSIONAL_DESCENDER.get()) || hasDescender()) {
            return false;
        }
        upgrades.setStackInSlot(0, stack.copyWithCount(1));
        return true;
    }

    public ItemStack removeDescender() {
        return upgrades.extractItem(0, 1, false);
    }

    public boolean canAcceptOutput(ItemStack stack) {
        ItemStack existing = outputStack();
        if (existing.isEmpty()) {
            return stack.getCount() <= stack.getMaxStackSize();
        }
        return ItemStack.isSameItemSameComponents(existing, stack)
                && existing.getCount() + stack.getCount() <= existing.getMaxStackSize();
    }

    public boolean insertOutput(ItemStack stack) {
        if (!canAcceptOutput(stack)) {
            return false;
        }
        ItemStack existing = outputStack();
        if (existing.isEmpty()) {
            output.setStackInSlot(0, stack.copy());
        } else {
            ItemStack combined = existing.copy();
            combined.grow(stack.getCount());
            output.setStackInSlot(0, combined);
        }
        return true;
    }

    public CompoundTag serialize(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.put(OUTPUT_TAG, output.serializeNBT(provider));
        tag.put(UPGRADES_TAG, upgrades.serializeNBT(provider));
        return tag;
    }

    public void deserialize(HolderLookup.Provider provider, CompoundTag tag) {
        if (tag.contains(OUTPUT_TAG)) {
            output.deserializeNBT(provider, tag.getCompound(OUTPUT_TAG));
        }
        if (tag.contains(UPGRADES_TAG)) {
            upgrades.deserializeNBT(provider, tag.getCompound(UPGRADES_TAG));
        }
    }

    public void dropAll(Consumer<ItemStack> dropper) {
        ItemStack outputStack = output.extractItem(0, Integer.MAX_VALUE, false);
        ItemStack descenderStack = upgrades.extractItem(0, Integer.MAX_VALUE, false);
        if (!outputStack.isEmpty()) {
            dropper.accept(outputStack);
        }
        if (!descenderStack.isEmpty()) {
            dropper.accept(descenderStack);
        }
    }

    private final class OutputHandler extends ItemStackHandler {
        private OutputHandler() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            changedCallback.run();
        }
    }

    private final class UpgradeHandler extends ItemStackHandler {
        private UpgradeHandler() {
            super(1);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.is(ModItems.DIMENSIONAL_DESCENDER.get());
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            changedCallback.run();
        }
    }
}
