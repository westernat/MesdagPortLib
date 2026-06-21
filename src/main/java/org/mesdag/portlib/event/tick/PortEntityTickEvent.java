package org.mesdag.portlib.event.tick;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;

public abstract class PortEntityTickEvent extends EntityEvent {
    @Diff
    public PortEntityTickEvent(Entity entity) {
        super(entity);
    }

    @Cancelable
    public static class Pre extends PortEntityTickEvent {
        @Diff
        public Pre(Entity entity) {
            super(entity);
        }
    }

    public static class Post extends PortEntityTickEvent {
        @Diff
        public Post(Entity entity) {
            super(entity);
        }
    }
}
