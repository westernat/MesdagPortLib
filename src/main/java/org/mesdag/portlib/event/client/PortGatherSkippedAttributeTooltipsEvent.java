package org.mesdag.portlib.event.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;

public class PortGatherSkippedAttributeTooltipsEvent extends PortEvent<GatherSkippedAttributeTooltipsEvent> {
    @Diff
    public PortGatherSkippedAttributeTooltipsEvent(GatherSkippedAttributeTooltipsEvent e) {
        super(e);
    }

    public AttributeTooltipContext getContext() {
        return e.getContext();
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    public void skipId(ResourceLocation id) {
        e.skipId(id);
    }

    public void skipGroup(PortEquipmentSlotGroup group) {
        e.skipGroup(group.unwrap());
    }

    public boolean isSkipped(ResourceLocation id) {
        return e.isSkipped(id);
    }

    public boolean isSkipped(PortEquipmentSlotGroup group) {
        return e.isSkipped(group.unwrap());
    }

    public void setSkipAll(boolean skip) {
        e.setSkipAll(skip);
    }

    public boolean isSkippingAll() {
        return e.isSkippingAll();
    }

    static {
        PortEventHooks.register();
    }
}
