package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.Function;

public class PortRegisterClientTooltipComponentFactoriesEvent extends PortEvent<RegisterClientTooltipComponentFactoriesEvent> {
    @Diff
    public PortRegisterClientTooltipComponentFactoriesEvent(RegisterClientTooltipComponentFactoriesEvent e) {
        super(e);
    }

    public <T extends TooltipComponent> void register(Class<T> type, Function<? super T, ? extends ClientTooltipComponent> factory) {
        e.register(type, factory);
    }

    static {
        PortEventHooks.register();
    }
}
