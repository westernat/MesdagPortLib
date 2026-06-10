package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.CrossbowItem;
import org.mesdag.portlib.wrapper.common.extensions.IPortCrossbowItemExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrossbowItem.class)
public class CrossBowItemMixin implements IPortCrossbowItemExtension {
}
