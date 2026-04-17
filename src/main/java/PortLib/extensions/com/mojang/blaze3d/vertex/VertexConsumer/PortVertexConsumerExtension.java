package PortLib.extensions.com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

@Extension
public class PortVertexConsumerExtension {

    public static VertexConsumer addVertex(@This VertexConsumer thiz, float x, float y, float z) {
        return thiz.vertex(x, y, z);
    }

    public static VertexConsumer setColor(@This VertexConsumer thiz, int r, int g, int b, int a) {
        return thiz.color(r, g, b, a);
    }

    public static VertexConsumer setColor(@This VertexConsumer thiz, int color) {
        return thiz.color(color);
    }

    public static VertexConsumer setUv(@This VertexConsumer thiz, float u, float v) {
        return thiz.uv(u, v);
    }

    public static VertexConsumer setUv1(@This VertexConsumer thiz, int u, int v) {
        return thiz.overlayCoords(u, v);
    }

    public static VertexConsumer setUv2(@This VertexConsumer thiz, int u, int v) {
        return thiz.uv2(u, v);
    }

    public static VertexConsumer setNormal(@This VertexConsumer thiz, float x, float y, float z) {
        return thiz.normal(x, y, z);
    }

    public static VertexConsumer setLight(@This VertexConsumer thiz, int packedLight) {
        return thiz.uv2(packedLight);
    }

    public static VertexConsumer setOverlay(@This VertexConsumer thiz, int packedOverlay) {
        return thiz.overlayCoords(packedOverlay);
    }

    public static VertexConsumer setWhiteAlpha(@This VertexConsumer thiz, int alpha) {
        return thiz.color(FastColor.ARGB32.color(alpha, 255, 255, 255));
    }

    public static VertexConsumer addVertex(@This VertexConsumer thiz, PoseStack.Pose pose, Vector3f pos) {
        return thiz.addVertex(pose, pos.x(), pos.y(), pos.z());
    }

    public static VertexConsumer addVertex(@This VertexConsumer thiz, PoseStack.Pose pose, float x, float y, float z) {
        return thiz.addVertex(pose, x, y, z);
    }

    public static VertexConsumer addVertex(@This VertexConsumer thiz, Vector3f pos) {
        return thiz.addVertex(pos.x(), pos.y(), pos.z());
    }

    public static VertexConsumer addVertex(@This VertexConsumer thiz, Matrix4f pose, float x, float y, float z) {
        Vector4f vector4f = pose.transform(new Vector4f(x, y, z, 1.0F));
        return thiz.addVertex(vector4f.x(), vector4f.y(), vector4f.z());
    }
}