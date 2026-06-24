package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

@SuppressWarnings("all")
public interface IPortItemPropertiesExtension {

    private Item.Properties self() {
        return (Item.Properties) this;
    }

    default <T> Item.Properties component(PortDataComponentType<T> type, T value) {
        return PortItemExtension.Properties.component(self(), type, value);
    }

    default <T> Item.Properties component(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
        return PortItemExtension.Properties.component(self(), type, value);
    }

    @Diff
    default <T> @Nullable T getComponent(PortDataComponentType<T> type) {
        return PortItemExtension.Properties.getComponent(self(), type);
    }

    @Diff
    default <T> @Nullable T getComponent(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return PortItemExtension.Properties.getComponent(self(), type);
    }

    @Diff
    default Item.Properties unbreakable() {
        return PortItemExtension.Properties.unbreakable(self());
    }

    default Item.Properties attributes(PortItemAttributeModifiers modifiers) {
        return PortItemExtension.Properties.attributes(self(), modifiers);
    }

    @Diff
    default PortItemAttributeModifiers getAttributes() {
        return PortItemExtension.Properties.getAttributes(self());
    }

    @Diff
    default Item.Properties dyedColor(int rgb, boolean showInTooltip) {
        return PortItemExtension.Properties.dyedColor(self(), rgb, showInTooltip);
    }
}
