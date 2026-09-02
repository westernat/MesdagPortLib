package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@SuppressWarnings("all")
public interface IPortVertexConsumerExtension {
    private VertexConsumer self() {
        return (VertexConsumer) this;
    }

    default VertexConsumer addVertex(float x, float y, float z) {
        return self().vertex(x, y, z);
    }

    default VertexConsumer setColor(int r, int g, int b, int a) {
        return self().color(r, g, b, a);
    }

    default VertexConsumer setColor(int color) {
        return self().color(color);
    }

    default VertexConsumer setUv(float u, float v) {
        return self().uv(u, v);
    }

    default VertexConsumer setUv1(int u, int v) {
        return self().overlayCoords(u, v);
    }

    default VertexConsumer setUv2(int u, int v) {
        return self().uv2(u, v);
    }

    default VertexConsumer setNormal(float x, float y, float z) {
        return self().normal(x, y, z);
    }

    default VertexConsumer setLight(int packedLight) {
        return self().uv2(packedLight);
    }

    default VertexConsumer setOverlay(int packedOverlay) {
        return self().overlayCoords(packedOverlay);
    }

    default VertexConsumer setWhiteAlpha(int alpha) {
        return self().color(FastColor.ARGB32.color(alpha, 255, 255, 255));
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, Vector3f pos) {
        return addVertex(pose, pos.x, pos.y, pos.z);
    }

    default VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
        return addVertex(pose.pose(), x, y, z);
    }

    default VertexConsumer addVertex(Vector3f pos) {
        return addVertex(pos.x, pos.y, pos.z);
    }

    default VertexConsumer addVertex(Matrix4f pose, float x, float y, float z) {
        Vector4f vector4f = pose.transform(new Vector4f(x, y, z, 1.0F));
        return addVertex(vector4f.x, vector4f.y, vector4f.z);
    }

    default VertexConsumer setNormal(PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        return self().normal(pose.normal(), normalX, normalY, normalZ);
    }

    default void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float nx, float ny, float nz) {
        self().vertex(x, y, z)
                .color(color)
                .uv(u, v)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(nx, ny, nz)
                .endVertex();
    }

    static IPortVertexConsumerExtension of(VertexConsumer consumer) {
        return (IPortVertexConsumerExtension) consumer;
    }
}
