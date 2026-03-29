package org.mesdag.portlib.event.other;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.conditions.IPortCondition;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.List;

public class PortAddReloadListenerEvent extends PortEvent<AddReloadListenerEvent> {
    @Diff
    public PortAddReloadListenerEvent(AddReloadListenerEvent e) {
        super(e);
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
