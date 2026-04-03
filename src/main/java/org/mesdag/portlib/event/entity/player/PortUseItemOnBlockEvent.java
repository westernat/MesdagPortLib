package org.mesdag.portlib.event.entity.player;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.fml.PortLogicalSide;
import org.mesdag.portlib.wrapper.world.PortItemInteractionResult;

@Cancelable
public class PortUseItemOnBlockEvent extends Event {
    private final Level level;
    private final @Nullable Player player;
    private final InteractionHand hand;
    private final ItemStack heldItem;
    private final BlockPos pos;
    private final @Nullable Direction face;
    private final UseOnContext context;
    private final PortUsePhase usePhase;
    private PortItemInteractionResult cancellationResult = PortItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

    @Diff
    public PortUseItemOnBlockEvent(UseOnContext context, PortUsePhase usePhase) {
        this.level = Preconditions.checkNotNull(context.getLevel(), "Null level in UseItemOnBlockEvent!");
        this.player = context.getPlayer();
        this.heldItem = Preconditions.checkNotNull(context.getItemInHand(), "Null heldItem in UseItemOnBlockEvent!");
        this.hand = Preconditions.checkNotNull(context.getHand(), "Null hand in UseItemOnBlockEvent!");
        this.pos = Preconditions.checkNotNull(context.getClickedPos(), "Null position in UseItemOnBlockEvent!");
        this.face = context.getClickedFace();
        this.context = context;
        this.usePhase = usePhase;
    }

    public @Nullable Player getPlayer() {
        return player;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public ItemStack getItemStack() {
        return heldItem;
    }

    public BlockPos getPos() {
        return pos;
    }

    public @Nullable Direction getFace() {
        return face;
    }

    public Level getLevel() {
        return level;
    }

    public UseOnContext getUseOnContext() {
        return context;
    }

    public PortUsePhase getUsePhase() {
        return usePhase;
    }

    public PortLogicalSide getSide() {
        return getLevel().isClientSide ? PortLogicalSide.CLIENT : PortLogicalSide.SERVER;
    }

    public void cancelWithResult(PortItemInteractionResult result) {
        setCancellationResult(result);
        setCanceled(true);
    }

    public PortItemInteractionResult getCancellationResult() {
        return cancellationResult;
    }

    public void setCancellationResult(PortItemInteractionResult result) {
        this.cancellationResult = result;
    }

    public enum PortUsePhase {
        ITEM_BEFORE_BLOCK,
        BLOCK,
        ITEM_AFTER_BLOCK
    }
}
