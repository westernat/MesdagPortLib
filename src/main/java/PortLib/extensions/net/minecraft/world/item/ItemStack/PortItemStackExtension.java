package PortLib.extensions.net.minecraft.world.item.ItemStack;

import com.google.common.collect.ImmutableList;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.event.enchanting.PortGetEnchantmentLevelEvent;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;
import java.util.function.Supplier;

@Extension
public class PortItemStackExtension {
    private static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy, String key) {
        CompoundTag data = orDefault ? stack.getOrCreateTagElement(key) : stack.getTagElement(key);
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    private static void setCustomData(ItemStack stack, CompoundTag data, String key) {
        stack.getOrCreateTag().put(key, data.copy());
    }

    public static @Nullable CompoundTag getCustomData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, "portlib:custom_data");
    }

    public static void setCustomData(@This ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, "portlib:custom_data");
    }

    public static boolean getUnbreakable(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        return tag != null && tag.getBoolean("Unbreakable");
    }

    public static @Nullable Component getCustomName(@This ItemStack thiz) {
        CompoundTag display = thiz.getTagElement("display");
        if (display != null && display.contains("Name", Tag.TAG_STRING)) {
            try {
                Component name = Component.Serializer.fromJson(display.getString("Name"));
                if (name != null) {
                    return name;
                }
                display.remove("Name");
            } catch (Exception exception) {
                display.remove("Name");
            }
        }
        return null;
    }

    public static Component getItemName(@This ItemStack thiz) {
        return thiz.getItem().getName(thiz);
    }

    public static List<Component> getLore(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.getTagType("Lore") == Tag.TAG_LIST) {
                ListTag lore = display.getList("Lore", Tag.TAG_STRING);
                ImmutableList.Builder<Component> builder = ImmutableList.builder();
                for (int i = 0; i < lore.size(); ++i) {
                    String s = lore.getString(i);
                    try {
                        MutableComponent mutablecomponent1 = Component.Serializer.fromJson(s);
                        if (mutablecomponent1 != null) {
                            builder.add(ComponentUtils.mergeStyles(mutablecomponent1, ItemStack.LORE_STYLE));
                        }
                    } catch (Exception exception) {
                        display.remove("Lore");
                    }
                }
                return builder.build();
            }
        }
        return List.of();
    }

    public static @Nullable PortItemEnchantments getEnchantments(@This ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return null;
        }
        return new PortItemEnchantments(thiz.getEnchantmentTags(), getShowEnchantmentsTooltip(thiz));
    }

    public static boolean getCanPlaceOn(@This ItemStack thiz, BlockInWorld block) {
        return thiz.hasAdventureModePlaceTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    public static boolean getCanBreak(@This ItemStack thiz, BlockInWorld block) {
        return thiz.hasAdventureModeBreakTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    public static int getCustomModelData(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        return tag == null ? 0 : tag.getInt("portlib:custom_model_data");
    }

    private static boolean getShowTooltipPart(ItemStack stack, ItemStack.TooltipPart part) {
        return ItemStack.shouldShowInTooltip(stack.getHideFlags(), part);
    }

    private static void setShowTooltipPart(ItemStack stack, ItemStack.TooltipPart part, boolean show) {
        if (show) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~part.getMask());
            }
        } else {
            stack.hideTooltipPart(part);
        }
    }

    public static boolean getShowEnchantmentsTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.ENCHANTMENTS);
    }

    private static final int HIDE_STORED_ENCHANTMENTS_MASK = 1 << ItemStack.TooltipPart.values().length;

    public static void setShowEnchantmentsTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.ENCHANTMENTS, show);
    }

    public static boolean getShowStoredEnchantmentsTooltip(@This ItemStack thiz) {
        return (thiz.getHideFlags() & HIDE_STORED_ENCHANTMENTS_MASK) == 0;
    }

    public static void setShowStoredEnchantmentsTooltip(@This ItemStack thiz, boolean show) {
        if (show) {
            CompoundTag tag = thiz.getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_STORED_ENCHANTMENTS_MASK);
            }
        } else {
            CompoundTag tag = thiz.getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_STORED_ENCHANTMENTS_MASK);
        }
    }

    public static boolean getShowAttributeModifiersTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.MODIFIERS);
    }

    public static void setShowAttributeModifiersTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.MODIFIERS, show);
    }

    public static boolean getShowUnbreakableTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.UNBREAKABLE);
    }

    public static void setShowUnbreakableTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.UNBREAKABLE, show);
    }

    public static boolean getShowCanBreakTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_DESTROY);
    }

    public static void setShowCanBreakTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_DESTROY, show);
    }

    public static boolean getShowCanPlaceOnTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_PLACE);
    }

    public static void setShowCanPlaceOnTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_PLACE, show);
    }

    public static boolean getShowAdditionalTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.ADDITIONAL);
    }

    public static void setShowAdditionalTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.ADDITIONAL, show);
    }

    public static boolean getShowDyeTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.DYE);
    }

    public static void setShowDyeTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.DYE, show);
    }

    public static boolean getShowTrimTooltip(@This ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.UPGRADES);
    }

    public static void setShowTrimTooltip(@This ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.UPGRADES, show);
    }

    private static final int HIDE_ALL_MASK = HIDE_STORED_ENCHANTMENTS_MASK << 1;

    public static boolean getShowTooltip(@This ItemStack thiz) {
        return (thiz.getHideFlags() & HIDE_ALL_MASK) == 0;
    }

    public static void setShowTooltip(@This ItemStack thiz, boolean show) {
        if (show) {
            CompoundTag tag = thiz.getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_ALL_MASK);
            }
        } else {
            CompoundTag tag = thiz.getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_ALL_MASK);
        }
    }

    public static int getRepaireCost(@This ItemStack thiz) {
        return thiz.getBaseRepairCost();
    }

    public static void setRepairCost(@This ItemStack thiz, int cost) {
        thiz.setRepairCost(cost);
    }

    private static boolean getUnit(ItemStack stack, String key) {
        return stack.getTagElement(key) != null;
    }

    private static void setUnit(ItemStack stack, String key, boolean has) {
        if (has) {
            stack.getOrCreateTagElement(key);
        } else {
            stack.removeTagKey(key);
        }
    }

    public static boolean getCreativeSlotLock(@This ItemStack thiz) {
        return getUnit(thiz, "CustomCreativeLock");
    }

    public static void setCreativeSlotLock(@This ItemStack thiz, boolean lock) {
        setUnit(thiz, "CustomCreativeLock", lock);
    }

    public static boolean getEnchantmentGlintOverride(@This ItemStack thiz) {
        return getUnit(thiz, "portlib:enchantment_glint_override");
    }

    public static void setEnchantmentGlintOverride(@This ItemStack thiz, boolean override) {
        setUnit(thiz, "portlib:enchantment_glint_override", override);
    }

    // todo AbstractArrow类
    public static boolean getIntangibleProjectile(@This ItemStack thiz) {
        return getUnit(thiz, "portlib:intangible_projectile");
    }

    public static void setIntangibleProjectile(@This ItemStack thiz, boolean intangible) {
        setUnit(thiz, "portlib:intangible_projectile", intangible);
    }

    public static @Nullable FoodProperties getFood(@This ItemStack thiz, @Nullable LivingEntity living) {
        IPortItemStack iStack = IPortItemStack.of(thiz);
        FoodProperties food = iStack.portlib$getFood(living);
        if (food == null) {
            CompoundTag data = thiz.getTagElement(PortFoodProperties.KEY);
            if (data == null) {
                food = thiz.getFoodProperties(living);
            } else {
                food = PortFoodProperties.load(data);
            }
            iStack.portlib$setFood(food, false);
        }
        return food;
    }

    public static void setFood(@This ItemStack thiz, @Nullable FoodProperties food) {
        IPortItemStack.of(thiz).portlib$setFood(food, true);
    }

    public static boolean getFireResistant(@This ItemStack thiz) {
        return getUnit(thiz, "portlib:fire_resistant");
    }

    public static void setFireResistant(@This ItemStack thiz, boolean resistant) {
        setUnit(thiz, "portlib:fire_resistant", resistant);
    }

    // todo 功能
    public static @Nullable PortTool getTool(@This ItemStack thiz) {
        IPortItemStack iStack = IPortItemStack.of(thiz);
        PortTool tool = iStack.portlib$getTool();
        if (tool == null) {
            CompoundTag data = thiz.getTagElement(PortFoodProperties.KEY);
            if (data != null) {
                tool = PortTool.load(data);
                iStack.portlib$setTool(tool, false);
            }
        }
        return tool;
    }

    public static void setTool(@This ItemStack thiz, @Nullable PortTool tool) {
        IPortItemStack.of(thiz).portlib$setTool(tool, true);
    }

    public static @Nullable PortItemEnchantments getStoredEnchantments(@This ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return new PortItemEnchantments(EnchantedBookItem.getEnchantments(thiz), getShowStoredEnchantmentsTooltip(thiz));
        }
        return null;
    }

    public static void setStoredEnchantments(@This ItemStack thiz, PortItemEnchantments value) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, Tag.TAG_LIST)) {
            thiz.getOrCreateTag().put(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, value.getListTag().copy());
        }
        setShowStoredEnchantmentsTooltip(thiz, value.showInTooltip);
    }

    /// rgb
    public static int getDyedColor(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("color", Tag.TAG_ANY_NUMERIC)) {
                return display.getInt("color");
            }
        }
        return -1;
    }

    public static void setDyedColor(@This ItemStack thiz, int rgb) {
        thiz.getOrCreateTagElement("display").putInt("color", rgb);
    }

    /// argb
    public static int getMapColor(@This ItemStack thiz) {
        return MapItem.getColor(thiz);
    }

    public static void setMapColor(@This ItemStack thiz, int argb) {
        thiz.getOrCreateTagElement("display").putInt("MapColor", argb);
    }

    public static @Nullable Integer getMapId(@This ItemStack thiz) {
        return MapItem.getMapId(thiz);
    }

    public static void setMapId(@This ItemStack thiz, int mapId) {
        thiz.getOrCreateTag().putInt("map", mapId);
    }

    // todo getMapDecortions MapItemSavedData#L201

    public static @Nullable PortMapPostProcessing getMapPostProcessing(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag == null) return null;
        if (tag.contains(MapItem.MAP_SCALE_TAG, Tag.TAG_ANY_NUMERIC)) {
            return PortMapPostProcessing.SCALE;
        } else if (tag.contains(MapItem.MAP_LOCK_TAG, Tag.TAG_BYTE) && tag.getBoolean(MapItem.MAP_LOCK_TAG)) {
            return PortMapPostProcessing.LOCK;
        }
        return null;
    }

    public static void setMapPostProcessing(@This ItemStack thiz, PortMapPostProcessing value) {
        CompoundTag tag = thiz.getOrCreateTag();
        switch (value) {
            case LOCK -> tag.putBoolean(MapItem.MAP_LOCK_TAG, true);
            case SCALE -> tag.putInt(MapItem.MAP_SCALE_TAG, 1);
        }
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

    // todo getWritableBookContent
    // todo getWrittenBookContent

    public static @Nullable ArmorTrim getTrim(@This ItemStack thiz) {
        return ArmorTrim.getTrim(PortEnvironment.registryAccess(), thiz).orElse(null);
    }

    public static void setTrim(@This ItemStack thiz, ArmorTrim value) {
        ArmorTrim.setTrim(PortEnvironment.registryAccess(), thiz, value);
    }

    public static PortDebugStickState getDebugStickState(@This ItemStack thiz) {
        return new PortDebugStickState(thiz);
    }

    public static void setDebugStickState(@This ItemStack thiz, PortDebugStickState value) {
        value.applyTo(thiz);
    }

    public static @Nullable CompoundTag getEntityData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, "EntityTag");
    }

    public static void setEntityData(@This ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, "EntityTag");
    }

    public static @Nullable CompoundTag getBucketEntityData(@This ItemStack thiz, boolean orDefault, boolean copy) {
        CompoundTag data = orDefault ? thiz.getOrCreateTag() : thiz.getTag();
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    public static void setBucketEntityData(@This ItemStack thiz, CompoundTag data) {
        thiz.setTag(data.copy());
    }

    public static @Nullable CompoundTag getBlockEntityData(@This ItemStack thiz, boolean copy) {
        CompoundTag data = BlockItem.getBlockEntityData(thiz);
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    public static void setBlockEntityData(@This ItemStack thiz, BlockEntityType<?> type, CompoundTag data) {
        BlockItem.setBlockEntityData(thiz, type, data);
    }

    public static @Nullable Holder<Instrument> getInstrument(@This ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("instrument", 8)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("instrument"));
            if (id != null) {
                return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, id)).orElse(null);
            }
        }
        return null;
    }

    public static int getEnchantmentLevel(@This ItemStack thiz, EnchantmentHolder enchantment) {
        int level = thiz.getEnchantmentLevel(enchantment.value());
        return PortGetEnchantmentLevelEvent.getEnchantmentLevelSpecific(level, thiz, enchantment);
    }

    public static PortItemEnchantments getAllPortEnchantments(@This ItemStack thiz, HolderLookup.RegistryLookup<Enchantment> lookup) {
        var enchantments = getEnchantments(thiz);
        return PortGetEnchantmentLevelEvent.getAllEnchantmentLevels(enchantments, thiz, lookup);
    }

    // region DataComponentHolder

    public static <T> @Nullable T getData(@This ItemStack thiz, PortDataComponentType<T> type) {
        return IPortItemStack.of(thiz).portlib$patch().get(type);
    }

    public static <T> @Nullable T getData(@This ItemStack thiz, Supplier<PortDataComponentType<T>> type) {
        return IPortItemStack.of(thiz).portlib$patch().get(type);
    }

    public static <T> @Nullable T setData(@This ItemStack thiz, PortDataComponentType<T> type, T value) {
        return IPortItemStack.of(thiz).portlib$patch().set(type, value);
    }

    public static <T> @Nullable T setData(@This ItemStack thiz, Supplier<PortDataComponentType<T>> type, T value) {
        return IPortItemStack.of(thiz).portlib$patch().set(type, value);
    }

    public static <T> @Nullable T removeData(@This ItemStack thiz, PortDataComponentType<T> type) {
        return IPortItemStack.of(thiz).portlib$patch().remove(type);
    }

    public static <T> @Nullable T removeData(@This ItemStack thiz, Supplier<PortDataComponentType<T>> type) {
        return IPortItemStack.of(thiz).portlib$patch().remove(type);
    }

    public static <T> T getDataOrDefault(@This ItemStack thiz, PortDataComponentType<? extends T> type, T defaultValue) {
        return IPortItemStack.of(thiz).portlib$patch().getOrDefault(type, defaultValue);
    }

    public static <T> T getDataOrDefault(@This ItemStack thiz, Supplier<PortDataComponentType<? extends T>> type, T defaultValue) {
        return IPortItemStack.of(thiz).portlib$patch().getOrDefault(type, defaultValue);
    }

    public static <T> boolean hasData(@This ItemStack thiz, PortDataComponentType<T> type) {
        return IPortItemStack.of(thiz).portlib$patch().has(type);
    }

    public static <T> boolean hasData(@This ItemStack thiz, Supplier<PortDataComponentType<T>> type) {
        return IPortItemStack.of(thiz).portlib$patch().has(type);
    }

    // endregion DataComponentHolder
}
