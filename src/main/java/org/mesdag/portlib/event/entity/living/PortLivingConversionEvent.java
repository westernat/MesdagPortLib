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

    public static class Pre extends PortLivingConversionEvent<LivingConversionEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public Pre(LivingConversionEvent.Pre e) {
            super(e);
        }

        public EntityType<? extends LivingEntity> getOutcome() {
            return e.getOutcome();
        }

        public void setConversionTimer(int ticks) {
            e.setConversionTimer(ticks);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Post extends PortLivingConversionEvent<LivingConversionEvent.Post> implements IPortCancellableEvent {
        @Diff
        public Post(LivingConversionEvent.Post e) {
            super(e);
        }

        public LivingEntity getOutcome() {
            return e.getOutcome();
        }

        static {
            PortEventHooks.register();
        }
    }
}
