package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.mesdag.portlib.wrapper.common.extensions.IPortInventoryScreenExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin implements IPortInventoryScreenExtension {
}
