package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;


public class PortLivingEquipmentChangeEvent extends PortLivingEvent{
    private final LivingEquipmentChangeEvent e;

    @Diff
    public PortLivingEquipmentChangeEvent(LivingEquipmentChangeEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public EquipmentSlot getSlot() {
        return e.getSlot();
    }

    public ItemStack getFrom() {
        return e.getFrom();
    }

    public ItemStack getTo() {
        return e.getTo();
    }

    static {
        PortEventHooks.register(LivingEquipmentChangeEvent.class, PortLivingEquipmentChangeEvent.class, PortLivingEquipmentChangeEvent::new);
    }
}