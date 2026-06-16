package PortLib.extensions.net.minecraft.core.HolderSet;

import net.minecraft.core.HolderSet;

public class PortHolderSetExtension {
    private static final HolderSet<?> EMPTY = HolderSet.direct();

    @SuppressWarnings("unchecked")
    public static <T> HolderSet<T> empty() {
        return (HolderSet<T>) EMPTY;
    }
}
