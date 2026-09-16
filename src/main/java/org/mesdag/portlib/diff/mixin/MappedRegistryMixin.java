package org.mesdag.portlib.diff.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortMappedRegistry;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> implements IPortMappedRegistry<T> {
    @Shadow
    public abstract boolean containsKey(ResourceLocation name);

    @Shadow
    public abstract ResourceKey<? extends Registry<T>> key();

    @Unique
    private final Map<ResourceLocation, ResourceLocation> portlib$aliases = new HashMap<>();

    @Override
    public void portlib$addAlias(ResourceLocation from, ResourceLocation to) {
        if (from.equals(to)) return;
        if (this.portlib$aliases.containsKey(from)) {
            ResourceLocation old = this.portlib$aliases.get(from);
            if (!old.equals(to)) {
                throw new IllegalStateException("Duplicate alias with key \"" + from + "\" attempting to map to \"" + to + "\", found existing mapping \"" + old + "\"");
            }
        }
        if (portlib$resolve(from).equals(to)) {
            throw new IllegalStateException("Infinite alias loop detected: from " + from + " to " + to);
        }
        this.portlib$aliases.put(from, to);
    }

    @Override
    public Map<ResourceLocation, ResourceLocation> portlib$getAliaes() {
        return portlib$aliases;
    }

    @Override
    public ResourceLocation portlib$resolve(ResourceLocation name) {
        while (true) {
            if (containsKey(name)) {
                return name;
            }
            ResourceLocation alias = portlib$getAliaes().get(name);
            if (alias == null) {
                return name;
            }
            name = alias;
        }
    }

    @Override
    public ResourceKey<T> portlib$resolve(ResourceKey<T> key) {
        ResourceLocation resolvedName = portlib$resolve(key.location());
        return resolvedName == key.location() ? key : ResourceKey.create(key(), resolvedName);
    }

    @ModifyVariable(method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;", at = @At("HEAD"), argsOnly = true)
    private @Nullable ResourceKey<T> resolveKey1(@Nullable ResourceKey<T> key) {
        if (key == null) return null;
        return portlib$resolve(key);
    }

    @ModifyVariable(method = "getHolder(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;", at = @At("HEAD"), argsOnly = true)
    private @Nullable ResourceKey<T> resolveKey2(@Nullable ResourceKey<T> key) {
        if (key == null) return null;
        return portlib$resolve(key);
    }

    @ModifyVariable(method = "getOrCreateHolderOrThrow", at = @At("HEAD"), argsOnly = true)
    private @Nullable ResourceKey<T> resolveKey3(@Nullable ResourceKey<T> key) {
        if (key == null) return null;
        return portlib$resolve(key);
    }

    @ModifyVariable(method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;", at = @At("HEAD"), argsOnly = true)
    private @Nullable ResourceLocation resolveKey4(@Nullable ResourceLocation name) {
        if (name == null) return null;
        return portlib$resolve(portlib$resolve(name));
    }

    @Unique
    private List<PortAddCallback.Vanilla<T>> portlib$add;

    @Override
    public void onAdd(PortAddCallback.Vanilla<T> callback) {
        if (portlib$add == null) {
            portlib$add = new ArrayList<>();
        }
        portlib$add.add(callback);
    }

    @Inject(method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;", at = @At("RETURN"))
    private void callOnAdd(int id, ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir) {
        if (portlib$add != null) {
            for (PortAddCallback.Vanilla<T> callback : portlib$add) {
                callback.onAdd(portlib$self(), id, key, value);
            }
        }
    }
}
