package org.mesdag.portlib.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class PortAdvancedDataMapType<R, T, VR extends PortDataMapValueRemover<R, T>> extends PortDataMapType<R, T> {
    private final Codec<VR> remover;
    private final PortDataMapValueMerger<R, T> merger;

    private PortAdvancedDataMapType(ResourceKey<Registry<R>> registryKey, ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec, boolean mandatorySync, Codec<VR> remover, PortDataMapValueMerger<R, T> merger) {
        super(registryKey, id, codec, networkCodec, mandatorySync);
        this.remover = Objects.requireNonNull(remover, "remover must not be null");
        this.merger = Objects.requireNonNull(merger, "merger must not be null");
    }

    public Codec<VR> remover() {
        return remover;
    }

    public PortDataMapValueMerger<R, T> merger() {
        return merger;
    }

    public static <T, R> Builder<T, R, PortDataMapValueRemover.PortDefault<T, R>> builder(ResourceLocation id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new Builder<>(registry, id, codec).remover(PortDataMapValueRemover.PortDefault.codec());
    }

    public static final class Builder<T, R, VR extends PortDataMapValueRemover<R, T>> extends PortDataMapType.Builder<T, R> {
        private Codec<VR> remover;
        private PortDataMapValueMerger<R, T> merger = PortDataMapValueMerger.defaultMerger();

        Builder(ResourceKey<Registry<R>> registryKey, ResourceLocation id, Codec<T> codec) {
            super(registryKey, id, codec);
        }

        @Override
        public Builder<T, R, VR> synced(Codec<T> networkCodec, boolean mandatory) {
            super.synced(networkCodec, mandatory);
            return this;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public <VR1 extends PortDataMapValueRemover<R, T>> Builder<T, R, VR1> remover(Codec<VR1> remover) {
            this.remover = (Codec) remover;
            return (Builder<T, R, VR1>) this;
        }

        public Builder<T, R, VR> merger(PortDataMapValueMerger<R, T> merger) {
            this.merger = merger;
            return this;
        }

        @Override
        public PortAdvancedDataMapType<R, T, VR> build() {
            return new PortAdvancedDataMapType<>(registryKey, id, codec, networkCodec, mandatorySync, remover, merger);
        }
    }
}
