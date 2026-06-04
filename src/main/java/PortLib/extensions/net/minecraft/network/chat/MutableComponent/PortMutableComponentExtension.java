package PortLib.extensions.net.minecraft.network.chat.MutableComponent;

import net.minecraft.network.chat.MutableComponent;

public class PortMutableComponentExtension {
    public static MutableComponent withColor(MutableComponent thiz, int color) {
        return thiz.setStyle(thiz.getStyle().withColor(color));
    }
}
