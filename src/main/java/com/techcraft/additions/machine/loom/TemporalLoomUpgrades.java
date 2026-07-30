package com.techcraft.additions.machine.loom;

import net.minecraft.world.item.ItemStack;

public final class TemporalLoomUpgrades {
    private TemporalLoomUpgrades() {
    }

    public static TemporalLoomOperatingProfile resolve(ItemStack installedDescender) {
        // The slot and resolver are stable extension points. The Descender deliberately
        // has no operating effect during the greybox milestone.
        return TemporalLoomOperatingProfile.GREYBOX_BASE;
    }
}
