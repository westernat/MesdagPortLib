package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;

import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortMobEffectInstanceExtension {
    Codec<MobEffectInstance> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<MobEffectInstance, T>> decode(DynamicOps<T> ops, T input) {
            Tag tag = ops.convertTo(NbtOps.INSTANCE, input);
            if (tag instanceof CompoundTag compoundTag) {
                MobEffectInstance instance = MobEffectInstance.load(compoundTag);
                if (instance == null) {
                    return DataResult.error(() -> "Failed to load MobEffectInstance from NBT: " + compoundTag);
                }
                return DataResult.success(Pair.of(instance, input));
            }
            return DataResult.error(() -> "Not a map: " + tag);
        }

        @Override
        public <T> DataResult<T> encode(MobEffectInstance input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(NbtOps.INSTANCE.convertTo(ops, input.save(new CompoundTag())));
        }
    };
    PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffectInstance> STREAM_CODEC = PortByteBufCodecs.fromCodecWithRegistries(CODEC);

    private MobEffectInstance self() {
        return (MobEffectInstance) this;
    }

    default ParticleOptions getParticleOptions() {
        if (self().getEffect() instanceof PortMobEffect port) {
            return port.createParticleOptions(self());
        }
        return self().isAmbient() ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
    }

    default boolean is(Supplier<MobEffect> effectSupplier) {
        return self().getEffect() == effectSupplier.get();
    }

    default Set<PortEffectCure> getCures() {
        return IPortMobEffectInstance.of(self()).portlib$getCures();
    }

    default void onEffectStarted(LivingEntity living) {
        IPortMobEffectExtension.of(self().getEffect()).onEffectStarted(living, self().getAmplifier());
    }

    default void onEffectAdded(LivingEntity living) {
        IPortMobEffectExtension.of(self().getEffect()).onEffectAdded(living, self().getAmplifier());
    }

    static IPortMobEffectInstanceExtension of(MobEffectInstance instance) {
        return (IPortMobEffectInstanceExtension) instance;
    }
}
