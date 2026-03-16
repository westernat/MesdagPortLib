package org.mesdag.portlib.component;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;

public class PortDataComponentType<T> {
    @Diff
    public final @Nullable Codec<T> codec;
    // todo
    @Diff
    public final @Nullable PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

    PortDataComponentType(PortBuilder<T> builder) {
        this.codec = builder.codec;
        this.streamCodec = builder.streamCodec;
    }

    public static class PortBuilder<T> {
        private @Nullable Codec<T> codec;
        private @Nullable PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

        public PortBuilder<T> persistent(Codec<T> codec) {
            this.codec = codec;
            return this;
        }

        public PortBuilder<T> networkSynchronized(PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            this.streamCodec = streamCodec;
            return this;
        }

        public PortDataComponentType<T> build() {
            return new PortDataComponentType<>(this);
        }
    }
}
