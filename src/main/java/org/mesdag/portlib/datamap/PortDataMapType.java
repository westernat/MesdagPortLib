package org.mesdag.portlib.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public sealed class PortDataMapType<R, T> permits PortAdvancedDataMapType {
    protected final DataMapType<R, T> delegate;

    PortDataMapType(DataMapType<R, T> delegate) {
        this.delegate = delegate;
    }

    @Diff
    public DataMapType<R, T> unwrap() {
        return delegate;
    }

    @Diff
    public static <R, T> PortDataMapType<R, T> wrap(DataMapType<R, T> type) {
        return new PortDataMapType<>(type);
    }

    public static <T, R> PortBuilder<T, R> builder(PortIdentifier id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new PortBuilder<>(DataMapType.builder(id, registry, codec));
    }

    public ResourceKey<Registry<R>> registryKey() {
        return delegate.registryKey();
    }

    public PortIdentifier id() {
        return PortIdentifier.fromNamespaceAndPath(delegate.id().getNamespace(), delegate.id().getPath());
    }

    public Codec<T> codec() {
        return delegate.codec();
    }

    public @Nullable Codec<T> networkCodec() {
        return delegate.networkCodec();
    }

    public boolean mandatorySync() {
        return delegate.mandatorySync();
    }

    public static sealed class PortBuilder<T, R> permits PortAdvancedDataMapType.PortBuilder {
        protected final DataMapType.Builder<T, R> delegate;

        PortBuilder(DataMapType.Builder<T, R> delegate) {
            this.delegate = delegate;
        }

        @Diff
        public static <T, R> PortBuilder<T, R> wrap(DataMapType.Builder<T, R> type) {
            return new PortBuilder<>(type);
        }

        public PortBuilder<T, R> synced(Codec<T> networkCodec, boolean mandatory) {
            delegate.synced(networkCodec, mandatory);
            return this;
        }

        public PortDataMapType<R, T> build() {
            return new PortDataMapType<>(delegate.build());
        }
    }
}
