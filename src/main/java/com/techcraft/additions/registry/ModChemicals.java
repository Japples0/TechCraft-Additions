package com.techcraft.additions.registry;

import com.techcraft.additions.TechCraftAdditions;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;
import net.neoforged.bus.api.IEventBus;

public final class ModChemicals {
    private static final int NETHER_ATMOSPHERE_TINT = 0xD85A2B;
    private static final int END_ATMOSPHERE_TINT = 0xC7D96B;
    private static final int OTHERSIDE_ATMOSPHERE_TINT = 0x6F39D6;

    private static final ChemicalDeferredRegister CHEMICALS =
            new ChemicalDeferredRegister(TechCraftAdditions.MOD_ID);

    public static final DeferredChemical<Chemical> NETHER_ATMOSPHERE =
            registerAtmosphere("nether_atmosphere", NETHER_ATMOSPHERE_TINT);

    public static final DeferredChemical<Chemical> END_ATMOSPHERE =
            registerAtmosphere("end_atmosphere", END_ATMOSPHERE_TINT);

    public static final DeferredChemical<Chemical> OTHERSIDE_ATMOSPHERE =
            registerAtmosphere("otherside_atmosphere", OTHERSIDE_ATMOSPHERE_TINT);

    private ModChemicals() {
    }

    public static void register(IEventBus modEventBus) {
        CHEMICALS.register(modEventBus);
    }

    private static DeferredChemical<Chemical> registerAtmosphere(String name, int tint) {
        return CHEMICALS.register(name, () -> new Chemical(
                ChemicalBuilder.builder(TechCraftAdditions.id("liquid/" + name))
                        .tint(tint)
                        .gaseous()
        ));
    }
}
