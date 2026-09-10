package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.IPortMappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(targets = "net.minecraftforge.registries.NamespacedWrapper", remap = false)
public abstract class NamespacedWrapperMixin<T> implements IPortMappedRegistry<T> {
    @Shadow(remap = false)
    private Map<ResourceLocation, Holder.Reference<T>> holdersByName;

    @Override
    public ResourceLocation portlib$resolve(ResourceLocation name) {
        while (true) {
            if (holdersByName.containsKey(name)) {
                return name;
            }
            ResourceLocation alias = portlib$getAlias().get(name);
            if (alias == null) {
                return name;
            }
            name = alias;
        }
    }

    @ModifyVariable(method = "getHolder(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;", at = @At("HEAD"), argsOnly = true, remap = true)
    private ResourceKey<T> resolveKey1(ResourceKey<T> key) {
        return portlib$resolve(key);
    }

    @ModifyVariable(method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;", at = @At("RETURN"), argsOnly = true, remap = false)
    private ResourceLocation resolveKey2(ResourceLocation location) {
        return portlib$resolve(location);
    }

    @ModifyVariable(method = "getOrCreateHolderOrThrow", at = @At("HEAD"), argsOnly = true, remap = true)
    private ResourceKey<T> resolveKey3(ResourceKey<T> key) {
        return portlib$resolve(key);
    }
}
