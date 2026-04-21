package org.mesdag.portlib.wrapper.common.extension;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

public interface IPortLivingEntityExtension {
    private LivingEntity self() {
        return (LivingEntity) this;
    }

    default boolean onDamageTaken(PortDamageContainer container) {
        return true;
    }

    static IPortLivingEntityExtension of(LivingEntity living) {
        return (IPortLivingEntityExtension) living;
    }
}
