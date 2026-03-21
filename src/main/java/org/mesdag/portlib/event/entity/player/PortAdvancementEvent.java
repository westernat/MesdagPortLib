package org.mesdag.portlib.event.entity.player;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortAdvancementEvent extends PortPlayerEvent {
    private final AdvancementEvent e;

    public PortAdvancementEvent(AdvancementEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public AdvancementHolder getAdvancement() {
        return e.getAdvancement();
    }

    public static class PortAdvancementEarnEvent extends PortAdvancementEvent {
        public PortAdvancementEarnEvent(AdvancementEvent.AdvancementEarnEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register(AdvancementEvent.AdvancementEarnEvent.class, PortAdvancementEarnEvent.class, PortAdvancementEarnEvent::new);
        }
    }

    public static class PortAdvancementProgressEvent extends PortAdvancementEvent {
        private final AdvancementEvent.AdvancementProgressEvent e;

        public PortAdvancementProgressEvent(AdvancementEvent.AdvancementProgressEvent e) {
            super(e);
            this.e = e;
        }

        public AdvancementProgress getAdvancementProgress() {
            return e.getAdvancementProgress();
        }

        public String getCriterionName() {
            return e.getCriterionName();
        }

        public PortProgressType getProgressType() {
            return PortProgressType.wrap(e.getProgressType());
        }

        public enum PortProgressType {
            GRANT,
            REVOKE;

            @Diff
            public AdvancementEvent.AdvancementProgressEvent.ProgressType unwrap() {
                return this == GRANT
                        ? AdvancementEvent.AdvancementProgressEvent.ProgressType.GRANT
                        : AdvancementEvent.AdvancementProgressEvent.ProgressType.REVOKE;
            }

            @Diff
            public static PortProgressType wrap(AdvancementEvent.AdvancementProgressEvent.ProgressType type) {
                return type == AdvancementEvent.AdvancementProgressEvent.ProgressType.GRANT ? GRANT : REVOKE;
            }
        }

        static {
            PortEventHooks.register(AdvancementEvent.AdvancementProgressEvent.class, PortAdvancementProgressEvent.class, PortAdvancementProgressEvent::new);
        }
    }
}
