package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ILivingEntityExtension.class)
public interface ILivingEntityExtensionMixin {
    @Shadow
    LivingEntity self();

    @WrapMethod(method = "onDamageTaken")
    private void port(DamageContainer damageContainer, Operation<Void> original) {
        if (self().onDamageTaken(PortDamageContainer.wrap(damageContainer))) {
            original.call(damageContainer);
        }
    }
}
