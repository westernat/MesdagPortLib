package PortLib.extensions.net.minecraft.core.Holder;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public class PortHolderExtension {
    public static <T> boolean is(Holder<T> thiz, Holder<T> holder) {
        if (thiz.kind() == Holder.Kind.DIRECT) {
            return thiz.value().equals(holder.value());
        }
        return thiz.unwrapKey().map(holder::is).orElse(false);
    }

    public static <T> String getRegisteredName(Holder<T> thiz) {
        return thiz.unwrapKey().map(key -> key.location().toString()).orElse("[unregistered]");
    }

    public static <T> @Nullable ResourceKey<T> getKey(Holder<T> thiz) {
        return thiz.unwrapKey().orElse(null);
    }

    public static <T> HolderLookup.@Nullable RegistryLookup<T> unwrapLookup(Holder<T> thiz) {
        return thiz instanceof Holder.Reference<T> ref
                ? ref.owner instanceof HolderLookup.RegistryLookup<T> rl ? rl : null
                : null;
    }
}
