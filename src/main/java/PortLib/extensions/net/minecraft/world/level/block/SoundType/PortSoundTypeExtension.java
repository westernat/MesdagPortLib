package PortLib.extensions.net.minecraft.world.level.block.SoundType;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import org.mesdag.portlib.wrapper.sounds.PortSoundEvents;

public class PortSoundTypeExtension {
    private static final SoundType TUFF_BRICKS = new ForgeSoundType(
            1.0F,
            1.0F,
            PortSoundEvents.TUFF_BRICKS_BREAK,
            PortSoundEvents.TUFF_BRICKS_STEP,
            PortSoundEvents.TUFF_BRICKS_PLACE,
            PortSoundEvents.TUFF_BRICKS_HIT,
            PortSoundEvents.TUFF_BRICKS_FALL
    );

    public static SoundType tuffBricks() {
        return TUFF_BRICKS;
    }
}
