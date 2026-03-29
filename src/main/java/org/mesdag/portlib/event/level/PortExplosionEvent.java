package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortExplosionEvent<E extends ExplosionEvent> extends PortEvent<E> {
    @Diff
    public PortExplosionEvent(E e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public Explosion getExplosion() {
        return e.getExplosion();
    }

    public static class PortStart extends PortExplosionEvent<ExplosionEvent.Start> implements IPortCancellableEvent {
        @Diff
        public PortStart(ExplosionEvent.Start e) {
            super(e);
        }

        static {
            PortEventHooks.register(ExplosionEvent.Start.class, PortStart.class, PortStart::new);
        }
    }

    public static class PortDetonate extends PortExplosionEvent<ExplosionEvent.Detonate> {
        @Diff
        public PortDetonate(ExplosionEvent.Detonate e) {
            super(e);
        }

        public List<BlockPos> getAffectedBlocks() {
            return e.getAffectedBlocks();
        }

        public List<Entity> getAffectedEntities() {
            return e.getAffectedEntities();
        }

        static {
            PortEventHooks.register(ExplosionEvent.Detonate.class, PortDetonate.class, PortDetonate::new);
        }
    }
}
