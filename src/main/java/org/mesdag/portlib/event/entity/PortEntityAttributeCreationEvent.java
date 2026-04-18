package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortEntityAttributeCreationEvent extends PortEvent<EntityAttributeCreationEvent> implements IPortModBusEvent {

    @Diff
    public PortEntityAttributeCreationEvent(EntityAttributeCreationEvent e) {
        super(e);
    }

    public void put(EntityType<? extends LivingEntity> entity, AttributeSupplier supplier) {
        e.put(entity, supplier);
    }

    static {
        PortEventHooks.register();
    }
}
