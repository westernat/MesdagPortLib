package org.mesdag.portlib.event.tick;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

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

        static {
            PortEventHooks.registerPredicated(TickEvent.LevelTickEvent.class, PortPre.class, PortPre::new, event -> event.phase == TickEvent.Phase.START);
        }
    }

    public static class PortPost extends PortLevelTickEvent {
        @Diff
        public PortPost(TickEvent.LevelTickEvent e) {
            super(e);
        }

        static {
            PortEventHooks.registerPredicated(TickEvent.LevelTickEvent.class, PortPost.class, PortPost::new, event -> event.phase == TickEvent.Phase.END);
        }
    }
}
