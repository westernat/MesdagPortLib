package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import org.mesdag.portlib.wrapper.sounds.PortSoundEvents;

public interface IPortSoundTypeExtension {
    SoundType TUFF_BRICKS = new ForgeSoundType(
            1.0F,
            1.0F,
            PortSoundEvents.TUFF_BRICKS_BREAK,
            PortSoundEvents.TUFF_BRICKS_STEP,
            PortSoundEvents.TUFF_BRICKS_PLACE,
            PortSoundEvents.TUFF_BRICKS_HIT,
            PortSoundEvents.TUFF_BRICKS_FALL
    );

}
