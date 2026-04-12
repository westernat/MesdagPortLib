package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.level.PortExplosionKnockbackEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Explosion.class)
public abstract class ExplosionMixin implements PortSelfGetter<Explosion> {
    @Shadow
    @Final
    private Level level;

    @ModifyVariable(method = "explode", at = @At(value = "STORE"), name = "vec31")
    private Vec3 getExplosionKnockback(Vec3 original, @Local(name = "entity") Entity entity) {
        PortExplosionKnockbackEvent event = new PortExplosionKnockbackEvent(level, portlib$self(), entity, original);
        PortEventHandler.postEvent(event);
        return event.getKnockbackVelocity();
    }
}
