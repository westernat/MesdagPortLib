package org.mesdag.portlib.event.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortModifyCustomSpawnersEvent extends PortEvent {
    private final ModifyCustomSpawnersEvent e;

    @Diff
    public PortModifyCustomSpawnersEvent(ModifyCustomSpawnersEvent e) {
        this.e = e;
    }

    public ServerLevel getLevel() {
        return e.getLevel();
    }

    public List<CustomSpawner> getCustomSpawners() {
        return e.getCustomSpawners();
    }

    public void addCustomSpawner(CustomSpawner customSpawner) {
        e.addCustomSpawner(customSpawner);
    }

    static {
        PortEventHooks.register(ModifyCustomSpawnersEvent.class, PortModifyCustomSpawnersEvent.class, PortModifyCustomSpawnersEvent::new);
    }
}