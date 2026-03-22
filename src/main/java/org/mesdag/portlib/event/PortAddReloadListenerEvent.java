package org.mesdag.portlib.event;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.conditions.IPortCondition;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.List;

public class PortAddReloadListenerEvent extends PortEvent {
    private final AddReloadListenerEvent e;

    @Diff
    public PortAddReloadListenerEvent(AddReloadListenerEvent e) {
        this.e = e;
    }

    public void addListener(PreparableReloadListener listener) {
        e.addListener(listener);
    }

    public List<PreparableReloadListener> getListeners() {
        return e.getListeners();
    }

    public ReloadableServerResources getServerResources() {
        return e.getServerResources();
    }

    public IPortCondition.IPortContext getConditionContext() {
        return IPortCondition.IPortContext.wrap(e.getConditionContext());
    }

    public PortRegistryAccess getRegistryAccess() {
        return new PortRegistryAccess(e.getRegistryAccess());
    }

    static {
        PortEventHooks.register(AddReloadListenerEvent.class, PortAddReloadListenerEvent.class, PortAddReloadListenerEvent::new);
    }
}
