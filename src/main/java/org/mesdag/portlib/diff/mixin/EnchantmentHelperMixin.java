package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.mesdag.portlib.wrapper.common.extensions.IPortEnchantmentHelperExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin implements IPortEnchantmentHelperExtension {
}
