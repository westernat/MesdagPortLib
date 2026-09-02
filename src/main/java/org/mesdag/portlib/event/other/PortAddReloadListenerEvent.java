package org.mesdag.portlib.event.other;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resource.PortContextAwareReloadListener;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PortAddReloadListenerEvent extends PortEvent<AddReloadListenerEvent> {
    @Diff
    public PortAddReloadListenerEvent(AddReloadListenerEvent e) {
        super(e);
    }

    public void addListener(PreparableReloadListener listener) {
        e.addListener(new WrappedStateAwareListener(listener));
    }

    public List<PreparableReloadListener> getListeners() {
        return e.getListeners();
    }

    public ReloadableServerResources getServerResources() {
        return e.getServerResources();
    }

    public ICondition.IContext getConditionContext() {
        return e.getConditionContext();
    }

    public RegistryAccess getRegistryAccess() {
        return e.getRegistryAccess();
    }

    private static class WrappedStateAwareListener extends PortContextAwareReloadListener implements PreparableReloadListener {
        private final PreparableReloadListener wrapped;

        private WrappedStateAwareListener(final PreparableReloadListener wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void injectContext(ICondition.IContext context, HolderLookup.Provider registryLookup) {
            if (wrapped instanceof PortContextAwareReloadListener contextAwareListener) {
                contextAwareListener.injectContext(context, registryLookup);
            }
        }

        @Override
        public CompletableFuture<Void> reload(final PreparationBarrier stage, final ResourceManager resourceManager, final ProfilerFiller preparationsProfiler, final ProfilerFiller reloadProfiler, final Executor backgroundExecutor, final Executor gameExecutor) {
// 不需要，因为forge会wrap           if (ModLoader.isLoadingStateValid()) {
                return wrapped.reload(stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
//            }
//            return CompletableFuture.completedFuture(null);
        }
    }

    static {
        PortEventHooks.register();
    }
}
