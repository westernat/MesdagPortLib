package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.mesdag.portlib.event.entity.living.PortMobDespawnEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin implements PortSelfGetter<WitherBoss> {
    @Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
    private void checkMobDespawn(CallbackInfo ci) {
        if (PortMobDespawnEvent.checkMobDespawn(portlib$self())) {
            ci.cancel();
        }
    }
}
