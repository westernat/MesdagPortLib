package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.other.PortStatAwardEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatsCounter.class)
public abstract class StatsCounterMixin {
    @WrapOperation(method = "setValue", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;put(Ljava/lang/Object;I)I"))
    private <K> int onStatAward(Object2IntMap<K> instance, Object o, int i, Operation<Integer> original, @Local(argsOnly = true) Player player) {
        PortStatAwardEvent event = new PortStatAwardEvent(player, (Stat<?>) o, i);
        PortEventHandler.postEvent(event);
        if (!event.isCanceled()) {
            return original.call(instance, event.getStat(), event.getValue());
        }
        return 0;
    }
}
