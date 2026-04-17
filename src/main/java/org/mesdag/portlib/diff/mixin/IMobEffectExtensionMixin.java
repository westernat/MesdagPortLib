package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.common.extensions.IMobEffectExtension;
import org.mesdag.portlib.util.PortSets;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(IMobEffectExtension.class)
public interface IMobEffectExtensionMixin {
    @Shadow
    private MobEffect self() {
        throw new UnsupportedOperationException();
    }

    @WrapMethod(method = "fillEffectCures")
    private void port(Set<EffectCure> cures, MobEffectInstance effectInstance, Operation<Void> original) {
        if (self().fillPortEffectCures(PortSets.mutableTransform(cures, PortEffectCure::wrap, PortEffectCure::unwrap), effectInstance)) {
            original.call(cures, effectInstance);
        }
    }
}
