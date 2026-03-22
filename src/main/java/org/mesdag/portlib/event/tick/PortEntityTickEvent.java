package org.mesdag.portlib.event.tick;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortEntityTickEvent<E extends EntityTickEvent> extends PortEntityEvent {
    protected final E e;

    protected PortEntityTickEvent(E e) {
        super(e.getEntity());
        this.e = e;
    }

    public static class PortPre extends PortEntityTickEvent<EntityTickEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(EntityTickEvent.Pre e) {
            super(e);
        }

        @Override
        public void setCanceled(boolean canceled) {
            IPortCancellableEvent.super.setCanceled(canceled);
            e.setCanceled(canceled);
        }

        static {
            PortEventHooks.register(EntityTickEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortEntityTickEvent<EntityTickEvent.Post> {
        @Diff
        public PortPost(EntityTickEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register(EntityTickEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
