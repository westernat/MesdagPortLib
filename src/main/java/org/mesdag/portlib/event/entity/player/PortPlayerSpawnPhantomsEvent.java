package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.event.entity.player.PlayerSpawnPhantomsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerSpawnPhantomsEvent extends PortPlayerEvent {
    private final PlayerSpawnPhantomsEvent e;

    @Diff
    public PortPlayerSpawnPhantomsEvent(PlayerSpawnPhantomsEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public int getPhantomsToSpawn() {
        return e.getPhantomsToSpawn();
    }

    public void setPhantomsToSpawn(int phantomsToSpawn) {
        e.setPhantomsToSpawn(phantomsToSpawn);
    }

    public void setResult(PortResult result) {
        e.setResult(result.unwrap());
    }

    public PortResult getResult() {
        return PortResult.wrap(e.getResult());
    }

    public boolean shouldSpawnPhantoms(ServerLevel level, BlockPos pos) {
        return e.shouldSpawnPhantoms(level, pos);
    }

    public enum PortResult {
        ALLOW,
        DEFAULT,
        DENY;

        @Diff
        public PlayerSpawnPhantomsEvent.Result unwrap() {
            return switch (this) {
                case ALLOW -> PlayerSpawnPhantomsEvent.Result.ALLOW;
                case DEFAULT -> PlayerSpawnPhantomsEvent.Result.DEFAULT;
                case DENY -> PlayerSpawnPhantomsEvent.Result.DENY;
            };
        }

        @Diff
        public static PortResult wrap(PlayerSpawnPhantomsEvent.Result result) {
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
