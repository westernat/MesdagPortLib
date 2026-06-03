package org.mesdag.portlib.wrapper.common.extensions;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

public interface IPortItemExtension {
    private Item self() {
        return (Item) this;
    }

    default PortItemAttributeModifiers getDefaultPortAttributeModifiers(ItemStack stack) {
        return PortItemAttributeModifiers.EMPTY;
    }

    static IPortItemExtension of(Item item) {
        return (IPortItemExtension) item;
    }
}
