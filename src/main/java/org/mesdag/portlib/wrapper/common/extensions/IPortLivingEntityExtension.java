package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

public interface IPortLivingEntityExtension extends PortSelfGetter<LivingEntity> {
    default void onDamageTaken(PortDamageContainer damageContainer) {}

    static IPortLivingEntityExtension of(LivingEntity living) {
        return living instanceof IPortLivingEntityExtension extension
                ? extension
                : new Delegate(living);
    }

    @Diff
    record Delegate(LivingEntity delegate) implements IPortLivingEntityExtension {
        @Override
        public LivingEntity portlib$self() {
            return delegate;
        }
    }
}
