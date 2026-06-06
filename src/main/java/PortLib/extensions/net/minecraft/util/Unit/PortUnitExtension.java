package PortLib.extensions.net.minecraft.util.Unit;

import com.mojang.serialization.Codec;
import net.minecraft.util.Unit;

public class PortUnitExtension {
    private static final Codec<Unit> CODEC = Codec.unit(Unit.INSTANCE);

    public static Codec<Unit> codec() {
        return CODEC;
    }
}
