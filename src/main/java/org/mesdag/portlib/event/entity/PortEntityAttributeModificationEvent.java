package org.mesdag.portlib.event.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortEntityAttributeModificationEvent extends PortEvent<EntityAttributeModificationEvent> implements IPortModBusEvent {
    @Diff
    public PortEntityAttributeModificationEvent(EntityAttributeModificationEvent e) {
        super(e);
    }

    public void add(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute, double value) {
        e.add(entityType, attribute, value);
    }

    public void add(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute) {
        e.add(entityType, attribute);
    }

    public boolean has(EntityType<? extends LivingEntity> entityType, Holder<Attribute> attribute) {
        return e.has(entityType, attribute);
    }

    public List<EntityType<? extends LivingEntity>> getTypes() {
        return e.getTypes();
    }

    static {
        PortEventHooks.register(EntityAttributeModificationEvent.class, PortEntityAttributeModificationEvent.class, PortEntityAttributeModificationEvent::new);
    }
}
