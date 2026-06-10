package org.mesdag.portlib.event.client;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.util.PortAttributeTooltipContext;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttributeModifier;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class PortGatherSkippedAttributeTooltipsEvent extends Event {
    protected final ItemStack stack;
    protected final PortAttributeTooltipContext ctx;

    private @Nullable Set<ResourceLocation> skippedIds = null;
    private @Nullable Set<PortEquipmentSlotGroup> skippedGroups = null;
    private boolean skipAll = false;

    @Diff
    public PortGatherSkippedAttributeTooltipsEvent(ItemStack stack, PortAttributeTooltipContext ctx) {
        this.stack = stack;
        this.ctx = ctx;
    }

    public PortAttributeTooltipContext getContext() {
        return ctx;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void skipId(ResourceLocation id) {
        getSkippedIds().add(id);
    }

    public void skipGroup(PortEquipmentSlotGroup group) {
        getSkippedGroups().add(group);
    }

    public boolean isSkipped(ResourceLocation id) {
        return skipAll || (skippedIds != null && skippedIds.contains(id));
    }

    public boolean isSkipped(PortEquipmentSlotGroup group) {
        return skipAll || (skippedGroups != null && skippedGroups.contains(group));
    }

    public void setSkipAll(boolean skip) {
        this.skipAll = skip;
    }

    public boolean isSkippingAll() {
        return skipAll;
    }

    protected Set<ResourceLocation> getSkippedIds() {
        if (this.skippedIds == null) {
            this.skippedIds = new HashSet<>();
        }
        return this.skippedIds;
    }

    protected Set<PortEquipmentSlotGroup> getSkippedGroups() {
        if (this.skippedGroups == null) {
            this.skippedGroups = EnumSet.noneOf(PortEquipmentSlotGroup.class);
        }
        return this.skippedGroups;
    }

    // Internal Only

    @Diff
    public static final Multimap<Attribute, AttributeModifier> EMPTY = Multimaps.unmodifiableMultimap(HashMultimap.create());

    @Diff
    public boolean hasSkippedIds() {
        return skippedIds != null;
    }

    @Diff
    public boolean isSkipped(AttributeModifier modifier) {
        if (skipAll) return true;
        if (skippedIds == null) return false;
        ResourceLocation id = PortAttributeModifier.uuid2rl(modifier.getId());
        return skippedIds.contains(id);
    }

    @Diff
    public boolean isSkipped(EquipmentSlot slot) {
        if (skipAll) return true;
        if (skippedGroups == null) return false;
        if (skippedGroups.contains(PortEquipmentSlotGroup.ANY)) return true;
        if (skippedGroups.contains(PortEquipmentSlotGroup.HAND)) {
            return slot.getType() == EquipmentSlot.Type.HAND;
        }
        if (skippedGroups.contains(PortEquipmentSlotGroup.ARMOR)) {
            return slot.isArmor();
        }
        return skippedGroups.contains(PortEquipmentSlotGroup.fromSlot(slot));
    }
}
