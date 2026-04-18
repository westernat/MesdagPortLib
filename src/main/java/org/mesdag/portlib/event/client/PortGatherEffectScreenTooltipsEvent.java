package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.event.GatherEffectScreenTooltipsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortGatherEffectScreenTooltipsEvent extends PortEvent<GatherEffectScreenTooltipsEvent> {
    @Diff
    public PortGatherEffectScreenTooltipsEvent(GatherEffectScreenTooltipsEvent e) {
        super(e);
    }

    public EffectRenderingInventoryScreen<?> getScreen() {
        return e.getScreen();
    }

    public MobEffectInstance getEffectInstance() {
        return e.getEffectInstance();
    }

    public List<Component> getTooltip() {
        return e.getTooltip();
    }

    static {
        PortEventHooks.register();
    }
}
