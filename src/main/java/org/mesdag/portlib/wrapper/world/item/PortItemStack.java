package org.mesdag.portlib.wrapper.world.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;

@SuppressWarnings("all")
public class PortItemStack {
    public static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy) {
        if (orDefault) {
            CompoundTag data = stack.getOrCreateTagElement("portlib:custom_data");
            return copy ? data.copy() : data;
        }
        CompoundTag data = stack.getTagElement("portlib:custom_data");
        return data == null ? null : copy ? data.copy() : data;
    }

    public static void setCustomData(ItemStack stack, CompoundTag tag) {
        stack.getOrCreateTag().put("portlib:custom_data", tag.copy());
    }

    public static boolean getUnbreakable(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean("Unbreakable");
    }

    public static @Nullable Component getCustomName(ItemStack stack) {
        CompoundTag display = stack.getTagElement("display");
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

    public static Component getItemName(ItemStack stack) {
        return stack.getItem().getName(stack);
    }

    public static List<Component> getLore(ItemStack stack) {
        CompoundTag tag = stack.getTag();
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

    public static @Nullable PortItemEnchantments getEnchantments(ItemStack stack) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(stack)) {
            return null;
        }
        return new PortItemEnchantments(stack);
    }

    public static boolean getCanPlaceOn(ItemStack stack, BlockInWorld block) {
        return stack.hasAdventureModePlaceTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    public static boolean getCanBreak(ItemStack stack, BlockInWorld block) {
        return stack.hasAdventureModeBreakTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    public static int getCustomModelData(ItemStack stack) {
        CompoundTag tag = stack.getTag();
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

    public static boolean getShowEnchantmentsTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.ENCHANTMENTS);
    }

    private static final int HIDE_STORED_ENCHANTMENTS_MASK = 1 << ItemStack.TooltipPart.values().length;

    public static void setShowEnchantmentsTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.ENCHANTMENTS, show);
    }

    public static boolean getShowStoredEnchantmentsTooltip(ItemStack stack) {
        return (stack.getHideFlags() & HIDE_STORED_ENCHANTMENTS_MASK) == 0;
    }

    public static void setShowStoredEnchantmentsTooltip(ItemStack stack, boolean show) {
        if (show) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_STORED_ENCHANTMENTS_MASK);
            }
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_STORED_ENCHANTMENTS_MASK);
        }
    }

    public static boolean getShowAttributeModifiersTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.MODIFIERS);
    }

    public static void setShowAttributeModifiersTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.MODIFIERS, show);
    }

    public static boolean getShowUnbreakableTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.UNBREAKABLE);
    }

    public static void setShowUnbreakableTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.UNBREAKABLE, show);
    }

    public static boolean getShowCanBreakTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.CAN_DESTROY);
    }

    public static void setShowCanBreakTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.CAN_DESTROY, show);
    }

    public static boolean getShowCanPlaceOnTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.CAN_PLACE);
    }

    public static void setShowCanPlaceOnTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.CAN_PLACE, show);
    }

    public static boolean getShowAdditionalTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.ADDITIONAL);
    }

    public static void setShowAdditionalTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.ADDITIONAL, show);
    }

    public static boolean getShowDyeTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.DYE);
    }

    public static void setShowDyeTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.DYE, show);
    }

    public static boolean getShowTrimTooltip(ItemStack stack) {
        return getShowTooltipPart(stack, ItemStack.TooltipPart.UPGRADES);
    }

    public static void setShowTrimTooltip(ItemStack stack, boolean show) {
        setShowTooltipPart(stack, ItemStack.TooltipPart.UPGRADES, show);
    }

    private static final int HIDE_ALL_MASK = HIDE_STORED_ENCHANTMENTS_MASK << 1;

    public static boolean getShowTooltip(ItemStack stack) {
        return (stack.getHideFlags() & HIDE_ALL_MASK) == 0;
    }

    public static void setShowTooltip(ItemStack stack, boolean show) {
        if (show) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_ALL_MASK);
            }
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_ALL_MASK);
        }
    }

    public static int getRepaireCost(ItemStack stack) {
        return stack.getBaseRepairCost();
    }

    public static void setRepairCost(ItemStack stack, int cost) {
        stack.setRepairCost(cost);
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

    public static boolean getCreativeSlotLock(ItemStack stack) {
        return getUnit(stack, "CustomCreativeLock");
    }

    public static void setCreativeSlotLock(ItemStack stack, boolean lock) {
        setUnit(stack, "CustomCreativeLock", lock);
    }

    public static boolean getEnchantmentGlintOverride(ItemStack stack) {
        return getUnit(stack, "portlib:enchantment_glint_override");
    }

    public static void setEnchantmentGlintOverride(ItemStack stack, boolean override) {
        setUnit(stack, "portlib:enchantment_glint_override", override);
    }

    // todo AbstractArrow类
    public static boolean getIntangibleProjectile(ItemStack stack) {
        return getUnit(stack, "portlib:intangible_projectile");
    }

    public static void setIntangibleProjectile(ItemStack stack, boolean intangible) {
        setUnit(stack, "portlib:intangible_projectile", intangible);
    }

    public static @Nullable FoodProperties getFood(ItemStack stack, @Nullable LivingEntity living) {
        IPortItemStack iStack = IPortItemStack.of(stack);
        FoodProperties food = iStack.portlib$getFood(living);
        if (food == null) {
            CompoundTag data = stack.getTagElement(PortFoodProperties.KEY);
            if (data == null) {
                food = stack.getFoodProperties(living);
            } else {
                food = PortFoodProperties.load(data);
            }
            iStack.portlib$setFood(food, false);
        }
        return food;
    }

    public static void setFood(ItemStack stack, @Nullable FoodProperties food) {
        IPortItemStack.of(stack).portlib$setFood(food, true);
    }

    public static boolean getFireResistant(ItemStack stack) {
        return getUnit(stack, "portlib:fire_resistant");
    }

    public static void setFireResistant(ItemStack stack, boolean resistant) {
        setUnit(stack, "portlib:fire_resistant", resistant);
    }

    // todo 功能
    public static @Nullable PortTool getTool(ItemStack stack) {
        IPortItemStack iStack = IPortItemStack.of(stack);
        PortTool tool = iStack.portlib$getTool();
        if (tool == null) {
            CompoundTag data = stack.getTagElement(PortFoodProperties.KEY);
            if (data != null) {
                tool = PortTool.load(data);
                iStack.portlib$setTool(tool, false);
            }
        }
        return tool;
    }

    public static void setTool(ItemStack stack, @Nullable PortTool tool) {
        IPortItemStack.of(stack).portlib$setTool(tool, true);
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
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("color", Tag.TAG_ANY_NUMERIC)) {
                return display.getInt("color");
            }
        }
        return -1;
    }

    public static void setDyedColor(ItemStack stack, int rgb) {
        stack.getOrCreateTagElement("display").putInt("color", rgb);
    }

    /// argb
    public static int getMapColor(ItemStack stack) {
        return MapItem.getColor(stack);
    }

    public static void setMapColor(ItemStack stack, int argb) {
        stack.getOrCreateTagElement("display").putInt("MapColor", argb);
    }

    public static @Nullable Integer getMapId(ItemStack stack) {
        return MapItem.getMapId(stack);
    }

    public static void setMapId(ItemStack stack, int mapId) {
        stack.getOrCreateTag().putInt("map", mapId);
    }

    // todo getMapDecortions MapItemSavedData#L201

    public static @Nullable PortMapPostProcessing getMapPostProcessing(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return null;
        if (tag.contains(MapItem.MAP_SCALE_TAG, Tag.TAG_ANY_NUMERIC)) {
            return PortMapPostProcessing.SCALE;
        } else if (tag.contains(MapItem.MAP_LOCK_TAG, Tag.TAG_BYTE) && tag.getBoolean(MapItem.MAP_LOCK_TAG)) {
            return PortMapPostProcessing.LOCK;
        }
        return null;
    }

    public static void setMapPostProcessing(ItemStack stack, PortMapPostProcessing value) {
        CompoundTag tag = stack.getOrCreateTag();
        switch (value) {
            case LOCK -> tag.putBoolean(MapItem.MAP_LOCK_TAG, true);
            case SCALE -> tag.putInt(MapItem.MAP_SCALE_TAG, 1);
        }
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
