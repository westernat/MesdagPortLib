package PortLib.extensions.net.minecraft.world.item.Item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemExtension;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

public class PortItemExtension {
    public static PortItemAttributeModifiers getDefaultPortAttributeModifiers(Item thiz, ItemStack stack) {
        return IPortItemExtension.of(thiz).getDefaultPortAttributeModifiers(stack);
    }

    public static class Properties {
        public static <T> Item.Properties component(Item.Properties thiz, PortDataComponentType<T> type, T value) {
            IPortItem.IPortProperties port = IPortItem.IPortProperties.of(thiz);
            PortDataComponentMap.PortBuilder builder = port.portlib$get();
            if (builder == null) {
                port.portlib$set(builder = PortDataComponentMap.builder());
            }
            builder.set(type, value);
            return thiz;
        }

        public static <T> Item.Properties component(Item.Properties thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
            return component(thiz, type.get(), value);
        }

        @SuppressWarnings("unchecked")
        @Diff
        public static <T> @Nullable T getComponent(Item.Properties thiz, PortDataComponentType<T> type) {
            IPortItem.IPortProperties port = IPortItem.IPortProperties.of(thiz);
            PortDataComponentMap.PortBuilder builder = port.portlib$get();
            if (builder == null) return null;
            return (T) builder.getMap().get(type);
        }

        @Diff
        public static <T> @Nullable T getComponent(Item.Properties thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
            return getComponent(thiz, type.get());
        }
    }
}
