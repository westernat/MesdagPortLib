package org.mesdag.portlib.datamap;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueRemover;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public final class PortAdvancedDataMapType<R, T, VR extends PortDataMapValueRemover<R, T>> extends PortDataMapType<R, T> {
    private PortAdvancedDataMapType(AdvancedDataMapType<R, T, ? extends DataMapValueRemover<R, T>> delegate) {
        super(delegate);
    }

    @Diff
    @Override
    public AdvancedDataMapType<R, T, ? extends DataMapValueRemover<R, T>> unwrap() {
        return (AdvancedDataMapType<R, T, ? extends DataMapValueRemover<R, T>>) delegate;
    }

    @Diff
    public static <R, T, VR extends PortDataMapValueRemover<R, T>> PortAdvancedDataMapType<R, T, VR> wrap(AdvancedDataMapType<R, T, ? extends DataMapValueRemover<R, T>> delegate) {
        return new PortAdvancedDataMapType<>(delegate);
    }

    public Codec<VR> remover() {
        return (Codec<VR>) unwrap().remover();
    }

    public PortDataMapValueMerger<R, T> merger() {
        return PortDataMapValueMerger.wrap(unwrap().merger());
    }

    public static <T, R> PortBuilder<T, R, PortDataMapValueRemover.PortDefault<T, R>> builder(PortIdentifier id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new PortBuilder<>(AdvancedDataMapType.builder(id, registry, codec));
    }

    public static final class PortBuilder<T, R, VR extends PortDataMapValueRemover<R, T>> extends PortDataMapType.PortBuilder<T, R> {
        PortBuilder(AdvancedDataMapType.Builder<T, R, ? extends DataMapValueRemover<R, T>> delegate) {
            super(delegate);
        }

        public <VR1 extends PortDataMapValueRemover<R, T>> PortBuilder<T, R, VR1> remover(Codec<VR1> remover) {
            ((AdvancedDataMapType.Builder<T, R, ? extends DataMapValueRemover<R, T>>) delegate).remover((Codec<? extends DataMapValueRemover<R, T>>) remover);
            return (PortBuilder<T, R, VR1>) this;
        }

        public PortBuilder<T, R, VR> merger(PortDataMapValueMerger<R, T> merger) {
            ((AdvancedDataMapType.Builder<T, R, ?>) delegate).merger(merger.unwrap());
            return this;
        }

        @Override
        public PortAdvancedDataMapType<R, T, VR> build() {
            return PortAdvancedDataMapType.wrap(((AdvancedDataMapType.Builder<T, R, ? extends DataMapValueRemover<R, T>>) delegate).build());
        }
    }
}
