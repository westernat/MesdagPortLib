package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.LightningBolt;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortEntityStruckByLightningEvent extends PortEntityEvent<EntityStruckByLightningEvent> implements IPortCancellableEvent {
    @Diff
    public PortEntityStruckByLightningEvent(EntityStruckByLightningEvent e) {
        super(e);
    }

    public LightningBolt getLightning() {
        return e.getLightning();
    }

    static {
        PortEventHooks.register();
    }
}
