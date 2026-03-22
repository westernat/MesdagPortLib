package org.mesdag.portlib.diff.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityMixin implements CPortAttachmentHolder, PortSelfGetter<Entity> {
    @Shadow
    private Level level;
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
        return CPortAttachmentHolder.super.setData(type, data);
    }

    @Final
    @Override
    public void syncData(PortAttachmentType<?> type) {
        PortAttachmentSync.syncEntityUpdate(portlib$self(), type);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void loadAttachments(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND)) {
            deserializeAttachments(new PortRegistryAccess(level.registryAccess()), compound.getCompound(ATTACHMENTS_NBT_KEY));
        }
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void saveAttachments(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag attachments = serializeAttachments(new PortRegistryAccess(level.registryAccess()));
        if (attachments != null) {
            compound.put(ATTACHMENTS_NBT_KEY, attachments);
        }
    }
}
