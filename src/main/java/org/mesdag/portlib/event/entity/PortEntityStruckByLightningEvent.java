package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.LightningBolt;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityStruckByLightningEvent extends PortEntityEvent implements IPortCancellableEvent {

    private final EntityStruckByLightningEvent e;

    @Diff
    public PortEntityStruckByLightningEvent(EntityStruckByLightningEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public LightningBolt getLightning() {
        return e.getLightning();
    }

    static {
        PortEventHooks.register(EntityStruckByLightningEvent.class, PortEntityStruckByLightningEvent.class, PortEntityStruckByLightningEvent::new);
    }
}