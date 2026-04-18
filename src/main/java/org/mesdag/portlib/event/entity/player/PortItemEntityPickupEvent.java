package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public abstract class PortItemEntityPickupEvent<E extends ItemEntityPickupEvent> extends PortEvent<E> {
    @Diff
    public PortItemEntityPickupEvent(E e) {
        super(e);
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public ItemEntity getItemEntity() {
        return e.getItemEntity();
    }

    public static class PortPre extends PortItemEntityPickupEvent<ItemEntityPickupEvent.Pre> {
        @Diff
        public PortPre(ItemEntityPickupEvent.Pre e) {
            super(e);
        }

        public void setCanPickup(PortTriState state) {
            e.setCanPickup(state.unwrap());
        }

        public PortTriState canPickup() {
            return e.canPickup().wrap();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortItemEntityPickupEvent<ItemEntityPickupEvent.Post> {
        public PortPost(ItemEntityPickupEvent.Post e) {
            super(e);
        }

        public ItemStack getOriginalStack() {
            return e.getOriginalStack();
        }

        public ItemStack getCurrentStack() {
            return e.getCurrentStack();
        }

        static {
            PortEventHooks.register();
        }
    }
}
