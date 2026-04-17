package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.GatherSkippedAttributeTooltipsEvent;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortGatherSkippedAttributeTooltipsEvent extends PortEvent {
    private final GatherSkippedAttributeTooltipsEvent e;

    @Diff
    public PortGatherSkippedAttributeTooltipsEvent(GatherSkippedAttributeTooltipsEvent e) {
        super(e);
        this.e = e;
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

    public void skipGroup(EquipmentSlotGroup group) {
        e.skipGroup(group);
    }

    public boolean isSkipped(ResourceLocation id) {
        return e.isSkipped(id);
    }

    public boolean isSkipped(EquipmentSlotGroup group) {
        return e.isSkipped(group);
    }

    public void setSkipAll(boolean skip) {
        e.setSkipAll(skip);
    }

    public boolean isSkippingAll() {
        return e.isSkippingAll();
    }

    static {
        PortEventHooks.register(GatherSkippedAttributeTooltipsEvent.class, PortGatherSkippedAttributeTooltipsEvent.class, PortGatherSkippedAttributeTooltipsEvent::new);
    }
}