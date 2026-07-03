package PortLib.extensions.net.minecraft.world.item.ItemStack;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import PortLib.extensions.net.minecraft.world.entity.LivingEntity.PortLivingEntityExtension;
import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.event.enchanting.PortGetEnchantmentLevelEvent;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PortItemStackExtension {
    private static final Codec<Holder<Item>> ITEM_NON_AIR_CODEC = PortCodecExtension.validate(BuiltInRegistries.ITEM.holderByNameCodec(), holder -> PortHolderExtension.is(holder, Items.AIR.builtInRegistryHolder())
            ? DataResult.error(() -> "Item must not be minecraft:air")
            : DataResult.success(holder));
    private static final Codec<ItemStack> CODEC = new Codec<>() {
        @Override
        public <T> DataResult<Pair<ItemStack, T>> decode(DynamicOps<T> ops, T input) {
            Tag u = ops.convertTo(NbtOps.INSTANCE, input);
            if (u instanceof CompoundTag tag) {
                return DataResult.success(new Pair<>(ItemStack.of(tag), input), Lifecycle.stable());
            }
            return DataResult.error(() -> "Unable to decode item stack");
        }

        @Override
        public <T> DataResult<T> encode(ItemStack input, DynamicOps<T> ops, T prefix) {
            return DataResult.success(NbtOps.INSTANCE.convertTo(ops, input.save(new CompoundTag())), Lifecycle.stable());
        }
    };
    private static final Codec<ItemStack> SINGLE_ITEM_CODEC = CODEC.mapResult(new Codec.ResultFunction<>() {
        @Override
        public <T> DataResult<Pair<ItemStack, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<ItemStack, T>> a) {
            return a.map(pair -> {
                ItemStack stack = pair.getFirst();
                stack.setCount(1);
                return new Pair<>(stack, pair.getSecond());
            });
        }

        @Override
        public <T> DataResult<T> coApply(DynamicOps<T> ops, ItemStack input, DataResult<T> t) {
            return t.map(t1 -> ops.set(t1, "Count", ops.createInt(1)));
        }
    });
    private static final Codec<ItemStack> STRICT_CODEC = PortCodecExtension.validate(CODEC, PortItemStackExtension::validateStrict);
    private static final Codec<ItemStack> STRICT_SINGLE_ITEM_CODEC = PortCodecExtension.validate(SINGLE_ITEM_CODEC, PortItemStackExtension::validateStrict);
    private static final Codec<ItemStack> OPTIONAL_CODEC = PortCodecExtension.optionalEmptyMap(CODEC).xmap(optional -> optional.orElse(ItemStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    private static final Codec<ItemStack> SIMPLE_ITEM_CODEC = ITEM_NON_AIR_CODEC.xmap(ItemStack::new, ItemStack::getItemHolder);
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> OPTIONAL_STREAM_CODEC = new PortStreamCodec<>() {
        public ItemStack decode(PortRegistryFriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readAnySizeNbt();
            if (tag == null) return ItemStack.EMPTY;
            return ItemStack.of(tag);
        }

        public void encode(PortRegistryFriendlyByteBuf buffer, ItemStack value) {
            buffer.writeNbt(value.save(new CompoundTag()));
        }
    };
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> STREAM_CODEC = new PortStreamCodec<>() {
        public ItemStack decode(PortRegistryFriendlyByteBuf buffer) {
            ItemStack stack = OPTIONAL_STREAM_CODEC.decode(buffer);
            if (stack.isEmpty()) {
                throw new DecoderException("Empty ItemStack not allowed");
            }
            return stack;
        }

        public void encode(PortRegistryFriendlyByteBuf buffer, ItemStack value) {
            if (value.isEmpty()) {
                throw new EncoderException("Empty ItemStack not allowed");
            }
            OPTIONAL_STREAM_CODEC.encode(buffer, value);
        }
    };
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> OPTIONAL_LIST_STREAM_CODEC = OPTIONAL_STREAM_CODEC.apply(PortByteBufCodecs.collection(NonNullList::createWithCapacity));
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> LIST_STREAM_CODEC = STREAM_CODEC.apply(PortByteBufCodecs.collection(NonNullList::createWithCapacity));

    public static Codec<Holder<Item>> itemNonAirCodec() {
        return ITEM_NON_AIR_CODEC;
    }

    public static Codec<ItemStack> codec() {
        return CODEC;
    }

    public static Codec<ItemStack> singleItemCodec() {
        return SINGLE_ITEM_CODEC;
    }

    public static Codec<ItemStack> strictCodec() {
        return STRICT_CODEC;
    }

    public static Codec<ItemStack> strictSingleItemCodec() {
        return STRICT_SINGLE_ITEM_CODEC;
    }

    public static Codec<ItemStack> optionalCodec() {
        return OPTIONAL_CODEC;
    }

    public static Codec<ItemStack> simpleItemCodec() {
        return SIMPLE_ITEM_CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> optionalStreamCodec() {
        return OPTIONAL_STREAM_CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> streamCodec() {
        return STREAM_CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> optionalListStreamCodec() {
        return OPTIONAL_LIST_STREAM_CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> listStreamCodec() {
        return LIST_STREAM_CODEC;
    }

    private static DataResult<ItemStack> validateStrict(ItemStack stack) {
        if (stack.isDamageableItem() && stack.getMaxStackSize() > 1) {
            return DataResult.error(() -> "Item cannot be both damageable and stackable");
        }
        CompoundTag data = getBlockEntityData(stack, false);
        if (data != null && data.contains("Items", 9)) {
            ListTag items = data.getList("Items", 10);
            for (Tag item : items) {
                if (item instanceof CompoundTag tag) {
                    ItemStack itemStack = ItemStack.of(tag);
                    int i = itemStack.getCount();
                    int j = itemStack.getMaxStackSize();
                    if (i > j) {
                        return DataResult.error(() -> "Item stack with count of " + i + " was larger than maximum: " + j);
                    }
                }
            }
        }
        return stack.getCount() > stack.getMaxStackSize()
                ? DataResult.error(() -> "Item stack with stack size of " + stack.getCount() + " was larger than maximum: " + stack.getMaxStackSize())
                : DataResult.success(stack);
    }

    private static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy, String key) {
        CompoundTag data = orDefault ? stack.getOrCreateTagElement(key) : stack.getTagElement(key);
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    private static void setCustomData(ItemStack stack, CompoundTag data, String key) {
        stack.getOrCreateTag().put(key, data.copy());
    }

    public static @Nullable CompoundTag getCustomData(ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, "portlib:custom_data");
    }

    public static void setCustomData(ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, "portlib:custom_data");
    }

    public static boolean getUnbreakable(ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("Unbreakable", Tag.TAG_BYTE)) {
            return tag.getBoolean("Unbreakable");
        }
        return false;
    }

    public static void setCustomName(ItemStack thiz, @Nullable Component name) {
        thiz.setHoverName(name);
    }

    public static @Nullable Component getCustomName(ItemStack thiz) {
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

    public static Component getItemName(ItemStack thiz) {
        return thiz.getItem().getName(thiz);
    }

    public static List<Component> getLore(ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.getTagType("Lore") == Tag.TAG_LIST) {
                ListTag lore = display.getList("Lore", Tag.TAG_STRING);
                ImmutableList.Builder<Component> builder = ImmutableList.builder();
                for (int i = 0; i < lore.size(); ++i) {
                    String s = lore.getString(i);
                    try {
                        MutableComponent component = Component.Serializer.fromJson(s);
                        if (component != null) {
                            builder.add(ComponentUtils.mergeStyles(component, ItemStack.LORE_STYLE));
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

    public static @Nullable PortItemEnchantments getPortEnchantments(ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return null;
        }
        return new PortItemEnchantments(thiz.getEnchantmentTags(), getShowEnchantmentsTooltip(thiz));
    }

    public static PortItemEnchantments getPortEnchantmentsOrDefault(ItemStack thiz, PortItemEnchantments enchantments) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return enchantments;
        }
        return new PortItemEnchantments(thiz.getEnchantmentTags(), getShowEnchantmentsTooltip(thiz));
    }

    public static boolean getCanPlaceOn(ItemStack thiz, BlockInWorld block) {
        return thiz.hasAdventureModePlaceTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    public static boolean getCanBreak(ItemStack thiz, BlockInWorld block) {
        return thiz.hasAdventureModeBreakTagForBlock(PortEnvironment.registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    // todo
    public static int getCustomModelData(ItemStack thiz) {
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

    public static boolean getShowEnchantmentsTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.ENCHANTMENTS);
    }

    private static final int HIDE_STORED_ENCHANTMENTS_MASK = 1 << ItemStack.TooltipPart.values().length;

    public static void setShowEnchantmentsTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.ENCHANTMENTS, show);
    }

    public static boolean getShowStoredEnchantmentsTooltip(ItemStack thiz) {
        return (thiz.getHideFlags() & HIDE_STORED_ENCHANTMENTS_MASK) == 0;
    }

    public static void setShowStoredEnchantmentsTooltip(ItemStack thiz, boolean show) {
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

    public static boolean getShowAttributeModifiersTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.MODIFIERS);
    }

    public static void setShowAttributeModifiersTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.MODIFIERS, show);
    }

    public static PortItemAttributeModifiers getPortAttributeModifiers(ItemStack thiz) {
        ListTag listTag = null;
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("AttributeModifiers", Tag.TAG_LIST)) {
            listTag = tag.getList("AttributeModifiers", Tag.TAG_COMPOUND);
        }
        if (listTag == null || listTag.isEmpty()) {
            return PortItemExtension.getDefaultPortAttributeModifiers(thiz.getItem(), thiz);
        }
        return new PortItemAttributeModifiers(listTag, getShowAttributeModifiersTooltip(thiz));
    }

    public static void setPortAttributeModifiers(ItemStack thiz, PortItemAttributeModifiers value) {
        value.applyTo(thiz);
    }

    public static boolean getShowUnbreakableTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.UNBREAKABLE);
    }

    public static void setShowUnbreakableTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.UNBREAKABLE, show);
    }

    public static boolean getShowCanBreakTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_DESTROY);
    }

    public static void setShowCanBreakTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_DESTROY, show);
    }

    public static boolean getShowCanPlaceOnTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_PLACE);
    }

    public static void setShowCanPlaceOnTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.CAN_PLACE, show);
    }

    public static boolean getShowAdditionalTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.ADDITIONAL);
    }

    public static void setShowAdditionalTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.ADDITIONAL, show);
    }

    public static boolean getShowDyeTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.DYE);
    }

    public static void setShowDyeTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.DYE, show);
    }

    public static boolean getShowTrimTooltip(ItemStack thiz) {
        return getShowTooltipPart(thiz, ItemStack.TooltipPart.UPGRADES);
    }

    public static void setShowTrimTooltip(ItemStack thiz, boolean show) {
        setShowTooltipPart(thiz, ItemStack.TooltipPart.UPGRADES, show);
    }

    private static final int HIDE_ALL_MASK = HIDE_STORED_ENCHANTMENTS_MASK << 1;

    public static boolean getShowTooltip(ItemStack thiz) {
        return (thiz.getHideFlags() & HIDE_ALL_MASK) == 0;
    }

    public static void setShowTooltip(ItemStack thiz, boolean show) {
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

    public static int getRepaireCost(ItemStack thiz) {
        return thiz.getBaseRepairCost();
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

    public static boolean getCreativeSlotLock(ItemStack thiz) {
        return getUnit(thiz, "CustomCreativeLock");
    }

    public static void setCreativeSlotLock(ItemStack thiz, boolean lock) {
        setUnit(thiz, "CustomCreativeLock", lock);
    }

    public static boolean getEnchantmentGlintOverride(ItemStack thiz) {
        return getUnit(thiz, "portlib:enchantment_glint_override");
    }

    public static void setEnchantmentGlintOverride(ItemStack thiz, boolean override) {
        setUnit(thiz, "portlib:enchantment_glint_override", override);
    }

    // todo AbstractArrow类
    public static boolean getIntangibleProjectile(ItemStack thiz) {
        return getUnit(thiz, "portlib:intangible_projectile");
    }

    public static void setIntangibleProjectile(ItemStack thiz, boolean intangible) {
        setUnit(thiz, "portlib:intangible_projectile", intangible);
    }

    public static @Nullable FoodProperties getFood(ItemStack thiz, @Nullable LivingEntity living) {
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

    public static void setFood(ItemStack thiz, @Nullable FoodProperties food) {
        IPortItemStack.of(thiz).portlib$setFood(food, true);
    }

    public static boolean getFireResistant(ItemStack thiz) {
        return getUnit(thiz, "portlib:fire_resistant");
    }

    public static void setFireResistant(ItemStack thiz, boolean resistant) {
        setUnit(thiz, "portlib:fire_resistant", resistant);
    }

    // todo 功能
    public static @Nullable PortTool getTool(ItemStack thiz) {
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

    public static void setTool(ItemStack thiz, @Nullable PortTool tool) {
        IPortItemStack.of(thiz).portlib$setTool(tool, true);
    }

    public static @Nullable PortItemEnchantments getStoredEnchantments(ItemStack thiz) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(thiz)) {
            return new PortItemEnchantments(EnchantedBookItem.getEnchantments(thiz), getShowStoredEnchantmentsTooltip(thiz));
        }
        return null;
    }

    public static void setStoredEnchantments(ItemStack thiz, PortItemEnchantments value) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, Tag.TAG_LIST)) {
            thiz.getOrCreateTag().put(EnchantedBookItem.TAG_STORED_ENCHANTMENTS, value.getListTag().copy());
        }
        setShowStoredEnchantmentsTooltip(thiz, value.showInTooltip);
    }

    /// rgb
    public static int getDyedColor(ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("color", Tag.TAG_ANY_NUMERIC)) {
                return display.getInt("color");
            }
        }
        return -1;
    }

    public static void setDyedColor(ItemStack thiz, int rgb) {
        thiz.getOrCreateTagElement("display").putInt("color", rgb);
    }

    /// argb
    public static int getMapColor(ItemStack thiz) {
        return MapItem.getColor(thiz);
    }

    public static void setMapColor(ItemStack thiz, int argb) {
        thiz.getOrCreateTagElement("display").putInt("MapColor", argb);
    }

    public static @Nullable Integer getMapId(ItemStack thiz) {
        return MapItem.getMapId(thiz);
    }

    public static void setMapId(ItemStack thiz, int mapId) {
        thiz.getOrCreateTag().putInt("map", mapId);
    }

    // todo getMapDecortions MapItemSavedData#L201

    public static @Nullable PortMapPostProcessing getMapPostProcessing(ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag == null) return null;
        if (tag.contains(MapItem.MAP_SCALE_TAG, Tag.TAG_ANY_NUMERIC)) {
            return PortMapPostProcessing.SCALE;
        } else if (tag.contains(MapItem.MAP_LOCK_TAG, Tag.TAG_BYTE) && tag.getBoolean(MapItem.MAP_LOCK_TAG)) {
            return PortMapPostProcessing.LOCK;
        }
        return null;
    }

    public static void setMapPostProcessing(ItemStack thiz, PortMapPostProcessing value) {
        CompoundTag tag = thiz.getOrCreateTag();
        switch (value) {
            case LOCK -> tag.putBoolean(MapItem.MAP_LOCK_TAG, true);
            case SCALE -> tag.putInt(MapItem.MAP_SCALE_TAG, 1);
        }
    }

    public static PortChargedProjectiles getChargedProjectiles(ItemStack thiz) {
        return new PortChargedProjectiles(thiz);
    }

    public static void setChargedProjectiles(ItemStack thiz, PortChargedProjectiles value) {
        value.applyTo(thiz);
    }

    public static PortBundleContents getBundleContents(ItemStack thiz) {
        return new PortBundleContents(thiz);
    }

    public static void setBundleContents(ItemStack thiz, PortBundleContents value) {
        value.applyTo(thiz);
    }

    public static PortPotionContents getPotionContents(ItemStack thiz) {
        return new PortPotionContents(thiz);
    }

    public static void setPotionContents(ItemStack thiz, PortPotionContents value) {
        value.applyTo(thiz);
    }

    public static PortSuspiciousStewEffects getSuspiciousStewEffects(ItemStack thiz) {
        return new PortSuspiciousStewEffects(thiz);
    }

    public static void setSuspiciousStewEffects(ItemStack thiz, PortSuspiciousStewEffects value) {
        value.applyTo(thiz);
    }

    // todo getWritableBookContent
    // todo getWrittenBookContent

    public static @Nullable ArmorTrim getTrim(ItemStack thiz) {
        return ArmorTrim.getTrim(PortEnvironment.registryAccess(), thiz).orElse(null);
    }

    public static void setTrim(ItemStack thiz, ArmorTrim value) {
        ArmorTrim.setTrim(PortEnvironment.registryAccess(), thiz, value);
    }

    public static PortDebugStickState getDebugStickState(ItemStack thiz) {
        return new PortDebugStickState(thiz);
    }

    public static void setDebugStickState(ItemStack thiz, PortDebugStickState value) {
        value.applyTo(thiz);
    }

    public static @Nullable CompoundTag getEntityData(ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, "EntityTag");
    }

    public static void setEntityData(ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, "EntityTag");
    }

    public static @Nullable CompoundTag getBucketEntityData(ItemStack thiz, boolean orDefault, boolean copy) {
        CompoundTag data = orDefault ? thiz.getOrCreateTag() : thiz.getTag();
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    public static void setBucketEntityData(ItemStack thiz, CompoundTag data) {
        thiz.setTag(data.copy());
    }

    public static @Nullable CompoundTag getBlockEntityData(ItemStack thiz, boolean copy) {
        CompoundTag data = BlockItem.getBlockEntityData(thiz);
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    public static void setBlockEntityData(ItemStack thiz, BlockEntityType<?> type, CompoundTag data) {
        BlockItem.setBlockEntityData(thiz, type, data);
    }

    public static @Nullable Holder<Instrument> getInstrument(ItemStack thiz) {
        CompoundTag tag = thiz.getTag();
        if (tag != null && tag.contains("instrument", 8)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("instrument"));
            if (id != null) {
                return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, id)).orElse(null);
            }
        }
        return null;
    }

    public static int getEnchantmentLevel(ItemStack thiz, EnchantmentHolder enchantment) {
        int level = thiz.getEnchantmentLevel(enchantment.value());
        return PortGetEnchantmentLevelEvent.getEnchantmentLevelSpecific(level, thiz, enchantment);
    }

    public static PortItemEnchantments getAllPortEnchantments(ItemStack thiz, HolderLookup.RegistryLookup<Enchantment> lookup) {
        var enchantments = getPortEnchantments(thiz);
        return PortGetEnchantmentLevelEvent.getAllEnchantmentLevels(enchantments, thiz, lookup);
    }

    // region DataComponentHolder

    public static <T> @Nullable T getData(ItemStack thiz, PortDataComponentType<T> type) {
        return IPortItemStack.of(thiz).portlib$patch().get(type);
    }

    public static <T> @Nullable T getData(ItemStack thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return getData(thiz, type.get());
    }

    public static <T> @Nullable T setData(ItemStack thiz, PortDataComponentType<T> type, T value) {
        IPortItemStack i = IPortItemStack.of(thiz);
        T t = i.portlib$patch().set(type, value);
        i.updateTag();
        return t;
    }

    public static <T> @Nullable T setData(ItemStack thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
        return setData(thiz, type.get(), value);
    }

    public static <T> @Nullable T removeData(ItemStack thiz, PortDataComponentType<T> type) {
        IPortItemStack i = IPortItemStack.of(thiz);
        T t = i.portlib$patch().remove(type);
        i.updateTag();
        return t;
    }

    public static <T> @Nullable T removeData(ItemStack thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return removeData(thiz, type.get());
    }

    public static <T> T getDataOrDefault(ItemStack thiz, PortDataComponentType<? extends T> type, T defaultValue) {
        return IPortItemStack.of(thiz).portlib$patch().getOrDefault(type, defaultValue);
    }

    public static <T> T getDataOrDefault(ItemStack thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T defaultValue) {
        return getDataOrDefault(thiz, type.get(), defaultValue);
    }

    public static <T> boolean hasData(ItemStack thiz, PortDataComponentType<T> type) {
        return IPortItemStack.of(thiz).portlib$patch().has(type);
    }

    public static <T> boolean hasData(ItemStack thiz, PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return hasData(thiz, type.get());
    }

    public static PortDataComponentMap getPrototypeData(ItemStack thiz) {
        return IPortItemStack.of(thiz).portlib$patch().getPrototype();
    }

    @Diff
    public static boolean is(ItemStack thiz, RegistryObject<? extends Item> holder) {
        return thiz.is(holder.get());
    }

    // endregion DataComponentHolder

    public static void hurtAndBreak(ItemStack thiz, int amount, LivingEntity entity, EquipmentSlot slot) {
        thiz.hurtAndBreak(amount, entity, living -> living.level().broadcastEntityEvent(living, (byte) switch (slot) {
            case MAINHAND -> 47;
            case OFFHAND -> 48;
            case HEAD -> 49;
            case FEET -> 52;
            case LEGS -> 51;
            default -> 50;
        }));
    }

    // true static
    public static int hashItemAndComponents(@Nullable ItemStack stack) {
        if (stack != null) {
            int i = 31 + stack.getItem().hashCode();
            return 31 * i + Objects.hashCode(stack.getTag());
        }
        return 0;
    }

    public static int hashStackList(List<ItemStack> list) {
        int i = 0;
        for (ItemStack itemstack : list) {
            i = i * 31 + hashItemAndComponents(itemstack);
        }
        return i;
    }

    public static boolean listMatches(List<ItemStack> list, List<ItemStack> other) {
        if (list.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!ItemStack.matches(list.get(i), other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSameItemSameComponents(ItemStack stack, ItemStack other) {
        return ItemStack.isSameItemSameTags(stack, other) && Objects.equals(IPortItemStack.of(stack).portlib$patch(), IPortItemStack.of(other).portlib$patch());
    }

    public static ItemStack transmuteCopy(ItemStack thiz, ItemLike item) {
        return null;
    }

    public static ItemStack transmuteCopy(ItemStack thiz, ItemLike item, int count) {
        return thiz.isEmpty() ? ItemStack.EMPTY : transmuteCopyIgnoreEmpty(thiz, item, count);
    }

    private static ItemStack transmuteCopyIgnoreEmpty(ItemStack thiz, ItemLike item, int count) {
        return new ItemStack(item, count, thiz.getTag());
    }

    public static void consume(ItemStack thiz, int amount, @Nullable LivingEntity living) {
        if (living == null || !PortLivingEntityExtension.hasInfiniteMaterials(living)) {
            thiz.shrink(amount);
        }
    }

    public static void limitSize(ItemStack thiz, int maxSize) {
        if (!thiz.isEmpty() && thiz.getCount() > maxSize) {
            thiz.setCount(maxSize);
        }
    }
}
