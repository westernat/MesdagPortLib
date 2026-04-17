package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements CPortAttachmentHolder, PortSelfGetter<BlockEntity> {
    @Shadow
    @javax.annotation.Nullable
    protected Level level;

    @Shadow
    public abstract void setChanged();

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
        setChanged();
        return CPortAttachmentHolder.super.setData(type, data);
    }

    @Override
    public <T> @Nullable T removeData(PortAttachmentType<T> type) {
        setChanged();
        return CPortAttachmentHolder.super.removeData(type);
    }

    @Override
    public void syncData(PortAttachmentType<?> type) {
        PortAttachmentSync.syncBlockEntityUpdate(portlib$self(), type);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void saveAttachments(CompoundTag tag, CallbackInfo ci) {
        RegistryAccess registryAccess = level == null
                ? PortEnvironment.registryAccess()
                : level.registryAccess();
        var attachmentsTag = serializeAttachments(registryAccess);
        if (attachmentsTag != null) {
            tag.put(ATTACHMENTS_NBT_KEY, attachmentsTag);
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void loadAttachments(CompoundTag tag, CallbackInfo ci) {
        RegistryAccess registryAccess = level == null
                ? PortEnvironment.registryAccess()
                : level.registryAccess();
        if (tag.contains(ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND)) {
            deserializeAttachments(registryAccess, tag.getCompound(ATTACHMENTS_NBT_KEY));
        }
    }
}
