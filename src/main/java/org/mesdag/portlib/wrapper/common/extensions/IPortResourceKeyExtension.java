package org.mesdag.portlib.wrapper.common.extensions;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@SuppressWarnings("all")
public interface IPortResourceKeyExtension<T> {
    @SuppressWarnings("unchecked")
    private ResourceKey<T> self() {
        return (ResourceKey<T>) (Object) this;
    }

    default ResourceKey<Registry<T>> registryKey() {
        return ResourceKey.createRegistryKey(self().registry());
    }

    static <T> PortStreamCodec<ByteBuf, ResourceKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryKey) {
        return IPortResourceLocationExtension.STREAM_CODEC.map(rl -> ResourceKey.create(registryKey, rl), ResourceKey::location);
    }

    static <T> IPortResourceKeyExtension<T> of(ResourceKey<T> key) {
        return (IPortResourceKeyExtension<T>) key;
    }
}
