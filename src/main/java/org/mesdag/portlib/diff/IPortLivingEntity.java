package org.mesdag.portlib.diff;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extensions.IPortLivingEntityExtension;

import java.util.List;
import java.util.Stack;

@Diff
public interface IPortLivingEntity extends IPortLivingEntityExtension {
    void portlib$setDamageContainers(Stack<PortDamageContainer> damageContainers);

    Stack<PortDamageContainer> portlib$getDamageContainers();

    void portlib$setEffectParticles(List<ParticleOptions> list);

    List<ParticleOptions> portlib$getEffectParticles();

    void portlib$setDirty(boolean dirty);

    boolean portlib$isDirty();

    static IPortLivingEntity of(LivingEntity living) {
        return (IPortLivingEntity) living;
    }
}
