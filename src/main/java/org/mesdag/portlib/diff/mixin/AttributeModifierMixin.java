package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeModifierExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AttributeModifier.class)
public abstract class AttributeModifierMixin implements IPortAttributeModifierExtension {
}
