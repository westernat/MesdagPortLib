package org.mesdag.portlib.diff;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

import java.util.Stack;

public interface IPortLivingEntity extends PortSelfGetter<LivingEntity> {
    void portlib$setDamageContainers(Stack<PortDamageContainer> damageContainers);

    Stack<PortDamageContainer> portlib$getDamageContainers();

    static IPortLivingEntity of(LivingEntity living) {
        return (IPortLivingEntity) living;
    }
}
