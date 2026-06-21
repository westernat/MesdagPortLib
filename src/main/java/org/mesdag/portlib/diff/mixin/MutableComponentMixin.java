package org.mesdag.portlib.diff.mixin;

import net.minecraft.network.chat.MutableComponent;
import org.mesdag.portlib.wrapper.common.extensions.IPortMutableComponentExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MutableComponent.class)
public abstract class MutableComponentMixin implements IPortMutableComponentExtension {
}
