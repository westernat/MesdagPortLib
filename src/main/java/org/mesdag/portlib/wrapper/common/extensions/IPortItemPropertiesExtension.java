package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

@SuppressWarnings("all")
public interface IPortItemPropertiesExtension {
    private Item.Properties self() {
        return (Item.Properties) this;
    }

    private static PortDataComponentMap.Builder getOrCreateBuilder(Item.Properties thiz) {
        IPortItem.IPortProperties port = IPortItem.IPortProperties.of(thiz);
        PortDataComponentMap.Builder builder = port.portlib$getBuilder();
        if (builder == null) {
            port.portlib$set(builder = PortDataComponentMap.builder());
        }
        return builder;
    }

    default <T> Item.Properties component(PortDataComponentType<T> type, T value) {
        getOrCreateBuilder(self()).set(type, value);
        return self();
    }

    default <T> Item.Properties component(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
        return component(type.get(), value);
    }

    @Diff
    default <T> @Nullable T getComponent(PortDataComponentType<T> type) {
        PortDataComponentMap.Builder builder = IPortItem.IPortProperties.of(self()).portlib$getBuilder();
        if (builder == null) return null;
        return (T) builder.getMap().get(type);
    }

    @Diff
    default <T> @Nullable T getComponent(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return getComponent(type.get());
    }

    @Diff
    default Item.Properties unbreakable() {
        getOrCreateBuilder(self()).unbreakable();
        return self();
    }

    default Item.Properties attributes(PortItemAttributeModifiers modifiers) {
        getOrCreateBuilder(self()).setModifiers(modifiers);
        return self();
    }

    @Diff
    default PortItemAttributeModifiers getAttributes() {
        return getOrCreateBuilder(self()).getModifiers();
    }

    @Diff
    default Item.Properties dyedColor(int rgb, boolean showInTooltip) {
        getOrCreateBuilder(self()).dyedColor(rgb, showInTooltip);
        return self();
    }
}
