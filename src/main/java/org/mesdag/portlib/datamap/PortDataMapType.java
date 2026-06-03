package org.mesdag.portlib.datamap;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public sealed class PortDataMapType<R, T> permits PortAdvancedDataMapType {
    private final ResourceKey<Registry<R>> registryKey;
    private final ResourceLocation id;
    private final Codec<T> codec;
    private final @Nullable Codec<T> networkCodec;
    private final boolean mandatorySync;

    PortDataMapType(ResourceKey<Registry<R>> registryKey, ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec, boolean mandatorySync) {
        Preconditions.checkArgument(networkCodec != null || !mandatorySync, "Mandatory sync cannot be enabled when the attachment isn't synchronized");

        this.registryKey = Objects.requireNonNull(registryKey, "registryKey must not be null");
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.codec = Objects.requireNonNull(codec, "codec must not be null");
        this.networkCodec = networkCodec;
        this.mandatorySync = mandatorySync;
    }

    public static <T, R> PortBuilder<T, R> builder(ResourceLocation id, ResourceKey<Registry<R>> registry, Codec<T> codec) {
        return new PortBuilder<>(registry, id, codec);
    }

    public ResourceKey<Registry<R>> registryKey() {
        return registryKey;
    }

    public ResourceLocation id() {
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

    public static sealed class PortBuilder<T, R> permits PortAdvancedDataMapType.PortBuilder {
        protected final ResourceKey<Registry<R>> registryKey;
        protected final ResourceLocation id;
        protected final Codec<T> codec;

        protected @Nullable Codec<T> networkCodec;
        protected boolean mandatorySync;

        PortBuilder(ResourceKey<Registry<R>> registryKey, ResourceLocation id, Codec<T> codec) {
            this.registryKey = registryKey;
            this.id = id;
            this.codec = codec;
        }

        public PortBuilder<T, R> synced(Codec<T> networkCodec, boolean mandatory) {
            this.mandatorySync = mandatory;
            this.networkCodec = networkCodec;
            return this;
        }

        public PortDataMapType<R, T> build() {
            return new PortDataMapType<>(registryKey, id, codec, networkCodec, mandatorySync);
        }
    }
}
