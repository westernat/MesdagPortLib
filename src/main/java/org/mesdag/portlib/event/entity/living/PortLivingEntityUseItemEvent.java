package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingEntityUseItemEvent<E extends LivingEntityUseItemEvent> extends PortLivingEvent<E> {
    private PortLivingEntityUseItemEvent(E e) {
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
        return e.getEntity().getUsedItemHand();
    }

    public static class Start extends PortLivingEntityUseItemEvent<LivingEntityUseItemEvent.Start> implements IPortCancellableEvent {
        @Diff
        public Start(LivingEntityUseItemEvent.Start e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Tick extends PortLivingEntityUseItemEvent<LivingEntityUseItemEvent.Tick> implements IPortCancellableEvent {
        @Diff
        public Tick(LivingEntityUseItemEvent.Tick e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Stop extends PortLivingEntityUseItemEvent<LivingEntityUseItemEvent.Stop> implements IPortCancellableEvent {
        @Diff
        public Stop(LivingEntityUseItemEvent.Stop e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Finish extends PortLivingEntityUseItemEvent<LivingEntityUseItemEvent.Finish> {
        @Diff
        public Finish(LivingEntityUseItemEvent.Finish e) {
            super(e);
        }

        public ItemStack getResultStack() {
            return e.getResultStack();
        }

        public void setResultStack(ItemStack result) {
            e.setResultStack(result);
        }

        static {
            PortEventHooks.register();
        }
    }
}
