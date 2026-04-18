package org.mesdag.portlib.diff.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@org.spongepowered.asm.mixin.Mixin(value = net.minecraftforge.client.event.RegisterGuiOverlaysEvent.class, remap = false)
public interface RegisterGuiOverlaysEventAccessor {
    @Accessor
    Map<ResourceLocation, IGuiOverlay> getOverlays();

    @Accessor
    List<ResourceLocation> getOrderedOverlays();
}
