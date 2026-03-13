package org.mesdag.portlib.wrapper;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("all")
public class PortIdentifier extends ResourceLocation {
    public PortIdentifier(String namespace, String path) {
        super(namespace, path);
    }

    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return new CustomPacketPayload.Type<>(this);
    }

    public static PortIdentifier parse(String location) {
        ResourceLocation rl = ResourceLocation.parse(location);
        return new PortIdentifier(rl.getNamespace(), rl.getPath());
    }

    public static PortIdentifier fromNamespaceAndPath(String namespace, String path) {
        return new PortIdentifier(namespace, path);
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