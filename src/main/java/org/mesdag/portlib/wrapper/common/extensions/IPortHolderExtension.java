package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;

/// implemented by coremod
@SuppressWarnings("unchecked")
public interface IPortHolderExtension<T> {
    private Holder<T> self() {
        return (Holder<T>) this;
    }

    default boolean is(Holder<T> holder) {
        if (self().kind() == Holder.Kind.DIRECT) {
            return self().value().equals(holder.value());
        }
        return self().unwrapKey().map(holder::is).orElse(false);
    }

    default String getRegisteredName() {
        return self().unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]");
    }

    default @Nullable ResourceKey<T> getKey() {
        return self().unwrapKey().orElse(null);
    }

    default HolderLookup.@Nullable RegistryLookup<T> unwrapLookup() {
        return self() instanceof Holder.Reference<T> ref
                ? ref.owner instanceof HolderLookup.RegistryLookup<T> rl ? rl : null
                : null;
    }

    default <A> @Nullable A getData(PortDataMapType<T, A> type) {
        if (self() instanceof Holder.Reference<T> reference && reference.owner instanceof HolderLookup.RegistryLookup<T> lookup) {
            return PortDataMapLoader.getInstance().getData(lookup.key(), type, getKey());
        }
        return null;
    }

    static <T> IPortHolderExtension<T> of(Holder<T> holder) {
        return (IPortHolderExtension<T>) holder;
    }
}
