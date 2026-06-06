package PortLib.extensions.net.minecraft.resources.ResourceKey;

import PortLib.extensions.net.minecraft.resources.ResourceLocation.PortResourceLocationExtension;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortResourceKeyExtension {
    public static <T> ResourceKey<Registry<T>> registryKey(ResourceKey<T> thiz) {
        return ResourceKey.createRegistryKey(thiz.registry());
    }

    public static <T> PortStreamCodec<ByteBuf, ResourceKey<T>> streamCodec(ResourceKey<? extends Registry<T>> registryKey) {
        return PortResourceLocationExtension.streamCodec().map(rl -> ResourceKey.create(registryKey, rl), ResourceKey::location);
    }
}
