package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PortEntityAttachments {
    private final Map<PortEntityAttachment, List<Vec3>> attachments;

    PortEntityAttachments(Map<PortEntityAttachment, List<Vec3>> attachments) {
        this.attachments = attachments;
    }

    public static PortEntityAttachments createDefault(float width, float height) {
        return builder().build(width, height);
    }

    public static PortEntityAttachments.Builder builder() {
        return new PortEntityAttachments.Builder();
    }

    public PortEntityAttachments scale(float xScale, float yScale, float zScale) {
        Map<PortEntityAttachment, List<Vec3>> map = new EnumMap<>(PortEntityAttachment.class);

        for (Entry<PortEntityAttachment, List<Vec3>> entry : attachments.entrySet()) {
            map.put(entry.getKey(), scalePoints(entry.getValue(), xScale, yScale, zScale));
        }

        return new PortEntityAttachments(map);
    }

    private static List<Vec3> scalePoints(List<Vec3> attachmentPoints, float xScale, float yScale, float zScale) {
        List<Vec3> list = new ArrayList<>(attachmentPoints.size());

        for (Vec3 vec3 : attachmentPoints) {
            list.add(vec3.multiply(xScale, yScale, zScale));
        }

        return list;
    }

    @Nullable
    public Vec3 getNullable(PortEntityAttachment attachment, int index, float yRot) {
        List<Vec3> list = attachments.get(attachment);
        return index >= 0 && index < list.size() ? transformPoint(list.get(index), yRot) : null;
    }

    public Vec3 get(PortEntityAttachment attachment, int index, float yRot) {
        Vec3 vec3 = getNullable(attachment, index, yRot);
        if (vec3 == null) {
            throw new IllegalStateException("Had no attachment point of type: " + attachment + " for index: " + index);
        }
        return vec3;
    }

    public Vec3 getClamped(PortEntityAttachment attachment, int index, float yRot) {
        List<Vec3> list = attachments.get(attachment);
        if (list.isEmpty()) {
            throw new IllegalStateException("Had no attachment points of type: " + attachment);
        }
        Vec3 vec3 = list.get(Mth.clamp(index, 0, list.size() - 1));
        return transformPoint(vec3, yRot);
    }

    private static Vec3 transformPoint(Vec3 point, float yRot) {
        return point.yRot(-yRot * (float) (Math.PI / 180.0));
    }

    public static class Builder {
        private final Map<PortEntityAttachment, List<Vec3>> attachments = new EnumMap<>(PortEntityAttachment.class);

        Builder() {}

        public PortEntityAttachments.Builder attach(PortEntityAttachment attachment, float x, float y, float z) {
            return this.attach(attachment, new Vec3(x, y, z));
        }

        public PortEntityAttachments.Builder attach(PortEntityAttachment attachment, Vec3 poas) {
            this.attachments.computeIfAbsent(attachment, p_316616_ -> new ArrayList<>(1)).add(poas);
            return this;
        }

        public PortEntityAttachments build(float width, float height) {
            Map<PortEntityAttachment, List<Vec3>> map = new EnumMap<>(PortEntityAttachment.class);

            for (PortEntityAttachment attachment : PortEntityAttachment.values()) {
                List<Vec3> list = this.attachments.get(attachment);
                map.put(attachment, list != null ? List.copyOf(list) : attachment.createFallbackPoints(width, height));
            }

            return new PortEntityAttachments(map);
        }
    }
}
