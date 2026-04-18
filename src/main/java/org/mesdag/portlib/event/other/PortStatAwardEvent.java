package org.mesdag.portlib.event.other;

import net.minecraft.stats.Stat;
import net.neoforged.neoforge.event.StatAwardEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public class PortStatAwardEvent extends PortPlayerEvent<StatAwardEvent> implements IPortCancellableEvent {
    @Diff
    public PortStatAwardEvent(StatAwardEvent e) {
        super(e);
    }

    public Stat<?> getStat() {
        return e.getStat();
    }

    public void setStat(Stat<?> stat) {
        e.setStat(stat);
    }

    public int getValue() {
        return e.getValue();
    }

    public void setValue(int value) {
        e.setValue(value);
    }

    static {
        PortEventHooks.register();
    }
}
