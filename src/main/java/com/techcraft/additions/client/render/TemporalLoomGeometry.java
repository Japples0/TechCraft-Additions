package com.techcraft.additions.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

final class TemporalLoomGeometry {
    static final int LOOP_SEGMENTS = 48;
    static final int MAX_RIBBON_SEGMENTS = 12;

    private static final float[] COS = new float[LOOP_SEGMENTS + 1];
    private static final float[] SIN = new float[LOOP_SEGMENTS + 1];

    static {
        for (int index = 0; index <= LOOP_SEGMENTS; index++) {
            double angle = Math.PI * 2.0 * index / LOOP_SEGMENTS;
            COS[index] = (float) Math.cos(angle);
            SIN[index] = (float) Math.sin(angle);
        }
    }

    private TemporalLoomGeometry() {
    }

    static void renderBand(PoseStack poseStack, VertexConsumer consumer, Direction facing,
                           float centerRight, float centerUp, float centerBack,
                           float outerRight, float outerUp, float innerRight, float innerUp,
                           float depth, float uvOffset, float uvRepeats,
                           int red, int green, int blue, int alpha, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        for (int index = 0; index < LOOP_SEGMENTS; index++) {
            float outerRight0 = centerRight + COS[index] * outerRight;
            float outerUp0 = centerUp + SIN[index] * outerUp;
            float innerRight0 = centerRight + COS[index] * innerRight;
            float innerUp0 = centerUp + SIN[index] * innerUp;
            float outerRight1 = centerRight + COS[index + 1] * outerRight;
            float outerUp1 = centerUp + SIN[index + 1] * outerUp;
            float innerRight1 = centerRight + COS[index + 1] * innerRight;
            float innerUp1 = centerUp + SIN[index + 1] * innerUp;
            float u0 = uvOffset + uvRepeats * index / LOOP_SEGMENTS;
            float u1 = uvOffset + uvRepeats * (index + 1) / LOOP_SEGMENTS;

            quad(pose, consumer, facing,
                    outerRight0, outerUp0, centerBack - depth, u0, 0.0F,
                    outerRight1, outerUp1, centerBack - depth, u1, 0.0F,
                    innerRight1, innerUp1, centerBack - depth, u1, 1.0F,
                    innerRight0, innerUp0, centerBack - depth, u0, 1.0F,
                    0.0F, 0.0F, -1.0F, red, green, blue, alpha, light, overlay);
            quad(pose, consumer, facing,
                    innerRight0, innerUp0, centerBack + depth, u0, 1.0F,
                    innerRight1, innerUp1, centerBack + depth, u1, 1.0F,
                    outerRight1, outerUp1, centerBack + depth, u1, 0.0F,
                    outerRight0, outerUp0, centerBack + depth, u0, 0.0F,
                    0.0F, 0.0F, 1.0F, red, green, blue, alpha, light, overlay);

            float normalRight = (COS[index] + COS[index + 1]) * 0.5F;
            float normalUp = (SIN[index] + SIN[index + 1]) * 0.5F;
            quad(pose, consumer, facing,
                    outerRight0, outerUp0, centerBack + depth, u0, 0.0F,
                    outerRight1, outerUp1, centerBack + depth, u1, 0.0F,
                    outerRight1, outerUp1, centerBack - depth, u1, 1.0F,
                    outerRight0, outerUp0, centerBack - depth, u0, 1.0F,
                    normalRight, normalUp, 0.0F, red, green, blue, alpha, light, overlay);
            quad(pose, consumer, facing,
                    innerRight0, innerUp0, centerBack - depth, u0, 1.0F,
                    innerRight1, innerUp1, centerBack - depth, u1, 1.0F,
                    innerRight1, innerUp1, centerBack + depth, u1, 0.0F,
                    innerRight0, innerUp0, centerBack + depth, u0, 0.0F,
                    -normalRight, -normalUp, 0.0F, red, green, blue, alpha, light, overlay);
        }
    }

