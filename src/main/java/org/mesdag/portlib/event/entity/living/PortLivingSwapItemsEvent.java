package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingSwapItemsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortLivingSwapItemsEvent extends PortLivingEvent {
    private PortLivingSwapItemsEvent(LivingEntity entity) {
        super(entity);
    }

    public static class PortHands extends PortLivingSwapItemsEvent implements IPortCancellableEvent {
        private final LivingSwapItemsEvent.Hands e;

        @Diff
        public PortHands(LivingSwapItemsEvent.Hands e) {
            super(e.getEntity());
            this.e = e;
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
