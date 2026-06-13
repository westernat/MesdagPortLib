package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.mesdag.portlib.wrapper.resource.PortContextAwareReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(ReloadableServerResources.class)
public abstract class ReloadableServerResourcesMixin {
    @Inject(method = "loadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;"))
    private static void init(
            CallbackInfoReturnable<CompletableFuture<ReloadableServerResources>> cir,
            @Local(argsOnly = true) RegistryAccess.Frozen registryAccess,
            @Local(name = "listeners") List<PreparableReloadListener> listeners,
            @Local(name = "reloadableserverresources") ReloadableServerResources reloadableserverresources
    ) {
        for (PreparableReloadListener listener : listeners) {
            if (listener instanceof PortContextAwareReloadListener c) {
                c.injectContext(reloadableserverresources.getConditionContext(), registryAccess);
            }
        }
    }

    @ModifyArg(method = "loadResources", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenApply(Ljava/util/function/Function;)Ljava/util/concurrent/CompletableFuture;"))
    private static <T, U> Function<? super T, ? extends U> clear(
            Function<? super T, ? extends U> fn,
            @Local(name = "listeners") List<PreparableReloadListener> listeners
    ) {
        return value -> {
            for (PreparableReloadListener listener : listeners) {
                if (listener instanceof PortContextAwareReloadListener c) {
                    c.injectContext(ICondition.IContext.EMPTY, RegistryAccess.EMPTY);
                }
            }
            return fn.apply(value);
        };
    }
}
