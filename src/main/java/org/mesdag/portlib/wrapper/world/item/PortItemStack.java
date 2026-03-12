package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;

@SuppressWarnings("all")
public class PortItemStack {
    public static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy) {
        if (orDefault) {
            CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            return copy ? data.copyTag() : data.getUnsafe();
        }
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? null : copy ? data.copyTag() : data.getUnsafe();
    }

    public static void setCustomData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean getUnbreakable(ItemStack stack) {
        return stack.has(DataComponents.UNBREAKABLE);
    }

    public static @Nullable Component getCustomName(ItemStack stack) {
        return stack.get(DataComponents.CUSTOM_NAME);
    }

    public static Component getItemName(ItemStack stack) {
        Component itemName = stack.get(DataComponents.ITEM_NAME);
        return itemName == null ? stack.getItem().getName(stack) : itemName;
    }

    public static List<Component> getLore(ItemStack stack) {
        return stack.getOrDefault(DataComponents.LORE, ItemLore.EMPTY).styledLines();
    }

    public static @Nullable PortItemEnchantments getEnchantments(ItemStack stack) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(stack)) {
            return null;
        }
        return new PortItemEnchantments(stack);
    }

    public static boolean getCanPlaceOn(ItemStack stack, BlockInWorld block) {
        return stack.canPlaceOnBlockInAdventureMode(block);
    }

    public static boolean getCanBreak(ItemStack stack, BlockInWorld block) {
        return stack.canBreakBlockInAdventureMode(block);
    }

    public static int getCustomModelData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value();
    }

    private static boolean getUnit(ItemStack stack, DataComponentType<Unit> type) {
        return stack.has(type);
    }

    private static void setUnit(ItemStack stack, DataComponentType<Unit> type, boolean has) {
        if (has) {
            stack.set(type, Unit.INSTANCE);
        } else {
            stack.remove(type);
        }
    }

    public static boolean getShowEnchantmentsTooltip(ItemStack stack) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        return enchantments == null || enchantments.showInTooltip;
    }

    public static void setShowEnchantmentsTooltip(ItemStack stack, boolean show) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            stack.set(DataComponents.ENCHANTMENTS, enchantments.withTooltip(show));
        }
    }

    public static boolean getShowStoredEnchantmentsTooltip(ItemStack stack) {
        ItemEnchantments enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
        return enchantments == null || enchantments.showInTooltip;
    }

    public static void setShowStoredEnchantmentsTooltip(ItemStack stack, boolean show) {
        ItemEnchantments enchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);
        if (enchantments != null) {
            stack.set(DataComponents.STORED_ENCHANTMENTS, enchantments.withTooltip(show));
        }
    }

    public static boolean getShowAttributeModifiersTooltip(ItemStack stack) {
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        return modifiers == null || modifiers.showInTooltip();
    }

    public static void setShowAttributeModifiersTooltip(ItemStack stack, boolean show) {
        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers != null) {
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers.withTooltip(show));
        }
    }

    public static boolean getShowUnbreakableTooltip(ItemStack stack) {
        Unbreakable unbreakable = stack.get(DataComponents.UNBREAKABLE);
        return unbreakable == null || unbreakable.showInTooltip();
    }

    public static void setShowUnbreakableTooltip(ItemStack stack, boolean show) {
        Unbreakable unbreakable = stack.get(DataComponents.UNBREAKABLE);
        if (unbreakable != null) {
            stack.set(DataComponents.UNBREAKABLE, unbreakable.withTooltip(show));
        }
    }

    public static boolean getShowCanBreakTooltip(ItemStack stack) {
        AdventureModePredicate predicate = stack.get(DataComponents.CAN_BREAK);
        return predicate == null || predicate.showInTooltip();
    }

    public static void setShowCanBreakTooltip(ItemStack stack, boolean show) {
        AdventureModePredicate predicate = stack.get(DataComponents.CAN_BREAK);
        if (predicate != null) {
            stack.set(DataComponents.CAN_BREAK, predicate.withTooltip(show));
        }
    }

    public static boolean getShowCanPlaceOnTooltip(ItemStack stack) {
        AdventureModePredicate predicate = stack.get(DataComponents.CAN_PLACE_ON);
        return predicate == null || predicate.showInTooltip();
    }

    public static void setShowCanPlaceOnTooltip(ItemStack stack, boolean show) {
        AdventureModePredicate predicate = stack.get(DataComponents.CAN_PLACE_ON);
        if (predicate != null) {
            stack.set(DataComponents.CAN_PLACE_ON, predicate.withTooltip(show));
        }
    }

    public static boolean getShowAdditionalTooltip(ItemStack stack) {
        return !getUnit(stack, DataComponents.HIDE_ADDITIONAL_TOOLTIP);
    }

    public static void setShowAdditionalTooltip(ItemStack stack, boolean show) {
        setUnit(stack, DataComponents.HIDE_ADDITIONAL_TOOLTIP, !show);
    }

    public static boolean getShowDyeTooltip(ItemStack stack) {
        DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
        return color == null || color.showInTooltip();
    }

    public static void setShowDyeTooltip(ItemStack stack, boolean show) {
        DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
        if (color != null) {
            stack.set(DataComponents.DYED_COLOR, color.withTooltip(show));
        }
    }

    public static boolean getShowTrimTooltip(ItemStack stack) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        return trim == null || trim.showInTooltip;
    }

    public static void setShowTrimTooltip(ItemStack stack, boolean show) {
        ArmorTrim trim = stack.get(DataComponents.TRIM);
        if (trim != null) {
            stack.set(DataComponents.TRIM, trim.withTooltip(show));
        }
    }

    public static boolean getShowTooltip(ItemStack stack) {
        return !getUnit(stack, DataComponents.HIDE_TOOLTIP);
    }

    public static void setShowTooltip(ItemStack stack, boolean show) {
        setUnit(stack, DataComponents.HIDE_TOOLTIP, !show);
    }

    public static int getRepaireCost(ItemStack stack) {
        return stack.getOrDefault(DataComponents.REPAIR_COST, 0);
    }

    public static void setRepairCost(ItemStack stack, int cost) {
        stack.set(DataComponents.REPAIR_COST, cost);
    }

    public static boolean getCreativeSlotLock(ItemStack stack) {
        return getUnit(stack, DataComponents.CREATIVE_SLOT_LOCK);
    }

    public static void setCreativeSlotLock(ItemStack stack, boolean lock) {
        setUnit(stack, DataComponents.CREATIVE_SLOT_LOCK, lock);
    }

    public static boolean getEnchantmentGlintOverride(ItemStack stack) {
        return stack.getOrDefault(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
    }

    public static void setEnchantmentGlintOverride(ItemStack stack, boolean override) {
        if (override) {
            stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            stack.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        }
    }

    public static boolean getIntangibleProjectile(ItemStack stack) {
        return getUnit(stack, DataComponents.INTANGIBLE_PROJECTILE);
    }

    public static void setIntangibleProjectile(ItemStack stack, boolean intangible) {
        setUnit(stack, DataComponents.INTANGIBLE_PROJECTILE, intangible);
    }

    public static @Nullable FoodProperties getFood(ItemStack stack, @Nullable LivingEntity living) {
        return stack.getFoodProperties(living);
    }

    public static void setFood(ItemStack stack, @Nullable FoodProperties food) {
        stack.set(DataComponents.FOOD, food);
    }

    public static boolean getFireResistant(ItemStack stack) {
        return getUnit(stack, DataComponents.FIRE_RESISTANT);
    }

    public static void setFireResistant(ItemStack stack, boolean resistant) {
        setUnit(stack, DataComponents.FIRE_RESISTANT, resistant);
    }

    public static @Nullable PortTool getTool(ItemStack stack) {
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool != null) {
            return PortTool.wrap(tool);
        }
        return null;
    }

    public static void setTool(ItemStack stack, @Nullable PortTool tool) {
        stack.set(DataComponents.TOOL, tool == null ? null : tool.unwrap());
    }

    public static @Nullable PortItemEnchantments getStoredEnchantments(ItemStack stack) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(stack)) {
            return new PortItemEnchantments(stack);
        }
        return null;
    }

    public static void setStoredEnchantments(ItemStack stack, PortItemEnchantments value) {
        value.applyTo(stack);
    }

    /// rgb
    public static int getDyedColor(ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, -1);
    }

    public static void setDyedColor(ItemStack stack, int rgb) {
        DyedItemColor color = stack.get(DataComponents.DYED_COLOR);
        if (color != null) {
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb, color.showInTooltip()));
        }
    }

    /// argb
    public static int getMapColor(ItemStack stack) {
        return FastColor.ARGB32.opaque(stack.getOrDefault(DataComponents.MAP_COLOR, MapItemColor.DEFAULT).rgb());
    }

    public static void setMapColor(ItemStack stack, int argb) {
        stack.set(DataComponents.MAP_COLOR, new MapItemColor(argb & 0x00FFFFFF));
    }

    public static @Nullable Integer getMapId(ItemStack stack) {
        MapId mapId = stack.get(DataComponents.MAP_ID);
        return mapId == null ? null : mapId.id();
    }

    public static void setMapId(ItemStack stack, int mapId) {
        stack.set(DataComponents.MAP_ID, new MapId(mapId));
    }

    public static @Nullable PortMapPostProcessing getMapPostProcessing(ItemStack stack) {
        MapPostProcessing processing = stack.get(DataComponents.MAP_POST_PROCESSING);
        if (processing == null) return null;
        return PortMapPostProcessing.wrap(processing);
    }

    public static void setMapPostProcessing(ItemStack stack, PortMapPostProcessing value) {
        stack.set(DataComponents.MAP_POST_PROCESSING, value.unwrap());
    }

    public static PortChargedProjectiles getChargedProjectiles(ItemStack stack) {
        return new PortChargedProjectiles(stack);
    }

    public static void setChargedProjectiles(ItemStack stack, PortChargedProjectiles value) {
        value.applyTo(stack);
    }

    public static PortBundleContents getBundleContents(ItemStack stack) {
        return new PortBundleContents(stack);
    }

    public static void setBundleContents(ItemStack stack, PortBundleContents value) {
        value.applyTo(stack);
    }

    public static PortPotionContents getPotionContents(ItemStack stack) {
        return new PortPotionContents(stack);
    }

    public static void setPotionContents(ItemStack stack, PortPotionContents value) {
        value.applyTo(stack);
    }

    public static PortSuspiciousStewEffects getSuspiciousStewEffects(ItemStack stack) {
        return new PortSuspiciousStewEffects(stack);
    }

    public static void setSuspiciousStewEffects(ItemStack stack, PortSuspiciousStewEffects value) {
        value.applyTo(stack);
    }
}
