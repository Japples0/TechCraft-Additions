package com.techcraft.additions.machine.loom;

public record TemporalLoomOperatingProfile(
        int searchingTicks,
        int harvestingTicks,
        int synthesizingTicks,
        int fePerTick,
        int outputAmount) {
    public static final TemporalLoomOperatingProfile GREYBOX_BASE =
            new TemporalLoomOperatingProfile(100, 200, 100, 1_000, 1);

    public int durationFor(TemporalLoomMachineState state) {
        return switch (state) {
            case SEARCHING -> searchingTicks;
            case HARVESTING -> harvestingTicks;
            case SYNTHESIZING -> synthesizingTicks;
            default -> 0;
        };
    }
}
