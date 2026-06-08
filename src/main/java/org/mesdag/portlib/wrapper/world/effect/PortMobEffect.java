package org.mesdag.portlib.wrapper.world.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.function.Function;

public class PortMobEffect extends MobEffect {
    private final Function<MobEffectInstance, ParticleOptions> particleFactory;

    public PortMobEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color);
        this.particleFactory = instance -> particle;
    }

    public PortMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.particleFactory = instance -> instance.isAmbient() ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
    }

    public ParticleOptions createParticleOptions(MobEffectInstance instance) {
        return particleFactory.apply(instance);
    }

    public PortMobEffect addAttributeModifier(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation) {
        AttributeModifier attributemodifier = new AttributeModifier(PortAttributeModifier.RL2UUID(id), id.getPath(), amount, operation.unwrap());
        getAttributeModifiers().put(attribute.value(), attributemodifier);
        return this;
    }
}
