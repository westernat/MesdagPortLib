package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerSetSpawnEvent extends PortPlayerEvent<PlayerSetSpawnEvent> implements IPortCancellableEvent {
    @Diff
    public PortPlayerSetSpawnEvent(PlayerSetSpawnEvent e) {
        super(e);
    }

    public boolean isForced() {
        return e.isForced();
    }

    public @Nullable BlockPos getNewSpawn() {
        return e.getNewSpawn();
    }

    public ResourceKey<Level> getSpawnLevel() {
        return e.getSpawnLevel();
    }

    static {
        PortEventHooks.register(PlayerSetSpawnEvent.class, PortPlayerSetSpawnEvent.class, PortPlayerSetSpawnEvent::new);
    }
}
