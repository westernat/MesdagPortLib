package org.mesdag.portlib.event.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortAttributeTooltipContext;

public class PortAddAttributeTooltipsEvent extends PortEvent<AddAttributeTooltipsEvent> {
    private PortAttributeTooltipContext context;

    @Diff
    public PortAddAttributeTooltipsEvent(AddAttributeTooltipsEvent e) {
        super(e);
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    public PortAttributeTooltipContext getContext() {
        if (context == null) {
            this.context = e.getContext().wrap();
        }
        return context;
    }

    public void addTooltipLines(Component... comps) {
        e.addTooltipLines(comps);
    }

    public boolean shouldShow() {
        return e.shouldShow();
    }

    static {
        PortEventHooks.register();
    }
}
