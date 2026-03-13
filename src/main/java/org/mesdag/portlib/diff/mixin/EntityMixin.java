package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentHolder;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.util.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityMixin implements PortAttachmentHolder {
    @Unique
    private @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments = null;

    @Override
    public @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments() {
        return portlib$attachments;
    }

    @Override
    public void portlib$attachments(Map<PortAttachmentType<?>, Object> map) {
        this.portlib$attachments = map;
    }

    @Final
    @Override
    public <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return PortAttachmentHolder.super.setData(type, data);
    }

    @Final
    @Override
    public void syncData(PortAttachmentType<?> type) {

    }
}
