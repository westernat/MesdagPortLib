package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.function.BiConsumer;

public class PortItemAttributeModifiers {
    public static final PortItemAttributeModifiers EMPTY = new PortItemAttributeModifiers(ItemAttributeModifiers.EMPTY);
    private final ItemAttributeModifiers delegate;
    private final boolean showInTooltip;

    @Diff
    public PortItemAttributeModifiers(ItemAttributeModifiers delegate) {
        this.delegate = delegate;
        this.showInTooltip = delegate.showInTooltip();
    }

    @Diff
    public ItemAttributeModifiers unwrap() {
        return delegate;
    }

    public boolean showInTooltip() {
        return showInTooltip;
    }

    public PortItemAttributeModifiers withTooltip(boolean showInTooltip) {
        return new PortItemAttributeModifiers(delegate.withTooltip(showInTooltip));
    }

    public PortItemAttributeModifiers withModifierAdded(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
        return unwrap().withModifierAdded(attribute, modifier.unwrap(), group.unwrap()).wrap();
    }

    public void forEach(PortEquipmentSlotGroup group, BiConsumer<Holder<Attribute>, PortAttributeModifier> action) {
        unwrap().forEach(group.unwrap(), (holder, modifier) -> action.accept(holder, modifier.wrap()));
    }

    public void forEach(EquipmentSlot slot, BiConsumer<Holder<Attribute>, PortAttributeModifier> action) {
        unwrap().forEach(slot, (holder, modifier) -> action.accept(holder, modifier.wrap()));
    }

    public double compute(double baseValue, EquipmentSlot slot) {
        return unwrap().compute(baseValue, slot);
    }

    public boolean isEmpty() {
        return delegate.modifiers().isEmpty();
    }

    public static PortBuilder builder() {
        return new PortBuilder();
    }

    public static class PortBuilder {
        private final ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        private PortBuilder() {}

        public PortBuilder add(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
            builder.add(attribute, modifier.unwrap(), group.unwrap());
            return this;
        }

        public PortItemAttributeModifiers build() {
            return builder.build().wrap();
        }
    }
}
