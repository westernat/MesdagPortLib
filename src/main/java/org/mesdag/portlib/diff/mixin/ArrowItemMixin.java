package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.ArrowItem;
import org.mesdag.portlib.wrapper.common.extensions.IPortArrowItemExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ArrowItem.class)
public abstract class ArrowItemMixin implements IPortArrowItemExtension {
}
