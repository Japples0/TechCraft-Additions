package com.techcraft.additions.client;

import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.client.render.TemporalLoomRenderer;
import com.techcraft.additions.registry.ModBlockEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = TechCraftAdditions.MOD_ID, value = Dist.CLIENT)
public final class TechCraftAdditionsClient {
    private TechCraftAdditionsClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntities.TEMPORAL_LOOM_CONTROLLER.get(), TemporalLoomRenderer::new);
    }
}
