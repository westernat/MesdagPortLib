package org.mesdag.portlib.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;

import java.util.List;

public class PortEntityAttributeModificationEvent extends PortEvent<EntityAttributeModificationEvent> implements IPortModBusEvent {
    @Diff
    public PortEntityAttributeModificationEvent(EntityAttributeModificationEvent e) {
        super(e);
    }

    public void add(EntityType<? extends LivingEntity> entityType, AttributeHolder attribute, double value) {
        e.add(entityType, attribute.value(), value);
    }

    public void add(EntityType<? extends LivingEntity> entityType, AttributeHolder attribute) {
        e.add(entityType, attribute.value());
    }

    public boolean has(EntityType<? extends LivingEntity> entityType, AttributeHolder attribute) {
        return e.has(entityType, attribute.value());
    }

    public List<EntityType<? extends LivingEntity>> getTypes() {
        return e.getTypes();
    }

    static {
        PortEventHooks.register(EntityAttributeModificationEvent.class, PortEntityAttributeModificationEvent.class, PortEntityAttributeModificationEvent::new);
    }
}
