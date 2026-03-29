package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortLivingSwapItemsEvent<E extends LivingSwapItemsEvent> extends PortLivingEvent<E> {
    @Diff
    public PortLivingSwapItemsEvent(E e) {
        super(e);
    }

    public static class PortHands extends PortLivingSwapItemsEvent<LivingSwapItemsEvent.Hands> implements IPortCancellableEvent {
        @Diff
        public PortHands(LivingSwapItemsEvent.Hands e) {
            super(e);
        }

        public ItemStack getItemSwappedToMainHand() {
            return e.getItemSwappedToMainHand();
        }

        public ItemStack getItemSwappedToOffHand() {
            return e.getItemSwappedToOffHand();
        }

        public void setItemSwappedToMainHand(ItemStack item) {
            e.setItemSwappedToMainHand(item);
        }

        public void setItemSwappedToOffHand(ItemStack item) {
            e.setItemSwappedToOffHand(item);
        }

        static {
            PortEventHooks.register(LivingSwapItemsEvent.Hands.class, PortHands.class, PortHands::new);
        }
    }
}
