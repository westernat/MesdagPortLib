package org.mesdag.portlib.event.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.util.PortAttributeTooltipContext;

import java.util.function.Consumer;

public class PortAddAttributeTooltipsEvent extends Event {
    protected final ItemStack stack;
    protected final Consumer<Component> tooltip;
    protected final PortAttributeTooltipContext ctx;

    @Diff
    public PortAddAttributeTooltipsEvent(ItemStack stack, Consumer<Component> tooltip, PortAttributeTooltipContext ctx) {
        this.stack = stack;
        this.tooltip = tooltip;
        this.ctx = ctx;
    }

    public ItemStack getStack() {
        return stack;
    }

    public PortAttributeTooltipContext getContext() {
        return ctx;
    }

    public void addTooltipLines(Component... comps) {
        for (Component comp : comps) {
            this.tooltip.accept(comp);
        }
    }

    public boolean shouldShow() {
        return stack.getShowAttributeModifiersTooltip();
    }

    static {
        PortEventHooks.register();
    }
}