    static void renderBox(PoseStack poseStack, VertexConsumer consumer, Direction facing,
                          float centerRight, float centerUp, float centerBack,
                          float sizeRight, float sizeUp, float sizeBack,
                          int red, int green, int blue, int alpha, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        float minRight = centerRight - sizeRight * 0.5F;
        float maxRight = centerRight + sizeRight * 0.5F;
        float minUp = centerUp - sizeUp * 0.5F;
        float maxUp = centerUp + sizeUp * 0.5F;
        float minBack = centerBack - sizeBack * 0.5F;
        float maxBack = centerBack + sizeBack * 0.5F;

        quad(pose, consumer, facing,
                minRight, minUp, minBack, 0, 1, maxRight, minUp, minBack, 1, 1,
                maxRight, maxUp, minBack, 1, 0, minRight, maxUp, minBack, 0, 0,
                0, 0, -1, red, green, blue, alpha, light, overlay);
        quad(pose, consumer, facing,
                maxRight, minUp, maxBack, 0, 1, minRight, minUp, maxBack, 1, 1,
                minRight, maxUp, maxBack, 1, 0, maxRight, maxUp, maxBack, 0, 0,
                0, 0, 1, red, green, blue, alpha, light, overlay);
        quad(pose, consumer, facing,
                minRight, minUp, maxBack, 0, 1, minRight, minUp, minBack, 1, 1,
                minRight, maxUp, minBack, 1, 0, minRight, maxUp, maxBack, 0, 0,
                -1, 0, 0, red, green, blue, alpha, light, overlay);
        quad(pose, consumer, facing,
                maxRight, minUp, minBack, 0, 1, maxRight, minUp, maxBack, 1, 1,
                maxRight, maxUp, maxBack, 1, 0, maxRight, maxUp, minBack, 0, 0,
                1, 0, 0, red, green, blue, alpha, light, overlay);
        quad(pose, consumer, facing,
                minRight, maxUp, minBack, 0, 1, maxRight, maxUp, minBack, 1, 1,
                maxRight, maxUp, maxBack, 1, 0, minRight, maxUp, maxBack, 0, 0,
                0, 1, 0, red, green, blue, alpha, light, overlay);
        quad(pose, consumer, facing,
                minRight, minUp, maxBack, 0, 1, maxRight, minUp, maxBack, 1, 1,
                maxRight, minUp, minBack, 1, 0, minRight, minUp, minBack, 0, 0,
                0, -1, 0, red, green, blue, alpha, light, overlay);
    }

    static void renderBillboard(PoseStack poseStack, VertexConsumer consumer, float radius,
                                int red, int green, int blue, int alpha, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();
        texturedVertex(pose, consumer, -radius, -radius, 0.0F, 0.0F, 1.0F,
                0, 0, 1, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, radius, -radius, 0.0F, 1.0F, 1.0F,
                0, 0, 1, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, radius, radius, 0.0F, 1.0F, 0.0F,
                0, 0, 1, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, -radius, radius, 0.0F, 0.0F, 0.0F,
                0, 0, 1, red, green, blue, alpha, light, overlay);
    }

    static void renderBezierRibbon(PoseStack poseStack, VertexConsumer consumer,
                                   Direction facing, BlockPos controllerPos, Vec3 cameraPosition,
                                   float p0Right, float p0Up, float p0Back,
                                   float p1Right, float p1Up, float p1Back,
                                   float p2Right, float p2Up, float p2Back,
                                   float p3Right, float p3Up, float p3Back,
                                   float startT, float endT, float width, int segments,
                                   int red, int green, int blue, int startAlpha, int endAlpha) {
        int boundedSegments = Math.max(1, Math.min(MAX_RIBBON_SEGMENTS, segments));
        float clampedStart = Math.max(0.0F, Math.min(1.0F, startT));
        float clampedEnd = Math.max(clampedStart, Math.min(1.0F, endT));
        if (clampedEnd - clampedStart < 0.001F) {
            return;
        }

        PoseStack.Pose pose = poseStack.last();
        double cameraX = cameraPosition.x - controllerPos.getX();
        double cameraY = cameraPosition.y - controllerPos.getY();
        double cameraZ = cameraPosition.z - controllerPos.getZ();

        for (int index = 0; index < boundedSegments; index++) {
            float t0 = clampedStart + (clampedEnd - clampedStart) * index / boundedSegments;
            float t1 = clampedStart + (clampedEnd - clampedStart) * (index + 1) / boundedSegments;
            float localRight0 = bezier(p0Right, p1Right, p2Right, p3Right, t0);
            float localUp0 = bezier(p0Up, p1Up, p2Up, p3Up, t0);
            float localBack0 = bezier(p0Back, p1Back, p2Back, p3Back, t0);
            float localRight1 = bezier(p0Right, p1Right, p2Right, p3Right, t1);
            float localUp1 = bezier(p0Up, p1Up, p2Up, p3Up, t1);
            float localBack1 = bezier(p0Back, p1Back, p2Back, p3Back, t1);

            float x0 = renderX(facing, localRight0, localBack0);
            float y0 = localUp0;
            float z0 = renderZ(facing, localRight0, localBack0);
            float x1 = renderX(facing, localRight1, localBack1);
            float y1 = localUp1;
            float z1 = renderZ(facing, localRight1, localBack1);
            float middleX = (x0 + x1) * 0.5F;
            float middleY = (y0 + y1) * 0.5F;
            float middleZ = (z0 + z1) * 0.5F;
            float tangentX = x1 - x0;
            float tangentY = y1 - y0;
            float tangentZ = z1 - z0;
            float viewX = (float) cameraX - middleX;
            float viewY = (float) cameraY - middleY;
            float viewZ = (float) cameraZ - middleZ;
            float sideX = tangentY * viewZ - tangentZ * viewY;
            float sideY = tangentZ * viewX - tangentX * viewZ;
            float sideZ = tangentX * viewY - tangentY * viewX;
            float sideLength = (float) Math.sqrt(sideX * sideX + sideY * sideY + sideZ * sideZ);
            if (sideLength < 0.0001F) {
                sideX = width;
                sideY = 0.0F;
                sideZ = 0.0F;
            } else {
                float scale = width / sideLength;
                sideX *= scale;
                sideY *= scale;
                sideZ *= scale;
            }

            int alpha0 = lerp(startAlpha, endAlpha, (t0 - clampedStart) / (clampedEnd - clampedStart));
            int alpha1 = lerp(startAlpha, endAlpha, (t1 - clampedStart) / (clampedEnd - clampedStart));
            colorVertex(pose, consumer, x0 - sideX, y0 - sideY, z0 - sideZ, red, green, blue, alpha0);
            colorVertex(pose, consumer, x0 + sideX, y0 + sideY, z0 + sideZ, red, green, blue, alpha0);
            colorVertex(pose, consumer, x1 + sideX, y1 + sideY, z1 + sideZ, red, green, blue, alpha1);
            colorVertex(pose, consumer, x1 - sideX, y1 - sideY, z1 - sideZ, red, green, blue, alpha1);
        }
    }

