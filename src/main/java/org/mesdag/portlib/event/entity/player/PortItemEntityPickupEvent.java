package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public abstract class PortItemEntityPickupEvent extends PortEvent {
    private final ItemEntityPickupEvent e;

    @Diff
    public PortItemEntityPickupEvent(ItemEntityPickupEvent e) {
        this.e = e;
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public ItemEntity getItemEntity() {
        return e.getItemEntity();
    }

    public static class PortPre extends PortItemEntityPickupEvent {
        private final ItemEntityPickupEvent.Pre e;

        @Diff
        public PortPre(ItemEntityPickupEvent.Pre e) {
            super(e);
            this.e = e;
        }

        public void setCanPickup(PortTriState state) {
            e.setCanPickup(state.unwrap());
        }

        public PortTriState canPickup() {
            return PortTriState.wrap(e.canPickup());
        }

        static {
            PortEventHooks.register(ItemEntityPickupEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortItemEntityPickupEvent {
        private final ItemEntityPickupEvent.Post e;

        public PortPost(ItemEntityPickupEvent.Post e) {
            super(e);
            this.e = e;
        }

        public ItemStack getOriginalStack() {
            return e.getOriginalStack();
        }

        public ItemStack getCurrentStack() {
            return e.getCurrentStack();
        }

        static {
            PortEventHooks.register(ItemEntityPickupEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
