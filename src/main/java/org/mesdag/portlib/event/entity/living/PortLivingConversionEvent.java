package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingConversionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortLivingConversionEvent extends PortLivingEvent {
    public PortLivingConversionEvent(LivingEntity entity) {
        super(entity);
    }

    public static class PortPre extends PortLivingConversionEvent implements ICancellableEvent {
        private final LivingConversionEvent.Pre e;

        @Diff
        public PortPre(LivingConversionEvent.Pre e) {
            super(e.getEntity());
            this.e = e;
        }

        public EntityType<? extends LivingEntity> getOutcome() {
            return e.getOutcome();
        }

        public void setConversionTimer(int ticks) {
            e.setConversionTimer(ticks);
        }

        static {
            PortEventHooks.register(LivingConversionEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortLivingConversionEvent {
        private final LivingConversionEvent.Post e;

        @Diff
        public PortPost(LivingConversionEvent.Post e) {
            super(e.getEntity());
            this.e = e;
        }

        public LivingEntity getOutcome() {
            return e.getOutcome();
        }

        static {
            PortEventHooks.register(LivingConversionEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
