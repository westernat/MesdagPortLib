package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.Item;
import org.mesdag.portlib.wrapper.common.extension.IPortItemExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class ItemMixin implements IPortItemExtension {}
