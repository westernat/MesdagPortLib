package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortBonemealEvent extends PortEvent<BonemealEvent> implements IPortCancellableEvent {
    @Diff
    public PortBonemealEvent(BonemealEvent e) {
        super(e);
    }

    public @Nullable Player getPlayer() {
        return e.getPlayer();
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

    public ItemStack getStack() {
        return e.getStack();
    }

    public boolean isValidBonemealTarget() {
        return e.isValidBonemealTarget();
    }

    public void setSuccessful(boolean success) {
        e.setSuccessful(success);
    }

    public boolean isSuccessful() {
        return e.isSuccessful();
    }

    static {
        PortEventHooks.register();
    }
}
