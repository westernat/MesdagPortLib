package PortLib.extensions.net.minecraft.resources.ResourceLocation;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

@Extension
public class PortResourceLocationExtension {
    public static PortIdentifier wrap(@This ResourceLocation thiz) {
        return PortIdentifier.fromNamespaceAndPath(thiz.getNamespace(), thiz.getPath());
    }
}
