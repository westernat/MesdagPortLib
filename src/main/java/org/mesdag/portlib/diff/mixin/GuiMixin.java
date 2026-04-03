package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.entity.player.PortPlayerHeartTypeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Mixin(Gui.HeartType.class)
    public abstract static class HeartTypeMixin {
        @ModifyReturnValue(method = "forPlayer", at = @At("RETURN"))
        private static Gui.HeartType firePlayerHeartTypeEvent(Gui.HeartType original, @Local(argsOnly = true) Player player) {
            return PortEventHandler.postEventWithReturn(new PortPlayerHeartTypeEvent(player, original)).getType();
        }
    }
}
