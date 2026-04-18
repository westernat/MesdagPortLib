package org.mesdag.portlib.event.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.level.BlockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortCreateFluidSourceEvent extends PortEvent<BlockEvent.CreateFluidSourceEvent> {
    @Diff
    public PortCreateFluidSourceEvent(BlockEvent.CreateFluidSourceEvent e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public BlockState getState() {
        return e.getState();
    }

    public FluidState getFluidState() {
        return e.getState().getFluidState();
    }

    public boolean getVanillaResult() {
        return getFluidState().canConvertToSource(getLevel(), getPos());
    }

    public boolean canConvert() {
        return getResult() == Result.DEFAULT ? getVanillaResult() : getResult() == Result.ALLOW;
    }

    public void setCanConvert(boolean convert) {
        if (convert == getVanillaResult()) {
            e.setResult(Result.DEFAULT);
        } else {
            e.setResult(convert ? Result.ALLOW : Result.DENY);
        }
    }

    static {
        PortEventHooks.register();
    }
}
