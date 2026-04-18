package org.mesdag.portlib.event.entity.player;

import net.minecraft.advancements.AdvancementProgress;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.advancements.PortAdvancementHolder;

public abstract class PortAdvancementEvent<E extends AdvancementEvent> extends PortPlayerEvent<E> {
    @Diff
    public PortAdvancementEvent(E e) {
        super(e);
    }

    public PortAdvancementHolder getAdvancement() {
        return PortAdvancementHolder.wrap(e.getAdvancement());
    }

    public static class PortAdvancementEarnEvent extends PortAdvancementEvent<AdvancementEvent.AdvancementEarnEvent> {
        @Diff
        public PortAdvancementEarnEvent(AdvancementEvent.AdvancementEarnEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortAdvancementProgressEvent extends PortAdvancementEvent<AdvancementEvent.AdvancementProgressEvent> {
        @Diff
        public PortAdvancementProgressEvent(AdvancementEvent.AdvancementProgressEvent e) {
            super(e);
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
            PortEventHooks.register();
        }
    }
}
