package org.mesdag.portlib.diff.mixin;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import org.mesdag.portlib.wrapper.common.extensions.IPortStatePropertiesPredicateExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StatePropertiesPredicate.class)
public abstract class StatePropertiesPredicateMixin implements IPortStatePropertiesPredicateExtension {
}
