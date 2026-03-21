package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortArrowLooseEvent extends PortPlayerEvent implements IPortCancellableEvent {
    private final ArrowLooseEvent e;

    @Diff
    public PortArrowLooseEvent(ArrowLooseEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public ItemStack getBow() {
        return e.getBow();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public boolean hasAmmo() {
        return e.hasAmmo();
    }

    public int getCharge() {
        return e.getCharge();
    }

    public void setCharge(int charge) {
        e.setCharge(charge);
    }

    static {
        PortEventHooks.register(ArrowLooseEvent.class, PortArrowLooseEvent.class, PortArrowLooseEvent::new);
    }
}
