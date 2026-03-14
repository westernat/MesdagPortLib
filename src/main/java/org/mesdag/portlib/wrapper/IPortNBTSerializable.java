package org.mesdag.portlib.wrapper;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {
    T serializeNBT(PortRegistryAccess provider);

    void deserializeNBT(PortRegistryAccess provider, T nbt);

    @Override
    @UnknownNullability
    default T serializeNBT(HolderLookup.Provider provider) {
        return serializeNBT(new PortRegistryAccess(provider));
    }

    @Override
    default void deserializeNBT(HolderLookup.Provider provider, T nbt) {
        deserializeNBT(new PortRegistryAccess(provider), nbt);
    }
}
