package org.mesdag.portlib.datamap;

import org.jetbrains.annotations.Nullable;

public interface IWithData<R> {
    default <T> @Nullable T getData(DataMapType<R, T> type) {
        return null;
    }
}
