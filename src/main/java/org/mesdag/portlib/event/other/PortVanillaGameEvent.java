package org.mesdag.portlib.event.other;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.VanillaGameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

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

    public Holder<GameEvent> getVanillaEvent() {
        return e.getVanillaEvent();
    }

    public Vec3 getEventPosition() {
        return e.getEventPosition();
    }

    public GameEvent.Context getContext() {
        return e.getContext();
    }

    static {
        PortEventHooks.register(VanillaGameEvent.class, PortVanillaGameEvent.class, PortVanillaGameEvent::new);
    }
}
