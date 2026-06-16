package org.mesdag.portlib.wrapper.world.effect;

import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class PortMobEffect extends MobEffect {
    private final Function<MobEffectInstance, ParticleOptions> particleFactory;
    private @Nullable Object2ObjectMap<UUID, Int2DoubleFunction> curves;

    public PortMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color);
        this.particleFactory = instance -> particle;
    }

    public PortMobEffect(MobEffectCategory category, int color) {
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
    public double getAttributeModifierValue(int amplifier, AttributeModifier modifier) {
        if (curves != null) {
            Int2DoubleFunction curve = curves.get(modifier.getId());
            if (curve != null) {
                return curve.get(amplifier);
            }
        }
        return super.getAttributeModifierValue(amplifier, modifier);
    }

    public ParticleOptions createParticleOptions(MobEffectInstance instance) {
        return particleFactory.apply(instance);
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        getAttributeModifiers().put(attribute, new AttributeModifier(PortAttributeModifier.rl2uuid(id), id.getPath(), amount, operation.unwrap()));
        return this;
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, ResourceLocation id, PortAttributeModifier.PortOperation operation, Int2DoubleFunction curve) {
        UUID uuid = PortAttributeModifier.rl2uuid(id);
        getAttributeModifiers().put(attribute, new AttributeModifier(uuid, id.getPath(), 0, operation.unwrap()));
        putCurve(uuid, curve);
        return this;
    }

    private void putCurve(UUID uuid, Int2DoubleFunction curve) {
        if (curves == null) {
            this.curves = new Object2ObjectOpenHashMap<>();
        }
        curves.put(uuid, curve);
    }

    public PortMobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        return addAttributeModifier(attribute.value(), id, amount, operation);
    }

    public PortMobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, PortAttributeModifier.PortOperation operation, Int2DoubleFunction curve) {
        return addAttributeModifier(attribute.value(), id, operation, curve);
    }

    public PortMobEffect addAttributeModifier(Attribute attribute, UUID uuid, String name, AttributeModifier.Operation operation, Int2DoubleFunction curve) {
        getAttributeModifiers().put(attribute, new AttributeModifier(uuid, name, 0, operation));
        putCurve(uuid, curve);
        return this;
    }
}
