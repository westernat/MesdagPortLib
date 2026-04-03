package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.player.PlayerSpawnPhantomsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerSpawnPhantomsEvent extends PortPlayerEvent<PlayerSpawnPhantomsEvent> {
    @Diff
    public PortPlayerSpawnPhantomsEvent(PlayerSpawnPhantomsEvent e) {
        super(e);
    }

    public int getPhantomsToSpawn() {
        return e.getPhantomsToSpawn();
    }

    public void setPhantomsToSpawn(int phantomsToSpawn) {
        e.setPhantomsToSpawn(phantomsToSpawn);
    }

    public void setPortResult(PortResult result) {
        e.setResult(result.unwrap());
    }

    public PortResult getPortResult() {
        return PortResult.wrap(e.getResult());
    }

    public boolean shouldSpawnPhantoms(ServerLevel level, BlockPos pos) {
        if (e.getResult() == Result.ALLOW) {
            return true;
        }
        return e.getResult() == Result.DEFAULT && (!level.dimensionType().hasSkyLight() || pos.getY() >= level.getSeaLevel() && level.canSeeSky(pos));
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
                case ALLOW -> ALLOW;
                case DEFAULT -> DEFAULT;
                case DENY -> DENY;
            };
        }
    }

    static {
        PortEventHooks.register(PlayerSpawnPhantomsEvent.class, PortPlayerSpawnPhantomsEvent.class, PortPlayerSpawnPhantomsEvent::new);
    }
}
