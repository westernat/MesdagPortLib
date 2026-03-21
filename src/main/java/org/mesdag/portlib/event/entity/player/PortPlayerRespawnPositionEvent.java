package org.mesdag.portlib.event.entity.player;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerRespawnPositionEvent extends PortPlayerEvent {
    private final PlayerRespawnPositionEvent e;

    @Diff
    public PortPlayerRespawnPositionEvent(PlayerRespawnPositionEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public DimensionTransition getDimensionTransition() {
        return e.getDimensionTransition();
    }

    public void setDimensionTransition(DimensionTransition dimensionTransition) {
        e.setDimensionTransition(dimensionTransition);
    }

    public void setRespawnLevel(ResourceKey<Level> respawnLevelResourceKey) {
        e.setRespawnLevel(respawnLevelResourceKey);
    }

    public DimensionTransition getOriginalDimensionTransition() {
        return e.getOriginalDimensionTransition();
    }

    public boolean copyOriginalSpawnPosition() {
        return e.copyOriginalSpawnPosition();
    }

    public void setCopyOriginalSpawnPosition(boolean copyOriginalSpawnPosition) {
        e.setCopyOriginalSpawnPosition(copyOriginalSpawnPosition);
    }

    public boolean isFromEndFight() {
        return e.isFromEndFight();
    }

    static {
        PortEventHooks.register(PlayerRespawnPositionEvent.class, PortPlayerRespawnPositionEvent.class, PortPlayerRespawnPositionEvent::new);
    }
}
