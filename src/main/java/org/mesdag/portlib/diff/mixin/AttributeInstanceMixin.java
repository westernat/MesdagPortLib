package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeInstanceExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements IPortAttributeInstanceExtension {
}
