package org.mesdag.portlib.event.tick;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortLevelTickEvent extends PortEvent<TickEvent.LevelTickEvent> {
    @Diff
    public PortLevelTickEvent(TickEvent.LevelTickEvent e) {
        super(e);
    }

    public boolean hasTime() {
        return e.haveTime();
    }

    public Level getLevel() {
        return e.level;
    }

    public static class PortPre extends PortLevelTickEvent {
        @Diff
        public PortPre(TickEvent.LevelTickEvent e) {
            super(e);
        }
    }

    public static class PortPost extends PortLevelTickEvent {
        @Diff
        public PortPost(TickEvent.LevelTickEvent e) {
            super(e);
        }
    }

    static {
        PortEventHooks.registerCombined(TickEvent.LevelTickEvent.class, List.of(
                PortPre.class,
                PortPost.class
        ), e -> {
            if (e.phase == TickEvent.Phase.START) {
                return new PortPre(e);
            }
            return new PortPost(e);
        });
    }
}
