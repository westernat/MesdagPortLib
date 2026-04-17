package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortComputeFovModifierEvent extends PortEvent {
    private final ComputeFovModifierEvent e;

    @Diff
    public PortComputeFovModifierEvent(ComputeFovModifierEvent e) {
        super(e);
        this.e = e;
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    public float getFovModifier() {
        return e.getFovModifier();
    }

    public float getNewFovModifier() {
        return e.getNewFovModifier();
    }

    public void setNewFovModifier(float newFovModifier) {
        e.setNewFovModifier(newFovModifier);
    }

    static {
        PortEventHooks.register(ComputeFovModifierEvent.class, PortComputeFovModifierEvent.class, PortComputeFovModifierEvent::new);
    }
}