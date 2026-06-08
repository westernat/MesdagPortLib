package org.mesdag.portlib.wrapper.common.extensions;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

public interface IPortItemExtension {
    @ApiStatus.Internal
    default PortItemAttributeModifiers portlib$defaultAttributeModifiers() {
        return PortItemAttributeModifiers.EMPTY;
    }

    default PortItemAttributeModifiers getDefaultPortAttributeModifiers(ItemStack stack) {
        return portlib$defaultAttributeModifiers();
    }

    static IPortItemExtension of(Item item) {
        return (IPortItemExtension) item;
    }
}