    private static float bezier(float p0, float p1, float p2, float p3, float t) {
        float inverse = 1.0F - t;
        return inverse * inverse * inverse * p0
                + 3.0F * inverse * inverse * t * p1
                + 3.0F * inverse * t * t * p2
                + t * t * t * p3;
    }

    private static int lerp(int from, int to, float amount) {
        return Math.round(from + (to - from) * amount);
    }

    private static void quad(PoseStack.Pose pose, VertexConsumer consumer, Direction facing,
                             float right0, float up0, float back0, float u0, float v0,
                             float right1, float up1, float back1, float u1, float v1,
                             float right2, float up2, float back2, float u2, float v2,
                             float right3, float up3, float back3, float u3, float v3,
                             float normalRight, float normalUp, float normalBack,
                             int red, int green, int blue, int alpha, int light, int overlay) {
        float normalX = vectorX(facing, normalRight, normalBack);
        float normalZ = vectorZ(facing, normalRight, normalBack);
        texturedVertex(pose, consumer, renderX(facing, right0, back0), up0, renderZ(facing, right0, back0),
                u0, v0, normalX, normalUp, normalZ, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, renderX(facing, right1, back1), up1, renderZ(facing, right1, back1),
                u1, v1, normalX, normalUp, normalZ, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, renderX(facing, right2, back2), up2, renderZ(facing, right2, back2),
                u2, v2, normalX, normalUp, normalZ, red, green, blue, alpha, light, overlay);
        texturedVertex(pose, consumer, renderX(facing, right3, back3), up3, renderZ(facing, right3, back3),
                u3, v3, normalX, normalUp, normalZ, red, green, blue, alpha, light, overlay);
    }

    private static void texturedVertex(PoseStack.Pose pose, VertexConsumer consumer,
                                       float x, float y, float z, float u, float v,
                                       float normalX, float normalY, float normalZ,
                                       int red, int green, int blue, int alpha, int light, int overlay) {
        consumer.addVertex(pose, x, y, z)
                .setColor(red, green, blue, alpha)
                .setUv(u, v)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, normalX, normalY, normalZ);
    }

    private static void colorVertex(PoseStack.Pose pose, VertexConsumer consumer,
                                    float x, float y, float z,
                                    int red, int green, int blue, int alpha) {
        consumer.addVertex(pose, x, y, z).setColor(red, green, blue, alpha);
    }

    private static float renderX(Direction facing, float right, float back) {
        Direction rightDirection = facing.getClockWise();
        Direction backDirection = facing.getOpposite();
        return 0.5F + rightDirection.getStepX() * right + backDirection.getStepX() * back;
    }

    private static float renderZ(Direction facing, float right, float back) {
        Direction rightDirection = facing.getClockWise();
        Direction backDirection = facing.getOpposite();
        return 0.5F + rightDirection.getStepZ() * right + backDirection.getStepZ() * back;
    }

    private static float vectorX(Direction facing, float right, float back) {
        return facing.getClockWise().getStepX() * right + facing.getOpposite().getStepX() * back;
    }

    private static float vectorZ(Direction facing, float right, float back) {
        return facing.getClockWise().getStepZ() * right + facing.getOpposite().getStepZ() * back;
    }
}
