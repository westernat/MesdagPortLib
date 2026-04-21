package PortLib.extensions.net.minecraft.world.item.Item;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.wrapper.common.extension.IPortItemExtension;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

import java.util.function.Consumer;

@Extension
public class PortItemExtension {
    public static PortItemAttributeModifiers getDefaultPortAttributeModifiers(@This Item thiz, ItemStack stack) {
        return IPortItemExtension.of(thiz).getDefaultPortAttributeModifiers(stack);
    }

    public static class Properties {
        public static Item.Properties component(@This Item.Properties properties, Consumer<PortDataComponentMap.PortBuilder> consumer) {
            PortDataComponentMap.PortBuilder builder = new PortDataComponentMap.PortBuilder(properties);
            consumer.accept(builder);
            return properties;
        }
    }
}
