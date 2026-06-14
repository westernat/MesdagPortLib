package PortLib.extensions.net.minecraft.world.item.Item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
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

    @SuppressWarnings("deprecation")
    public static int getDefaultMaxStackSize(Item thiz) {
        return thiz.getMaxStackSize();
    }

    public static class Properties {
        private static PortDataComponentMap.@NotNull PortBuilder getOrCreateBuilder(Item.Properties thiz) {
            IPortItem.IPortProperties port = IPortItem.IPortProperties.of(thiz);
            PortDataComponentMap.PortBuilder builder = port.portlib$getBuilder();
            if (builder == null) {
                port.portlib$set(builder = PortDataComponentMap.builder());
            }
            return builder;
        }

        public static <T> Item.Properties component(Item.Properties thiz, PortDataComponentType<T> type, T value) {
            getOrCreateBuilder(thiz).set(type, value);
            return thiz;
        }

        public static <T> Item.Properties component(Item.Properties thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
            return component(thiz, type.get(), value);
        }

        @SuppressWarnings("unchecked")
        @Diff
        public static <T> @Nullable T getComponent(Item.Properties thiz, PortDataComponentType<T> type) {
            PortDataComponentMap.PortBuilder builder = IPortItem.IPortProperties.of(thiz).portlib$getBuilder();
            if (builder == null) return null;
            return (T) builder.getMap().get(type);
        }

        @Diff
        public static <T> @Nullable T getComponent(Item.Properties thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
            return getComponent(thiz, type.get());
        }

        @Diff
        public static Item.Properties unbreakable(Item.Properties thiz) {
            getOrCreateBuilder(thiz).unbreakable();
            return thiz;
        }

        public static Item.Properties attributes(Item.Properties thiz, PortItemAttributeModifiers modifiers) {
            getOrCreateBuilder(thiz).setModifiers(modifiers);
            return thiz;
        }

        @Diff
        public static PortItemAttributeModifiers getAttributes(Item.Properties thiz) {
            return getOrCreateBuilder(thiz).getModifiers();
        }

        @Diff
        public static Item.Properties dyedColor(Item.Properties thiz, int rgb, boolean showInTooltip) {
            getOrCreateBuilder(thiz).dyedColor(rgb, showInTooltip);
            return thiz;
        }
    }
}
