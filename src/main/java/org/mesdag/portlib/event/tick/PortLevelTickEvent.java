package org.mesdag.portlib.event.tick;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortLevelTickEvent<E extends LevelTickEvent> extends PortEvent<E> {
    @Diff
    public PortLevelTickEvent(E e) {
        super(e);
    }

    public boolean hasTime() {
        return e.hasTime();
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public static class PortPre extends PortLevelTickEvent<LevelTickEvent.Pre> {
        @Diff
        public PortPre(LevelTickEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortLevelTickEvent<LevelTickEvent.Post> {
        @Diff
        public PortPost(LevelTickEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
