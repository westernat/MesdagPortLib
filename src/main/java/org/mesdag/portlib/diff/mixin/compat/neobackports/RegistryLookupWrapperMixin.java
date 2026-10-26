package org.mesdag.portlib.diff.mixin.compat.neobackports;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.nikdo53.neobackports.registry.RegistryLookupWrapper", remap = false)
public abstract class RegistryLookupWrapperMixin<T> {
    @Shadow
    public HolderLookup.RegistryLookup<T> parent;

    @Dynamic
    @SuppressWarnings("EqualsBetweenInconvertibleTypes")
    @ModifyReturnValue(method = "canSerializeIn", at = @At(value = "RETURN", ordinal = 1)) // remap in build.gradle
    private boolean useEquals(boolean original, HolderOwner<T> owner) {
        return owner.equals(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == getClass()) {
            return parent.canSerializeIn(((RegistryLookupWrapperMixin<T>) obj).parent);
        }
        return super.equals(obj);
    }
}
