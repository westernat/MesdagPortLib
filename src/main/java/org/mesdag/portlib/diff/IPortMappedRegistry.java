package org.mesdag.portlib.diff;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.wrapper.PortSelfGetter;

import java.util.Map;

public interface IPortMappedRegistry<T> extends PortSelfGetter<MappedRegistry<T>> {
    void portlib$addAlias(ResourceLocation from, ResourceLocation to);

    Map<ResourceLocation, ResourceLocation> portlib$getAliaes();

    ResourceLocation portlib$resolve(ResourceLocation name);

    ResourceKey<T> portlib$resolve(ResourceKey<T> key);

    void onAdd(PortAddCallback.Vanilla<T> callback);

    @SuppressWarnings("unchecked")
    static <T> IPortMappedRegistry<T> of(MappedRegistry<T> registry) {
        return (IPortMappedRegistry<T>) registry;
    }
}
