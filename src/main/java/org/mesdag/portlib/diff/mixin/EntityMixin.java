package org.mesdag.portlib.diff.mixin;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.IPortEntity;
import org.mesdag.portlib.diff.IPortEntityDimensions;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.PortEntityInvulnerabilityCheckEvent;
import org.mesdag.portlib.event.tick.PortEntityTickEvent;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Entity.class)
public abstract class EntityMixin implements IPortEntity, PortSelfGetter<Entity> {
    @Shadow
    private Level level;
    @Shadow
    public EntityDimensions dimensions;
    @Unique
    private @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments;

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
    public void portlib$syncAttach(PortAttachmentType<?> type) {
        PortAttachmentSync.syncEntityUpdate(portlib$self(), type);
    }

    @Override
    public PortEntityAttachments portlib$getAttachments() {
        return IPortEntityDimensions.of(dimensions).portlib$getAttachments();
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void loadAttachments(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND)) {
            deserializeAttachments(level.registryAccess(), compound.getCompound(ATTACHMENTS_NBT_KEY));
        }
    }

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void saveAttachments(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        CompoundTag attachments = serializeAttachments(level.registryAccess());
        if (attachments != null) {
            compound.put(ATTACHMENTS_NBT_KEY, attachments);
        }
    }

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean isEntityInvulnerableTo(boolean original, @Local(argsOnly = true) DamageSource source) {
        return PortEventHandler.postEventWithReturn(new PortEntityInvulnerabilityCheckEvent(portlib$self(), source, original)).isInvulnerable();
    }

    @WrapOperation(method = "rideTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void fireEntityTick(Entity instance, Operation<Void> original) {
        if (!PortEventHandler.postEventWithReturn(new PortEntityTickEvent.Pre(instance)).isCanceled()) {
            original.call(instance);
            PortEventHandler.postEvent(new PortEntityTickEvent.Post(instance));
        }
    }

    // region attributes

    @ModifyVariable(method = "setRemainingFireTicks", at = @At("HEAD"), argsOnly = true)
    private int applyBurningTime(int ticks) {
        if (portlib$self() instanceof LivingEntity living) {
            return (int) (ticks * living.getAttributeValue(PortAttributesExtension.burningTime()));
        }
        return ticks;
    }

    /**
     * 返回 1.21 总值语义的跨步高度，并兼容只面向 Forge 1.20 增量属性的模组。
     *
     * <p>Forge 把 {@code getStepHeight} 作为接口默认方法提供，目标类没有可注入的方法体，
     * 因此 Mixin 在实体类上补充同签名实现来覆盖默认方法。</p>
     */
    public float getStepHeight() {
        Entity self = portlib$self();
        if (!(self instanceof LivingEntity living)) {
            return self.maxUpStep();
        }
        AttributeInstance totalHeight = living.getAttribute(
                PortAttributesExtension.stepHeight().value());
        float baseHeight = totalHeight == null
                ? living.maxUpStep()
                : (float) totalHeight.getValue();
        AttributeInstance forgeAddition = living.getAttribute(
                ForgeMod.STEP_HEIGHT_ADDITION.get());
        float addition = forgeAddition == null
                ? 0.0F
                : (float) forgeAddition.getValue();
        return Math.max(0.0F, baseHeight + addition);
    }

    // endregion attributes
}
