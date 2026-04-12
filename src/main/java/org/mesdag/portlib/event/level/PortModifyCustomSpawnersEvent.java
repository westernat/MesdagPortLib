package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

import java.util.List;

public class PortModifyCustomSpawnersEvent extends Event {
    private final ServerLevel serverLevel;
    private final List<CustomSpawner> customSpawners;

    @Diff
    public PortModifyCustomSpawnersEvent(ServerLevel serverLevel, List<CustomSpawner> customSpawners) {
        this.serverLevel = serverLevel;
        this.customSpawners = customSpawners;
    }

    public ServerLevel getLevel() {
        return serverLevel;
    }

    public List<CustomSpawner> getCustomSpawners() {
        return customSpawners;
    }

    public void addCustomSpawner(CustomSpawner customSpawner) {
        customSpawners.add(customSpawner);
    }
}
