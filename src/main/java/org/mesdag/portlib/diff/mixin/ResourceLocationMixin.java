package org.mesdag.portlib.diff.mixin;

import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.wrapper.common.extensions.IPortResourceLocationExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ResourceLocation.class)
public abstract class ResourceLocationMixin implements IPortResourceLocationExtension {
}
