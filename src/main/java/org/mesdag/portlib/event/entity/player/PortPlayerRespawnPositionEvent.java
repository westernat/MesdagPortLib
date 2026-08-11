package org.mesdag.portlib.event.entity.player;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.level.portal.PortDimensionTransition;

import java.util.Objects;

public class PortPlayerRespawnPositionEvent extends PlayerEvent {
    private PortDimensionTransition dimensionTransition;
    private final PortDimensionTransition originalDimensionTransition;
    private final boolean fromEndFight;
    private boolean copyOriginalSpawnPosition;

    @Diff
    public PortPlayerRespawnPositionEvent(ServerPlayer player, PortDimensionTransition dimensionTransition, boolean fromEndFight, boolean copyOriginalSpawnPosition) {
        super(player);
        this.dimensionTransition = dimensionTransition;
        this.originalDimensionTransition = dimensionTransition;
        this.fromEndFight = fromEndFight;
        this.copyOriginalSpawnPosition = copyOriginalSpawnPosition;
    }

    public PortDimensionTransition getDimensionTransition() {
        return dimensionTransition;
    }

    public void setDimensionTransition(PortDimensionTransition dimensionTransition) {
        this.dimensionTransition = dimensionTransition;
    }

    public void setRespawnLevel(ResourceKey<Level> respawnLevelResourceKey) {
        MinecraftServer server = Objects.requireNonNull(getEntity().getServer(), "The player is not in a ServerLevel somehow?");
        ServerLevel level = Objects.requireNonNull(server.getLevel(respawnLevelResourceKey), "Level " + respawnLevelResourceKey + " does not exist!");
        PortDimensionTransition dt = getDimensionTransition();
        setDimensionTransition(new PortDimensionTransition(level, dt.pos(), dt.speed(), dt.yRot(), dt.xRot(), dt.postDimensionTransition()));
    }

    public PortDimensionTransition getOriginalDimensionTransition() {
        return originalDimensionTransition;
    }

    public boolean copyOriginalSpawnPosition() {
        return copyOriginalSpawnPosition;
    }

    public void setCopyOriginalSpawnPosition(boolean copyOriginalSpawnPosition) {
        this.copyOriginalSpawnPosition = copyOriginalSpawnPosition;
    }

    public boolean isFromEndFight() {
        return fromEndFight;
    }
}
