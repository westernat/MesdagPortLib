package PortLib.extensions.net.minecraft.world.effect.MobEffect;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.PortEffectCures;

import java.util.Set;

public class PortMobEffectExtension {
    private static final Codec<Holder<MobEffect>> CODEC = BuiltInRegistries.MOB_EFFECT.holderByNameCodec();
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<MobEffect>> STREAM_CODEC = PortByteBufCodecs.holderRegistry(Registries.MOB_EFFECT);

    public static void fillPortEffectCures(MobEffect thiz, Set<PortEffectCure> cures, MobEffectInstance effectInstance) {
        cures.addAll(PortEffectCures.DEFAULT_CURES);
        if (thiz == MobEffects.POISON) {
            cures.add(PortEffectCures.HONEY);
        }
    }

    public static Codec<Holder<MobEffect>> codec() {
        return CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<MobEffect>> streamCodec() {
        return STREAM_CODEC;
    }
}
