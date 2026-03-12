package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.world.item.component.MapPostProcessing;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

public enum PortMapPostProcessing {
    LOCK,
    SCALE;

    @Diff
    public static @Nullable PortMapPostProcessing wrap(MapPostProcessing processing) {
        return switch (processing) {
            case LOCK -> LOCK;
            case SCALE -> SCALE;
            default -> null;
        };
    }

    @Diff
    public MapPostProcessing unwrap() {
        return switch (this) {
            case LOCK -> MapPostProcessing.LOCK;
            case SCALE -> MapPostProcessing.SCALE;
        };
    }
}
