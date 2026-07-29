package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;
import net.neoforged.bus.api.IEventBus;

public final class ModChemicals {
    private static final int OTHERSIDE_ATMOSPHERE_TINT = 0x6F39D6;

    private static final ChemicalDeferredRegister CHEMICALS =
            new ChemicalDeferredRegister(TechCraftAdditions.MOD_ID);

    public static final DeferredChemical<Chemical> OTHERSIDE_ATMOSPHERE =
            CHEMICALS.register("otherside_atmosphere", () -> new Chemical(
                    ChemicalBuilder.builder(TechCraftAdditions.id("chemical/otherside_atmosphere"))
                            .tint(OTHERSIDE_ATMOSPHERE_TINT)
            ));

    private ModChemicals() {
    }

    public static void register(IEventBus modEventBus) {
        CHEMICALS.register(modEventBus);
    }
}
