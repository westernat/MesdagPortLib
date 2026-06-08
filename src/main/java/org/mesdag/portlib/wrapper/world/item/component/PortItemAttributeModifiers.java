package org.mesdag.portlib.wrapper.world.item.component;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier.PortAttributeModifierExtension;
import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PortItemAttributeModifiers {
    public static final PortItemAttributeModifiers EMPTY = new PortItemAttributeModifiers(new ListTag(), true);
    private final ListTag listTag;
    private final boolean showInTooltip;

    private @Nullable EnumMap<EquipmentSlot, Multimap<Attribute, AttributeModifier>> cache;

    @Diff
    public PortItemAttributeModifiers(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("AttributeModifiers", Tag.TAG_LIST)) {
            this.listTag = tag.getList("AttributeModifiers", Tag.TAG_COMPOUND);
        } else {
            this.listTag = new ListTag();
        }
        this.showInTooltip = PortItemStackExtension.getShowAttributeModifiersTooltip(stack);
    }

    @Diff
    public PortItemAttributeModifiers(ListTag listTag, boolean showInTooltip) {
        this.listTag = listTag;
        this.showInTooltip = showInTooltip;
    }

    public boolean showInTooltip() {
        return showInTooltip;
    }

    public PortItemAttributeModifiers withTooltip(boolean showInTooltip) {
        return new PortItemAttributeModifiers(listTag, showInTooltip);
    }

    public PortItemAttributeModifiers withModifierAdded(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
        ListTag copy = listTag.copy();
        addModifier(attribute, modifier, group, copy);
        return new PortItemAttributeModifiers(copy, showInTooltip);
    }

    @Diff
    public static void addModifier(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group, ListTag listTag) {
        String attributeName = PortHolderExtension.getRegisteredName(attribute);
        if (group != null && group != PortEquipmentSlotGroup.ANY) {
            Arrays.stream(EquipmentSlot.values()).filter(group::test).forEach(slot -> {
                CompoundTag tag = modifier.unwrap().save();
                tag.putString("AttributeName", attributeName);
                tag.putString("Slot", slot.getName());
                listTag.add(tag);
            });
        } else {
            CompoundTag tag = modifier.unwrap().save();
            tag.putString("AttributeName", attributeName);
            listTag.add(tag);
        }
    }

    private void forEach(BiConsumer<Holder<Attribute>, PortAttributeModifier> action, Predicate<String> predicate) {
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            if (!tag.contains("Slot", 8) || predicate.test(tag.getString("Slot"))) {
                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(tag.getString("AttributeName")));
                if (attribute == null) continue;
                AttributeModifier modifier = AttributeModifier.load(tag);
                if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                    action.accept(PortAttributeExtension.wrap(attribute), PortAttributeModifierExtension.wrap(modifier));
                }
            }
        }
    }

    public void forEach(PortEquipmentSlotGroup group, BiConsumer<Holder<Attribute>, PortAttributeModifier> action) {
        forEach(action, s -> group.test(EquipmentSlot.byName(s)));
    }

    public void forEach(EquipmentSlot slot, BiConsumer<Holder<Attribute>, PortAttributeModifier> action) {
        forEach(action, s -> s.equals(slot.getName()));
    }

    public double compute(double baseValue, EquipmentSlot slot) {
        MutableDouble value = new MutableDouble(baseValue);
        forEach((attribute, modifier) -> {
            double amount = modifier.amount();
            value.add(switch (modifier.operation()) {
                case ADD_VALUE -> amount;
                case ADD_MULTIPLIED_BASE -> amount * baseValue;
                case ADD_MULTIPLIED_TOTAL -> amount * value.doubleValue();
            });
        }, s -> s.equals(slot.getName()));
        return value.doubleValue();
    }

    public boolean isEmpty() {
        return listTag.isEmpty();
    }

    @Diff
    public void applyTo(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("AttributeModifiers", listTag);
        PortItemStackExtension.setShowAttributeModifiersTooltip(stack, showInTooltip);
    }

    @Diff
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (isEmpty()) return ImmutableMultimap.of();
        if (cache == null) {
            cache = new EnumMap<>(EquipmentSlot.class);
        }
        return cache.computeIfAbsent(slot, s -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            forEach(slot, (h, m) -> builder.put(h.value(), m.unwrap()));
            return builder.build();
        });
    }

    public static PortBuilder builder() {
        return new PortBuilder();
    }

    public static class PortBuilder {
        private final ListTag listTag = new ListTag();

        private PortBuilder() {}

        public PortBuilder add(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
            addModifier(attribute, modifier, group, listTag);
            return this;
        }

        public PortItemAttributeModifiers build() {
            return new PortItemAttributeModifiers(listTag, true);
        }
    }
}
