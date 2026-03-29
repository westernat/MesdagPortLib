package org.mesdag.portlib.event.entity.living;

import net.neoforged.neoforge.event.entity.living.MobDespawnEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortMobDespawnEvent extends PortMobSpawnEvent<MobDespawnEvent> {
    @Diff
    public PortMobDespawnEvent(MobDespawnEvent e) {
        super(e);
    }

    public void setResult(PortResult result) {
        e.setResult(result.unwrap());
    }

    public PortResult getResult() {
        return PortResult.wrap(e.getResult());
    }

    public enum PortResult {
        ALLOW,
        DEFAULT,
        DENY;

        @Diff
        public MobDespawnEvent.Result unwrap() {
            return switch (this) {
                case ALLOW -> MobDespawnEvent.Result.ALLOW;
                case DEFAULT -> MobDespawnEvent.Result.DEFAULT;
                case DENY -> MobDespawnEvent.Result.DENY;
            };
        }

        @Diff
        public static PortResult wrap(MobDespawnEvent.Result result) {
            return switch (result) {
                case ALLOW -> PortResult.ALLOW;
                case DEFAULT -> PortResult.DEFAULT;
                case DENY -> PortResult.DENY;
            };
        }
    }

    static {
        PortEventHooks.register(MobDespawnEvent.class, PortMobDespawnEvent.class, PortMobDespawnEvent::new);
    }
}
