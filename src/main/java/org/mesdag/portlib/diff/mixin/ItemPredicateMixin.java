package org.mesdag.portlib.diff.mixin;

import net.minecraft.advancements.critereon.ItemPredicate;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemPredicateExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements IPortItemPredicateExtension {
}
