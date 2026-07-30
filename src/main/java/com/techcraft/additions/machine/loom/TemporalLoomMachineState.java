package com.techcraft.additions.machine.loom;

import java.util.Locale;

public enum TemporalLoomMachineState {
    UNFORMED,
    DORMANT,
    SEARCHING,
    HARVESTING,
    SYNTHESIZING,
    OUTPUT_BLOCKED;

    public String serializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static TemporalLoomMachineState byName(String name) {
        for (TemporalLoomMachineState state : values()) {
            if (state.serializedName().equals(name)) {
                return state;
            }
        }
        return UNFORMED;
    }
}
