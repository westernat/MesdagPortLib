package org.mesdag.portlib.wrapper;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {
    T serializeNBT(PortRegistryAccess provider);

    void deserializeNBT(PortRegistryAccess provider, T nbt);

    @Override
    default T serializeNBT() {
        return serializeNBT(new PortRegistryAccess());
    }

    @Override
    default void deserializeNBT(T nbt) {
        deserializeNBT(new PortRegistryAccess(), nbt);
    }
}
