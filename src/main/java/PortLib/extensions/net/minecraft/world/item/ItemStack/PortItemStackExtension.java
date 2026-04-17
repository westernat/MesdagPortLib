package PortLib.extensions.net.minecraft.world.item.ItemStack;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.item.InstrumentHolder;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;

@Extension
@SuppressWarnings("all")
public class PortItemStackExtension {
    private static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy, DataComponentType<CustomData> type) {
        CustomData data = orDefault ? stack.getOrDefault(type, CustomData.EMPTY) : stack.get(type);
        if (data == null) return null;
        return copy ? data.copyTag() : data.getUnsafe();
    }

    private static void setCustomData(ItemStack stack, CompoundTag data, DataComponentType<CustomData> type) {
        stack.set(type, CustomData.of(data));
    }

    public static @Nullable CompoundTag getCustomData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, DataComponents.CUSTOM_DATA);
    }

    public static void setCustomData(@This ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, DataComponents.CUSTOM_DATA);
    }

    public static boolean getUnbreakable(@This ItemStack thiz) {
        return thiz.has(DataComponents.UNBREAKABLE);
    }

    public static @Nullable Component getCustomName(@This ItemStack thiz) {
        return thiz.get(DataComponents.CUSTOM_NAME);
    }

    public static Component getItemName(@This ItemStack thiz) {
        Component itemName = thiz.get(DataComponents.ITEM_NAME);
        return itemName == null ? thiz.getItem().getName(thiz) : itemName;
    }

    public static List<Component> getLore(@This ItemStack thiz) {
        return thiz.getOrDefault(DataComponents.LORE, ItemLore.EMPTY).styledLines();
    }

    public static @Nullable PortItemEnchantments getEnchantments(@This ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return null;
        }
        return PortItemEnchantments.wrap(thiz.get(DataComponents.ENCHANTMENTS));
    }

    public static void setEnchantments(@This ItemStack thiz, PortItemEnchantments value) {
        thiz.set(DataComponents.ENCHANTMENTS, value.unwrap());
    }

    public static boolean getCanPlaceOn(@This ItemStack thiz, BlockInWorld block) {
        return thiz.canPlaceOnBlockInAdventureMode(block);
    }

    public static boolean getCanBreak(@This ItemStack thiz, BlockInWorld block) {
        return thiz.canBreakBlockInAdventureMode(block);
    }

    public static int getCustomModelData(@This ItemStack thiz) {
        return thiz.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.DEFAULT).value();
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

    public static boolean getShowEnchantmentsTooltip(@This ItemStack thiz) {
        ItemEnchantments enchantments = thiz.get(DataComponents.ENCHANTMENTS);
        return enchantments == null || enchantments.showInTooltip;
    }

    public static void setShowEnchantmentsTooltip(@This ItemStack thiz, boolean show) {
        ItemEnchantments enchantments = thiz.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            thiz.set(DataComponents.ENCHANTMENTS, enchantments.withTooltip(show));
        }
    }

    public static boolean getShowStoredEnchantmentsTooltip(@This ItemStack thiz) {
        ItemEnchantments enchantments = thiz.get(DataComponents.STORED_ENCHANTMENTS);
        return enchantments == null || enchantments.showInTooltip;
    }

    public static void setShowStoredEnchantmentsTooltip(@This ItemStack thiz, boolean show) {
        ItemEnchantments enchantments = thiz.get(DataComponents.STORED_ENCHANTMENTS);
        if (enchantments != null) {
            thiz.set(DataComponents.STORED_ENCHANTMENTS, enchantments.withTooltip(show));
        }
    }

    public static boolean getShowAttributeModifiersTooltip(@This ItemStack thiz) {
        ItemAttributeModifiers modifiers = thiz.get(DataComponents.ATTRIBUTE_MODIFIERS);
        return modifiers == null || modifiers.showInTooltip();
    }

    public static void setShowAttributeModifiersTooltip(@This ItemStack thiz, boolean show) {
        ItemAttributeModifiers modifiers = thiz.get(DataComponents.ATTRIBUTE_MODIFIERS);
        if (modifiers != null) {
            thiz.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers.withTooltip(show));
        }
    }

    public static boolean getShowUnbreakableTooltip(@This ItemStack thiz) {
        Unbreakable unbreakable = thiz.get(DataComponents.UNBREAKABLE);
        return unbreakable == null || unbreakable.showInTooltip();
    }

    public static void setShowUnbreakableTooltip(@This ItemStack thiz, boolean show) {
        Unbreakable unbreakable = thiz.get(DataComponents.UNBREAKABLE);
        if (unbreakable != null) {
            thiz.set(DataComponents.UNBREAKABLE, unbreakable.withTooltip(show));
        }
    }

    public static boolean getShowCanBreakTooltip(@This ItemStack thiz) {
        AdventureModePredicate predicate = thiz.get(DataComponents.CAN_BREAK);
        return predicate == null || predicate.showInTooltip();
    }

    public static void setShowCanBreakTooltip(@This ItemStack thiz, boolean show) {
        AdventureModePredicate predicate = thiz.get(DataComponents.CAN_BREAK);
        if (predicate != null) {
            thiz.set(DataComponents.CAN_BREAK, predicate.withTooltip(show));
        }
    }

    public static boolean getShowCanPlaceOnTooltip(@This ItemStack thiz) {
        AdventureModePredicate predicate = thiz.get(DataComponents.CAN_PLACE_ON);
        return predicate == null || predicate.showInTooltip();
    }

    public static void setShowCanPlaceOnTooltip(@This ItemStack thiz, boolean show) {
        AdventureModePredicate predicate = thiz.get(DataComponents.CAN_PLACE_ON);
        if (predicate != null) {
            thiz.set(DataComponents.CAN_PLACE_ON, predicate.withTooltip(show));
        }
    }

    public static boolean getShowAdditionalTooltip(@This ItemStack thiz) {
        return !getUnit(thiz, DataComponents.HIDE_ADDITIONAL_TOOLTIP);
    }

    public static void setShowAdditionalTooltip(@This ItemStack thiz, boolean show) {
        setUnit(thiz, DataComponents.HIDE_ADDITIONAL_TOOLTIP, !show);
    }

    public static boolean getShowDyeTooltip(@This ItemStack thiz) {
        DyedItemColor color = thiz.get(DataComponents.DYED_COLOR);
        return color == null || color.showInTooltip();
    }

    public static void setShowDyeTooltip(@This ItemStack thiz, boolean show) {
        DyedItemColor color = thiz.get(DataComponents.DYED_COLOR);
        if (color != null) {
            thiz.set(DataComponents.DYED_COLOR, color.withTooltip(show));
        }
    }

    public static boolean getShowTrimTooltip(@This ItemStack thiz) {
        ArmorTrim trim = thiz.get(DataComponents.TRIM);
        return trim == null || trim.showInTooltip;
    }

    public static void setShowTrimTooltip(@This ItemStack thiz, boolean show) {
        ArmorTrim trim = thiz.get(DataComponents.TRIM);
        if (trim != null) {
            thiz.set(DataComponents.TRIM, trim.withTooltip(show));
        }
    }

    public static boolean getShowTooltip(@This ItemStack thiz) {
        return !getUnit(thiz, DataComponents.HIDE_TOOLTIP);
    }

    public static void setShowTooltip(@This ItemStack thiz, boolean show) {
        setUnit(thiz, DataComponents.HIDE_TOOLTIP, !show);
    }

    public static int getRepaireCost(@This ItemStack thiz) {
        return thiz.getOrDefault(DataComponents.REPAIR_COST, 0);
    }

    public static void setRepairCost(@This ItemStack thiz, int cost) {
        thiz.set(DataComponents.REPAIR_COST, cost);
    }

    public static boolean getCreativeSlotLock(@This ItemStack thiz) {
        return getUnit(thiz, DataComponents.CREATIVE_SLOT_LOCK);
    }

    public static void setCreativeSlotLock(@This ItemStack thiz, boolean lock) {
        setUnit(thiz, DataComponents.CREATIVE_SLOT_LOCK, lock);
    }

    public static boolean getEnchantmentGlintOverride(@This ItemStack thiz) {
        return thiz.getOrDefault(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
    }

    public static void setEnchantmentGlintOverride(@This ItemStack thiz, boolean override) {
        if (override) {
            thiz.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            thiz.remove(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        }
    }

    public static boolean getIntangibleProjectile(@This ItemStack thiz) {
        return getUnit(thiz, DataComponents.INTANGIBLE_PROJECTILE);
    }

    public static void setIntangibleProjectile(@This ItemStack thiz, boolean intangible) {
        setUnit(thiz, DataComponents.INTANGIBLE_PROJECTILE, intangible);
    }

    public static @Nullable FoodProperties getFood(@This ItemStack thiz, @Nullable LivingEntity living) {
        return thiz.getFoodProperties(living);
    }

    public static void setFood(@This ItemStack thiz, @Nullable FoodProperties food) {
        thiz.set(DataComponents.FOOD, food);
    }

    public static boolean getFireResistant(@This ItemStack thiz) {
        return getUnit(thiz, DataComponents.FIRE_RESISTANT);
    }

    public static void setFireResistant(@This ItemStack thiz, boolean resistant) {
        setUnit(thiz, DataComponents.FIRE_RESISTANT, resistant);
    }

    public static @Nullable PortTool getTool(@This ItemStack thiz) {
        Tool tool = thiz.get(DataComponents.TOOL);
        if (tool != null) {
            return PortTool.wrap(tool);
        }
        return null;
    }

    public static void setTool(@This ItemStack thiz, @Nullable PortTool tool) {
        thiz.set(DataComponents.TOOL, tool == null ? null : tool.unwrap());
    }

    public static @Nullable PortItemEnchantments getStoredEnchantments(@This ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return PortItemEnchantments.wrap(thiz.get(DataComponents.STORED_ENCHANTMENTS));
        }
        return null;
    }

    public static void setStoredEnchantments(@This ItemStack thiz, PortItemEnchantments value) {
        thiz.set(DataComponents.STORED_ENCHANTMENTS, value.unwrap());
    }

    /// rgb
    public static int getDyedColor(@This ItemStack thiz) {
        return DyedItemColor.getOrDefault(thiz, -1);
    }

    public static void setDyedColor(@This ItemStack thiz, int rgb) {
        DyedItemColor color = thiz.get(DataComponents.DYED_COLOR);
        if (color != null) {
            thiz.set(DataComponents.DYED_COLOR, new DyedItemColor(rgb, color.showInTooltip()));
        }
    }

    /// argb
    public static int getMapColor(@This ItemStack thiz) {
        return FastColor.ARGB32.opaque(thiz.getOrDefault(DataComponents.MAP_COLOR, MapItemColor.DEFAULT).rgb());
    }

    public static void setMapColor(@This ItemStack thiz, int argb) {
        thiz.set(DataComponents.MAP_COLOR, new MapItemColor(argb & 0x00FFFFFF));
    }

    public static @Nullable Integer getMapId(@This ItemStack thiz) {
        MapId mapId = thiz.get(DataComponents.MAP_ID);
        return mapId == null ? null : mapId.id();
    }

    public static void setMapId(@This ItemStack thiz, int mapId) {
        thiz.set(DataComponents.MAP_ID, new MapId(mapId));
    }

    public static @Nullable PortMapPostProcessing getMapPostProcessing(@This ItemStack thiz) {
        MapPostProcessing processing = thiz.get(DataComponents.MAP_POST_PROCESSING);
        if (processing == null) return null;
        return PortMapPostProcessing.wrap(processing);
    }

    public static void setMapPostProcessing(@This ItemStack thiz, PortMapPostProcessing value) {
        thiz.set(DataComponents.MAP_POST_PROCESSING, value.unwrap());
    }

    public static PortChargedProjectiles getChargedProjectiles(@This ItemStack thiz) {
        return new PortChargedProjectiles(thiz);
    }

    public static void setChargedProjectiles(@This ItemStack thiz, PortChargedProjectiles value) {
        value.applyTo(thiz);
    }

    public static PortBundleContents getBundleContents(@This ItemStack thiz) {
        return new PortBundleContents(thiz);
    }

    public static void setBundleContents(@This ItemStack thiz, PortBundleContents value) {
        value.applyTo(thiz);
    }

    public static PortPotionContents getPotionContents(@This ItemStack thiz) {
        return new PortPotionContents(thiz);
    }

    public static void setPotionContents(@This ItemStack thiz, PortPotionContents value) {
        value.applyTo(thiz);
    }

    public static PortSuspiciousStewEffects getSuspiciousStewEffects(@This ItemStack thiz) {
        return new PortSuspiciousStewEffects(thiz);
    }

    public static void setSuspiciousStewEffects(@This ItemStack thiz, PortSuspiciousStewEffects value) {
        value.applyTo(thiz);
    }

    public static @Nullable ArmorTrim getTrim(@This ItemStack thiz) {
        return thiz.get(DataComponents.TRIM);
    }

    public static void setTrim(@This ItemStack thiz, ArmorTrim value) {
        thiz.set(DataComponents.TRIM, value);
    }

    public static PortDebugStickState getDebugStickState(@This ItemStack thiz) {
        return new PortDebugStickState(thiz);
    }

    public static void setDebugStickState(@This ItemStack thiz, PortDebugStickState value) {
        value.applyTo(thiz);
    }

    public static @Nullable CompoundTag getEntityData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, DataComponents.ENTITY_DATA);
    }

    public static void setEntityData(@This ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, DataComponents.ENTITY_DATA);
    }

    public static @Nullable CompoundTag getBucketEntityData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, DataComponents.BUCKET_ENTITY_DATA);
    }

    public static void setBucketEntityData(@This ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, DataComponents.BUCKET_ENTITY_DATA);
    }

    public static @Nullable CompoundTag getBlockEntityData(@This ItemStack thiz, boolean copy) {
        CustomData data = thiz.get(DataComponents.BLOCK_ENTITY_DATA);
        if (data == null) return null;
        return copy ? data.copyTag() : data.getUnsafe();
    }

    public static void setBlockEntityData(@This ItemStack thiz, BlockEntityType<?> type, CompoundTag data) {
        BlockItem.setBlockEntityData(thiz, type, data);
    }

    public static @Nullable InstrumentHolder getInstrument(@This ItemStack thiz) {
        Holder<Instrument> value = thiz.get(DataComponents.INSTRUMENT);
        return value == null ? null : InstrumentHolder.wrap(value);
    }
    public static int getEnchantmentLevel(@This ItemStack thiz, EnchantmentHolder enchantment) {
        return thiz.getEnchantmentLevel(enchantment.delegate());
    }

    public static PortItemEnchantments getAllPortEnchantments(@This ItemStack thiz, HolderLookup.RegistryLookup<Enchantment> lookup) {
        return PortItemEnchantments.wrap(thiz.getAllEnchantments(lookup));
    }
}
