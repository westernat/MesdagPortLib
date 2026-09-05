package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.Timer;
import org.mesdag.portlib.wrapper.common.extensions.IPortTimerExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Timer.class)
public abstract class TimerMixin implements IPortTimerExtension {
}
