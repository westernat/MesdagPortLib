package org.mesdag.portlib.event.other;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.extensions.IPortAttributeModifierExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemStackExtension;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

import java.util.*;
import java.util.function.Predicate;

public class PortItemAttributeModifierEvent extends PortEvent<ItemAttributeModifierEvent> {
    private final PortItemAttributeModifiers defaultModifiers;
    private @Nullable List<PortItemAttributeModifiers.Entry> entries;
    private boolean changed;

    @Diff
    public PortItemAttributeModifierEvent(ItemAttributeModifierEvent e) {
        super(e);
        this.defaultModifiers = IPortItemStackExtension.of(e.getItemStack()).getPortAttributeModifiers();
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public PortItemAttributeModifiers getDefaultModifiers() {
        return defaultModifiers;
    }

    public List<PortItemAttributeModifiers.Entry> getModifiers() {
        if (entries == null) {
            return defaultModifiers.modifiers();
        }
        return Collections.unmodifiableList(entries);
    }

    public boolean addModifier(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup slot) {
        if (slot.test(e.getSlotType()) && e.addModifier(attribute.value(), modifier.unwrap())) {
            this.changed = true;
            return true;
        }
        return false;
    }

    @Diff
    public boolean addModifier(Attribute attribute, AttributeModifier modifier, PortEquipmentSlotGroup slot) {
        if (slot.test(e.getSlotType()) && e.addModifier(attribute, modifier)) {
            this.changed = true;
            return true;
        }
        return false;
    }

    public boolean removeModifier(Holder<Attribute> attribute, ResourceLocation id) {
        Attribute value = attribute.value();
        UUID uuid = PortAttributeModifier.rl2uuid(id);
        for (AttributeModifier modifier : e.getModifiers().get(value)) {
            if (modifier != null && uuid.equals(modifier.getId())) {
                if (e.removeModifier(value, modifier)) {
                    this.changed = true;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void replaceModifier(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup slot) {
        if (slot.test(e.getSlotType())) {
            Attribute attribute1 = attribute.value();
            AttributeModifier modifier1 = modifier.unwrap();
            e.removeModifier(attribute1, modifier1);
            e.addModifier(attribute1, modifier1);
            this.changed = true;
        }
    }

    public boolean removeIf(Predicate<PortItemAttributeModifiers.Entry> condition) {
        PortEquipmentSlotGroup group = PortEquipmentSlotGroup.fromSlot(e.getSlotType());
        for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : e.getModifiers().asMap().entrySet()) {
            Attribute attribute = entry.getKey();
            Holder<Attribute> holder = AttributeHolder.wrap(attribute);
            for (AttributeModifier modifier : entry.getValue()) {
                PortItemAttributeModifiers.Entry portEntry = new PortItemAttributeModifiers.Entry(holder, IPortAttributeModifierExtension.of(modifier).wrap(), group);
                if (condition.test(portEntry)) {
                    if (e.removeModifier(attribute, modifier)) {
                        this.changed = true;
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public boolean removeAllModifiersFor(Holder<Attribute> attribute) {
        if (!e.removeAttribute(attribute.value()).isEmpty()) {
            this.changed = true;
            return true;
        }
        return false;
    }

    public void clearModifiers() {
        e.clearModifiers();
        this.changed = true;
    }

    public PortItemAttributeModifiers build() {
        if (changed) {
            this.changed = false;
            PortEquipmentSlotGroup group = PortEquipmentSlotGroup.fromSlot(e.getSlotType());
            List<PortItemAttributeModifiers.Entry> list = new LinkedList<>();
            for (Map.Entry<Attribute, Collection<AttributeModifier>> entry : e.getModifiers().asMap().entrySet()) {
                Holder<Attribute> attribute = AttributeHolder.wrap(entry.getKey());
                for (AttributeModifier modifier : entry.getValue()) {
                    list.add(new PortItemAttributeModifiers.Entry(attribute, IPortAttributeModifierExtension.of(modifier).wrap(), group));
                }
            }
            this.entries = list;
            return new PortItemAttributeModifiers(entries, defaultModifiers.showInTooltip());
        }
        return defaultModifiers;
    }

    static {
        PortEventHooks.register();
    }
}
