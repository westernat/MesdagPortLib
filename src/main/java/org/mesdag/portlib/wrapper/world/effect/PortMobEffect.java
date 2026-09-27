package org.mesdag.portlib.wrapper.world.effect;

import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectExtension;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.*;
import java.util.function.Function;

public class PortMobEffect extends MobEffect implements IPortMobEffectExtension {
    private final Function<MobEffectInstance, ParticleOptions> particleFactory;
    /// 不同属性允许共用修改器 ID，等级曲线必须绑定各自的修改器实例。
    private @Nullable Map<AttributeModifier, Int2DoubleFunction> curves;
    private Optional<SoundEvent> soundOnAdded = Optional.empty();

    protected PortMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color);
        this.particleFactory = instance -> particle;
    }

    protected PortMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.particleFactory = instance -> instance.isAmbient() ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
    }

    public boolean applyEffectTick1211(LivingEntity living, int amplifier) {
        applyEffectTick(living, amplifier);
        return true;
    }

    public void fillEffectCures(Set<PortEffectCure> cures, MobEffectInstance effectInstance) {}

    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return isDurationEffectTick(duration, amplifier);
    }

    @Override
    public void onEffectAdded(LivingEntity living, int amplifier) {
        soundOnAdded.ifPresent(event -> living.level().playSound(null, living.getX(), living.getY(), living.getZ(), event, living.getSoundSource(), 1.0F, 1.0F));
    }

    @Override
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (curves != null) {
            Int2DoubleFunction curve = curves.get(modifier);
            if (curve != null) {
                return curve.get(amplifier);
            }
        }
        return super.getAttributeModifierValue(amplifier, modifier);
    }

    public ParticleOptions createParticleOptions(MobEffectInstance instance) {
        return particleFactory.apply(instance);
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, ResourceLocation id, double amount, PortAttributeModifier.Operation operation) {
        replaceModifier(attribute, new AttributeModifier(PortAttributeModifier.rl2uuid(id), id.getPath(), amount, operation.unwrap()));
        return this;
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, ResourceLocation id, PortAttributeModifier.Operation operation, Int2DoubleFunction curve) {
        UUID uuid = PortAttributeModifier.rl2uuid(id);
        AttributeModifier modifier = new AttributeModifier(uuid, id.getPath(), 0, operation.unwrap());
        replaceModifier(attribute, modifier);
        putCurve(modifier, curve);
        return this;
    }

    /// 重复登记同一属性时清理旧曲线，固定数值登记也不会继承旧曲线。
    private void replaceModifier(Attribute attribute, AttributeModifier modifier) {
        AttributeModifier previous = getAttributeModifiers().put(attribute, modifier);
        if (curves != null && previous != null) curves.remove(previous);
    }

    private void putCurve(AttributeModifier modifier, Int2DoubleFunction curve) {
        if (curves == null) {
            this.curves = new IdentityHashMap<>();
        }
        curves.put(modifier, curve);
    }

    public PortMobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.Operation operation) {
        return addAttributeModifier(attribute.value(), id, amount, operation);
    }

    public PortMobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, PortAttributeModifier.Operation operation, Int2DoubleFunction curve) {
        return addAttributeModifier(attribute.value(), id, operation, curve);
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, UUID uuid, String name, AttributeModifier.Operation operation, Int2DoubleFunction curve) {
        AttributeModifier modifier = new AttributeModifier(uuid, name, 0, operation);
        replaceModifier(attribute, modifier);
        putCurve(modifier, curve);
        return this;
    }

    public PortMobEffect withSoundOnAdded(SoundEvent sound) {
        this.soundOnAdded = Optional.of(sound);
        return this;
    }
}
