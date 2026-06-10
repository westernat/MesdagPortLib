package PortLib.extensions.net.minecraft.world.effect.MobEffectInstance;

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
import org.mesdag.portlib.diff.IPortMobEffectInstance;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;

import java.util.Set;
import java.util.function.Supplier;

public class PortMobEffectInstanceExtension {
    private static final Codec<MobEffectInstance> CODEC = new Codec<>() {
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
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffectInstance> STREAM_CODEC = PortByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static Set<PortEffectCure> getPortCures(MobEffectInstance thiz) {
        return IPortMobEffectInstance.of(thiz).portlib$getCures();
    }

    public static Codec<MobEffectInstance> codec() {
        return CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffectInstance> streamCodec() {
        return STREAM_CODEC;
    }

    public static ParticleOptions getParticleOptions(MobEffectInstance thiz) {
        MobEffect effect = thiz.getEffect();
        if (effect instanceof PortMobEffect port) {
            return port.createParticleOptions(thiz);
        }
        return thiz.isAmbient() ? ParticleTypes.AMBIENT_ENTITY_EFFECT : ParticleTypes.ENTITY_EFFECT;
    }

    public static boolean is(MobEffectInstance thiz, Supplier<MobEffect> love) {
        return thiz.getEffect() == love.get();
    }
}
