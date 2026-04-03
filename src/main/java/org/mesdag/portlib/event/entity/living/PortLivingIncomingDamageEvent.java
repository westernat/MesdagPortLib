package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.damagesource.IPortReductionFunction;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

@Cancelable
public class PortLivingIncomingDamageEvent extends LivingEvent {
    private final PortDamageContainer container;

    @Diff
    public PortLivingIncomingDamageEvent(LivingEntity entity, PortDamageContainer container) {
        super(entity);
        this.container = container;
    }

    public PortDamageContainer getContainer() {
        return container;
    }

    public DamageSource getSource() {
        return container.getSource();
    }

    public float getAmount() {
        return container.getNewDamage();
    }

    public float getOriginalAmount() {
        return container.getOriginalDamage();
    }

    public void setAmount(float newDamage) {
        container.setNewDamage(newDamage);
    }

    public void addReductionModifier(PortDamageContainer.PortReduction type, IPortReductionFunction reductionFunc) {
        container.addModifier(type, reductionFunc);
    }

    public void setInvulnerabilityTicks(int ticks) {
        container.setPostAttackInvulnerabilityTicks(ticks);
    }
}
