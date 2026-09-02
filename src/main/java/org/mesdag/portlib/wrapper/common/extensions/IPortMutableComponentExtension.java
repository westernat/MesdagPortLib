package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.network.chat.MutableComponent;

@SuppressWarnings("all")
public interface IPortMutableComponentExtension {
    private MutableComponent self() {
        return (MutableComponent) this;
    }

    default MutableComponent withColor(int color) {
        return self().setStyle(self().getStyle().withColor(color));
    }

    static IPortMutableComponentExtension of(MutableComponent component) {
        return (IPortMutableComponentExtension) component;
    }
}
