package org.mesdag.portlib.diff.mixin;

import net.minecraft.advancements.critereon.EntityPredicate;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityPredicateExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityPredicate.class)
public abstract class EntityPredicateMixin implements IPortEntityPredicateExtension {
}
