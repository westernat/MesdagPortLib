package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.gui.GuiGraphics;
import org.mesdag.portlib.wrapper.common.extensions.IPortGuiGraphicsExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements IPortGuiGraphicsExtension {
}
