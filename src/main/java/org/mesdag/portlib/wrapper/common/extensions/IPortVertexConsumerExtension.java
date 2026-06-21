package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.blaze3d.vertex.VertexConsumer.PortVertexConsumerExtension;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@SuppressWarnings("all")
public interface IPortVertexConsumerExtension {

    private VertexConsumer self() {
        return (VertexConsumer) this;
    }

    default VertexConsumer addVertex(float x, float y, float z) {
        return PortVertexConsumerExtension.addVertex(self(), x, y, z);
    }

    default VertexConsumer setColor(int r, int g, int b, int a) {
        return PortVertexConsumerExtension.setColor(self(), r, g, b, a);
    }

    default VertexConsumer setColor(int color) {
        return PortVertexConsumerExtension.setColor(self(), color);
    }

    default VertexConsumer setUv(float u, float v) {
        return PortVertexConsumerExtension.setUv(self(), u, v);
    }

    default VertexConsumer setUv1(int u, int v) {
        return PortVertexConsumerExtension.setUv1(self(), u, v);
    }

    default VertexConsumer setUv2(int u, int v) {
        return PortVertexConsumerExtension.setUv2(self(), u, v);
    }

    default VertexConsumer setNormal(float x, float y, float z) {
        return PortVertexConsumerExtension.setNormal(self(), x, y, z);
    }

    default VertexConsumer setLight(int packedLight) {
        return PortVertexConsumerExtension.setLight(self(), packedLight);
    }

    default VertexConsumer setOverlay(int packedOverlay) {
        return PortVertexConsumerExtension.setOverlay(self(), packedOverlay);
    }

    default VertexConsumer setWhiteAlpha(int alpha) {
        return PortVertexConsumerExtension.setWhiteAlpha(self(), alpha);
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, Vector3f pos) {
        return PortVertexConsumerExtension.addVertex(self(), pose, pos);
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
        return PortVertexConsumerExtension.addVertex(self(), pose, x, y, z);
    }

    default VertexConsumer addVertex(Vector3f pos) {
        return PortVertexConsumerExtension.addVertex(self(), pos);
    }

    default VertexConsumer addVertex(Matrix4f pose, float x, float y, float z) {
        return PortVertexConsumerExtension.addVertex(self(), pose, x, y, z);
    }

    default VertexConsumer setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        return PortVertexConsumerExtension.setNormal(self(), pose, normalX, normalY, normalZ);
    }

    default void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float nx, float ny, float nz) {
        PortVertexConsumerExtension.vertex(self(), x, y, z, color, u, v, overlay, light, nx, ny, nz);
    }

    static IPortVertexConsumerExtension of(VertexConsumer consumer) {
        return (IPortVertexConsumerExtension) consumer;
    }
}
