package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IPortDamageSourceExtension {
    private DamageSource self() {
        return (DamageSource) this;
    }

    default @Nullable ItemStack getWeaponItem() {
        DamageSource self = self();
        return self.getDirectEntity() == null ? null : IPortEntityExtension.of(self.getDirectEntity()).getWeaponItem();
    }
}
