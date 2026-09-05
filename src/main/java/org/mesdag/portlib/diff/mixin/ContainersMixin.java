package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.Containers;
import org.mesdag.portlib.wrapper.common.extensions.IPortContainersExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Containers.class)
public abstract class ContainersMixin implements IPortContainersExtension {
}
