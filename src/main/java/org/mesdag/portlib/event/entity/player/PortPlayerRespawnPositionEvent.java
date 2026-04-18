package org.mesdag.portlib.event.entity.player;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.player.PlayerRespawnPositionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.level.portal.PortDimensionTransition;

public class PortPlayerRespawnPositionEvent extends PortPlayerEvent<PlayerRespawnPositionEvent> {
    @Diff
    public PortPlayerRespawnPositionEvent(PlayerRespawnPositionEvent e) {
        super(e);
    }

    public PortDimensionTransition getDimensionTransition() {
        return PortDimensionTransition.wrap(e.getDimensionTransition());
    }

    public void setDimensionTransition(PortDimensionTransition dimensionTransition) {
        e.setDimensionTransition(dimensionTransition.unwrap());
    }

    public void setRespawnLevel(ResourceKey<Level> respawnLevelResourceKey) {
        e.setRespawnLevel(respawnLevelResourceKey);
    }

    public PortDimensionTransition getOriginalDimensionTransition() {
        return PortDimensionTransition.wrap(e.getOriginalDimensionTransition());
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
        PortEventHooks.register();
    }
}
