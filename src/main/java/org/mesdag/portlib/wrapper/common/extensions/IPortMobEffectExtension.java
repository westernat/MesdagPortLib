package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.PortEffectCures;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;

import java.util.Set;

@SuppressWarnings("all")
public interface IPortMobEffectExtension {
    Codec<MobEffect> DIRECT_CODEC = BuiltInRegistries.MOB_EFFECT.byNameCodec();
    Codec<Holder<MobEffect>> CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
    PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffect> DIRECT_STREAM_CODEC = PortByteBufCodecs.registry(Registries.MOB_EFFECT);
    PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<MobEffect>> STREAM_CODEC = PortByteBufCodecs.holderRegistry(Registries.MOB_EFFECT);

    default void onEffectStarted(LivingEntity living, int amplifier) {}

    default void onEffectAdded(LivingEntity living, int amplifier) {}

    default void fillPortEffectCures(Set<PortEffectCure> cures, MobEffectInstance effectInstance) {
        MobEffect thiz = self();
        cures.addAll(PortEffectCures.DEFAULT_CURES);
        if (thiz == MobEffects.POISON) {
            cures.add(PortEffectCures.HONEY);
        }
        if (thiz instanceof PortMobEffect port) {
            port.fillEffectCures(cures, effectInstance);
        }
    }

    private MobEffect self() {
        return (MobEffect) this;
    }

    static IPortMobEffectExtension of(MobEffect effect) {
        return (IPortMobEffectExtension) effect;
    }
}
