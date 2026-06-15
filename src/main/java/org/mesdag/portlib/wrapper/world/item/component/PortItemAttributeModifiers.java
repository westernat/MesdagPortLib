package org.mesdag.portlib.wrapper.world.item.component;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute.PortAttributeExtension;
import PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeModifier.PortAttributeModifierExtension;
import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.EnumMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PortItemAttributeModifiers {
    public static final PortItemAttributeModifiers EMPTY = new PortItemAttributeModifiers(new ListTag(), true);
    private final ListTag listTag;
    private final boolean showInTooltip;

    private @Nullable EnumMap<EquipmentSlot, Multimap<Attribute, AttributeModifier>> mapCache;
    private @Nullable List<PortEntry> listCache;

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

    public PortItemAttributeModifiers(List<PortEntry> modifiers, boolean showInTooltip) {
        ListTag listTag = new ListTag();
        for (PortEntry entry : modifiers) {
            addModifier(entry.attribute, entry.modifier, entry.slot, listTag);
        }
        this.listTag = listTag;
        this.showInTooltip = showInTooltip;
        this.listCache = modifiers;
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
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (group.test(slot)) {
                    CompoundTag tag = modifier.unwrap().save();
                    tag.putString("AttributeName", attributeName);
                    tag.putString("Slot", slot.getName());
                    listTag.add(tag);
                }
            }
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
        if (mapCache == null) {
            this.mapCache = new EnumMap<>(EquipmentSlot.class);
        }
        return mapCache.computeIfAbsent(slot, s -> {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            forEach(slot, (h, m) -> builder.put(h.value(), m.unwrap()));
            return builder.build();
        });
    }

    public static PortBuilder builder() {
        return new PortBuilder();
    }

    public List<PortEntry> modifiers() {
        if (listCache == null) {
            ImmutableList.Builder<PortEntry> builder = ImmutableList.builder();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag tag = listTag.getCompound(i);
                Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(tag.getString("AttributeName")));
                if (attribute == null) continue;
                AttributeModifier modifier = AttributeModifier.load(tag);
                if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                    PortEquipmentSlotGroup group = tag.contains("Slot", 8)
                            ? PortEquipmentSlotGroup.fromSlot(EquipmentSlot.byName(tag.getString("Slot")))
                            : PortEquipmentSlotGroup.ANY;
                    builder.add(new PortEntry(
                            PortAttributeExtension.wrap(attribute),
                            PortAttributeModifierExtension.wrap(modifier),
                            group
                    ));
                }
            }
            this.listCache = builder.build();
        }
        return listCache;
    }

    public static class PortBuilder {
        private final ListTag listTag = new ListTag();

        private PortBuilder() {}

        public PortBuilder add(Holder<Attribute> attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
            addModifier(attribute, modifier, group, listTag);
            return this;
        }

        @Diff
        public PortBuilder add(Attribute attribute, PortAttributeModifier modifier, PortEquipmentSlotGroup group) {
            return add(AttributeHolder.wrap(attribute), modifier, group);
        }

        @Diff
        public PortBuilder add(Holder<Attribute> attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation, PortEquipmentSlotGroup group) {
            return add(attribute, new PortAttributeModifier(id, amount, operation), group);
        }

        @Diff
        public PortBuilder add(Attribute attribute, ResourceLocation id, double amount, PortAttributeModifier.PortOperation operation, PortEquipmentSlotGroup group) {
            return add(AttributeHolder.wrap(attribute), id, amount, operation, group);
        }

        public PortItemAttributeModifiers build() {
            return new PortItemAttributeModifiers(listTag, true);
        }
    }

    public record PortEntry(
            Holder<Attribute> attribute,
            PortAttributeModifier modifier,
            PortEquipmentSlotGroup slot
    ) {
        public static final Codec<PortEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PortAttributeExtension.codec().fieldOf("type").forGetter(PortEntry::attribute),
                PortAttributeModifier.MAP_CODEC.forGetter(PortEntry::modifier),
                PortEquipmentSlotGroup.CODEC.optionalFieldOf("slot", PortEquipmentSlotGroup.ANY).forGetter(PortEntry::slot)
        ).apply(instance, PortEntry::new));
        public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortEntry> STREAM_CODEC = PortStreamCodec.composite(
                PortAttributeExtension.streamCodec(), PortEntry::attribute,
                PortAttributeModifier.STREAM_CODEC, PortEntry::modifier,
                PortEquipmentSlotGroup.STREAM_CODEC, PortEntry::slot,
                PortEntry::new
        );

        public boolean matches(Holder<Attribute> attribute, ResourceLocation id) {
            return attribute.equals(this.attribute) && modifier.is(id);
        }
    }
}
