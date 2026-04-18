package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.Collection;

public class PortLivingDropsEvent extends PortLivingEvent<LivingDropsEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingDropsEvent(LivingDropsEvent e) {
        super(e);
    }

    public DamageSource getSource() {
        return e.getSource();
    }

    public Collection<ItemEntity> getDrops() {
        return e.getDrops();
    }

    public boolean isRecentlyHit() {
        return e.isRecentlyHit();
    }

    static {
        PortEventHooks.register();
    }
}
