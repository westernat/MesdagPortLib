package org.mesdag.portlib.datamap;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.Objects;

public sealed class DataMapType<R, T> permits AdvancedDataMapType {
    private final ResourceKey<Registry<R>> registryKey;
    private final PortIdentifier id;
    private final Codec<T> codec;
    private final @Nullable Codec<T> networkCodec;
    private final boolean mandatorySync;

    DataMapType(ResourceKey<Registry<R>> registryKey, PortIdentifier id, Codec<T> codec, @Nullable Codec<T> networkCodec, boolean mandatorySync) {
        Preconditions.checkArgument(networkCodec != null || !mandatorySync, "Mandatory sync cannot be enabled when the attachment isn't synchronized");

        this.registryKey = Objects.requireNonNull(registryKey, "registryKey must not be null");
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.codec = Objects.requireNonNull(codec, "codec must not be null");
        this.networkCodec = networkCodec;
        this.mandatorySync = mandatorySync;
    }

    public static <T, R> Builder<T, R> builder(PortIdentifier id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new Builder<>(registry, id, codec);
    }

    public ResourceKey<Registry<R>> registryKey() {
        return registryKey;
    }

    public PortIdentifier id() {
        return id;
    }

    public Codec<T> codec() {
        return codec;
    }

    public @Nullable Codec<T> networkCodec() {
        return networkCodec;
    }

    public boolean mandatorySync() {
        return mandatorySync;
    }

    public static sealed class Builder<T, R> permits AdvancedDataMapType.Builder {
        protected final ResourceKey<Registry<R>> registryKey;
        protected final PortIdentifier id;
        protected final Codec<T> codec;

        protected @Nullable Codec<T> networkCodec;
        protected boolean mandatorySync;

        Builder(ResourceKey<Registry<R>> registryKey, PortIdentifier id, Codec<T> codec) {
            this.registryKey = registryKey;
            this.id = id;
            this.codec = codec;
        }

        public Builder<T, R> synced(Codec<T> networkCodec, boolean mandatory) {
            this.mandatorySync = mandatory;
            this.networkCodec = networkCodec;
            return this;
        }

        public DataMapType<R, T> build() {
            return new DataMapType<>(registryKey, id, codec, networkCodec, mandatorySync);
        }
    }
}
