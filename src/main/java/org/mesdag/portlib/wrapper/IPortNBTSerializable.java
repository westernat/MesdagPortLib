package org.mesdag.portlib.wrapper;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {
    default T serializeNBT(HolderLookup.Provider provider) {
        return serializeNBT();
    }

    default void deserializeNBT(HolderLookup.Provider provider, T nbt) {
        deserializeNBT(nbt);
    }

    T serializeNBT();

    void deserializeNBT(T nbt);
}
