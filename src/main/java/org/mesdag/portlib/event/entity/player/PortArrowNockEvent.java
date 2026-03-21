package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortArrowNockEvent extends PortPlayerEvent implements IPortCancellableEvent {
    private final ArrowNockEvent e;

    @Diff
    public PortArrowNockEvent(ArrowNockEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public ItemStack getBow() {
        return e.getBow();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public InteractionHand getHand() {
        return e.getHand();
    }

    public boolean hasAmmo() {
        return e.hasAmmo();
    }

    public InteractionResultHolder<ItemStack> getAction() {
        return e.getAction();
    }

    public void setAction(InteractionResultHolder<ItemStack> action) {
        e.setAction(action);
    }

    static {
        PortEventHooks.register(ArrowNockEvent.class, PortArrowNockEvent.class, PortArrowNockEvent::new);
    }
}
