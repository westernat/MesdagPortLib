package PortLib.extensions.com.mojang.blaze3d.vertex.VertexConsumer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.function.Consumer;

public class PortVertexConsumerExtension {
    public static VertexConsumer addVertex(VertexConsumer thiz, float x, float y, float z) {
        return thiz.vertex(x, y, z);
    }

    public static VertexConsumer setColor(VertexConsumer thiz, int r, int g, int b, int a) {
        return thiz.color(r, g, b, a);
    }

    public static VertexConsumer setColor(VertexConsumer thiz, int color) {
        return thiz.color(color);
    }

    public static VertexConsumer setUv(VertexConsumer thiz, float u, float v) {
        return thiz.uv(u, v);
    }

    public static VertexConsumer setUv1(VertexConsumer thiz, int u, int v) {
        return thiz.overlayCoords(u, v);
    }

    public static VertexConsumer setUv2(VertexConsumer thiz, int u, int v) {
        return thiz.uv2(u, v);
    }

    public static VertexConsumer setNormal(VertexConsumer thiz, float x, float y, float z) {
        return thiz.normal(x, y, z);
    }

    public static VertexConsumer setLight(VertexConsumer thiz, int packedLight) {
        return thiz.uv2(packedLight);
    }

    public static VertexConsumer setOverlay(VertexConsumer thiz, int packedOverlay) {
        return thiz.overlayCoords(packedOverlay);
    }

    public static VertexConsumer setWhiteAlpha(VertexConsumer thiz, int alpha) {
        return thiz.color(FastColor.ARGB32.color(alpha, 255, 255, 255));
    }

    public static VertexConsumer addVertex(VertexConsumer thiz, PoseStack.Pose pose, Vector3f pos) {
        return addVertex(thiz, pose, pos.x(), pos.y(), pos.z());
    }

    public static VertexConsumer addVertex(VertexConsumer thiz, PoseStack.Pose pose, float x, float y, float z) {
        return addVertex(thiz, pose.pose(), x, y, z);
    }

    public static VertexConsumer addVertex(VertexConsumer thiz, Vector3f pos) {
        return addVertex(thiz, pos.x(), pos.y(), pos.z());
    }

    public static VertexConsumer addVertex(VertexConsumer thiz, Matrix4f pose, float x, float y, float z) {
        Vector4f vector4f = pose.transform(new Vector4f(x, y, z, 1.0F));
        return addVertex(thiz, vector4f.x(), vector4f.y(), vector4f.z());
    }

    public static VertexConsumer setNormal(VertexConsumer thiz, PoseStack.Pose pose, float normalX, float normalY, float normalZ) {
        return thiz.normal(pose.normal(), normalX, normalY, normalZ);
    }

    public static void vertex(VertexConsumer thiz, PoseStack.Pose pose, float x, float y, float z, Consumer<VertexConsumer> builder) {
        VertexConsumer v = addVertex(thiz, pose, x, y, z);
        builder.accept(v);
        v.endVertex();
    }
}
