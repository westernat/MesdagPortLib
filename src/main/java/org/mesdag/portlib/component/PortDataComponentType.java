package org.mesdag.portlib.component;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortDataComponentType<T> {
    @Diff
    public final @Nullable Codec<T> codec;
    // todo
    @Diff
    public final @Nullable PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

    PortDataComponentType(Builder<T> builder) {
        this.codec = builder.codec;
        this.streamCodec = builder.streamCodec;
    }

    public static class Builder<T> {
        private @Nullable Codec<T> codec;
        private @Nullable PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

        public Builder<T> persistent(Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        public Builder<T> networkSynchronized(PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public PortDataComponentType<T> build() {
            return new PortDataComponentType<>(this);
        }
    }
}
