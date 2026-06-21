package org.mesdag.portlib.diff;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.extensions.IPortAbstractArrowExtension;

@Diff
public interface IPortAbstractArrow extends IPortAbstractArrowExtension {
    ItemStack portlib$getPickupItem();

    void portlib$setPickupItem(ItemStack stack);

    @Nullable ItemStack portlib$getFiredFromWeapon();

    void portlib$setFiredFromWeapon(@Nullable ItemStack stack);

    static IPortAbstractArrow of(AbstractArrow arrow) {
        return (IPortAbstractArrow) arrow;
    }
}
