package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortLivingConversionEvent<E extends LivingConversionEvent> extends PortLivingEvent<E> {
    public PortLivingConversionEvent(E e) {
        super(e);
    }

    public static class PortPre extends PortLivingConversionEvent<LivingConversionEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(LivingConversionEvent.Pre e) {
            super(e);
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

    public static class PortPost extends PortLivingConversionEvent<LivingConversionEvent.Post> implements IPortCancellableEvent {
        @Diff
        public PortPost(LivingConversionEvent.Post e) {
            super(e);
        }

        public LivingEntity getOutcome() {
            return e.getOutcome();
        }

        static {
            PortEventHooks.register(LivingConversionEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
