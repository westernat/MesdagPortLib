package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.IPortEntityDimensions;
import org.mesdag.portlib.diff.IPortEntityType;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachment;

public interface IPortEntityTypeBuilderExtension<T extends Entity> {
    @SuppressWarnings("unchecked")
    private EntityType.Builder<T> self() {
        return (EntityType.Builder<T>) this;
    }

    default EntityType.Builder<T> spawnDimensionsScale(float spawnDimensionsScale) {
        ((IPortEntityType.IPortBuilder<T>) this).portlib$setSpawnDimensionsScale(spawnDimensionsScale);
        return self();
    }

    default EntityType.Builder<T> eyeHeight(float eyeHeight) {
        EntityType.Builder<T> self = self();
        self.dimensions = IPortEntityDimensions.of(self.dimensions).withEyeHeight(eyeHeight);
        return self;
    }

    default EntityType.Builder<T> passengerAttachments(float... attachPoints) {
        IPortEntityType.IPortBuilder<T> builder = (IPortEntityType.IPortBuilder<T>) this;
        for (float point : attachPoints) {
            builder.portlib$withAttachments(attachments -> attachments.attach(PortEntityAttachment.PASSENGER, 0, point, 0));
        }

        return self();
    }

    default EntityType.Builder<T> passengerAttachments(Vec3... attachPoints) {
        IPortEntityType.IPortBuilder<T> builder = (IPortEntityType.IPortBuilder<T>) this;
        for (Vec3 point : attachPoints) {
            builder.portlib$withAttachments(attachments -> attachments.attach(PortEntityAttachment.PASSENGER, point));
        }

        return self();
    }

    default EntityType.Builder<T> vehicleAttachment(Vec3 attachPoint) {
        return this.attach(PortEntityAttachment.VEHICLE, attachPoint);
    }

    default EntityType.Builder<T> ridingOffset(float ridingOffset) {
        return this.attach(PortEntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F);
    }

    default EntityType.Builder<T> nameTagOffset(float nameTagOffset) {
        return this.attach(PortEntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F);
    }

    default EntityType.Builder<T> attach(PortEntityAttachment attachment, float x, float y, float z) {
        ((IPortEntityType.IPortBuilder<T>) this).portlib$withAttachments(attachments -> attachments.attach(attachment, x, y, z));
        return self();
    }

    default EntityType.Builder<T> attach(PortEntityAttachment attachment, Vec3 pos) {
        ((IPortEntityType.IPortBuilder<T>) this).portlib$withAttachments(attachments -> attachments.attach(attachment, pos));
        return self();
    }
}
