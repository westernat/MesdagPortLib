package org.mesdag.portlib.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortDataComponentType<T> {
    private final DataComponentType<T> delegate;

    PortDataComponentType(DataComponentType<T> delegate) {
        this.delegate = delegate;
    }

    @Diff
    public DataComponentType<T> unwrap() {
        return delegate;
    }

    @Diff
    public static <T> PortDataComponentType<T> wrap(DataComponentType<T> delegate) {
        return new PortDataComponentType<>(delegate);
    }

    public static class PortBuilder<T> {
        private final DataComponentType.Builder<T> builder = new DataComponentType.Builder<>();

        @Nullable PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

        public PortBuilder<T> persistent(Codec<T> codec) {
            builder.persistent(codec);
            return this;
        }

        public PortBuilder<T> networkSynchronized(PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            builder.networkSynchronized((StreamCodec<? super RegistryFriendlyByteBuf, T>) streamCodec.unwrap());
            return this;
        }

        public PortDataComponentType<T> build() {
            return new PortDataComponentType<>(builder.build());
        }
    }
}
