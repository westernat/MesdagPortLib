package PortLib.extensions.net.minecraft.world.item.component.ItemAttributeModifiers;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

@Extension
public class PortItemAttributeModifiersExtension {
    @Diff
    public static PortItemAttributeModifiers wrap(@This ItemAttributeModifiers thiz) {
        return new PortItemAttributeModifiers(thiz);
    }
}
