package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortGatherEffectScreenTooltipsEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin implements PortSelfGetter<EffectRenderingInventoryScreen<?>> {
    @ModifyExpressionValue(method = "renderEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private <E> List<E> getEffectTooltip(List<E> original, @Local(name = "mobeffectinstance") MobEffectInstance effectInst) {
        var event = new PortGatherEffectScreenTooltipsEvent(portlib$self(), effectInst, (List<Component>) original);
        PortEventHandler.postEvent(event);
        return (List<E>) event.getTooltip();
    }
}
