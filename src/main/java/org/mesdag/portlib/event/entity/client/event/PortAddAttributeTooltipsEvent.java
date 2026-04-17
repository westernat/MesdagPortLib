package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.AddAttributeTooltipsEvent;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAddAttributeTooltipsEvent extends PortEvent {
    private final AddAttributeTooltipsEvent e;

    @Diff
    public PortAddAttributeTooltipsEvent(AddAttributeTooltipsEvent e) {
        super(e);
        this.e = e;
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    public AttributeTooltipContext getContext() {
        return e.getContext();
    }

    public void addTooltipLines(Component... comps) {
        e.addTooltipLines(comps);
    }

    public boolean shouldShow() {
        return e.shouldShow();
    }

    static {
        PortEventHooks.register(AddAttributeTooltipsEvent.class, PortAddAttributeTooltipsEvent.class, PortAddAttributeTooltipsEvent::new);
    }
}