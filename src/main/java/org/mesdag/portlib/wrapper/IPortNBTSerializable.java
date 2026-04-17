package org.mesdag.portlib.wrapper;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {
    T serializeNBT(HolderLookup.Provider provider);

    void deserializeNBT(HolderLookup.Provider provider, T nbt);

    @Override
    default T serializeNBT() {
        return serializeNBT(PortEnvironment.registryAccess());
    }

    @Override
    default void deserializeNBT(T nbt) {
        deserializeNBT(PortEnvironment.registryAccess(), nbt);
    }
}
