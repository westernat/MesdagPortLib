package org.mesdag.portlib.event.other;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

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

    public ICondition.IContext getConditionContext() {
        return e.getConditionContext();
    }

    public RegistryAccess getRegistryAccess() {
        return e.getRegistryAccess();
    }

    static {
        PortEventHooks.register();
    }
}
