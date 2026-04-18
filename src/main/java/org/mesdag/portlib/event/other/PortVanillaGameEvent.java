package org.mesdag.portlib.event.other;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.VanillaGameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.level.gameevent.GameEventHolder;

import javax.annotation.Nullable;

public class PortVanillaGameEvent extends PortEvent<VanillaGameEvent> implements IPortCancellableEvent {
    @Diff
    public PortVanillaGameEvent(VanillaGameEvent e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    @Nullable
    public Entity getCause() {
        return e.getCause();
    }

    public GameEventHolder getVanillaEvent() {
        return GameEventHolder.wrap(e.getVanillaEvent());
    }

    public Vec3 getEventPosition() {
        return e.getEventPosition();
    }

    public GameEvent.Context getContext() {
        return e.getContext();
    }

    static {
        PortEventHooks.register();
    }
}
