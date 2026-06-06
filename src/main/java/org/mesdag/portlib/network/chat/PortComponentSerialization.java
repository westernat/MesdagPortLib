package org.mesdag.portlib.network.chat;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.network.chat.Component;

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
}
