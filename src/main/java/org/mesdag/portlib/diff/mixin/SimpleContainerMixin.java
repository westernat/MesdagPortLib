package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.SimpleContainer;
import org.mesdag.portlib.wrapper.common.extensions.ISimpleContainerExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SimpleContainer.class)
public class SimpleContainerMixin implements ISimpleContainerExtension {
}
