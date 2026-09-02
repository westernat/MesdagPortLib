package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.BonemealEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortBonemealEvent extends PortEvent<BonemealEvent> implements IPortCancellableEvent {
    private final Player player;
    private final boolean isValidBonemealTarget;
    private boolean isSuccess = false;

    @Diff
    public PortBonemealEvent(BonemealEvent e) {
        super(e);
        this.player = e.getEntity();
        this.isValidBonemealTarget = e.getBlock().getBlock() instanceof BonemealableBlock bonemealable &&
                bonemealable.isValidBonemealTarget(e.getLevel(), e.getPos(), e.getBlock(), e.getLevel().isClientSide);
    }

    public @Nullable Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public BlockState getState() {
        return e.getBlock();
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    public boolean isValidBonemealTarget() {
        return isValidBonemealTarget;
    }

    public void setSuccessful(boolean success) {
        this.isSuccess = success;
        setCanceled(true);
    }

    public boolean isSuccessful() {
        return isSuccess;
    }

    static {
        PortEventHooks.register();
    }
}
