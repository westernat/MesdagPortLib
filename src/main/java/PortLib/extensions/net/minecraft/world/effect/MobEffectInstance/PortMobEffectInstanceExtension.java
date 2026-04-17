package PortLib.extensions.net.minecraft.world.effect.MobEffectInstance;

import com.mojang.serialization.Codec;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.util.PortSets;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;

@Extension
public class PortMobEffectInstanceExtension {
    private static final PortStreamCodec<RegistryFriendlyByteBuf, MobEffectInstance> STREAM_CODEC = PortStreamCodec.wrap(MobEffectInstance.STREAM_CODEC);

    public static Set<PortEffectCure> getPortCures(@This MobEffectInstance thiz) {
        return PortSets.mutableTransform(thiz.getCures(), PortEffectCure::wrap, PortEffectCure::unwrap);
    }

    @Extension
    public static Codec<MobEffectInstance> codec() {
        return MobEffectInstance.CODEC;
    }

    @SuppressWarnings("unchecked")
    @Extension
    public static PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffectInstance> streamCodec() {
        return (PortStreamCodec<PortRegistryFriendlyByteBuf, MobEffectInstance>) (PortStreamCodec<?, ?>) STREAM_CODEC;
    }
}
