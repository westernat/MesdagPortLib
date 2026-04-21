package PortLib.extensions.net.minecraft.network.chat.MutableComponent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.chat.MutableComponent;

@Extension
public class PortMutableComponentExtension {
    public static MutableComponent withColor(@This MutableComponent thiz, int color) {
        return thiz.setStyle(thiz.getStyle().withColor(color));
    }
}
