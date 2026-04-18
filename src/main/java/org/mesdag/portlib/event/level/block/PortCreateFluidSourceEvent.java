package org.mesdag.portlib.event.level.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.level.block.CreateFluidSourceEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.level.PortBlockEvent;

public class PortCreateFluidSourceEvent extends PortBlockEvent<CreateFluidSourceEvent> {
    @Diff
    public PortCreateFluidSourceEvent(CreateFluidSourceEvent e) {
        super(e);
    }

    @Override
    public Level getLevel() {
        return e.getLevel();
    }

    public FluidState getFluidState() {
        return e.getFluidState();
    }

    public boolean getVanillaResult() {
        return e.getVanillaResult();
    }

    public boolean canConvert() {
        return e.canConvert();
    }

    public void setCanConvert(boolean convert) {
        e.setCanConvert(convert);
    }

    static {
        PortEventHooks.register();
    }
}
