package org.mesdag.portlib.wrapper.core;

import net.minecraft.core.Holder;
import net.minecraftforge.registries.IForgeRegistry;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;

public interface PortHolder<T> extends Holder<T> {
    @Diff
    static <T> Holder<T> getDelegate(IForgeRegistry<T> registry, T value) {
        Optional<Holder<T>> optional = registry.getHolder(value);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Not registered: " + value);
        }
        return optional.get();
    }

    default boolean is(Holder<T> holder) {
        if (kind() == Kind.DIRECT) {
            return value().equals(holder.value());
        }
        return unwrapKey().map(holder::is).orElse(false);
    }
}
