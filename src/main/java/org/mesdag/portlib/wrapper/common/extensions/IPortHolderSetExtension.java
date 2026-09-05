package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.HolderSet;

public interface IPortHolderSetExtension {
    HolderSet<?> EMPTY = HolderSet.direct();

    @SuppressWarnings("unchecked")
    static <T> HolderSet<T> empty() {
        return (HolderSet<T>) EMPTY;
    }
}
