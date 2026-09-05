package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributesExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Attributes.class)
public abstract class AttributesMixin implements IPortAttributesExtension {
}
