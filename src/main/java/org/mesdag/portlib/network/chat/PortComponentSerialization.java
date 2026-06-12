package org.mesdag.portlib.network.chat;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.network.chat.Component;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.Optional;

public class PortComponentSerialization {
    public static final Codec<Component> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<Component, T>> decode(DynamicOps<T> ops, T input) {
            Component component = Component.Serializer.fromJson(ops.convertTo(JsonOps.INSTANCE, input));
            return DataResult.success(new Pair<>(component, input), Lifecycle.stable());
        }

        @Override
        public <T> DataResult<T> encode(Component input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(JsonOps.INSTANCE.convertTo(ops, Component.Serializer.toJsonTree(input)), Lifecycle.stable());
        }
    };

    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, Component> STREAM_CODEC =
            PortByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, Optional<Component>> OPTIONAL_STREAM_CODEC =
            STREAM_CODEC.apply(PortByteBufCodecs::optional);

    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, Component> TRUSTED_STREAM_CODEC =
            PortByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC);

    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, Optional<Component>> TRUSTED_OPTIONAL_STREAM_CODEC =
            TRUSTED_STREAM_CODEC.apply(PortByteBufCodecs::optional);
}
