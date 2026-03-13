package org.mesdag.portlib.wrapper;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("all")
public class PortIdentifier extends ResourceLocation {

    public PortIdentifier(String namespace, String path) {
        super(namespace, path);
    }

    public <T> Type<T> getType() {
        return new Type<>(this);
    }

    public record Type<T>(ResourceLocation id) {}

    public static PortIdentifier parse(String location) {
        ResourceLocation rl = new ResourceLocation(location);
        return new PortIdentifier(rl.getNamespace(), rl.getPath());
    }

    public static PortIdentifier fromNamespaceAndPath(String namespace, String path) {
        return new PortIdentifier(namespace, path);
    }

    @Nullable
    public static PortIdentifier tryParse(String location) {
        ResourceLocation rl = ResourceLocation.tryParse(location);
        return rl == null ? null : new PortIdentifier(rl.getNamespace(), rl.getPath());
    }

    @Override
    public PortIdentifier withPath(String path) {
        return new PortIdentifier(this.getNamespace(), path);
    }

    @Override
    public PortIdentifier withPrefix(String prefix) {
        return new PortIdentifier(this.getNamespace(), prefix + this.getPath());
    }

    @Override
    public PortIdentifier withSuffix(String suffix) {
        return new PortIdentifier(this.getNamespace(), this.getPath() + suffix);
    }
}