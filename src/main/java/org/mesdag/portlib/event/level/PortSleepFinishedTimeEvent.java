package org.mesdag.portlib.event.level;

import net.neoforged.neoforge.event.level.SleepFinishedTimeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortSleepFinishedTimeEvent extends PortLevelEvent<SleepFinishedTimeEvent> {
    @Diff
    public PortSleepFinishedTimeEvent(SleepFinishedTimeEvent e) {
        super(e);
    }

    public long getNewTime() {
        return e.getNewTime();
    }

    public boolean setTimeAddition(long newTimeIn) {
        return e.setTimeAddition(newTimeIn);
    }

    static {
        PortEventHooks.register(SleepFinishedTimeEvent.class, PortSleepFinishedTimeEvent.class, PortSleepFinishedTimeEvent::new);
    }
}
