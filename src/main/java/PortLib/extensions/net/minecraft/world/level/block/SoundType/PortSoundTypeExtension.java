package PortLib.extensions.net.minecraft.world.level.block.SoundType;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import org.mesdag.portlib.PortLib;

public class PortSoundTypeExtension {
    private static final SoundType TUFF_BRICKS = new ForgeSoundType(
            1.0F,
            1.0F,
            PortLib.TUFF_BRICKS_BREAK,
            PortLib.TUFF_BRICKS_STEP,
            PortLib.TUFF_BRICKS_PLACE,
            PortLib.TUFF_BRICKS_HIT,
            PortLib.TUFF_BRICKS_FALL
    );

    public static SoundType tuffBricks() {
        return TUFF_BRICKS;
    }
}
