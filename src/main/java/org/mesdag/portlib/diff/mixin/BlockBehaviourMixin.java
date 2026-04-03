package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortUseItemOnBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Mixin(BlockBehaviour.BlockStateBase.class)
    public abstract static class BlockStateBaseMixin {
        @Inject(method = "use", at = @At("HEAD"), cancellable = true)
        private void useItemOn(Level level, Player player, InteractionHand hand, BlockHitResult result, CallbackInfoReturnable<InteractionResult> cir) {
            var useOnContext = new UseOnContext(level, player, hand, player.getItemInHand(hand).copy(), result);
            var e = PortEventHandler.postEventWithReturn(new PortUseItemOnBlockEvent(useOnContext, PortUseItemOnBlockEvent.PortUsePhase.BLOCK));
            if (e.isCanceled())
                cir.setReturnValue(e.getCancellationResult().result());
        }
    }
}
