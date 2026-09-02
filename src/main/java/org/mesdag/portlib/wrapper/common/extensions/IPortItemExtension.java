package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

@SuppressWarnings("all")
public interface IPortItemExtension {
    private Item self() {
        return (Item) this;
    }

    @ApiStatus.Internal
    default PortItemAttributeModifiers portlib$defaultAttributeModifiers() {
        return PortItemAttributeModifiers.EMPTY;
    }

    default PortItemAttributeModifiers getDefaultPortAttributeModifiers(ItemStack stack) {
        return portlib$defaultAttributeModifiers();
    }

    @SuppressWarnings("deprecation")
    default int getDefaultMaxStackSize() {
        return self().getMaxStackSize();
    }

    /// not replace vanilla yet
    default int getUseDuration(ItemStack stack, LivingEntity living) {
        return self().getUseDuration(stack);
    }

    static IPortItemExtension of(Item item) {
        return (IPortItemExtension) item;
    }
}
