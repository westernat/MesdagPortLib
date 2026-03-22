package org.mesdag.portlib.diff;

import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.datamap.DataMapType;

@Diff
public interface IWithData<R> {
    default <T> @Nullable T getData(DataMapType<R, T> type) {
        return null;
    }
}
