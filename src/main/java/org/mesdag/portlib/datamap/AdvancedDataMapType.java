package org.mesdag.portlib.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.Objects;

public final class AdvancedDataMapType<R, T, VR extends DataMapValueRemover<R, T>> extends DataMapType<R, T> {
    private final Codec<VR> remover;
    private final DataMapValueMerger<R, T> merger;

    private AdvancedDataMapType(ResourceKey<Registry<R>> registryKey, PortIdentifier id, Codec<T> codec, @Nullable Codec<T> networkCodec, boolean mandatorySync, Codec<VR> remover, DataMapValueMerger<R, T> merger) {
        super(registryKey, id, codec, networkCodec, mandatorySync);
        this.remover = Objects.requireNonNull(remover, "remover must not be null");
        this.merger = Objects.requireNonNull(merger, "merger must not be null");
    }

    public Codec<VR> remover() {
        return remover;
    }

    public DataMapValueMerger<R, T> merger() {
        return merger;
    }

    public static <T, R> AdvancedDataMapType.Builder<T, R, DataMapValueRemover.Default<T, R>> builder(PortIdentifier id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new AdvancedDataMapType.Builder<>(registry, id, codec).remover(DataMapValueRemover.Default.codec());
    }

    public static final class Builder<T, R, VR extends DataMapValueRemover<R, T>> extends DataMapType.Builder<T, R> {
        private Codec<VR> remover;
        private DataMapValueMerger<R, T> merger = DataMapValueMerger.defaultMerger();

        Builder(ResourceKey<Registry<R>> registryKey, PortIdentifier id, Codec<T> codec) {
            super(registryKey, id, codec);
        }

        public <VR1 extends DataMapValueRemover<R, T>> AdvancedDataMapType.Builder<T, R, VR1> remover(Codec<VR1> remover) {
            this.remover = (Codec) remover;
            return (Builder<T, R, VR1>) this;
        }

        public AdvancedDataMapType.Builder<T, R, VR> merger(DataMapValueMerger<R, T> merger) {
            this.merger = merger;
            return this;
        }

        @Override
        public AdvancedDataMapType.Builder<T, R, VR> synced(Codec<T> networkCodec, boolean mandatory) {
            super.synced(networkCodec, mandatory);
            return this;
        }

        @Override
        public AdvancedDataMapType<R, T, VR> build() {
            return new AdvancedDataMapType<>(registryKey, id, codec, networkCodec, mandatorySync, remover, merger);
        }
    }
}
