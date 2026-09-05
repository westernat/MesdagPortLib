package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.resources.model.ModelResourceLocation;
import org.mesdag.portlib.wrapper.common.extensions.IPortModelResourceLocationExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ModelResourceLocation.class)
public abstract class ModelResourceLocationMixin implements IPortModelResourceLocationExtension {
}
