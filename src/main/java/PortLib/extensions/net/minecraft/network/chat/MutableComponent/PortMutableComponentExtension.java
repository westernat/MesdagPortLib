package PortLib.extensions.net.minecraft.network.chat.MutableComponent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Extension
public class PortMutableComponentExtension {
    public static MutableComponent append(@This MutableComponent thiz, String string) {
        return string.isEmpty() ? thiz : thiz.append(Component.literal(string));
    }

    public static MutableComponent withColor(@This MutableComponent thiz, int color) {
        return thiz.setStyle(thiz.getStyle().withColor(color));
    }
}
