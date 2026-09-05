package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;

public interface IPortUnitExtension {
    Codec<Unit> CODEC = Codec.unit(Unit.INSTANCE);
}
