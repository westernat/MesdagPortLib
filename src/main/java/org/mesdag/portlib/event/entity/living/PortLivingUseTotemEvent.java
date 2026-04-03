package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingUseTotemEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingUseTotemEvent extends PortLivingEvent<LivingUseTotemEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingUseTotemEvent(LivingUseTotemEvent e) {
        super(e);
    }

    public DamageSource getSource() {
        return e.getSource();
    }

    public ItemStack getTotem() {
        return e.getTotem();
    }

    public InteractionHand getHandHolding() {
        return e.getHandHolding();
    }

    static {
        PortEventHooks.register(LivingUseTotemEvent.class, PortLivingUseTotemEvent.class, PortLivingUseTotemEvent::new);
    }
}
