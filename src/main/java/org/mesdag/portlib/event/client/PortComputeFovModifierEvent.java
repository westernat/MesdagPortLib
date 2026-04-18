package org.mesdag.portlib.event.client;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortComputeFovModifierEvent extends PortEvent<ComputeFovModifierEvent> {
    @Diff
    public PortComputeFovModifierEvent(ComputeFovModifierEvent e) {
        super(e);
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
        PortEventHooks.register();
    }
}
