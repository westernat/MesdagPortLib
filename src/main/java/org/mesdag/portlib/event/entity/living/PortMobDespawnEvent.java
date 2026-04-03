package org.mesdag.portlib.event.entity.living;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

public class PortMobDespawnEvent extends MobSpawnEvent {
    protected PortResult result = PortResult.DEFAULT;

    @Diff
    public PortMobDespawnEvent(Mob mob, ServerLevelAccessor level) {
        super(mob, level, mob.getX(), mob.getY(), mob.getZ());
    }

    public void setPortResult(PortResult result) {
        this.result = result;
    }

    public PortResult getPortResult() {
        return result;
    }

    public enum PortResult {
        ALLOW,
        DEFAULT,
        DENY;

        @Diff
        public Result unwrap() {
            return switch (this) {
                case ALLOW -> Result.ALLOW;
                case DEFAULT -> Result.DEFAULT;
                case DENY -> Result.DENY;
            };
        }

        @Diff
        public static PortResult wrap(Result result) {
            return switch (result) {
                case ALLOW -> PortResult.ALLOW;
                case DEFAULT -> PortResult.DEFAULT;
                case DENY -> PortResult.DENY;
            };
        }
    }

    @Diff
    public static boolean checkMobDespawn(Mob mob) {
        PortMobDespawnEvent event = new PortMobDespawnEvent(mob, (ServerLevel) mob.level());
        PortEventHandler.postEvent(event);
        return switch (event.getPortResult()) {
            case ALLOW -> {
                mob.discard();
                yield true;
            }
            case DEFAULT -> false;
            case DENY -> {
                mob.setNoActionTime(0);
                yield true;
            }
        };
    }
}
