package org.mesdag.portlib.diff.mixin;

import net.minecraft.advancements.critereon.NbtPredicate;
import org.mesdag.portlib.wrapper.common.extensions.IPortNbtPredicateExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NbtPredicate.class)
public abstract class NbtPredicateMixin implements IPortNbtPredicateExtension {
}
