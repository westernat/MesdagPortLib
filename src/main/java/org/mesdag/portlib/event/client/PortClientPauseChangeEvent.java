package org.mesdag.portlib.event.client;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

public abstract class PortClientPauseChangeEvent extends Event {
    private final boolean pause;

    @Diff
    public PortClientPauseChangeEvent(boolean pause) {
        this.pause = pause;
    }

    public boolean isPaused() {
        return pause;
    }

    @Cancelable
    public static class Pre extends PortClientPauseChangeEvent {
        @Diff
        public Pre(boolean pause) {
            super(pause);
        }
    }

    public static class Post extends PortClientPauseChangeEvent {
        @Diff
        public Post(boolean pause) {
            super(pause);
        }
    }
}
