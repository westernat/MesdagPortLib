package org.mesdag.portlib.event.entity.living;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingDestroyBlockEvent extends PortLivingEvent<LivingDestroyBlockEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingDestroyBlockEvent(LivingDestroyBlockEvent e) {
        super(e);
    }

    public BlockState getState() {
        return e.getState();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    static {
        PortEventHooks.register(LivingDestroyBlockEvent.class, PortLivingDestroyBlockEvent.class, PortLivingDestroyBlockEvent::new);
    }
}
