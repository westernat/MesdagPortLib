package org.mesdag.portlib.wrapper;

import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {
    default T serializeNBT(PortRegistryAccess provider) {
        return serializeNBT();
    }

    default void deserializeNBT(PortRegistryAccess provider, T nbt) {
        deserializeNBT(nbt);
    }
}
