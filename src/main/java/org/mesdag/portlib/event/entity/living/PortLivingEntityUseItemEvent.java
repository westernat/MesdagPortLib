package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingEntityUseItemEvent extends PortLivingEvent<LivingEntityUseItemEvent> {
    private PortLivingEntityUseItemEvent(LivingEntityUseItemEvent e) {
        super(e);
    }

    public ItemStack getItem() {
        return e.getItem();
    }

    public int getDuration() {
        return e.getDuration();
    }

    public void setDuration(int duration) {
        e.setDuration(duration);
    }

    public InteractionHand getHand() {
        return e.getHand();
    }

    public static class PortStart extends PortLivingEntityUseItemEvent implements IPortCancellableEvent {
        @Diff
        public PortStart(LivingEntityUseItemEvent.Start e) {
            super(e);
        }

        static {
            PortEventHooks.register(LivingEntityUseItemEvent.Start.class, PortStart.class, PortStart::new);
        }
    }

    public static class PortTick extends PortLivingEntityUseItemEvent implements IPortCancellableEvent {
        @Diff
        public PortTick(LivingEntityUseItemEvent.Tick e) {
            super(e);
        }

        static {
            PortEventHooks.register(LivingEntityUseItemEvent.Tick.class, PortTick.class, PortTick::new);
        }
    }

    public static class PortStop extends PortLivingEntityUseItemEvent implements IPortCancellableEvent {
        @Diff
        public PortStop(LivingEntityUseItemEvent.Stop e) {
            super(e);
        }

        static {
            PortEventHooks.register(LivingEntityUseItemEvent.Stop.class, PortStop.class, PortStop::new);
        }
    }

    public static class PortFinish extends PortLivingEntityUseItemEvent {
        private final LivingEntityUseItemEvent.Finish e;

        @Diff
        public PortFinish(LivingEntityUseItemEvent.Finish e) {
            super(e);
            this.e = e;
        }

        public ItemStack getResultStack() {
            return e.getItem();
        }

        public void setResultStack(ItemStack result) {
            e.setResultStack(result);
        }

        static {
            PortEventHooks.register(LivingEntityUseItemEvent.Finish.class, PortFinish.class, PortFinish::new);
        }
    }
}
