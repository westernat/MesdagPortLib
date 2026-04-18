package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortTriState;

public abstract class PortItemEntityPickupEvent<E extends PlayerEvent> extends PortEvent<E> {
    private final ItemEntity item;

    @Diff
    public PortItemEntityPickupEvent(E e, ItemEntity item){
        super(e);
        this.item = item;
    }

    public Player getPlayer() {
        return e.getEntity();
    }

    public ItemEntity getItemEntity() {
        return item;
    }

    public static class PortPre extends PortItemEntityPickupEvent<EntityItemPickupEvent> {
        @Diff
        public PortPre(EntityItemPickupEvent e) {
            super(e, e.getItem());
        }

        public void setCanPickup(PortTriState state) {
            e.setResult(state.unwrapResult());
        }

        public PortTriState canPickup() {
            return e.getResult().wrap();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortItemEntityPickupEvent<PlayerEvent.ItemPickupEvent> {
        public PortPost(PlayerEvent.ItemPickupEvent e) {
            super(e, e.getOriginalEntity());
        }

        public ItemStack getOriginalStack() {
            return e.getStack();
        }

        public ItemStack getCurrentStack() {
            return e.getOriginalEntity().getItem();
        }

        static {
            PortEventHooks.register();
        }
    }
}
