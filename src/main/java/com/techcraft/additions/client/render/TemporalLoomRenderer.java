package com.techcraft.additions.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.techcraft.additions.TechCraftAdditions;
import com.techcraft.additions.block.TemporalLoomControllerBlock;
import com.techcraft.additions.machine.loom.TemporalLoomAnchors;
import com.techcraft.additions.machine.loom.TemporalLoomControllerBlockEntity;
import com.techcraft.additions.machine.loom.TemporalLoomMachineState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class TemporalLoomRenderer implements BlockEntityRenderer<TemporalLoomControllerBlockEntity> {
    public static final int VIEW_DISTANCE = 48;
    public static final int MAX_VISIBLE_STRANDS = 10;
    public static final int STRAND_SEGMENTS = 12;

    private static final float TWO_PI = (float) (Math.PI * 2.0);
    private static final int OUTPUT_SEQUENCE_TICKS = 28;
    private static final int FULL_BRIGHT = LightTexture.FULL_BRIGHT;
    private static final int NO_OVERLAY = OverlayTexture.NO_OVERLAY;

    private static final ResourceLocation METAL_TEXTURE = texture("resonance_metal");
    private static final ResourceLocation AMETHYST_TEXTURE = texture("amethyst_lining");
    private static final ResourceLocation GOLD_TEXTURE = texture("gold_rail");
    private static final ResourceLocation SINGULARITY_TEXTURE = texture("singularity");
    private static final RenderType METAL = RenderType.entityCutoutNoCull(METAL_TEXTURE);
    private static final RenderType AMETHYST = RenderType.entityTranslucentEmissive(AMETHYST_TEXTURE);
    private static final RenderType GOLD = RenderType.entityTranslucentEmissive(GOLD_TEXTURE);
    private static final RenderType SINGULARITY = RenderType.entityTranslucentEmissive(SINGULARITY_TEXTURE);
    private static final RenderType RIBBON = RenderType.lightning();
    private static final int[] LOOP_SIDES = {-1, 1};

    private static final float[][] STRAND_SEEDS = {
            {-7.3F, 5.8F, 5.4F, -1.0F, 0.22F},
            {7.6F, 4.9F, 6.8F, 1.0F, -0.36F},
            {-5.8F, 0.6F, 8.2F, -1.0F, 0.57F},
            {6.5F, 1.1F, 7.5F, 1.0F, -0.72F},
            {-8.0F, 3.2F, 3.6F, -1.0F, -0.48F},
            {8.3F, 2.7F, 4.4F, 1.0F, 0.43F},
            {-4.9F, 6.3F, 7.7F, -1.0F, 0.81F},
            {5.2F, 6.0F, 8.5F, 1.0F, -0.84F},
            {-6.7F, 1.8F, 5.9F, -1.0F, 0.03F},
            {7.1F, 0.4F, 6.2F, 1.0F, 0.69F}
    };

    public TemporalLoomRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(TemporalLoomControllerBlockEntity controller, float partialTick,
                       PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (!controller.isFormed() || controller.getLevel() == null) {
            return;
        }

        TemporalLoomMachineState state = controller.machineState();
        VisualProfile profile = VisualProfile.forState(state);
        Direction facing = controller.getBlockState().getValue(TemporalLoomControllerBlock.FACING);
        float time = controller.getLevel().getGameTime() + partialTick;
        int duration = controller.currentStateDuration();
        float progress = duration == 0
                ? 0.0F
                : Mth.clamp((controller.progress() + partialTick) / duration, 0.0F, 1.0F);
        float outputAge = controller.lastSuccessfulOutputGameTime() < 0
                ? Float.POSITIVE_INFINITY
                : time - controller.lastSuccessfulOutputGameTime();

        renderLoops(poseStack, buffers, facing, packedLight, time, progress, state, profile);
        renderTemporalStrands(controller, poseStack, buffers, facing, time, progress, profile);
        renderDescender(controller, poseStack, buffers, facing, time, profile);
        renderOutputPath(controller, poseStack, buffers, facing, time, progress, state, outputAge);
        renderSingularity(controller, poseStack, buffers, facing, time, progress, state, outputAge, profile);
    }

    private static void renderLoops(PoseStack poseStack, MultiBufferSource buffers, Direction facing,
                                    int packedLight, float time, float progress,
                                    TemporalLoomMachineState state, VisualProfile profile) {
        VertexConsumer metal = buffers.getBuffer(METAL);
        VertexConsumer amethyst = buffers.getBuffer(AMETHYST);
        VertexConsumer gold = buffers.getBuffer(GOLD);
        float speed = profile.loopSpeed();
        if (state == TemporalLoomMachineState.SYNTHESIZING) {
            speed *= 1.0F - progress * 0.82F;
        }
        float phase = time * speed;
        float warningFlicker = state == TemporalLoomMachineState.OUTPUT_BLOCKED
                ? 0.55F + 0.45F * Mth.sin(time * 0.34F)
                : 1.0F;
        int goldAlpha = Math.round(profile.goldAlpha() * warningFlicker);

        for (int side : LOOP_SIDES) {
            float center = side * 2.0F;
            float direction = side < 0 ? 1.0F : -1.0F;
            TemporalLoomGeometry.renderBand(poseStack, metal, facing,
                    center, 2.5F, 2.0F,
                    1.48F, 1.38F, 1.16F, 1.06F, 0.22F,
                    0.0F, 4.0F, 255, 255, 255, 255, packedLight, NO_OVERLAY);
            TemporalLoomGeometry.renderBand(poseStack, amethyst, facing,
                    center, 2.5F, 2.0F,
                    1.16F, 1.06F, 1.01F, 0.91F, 0.235F,
                    direction * phase / TWO_PI, 6.0F,
                    255, 255, 255, profile.amethystAlpha(), FULL_BRIGHT, NO_OVERLAY);
            TemporalLoomGeometry.renderBand(poseStack, gold, facing,
                    center, 2.5F, 2.0F,
                    1.48F, 1.38F, 1.42F, 1.32F, 0.245F,
                    direction * phase / TWO_PI * 2.0F, 4.0F,
                    state == TemporalLoomMachineState.OUTPUT_BLOCKED ? 255 : 255,
                    state == TemporalLoomMachineState.OUTPUT_BLOCKED ? 92 : 235,
                    state == TemporalLoomMachineState.OUTPUT_BLOCKED ? 35 : 154,
                    goldAlpha, FULL_BRIGHT, NO_OVERLAY);
        }
    }

    private static void renderTemporalStrands(TemporalLoomControllerBlockEntity controller,
                                              PoseStack poseStack, MultiBufferSource buffers,
                                              Direction facing, float time, float progress,
                                              VisualProfile profile) {
        int strandCount = Math.min(MAX_VISIBLE_STRANDS, profile.strandCount());
        if (strandCount <= 0) {
            return;
        }

        VertexConsumer ribbon = buffers.getBuffer(RIBBON);
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        BlockPos controllerPos = controller.getBlockPos();
        int seedOffset = Math.floorMod(Mth.murmurHash3Mixer((int) controllerPos.asLong()), STRAND_SEEDS.length);
        boolean blocked = controller.machineState() == TemporalLoomMachineState.OUTPUT_BLOCKED;
        boolean synthesizing = controller.machineState() == TemporalLoomMachineState.SYNTHESIZING;

        for (int visibleIndex = 0; visibleIndex < strandCount; visibleIndex++) {
            int index = (visibleIndex + seedOffset) % STRAND_SEEDS.length;
            float[] seed = STRAND_SEEDS[index];
            float collapse = synthesizing ? progress * progress * 0.72F : 0.0F;
            float startRight = Mth.lerp(collapse, seed[0], 0.0F);
            float startUp = Mth.lerp(collapse, seed[1], 2.5F);
            float startBack = Mth.lerp(collapse, seed[2], 2.0F);
            float loopRight = seed[3] * 2.0F;
            float loopAngle = seed[4] * (float) Math.PI;
            float loopEdgeRight = loopRight + Mth.cos(loopAngle) * 1.18F;
            float loopEdgeUp = 2.5F + Mth.sin(loopAngle) * 1.08F;
            float p1Right = Mth.lerp(collapse * 0.7F, startRight * 0.72F, loopEdgeRight * 1.25F);
            float p1Up = Mth.lerp(collapse * 0.7F, startUp + Mth.sin(time * 0.025F + index) * 0.25F, loopEdgeUp);
            float p1Back = Mth.lerp(collapse * 0.7F, startBack * 0.76F, 2.4F);
            float p2Right = Mth.lerp(collapse, loopEdgeRight, loopRight * 0.25F);
            float p2Up = Mth.lerp(collapse, loopEdgeUp, 2.5F);
            float p2Back = Mth.lerp(collapse, 2.0F + Mth.sin(loopAngle * 2.0F) * 0.22F, 2.0F);
            float visibleEnd = blocked ? 0.74F : profile.strandReach();
            float flicker = blocked ? 0.55F + 0.45F * Mth.sin(time * 0.45F + index * 1.7F) : 1.0F;
            int red = 255;
            int green = blocked ? 92 : 197;
            int blue = blocked ? 31 : 86;

            TemporalLoomGeometry.renderBezierRibbon(poseStack, ribbon, facing, controllerPos, camera,
                    startRight, startUp, startBack,
                    p1Right, p1Up, p1Back,
                    p2Right, p2Up, p2Back,
                    0.0F, 2.5F, 2.0F,
                    0.0F, visibleEnd, 0.018F, STRAND_SEGMENTS,
                    red, green, blue, 0, Math.round(170 * flicker));

            float highlightHead = Mth.frac(time * profile.strandFlowSpeed() + index * 0.173F);
            float highlightStart = Math.max(0.0F, highlightHead - 0.13F);
            float highlightEnd = Math.min(visibleEnd, highlightHead);
            if (highlightEnd > highlightStart) {
                TemporalLoomGeometry.renderBezierRibbon(poseStack, ribbon, facing, controllerPos, camera,
                        startRight, startUp, startBack,
                        p1Right, p1Up, p1Back,
                        p2Right, p2Up, p2Back,
                        0.0F, 2.5F, 2.0F,
                        highlightStart, highlightEnd, 0.032F, 3,
                        255, blocked ? 156 : 244, blocked ? 92 : 206, 20, Math.round(235 * flicker));
            }
        }
    }

    private static void renderDescender(TemporalLoomControllerBlockEntity controller,
                                        PoseStack poseStack, MultiBufferSource buffers,
                                        Direction facing, float time, VisualProfile profile) {
        if (!controller.hasDescender()) {
            return;
        }

        VertexConsumer metal = buffers.getBuffer(METAL);
        VertexConsumer amethyst = buffers.getBuffer(AMETHYST);
        for (int right : LOOP_SIDES) {
            for (int back : LOOP_SIDES) {
                TemporalLoomGeometry.renderBox(poseStack, metal, facing,
                        right * 0.28F, 4.5F, 2.0F + back * 0.28F,
                        0.12F, 0.9F, 0.12F,
                        255, 255, 255, 255, FULL_BRIGHT, NO_OVERLAY);
            }
        }
        float pulse = 0.75F + 0.25F * Mth.sin(time * 0.12F);
        TemporalLoomGeometry.renderBox(poseStack, amethyst, facing,
                0.0F, 4.5F, 2.0F, 0.26F, 0.72F, 0.26F,
                230, 137, 255, Math.round(220 * pulse), FULL_BRIGHT, NO_OVERLAY);

        if (profile.descenderBeamAlpha() > 0) {
            VertexConsumer ribbon = buffers.getBuffer(RIBBON);
            Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            TemporalLoomGeometry.renderBezierRibbon(poseStack, ribbon, facing,
                    controller.getBlockPos(), camera,
                    0.0F, 4.12F, 2.0F,
                    0.0F, 3.75F, 2.0F,
                    0.0F, 3.05F, 2.0F,
                    0.0F, 2.68F, 2.0F,
                    0.0F, 1.0F, 0.055F, 8,
                    191, 72, 255, profile.descenderBeamAlpha() / 3, profile.descenderBeamAlpha());
        }
    }

    private static void renderOutputPath(TemporalLoomControllerBlockEntity controller,
                                         PoseStack poseStack, MultiBufferSource buffers,
                                         Direction facing, float time, float progress,
                                         TemporalLoomMachineState state, float outputAge) {
        VertexConsumer ribbon = buffers.getBuffer(RIBBON);
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        BlockPos controllerPos = controller.getBlockPos();

        if (state == TemporalLoomMachineState.SYNTHESIZING && progress > 0.55F) {
            float intensity = Mth.clamp((progress - 0.55F) / 0.45F, 0.0F, 1.0F);
            TemporalLoomGeometry.renderBezierRibbon(poseStack, ribbon, facing, controllerPos, camera,
                    0.0F, 2.42F, 2.0F, 0.14F, 2.15F, 2.0F,
                    -0.12F, 1.78F, 2.0F, 0.0F, 1.52F, 2.0F,
                    0.0F, intensity, 0.045F, 8,
                    255, 224, 132, 15, Math.round(205 * intensity));
        }

        if (outputAge >= 0.0F && outputAge < OUTPUT_SEQUENCE_TICKS) {
            float travel = Mth.clamp(outputAge / 12.0F, 0.0F, 1.0F);
            float decay = 1.0F - Mth.clamp((outputAge - 14.0F) / 14.0F, 0.0F, 1.0F);
            TemporalLoomGeometry.renderBezierRibbon(poseStack, ribbon, facing, controllerPos, camera,
                    0.0F, 2.48F, 2.0F, 0.18F, 2.22F, 2.0F,
                    -0.16F, 1.76F, 2.0F, 0.0F, 1.52F, 2.0F,
                    0.0F, travel, 0.068F, 10,
                    255, 248, 205, 40, Math.round(255 * decay));
        }
    }

    private static void renderSingularity(TemporalLoomControllerBlockEntity controller,
                                          PoseStack poseStack, MultiBufferSource buffers,
                                          Direction facing, float time, float progress,
                                          TemporalLoomMachineState state, float outputAge,
                                          VisualProfile profile) {
        Vec3 worldAnchor = TemporalLoomAnchors.toWorld(
                controller.getBlockPos(), facing, TemporalLoomAnchors.CENTRAL_SINGULARITY);
        float outputFlash = outputAge >= 0.0F && outputAge < OUTPUT_SEQUENCE_TICKS
                ? 1.0F - outputAge / OUTPUT_SEQUENCE_TICKS
                : 0.0F;
        float pulse = 1.0F + Mth.sin(time * profile.corePulseSpeed()) * profile.corePulseAmount();
        if (state == TemporalLoomMachineState.SYNTHESIZING) {
            pulse *= 1.0F - progress * 0.26F;
        }
        float coreRadius = profile.coreRadius() * pulse + outputFlash * 0.18F;

        poseStack.pushPose();
        poseStack.translate(
                worldAnchor.x - controller.getBlockPos().getX(),
                worldAnchor.y - controller.getBlockPos().getY(),
                worldAnchor.z - controller.getBlockPos().getZ());
        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());

        VertexConsumer consumer = buffers.getBuffer(SINGULARITY);
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(time * profile.coreRotationSpeed()));
        TemporalLoomGeometry.renderBillboard(poseStack, consumer, coreRadius,
                255, 255, 255, profile.coreAlpha(), FULL_BRIGHT, NO_OVERLAY);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZN.rotationDegrees(time * (profile.coreRotationSpeed() * 0.57F)));
        int coronaAlpha = Math.min(255, profile.coronaAlpha() + Math.round(outputFlash * 110.0F));
        TemporalLoomGeometry.renderBillboard(poseStack, consumer, coreRadius * 1.32F,
                outputFlash > 0.0F ? 255 : 198,
                outputFlash > 0.0F ? 239 : 103,
                255, coronaAlpha, FULL_BRIGHT, NO_OVERLAY);
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public boolean shouldRender(TemporalLoomControllerBlockEntity controller, Vec3 cameraPosition) {
        if (!controller.isFormed()) {
            return false;
        }
        Direction facing = controller.getBlockState().getValue(TemporalLoomControllerBlock.FACING);
        Vec3 centre = TemporalLoomAnchors.toWorld(
                controller.getBlockPos(), facing, TemporalLoomAnchors.CENTRAL_SINGULARITY);
        return centre.distanceToSqr(cameraPosition) <= VIEW_DISTANCE * VIEW_DISTANCE;
    }

    @Override
    public int getViewDistance() {
        return VIEW_DISTANCE;
    }

    @Override
    public AABB getRenderBoundingBox(TemporalLoomControllerBlockEntity controller) {
        return new AABB(controller.getBlockPos()).inflate(10.0, 6.0, 10.0);
    }

    private static ResourceLocation texture(String name) {
        return TechCraftAdditions.id("textures/entity/temporal_loom/" + name + ".png");
    }

    private record VisualProfile(
            float loopSpeed,
            int strandCount,
            float strandReach,
            float strandFlowSpeed,
            int amethystAlpha,
            int goldAlpha,
            float coreRadius,
            float corePulseSpeed,
            float corePulseAmount,
            float coreRotationSpeed,
            int coreAlpha,
            int coronaAlpha,
            int descenderBeamAlpha) {

        private static final VisualProfile UNFORMED = new VisualProfile(0.0F, 0, 0.0F, 0.0F,
                0, 0, 0.0F, 0.0F, 0.0F, 0.0F, 0, 0, 0);
        private static final VisualProfile DORMANT = new VisualProfile(0.004F, 0, 0.0F, 0.0F,
                92, 68, 0.47F, 0.035F, 0.035F, 0.32F, 230, 72, 0);
        private static final VisualProfile SEARCHING = new VisualProfile(0.018F, 3, 0.78F, 0.013F,
                168, 128, 0.50F, 0.075F, 0.055F, 0.68F, 242, 125, 120);
        private static final VisualProfile HARVESTING = new VisualProfile(0.052F, 10, 1.0F, 0.026F,
                236, 228, 0.57F, 0.13F, 0.085F, 1.25F, 255, 205, 205);
        private static final VisualProfile SYNTHESIZING = new VisualProfile(0.036F, 8, 1.0F, 0.052F,
                255, 255, 0.60F, 0.24F, 0.12F, 1.8F, 255, 248, 255);
        private static final VisualProfile OUTPUT_BLOCKED = new VisualProfile(0.003F, 4, 0.74F, 0.008F,
                116, 190, 0.53F, 0.19F, 0.07F, 0.18F, 250, 160, 45);

        private static VisualProfile forState(TemporalLoomMachineState state) {
            return switch (state) {
                case UNFORMED -> UNFORMED;
                case DORMANT -> DORMANT;
                case SEARCHING -> SEARCHING;
                case HARVESTING -> HARVESTING;
                case SYNTHESIZING -> SYNTHESIZING;
                case OUTPUT_BLOCKED -> OUTPUT_BLOCKED;
            };
        }
    }
}
