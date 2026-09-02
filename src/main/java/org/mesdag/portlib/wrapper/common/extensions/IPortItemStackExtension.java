package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
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
import net.minecraft.nbt.*;
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
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.event.enchanting.PortGetEnchantmentLevelEvent;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.util.Private;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.world.food.PortFoodProperties;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("all")
public interface IPortItemStackExtension {
    Codec<Holder<Item>> ITEM_NON_AIR_CODEC = PortCodecExtension.validate(BuiltInRegistries.ITEM.holderByNameCodec(), holder -> IPortHolderExtension.of(holder).is(Items.AIR.builtInRegistryHolder())
            ? DataResult.error(() -> "Item must not be minecraft:air")
            : DataResult.success(holder));
    Codec<ItemStack> CODEC = new Codec<>() {
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
    Codec<ItemStack> SINGLE_ITEM_CODEC = CODEC.mapResult(new Codec.ResultFunction<>() {
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
    Codec<ItemStack> STRICT_CODEC = PortCodecExtension.validate(CODEC, IPortItemStackExtension::validateStrict);
    Codec<ItemStack> STRICT_SINGLE_ITEM_CODEC = PortCodecExtension.validate(SINGLE_ITEM_CODEC, IPortItemStackExtension::validateStrict);
    Codec<ItemStack> OPTIONAL_CODEC = PortCodecExtension.optionalEmptyMap(CODEC).xmap(optional -> optional.orElse(ItemStack.EMPTY), stack -> stack.isEmpty() ? Optional.empty() : Optional.of(stack));
    Codec<ItemStack> SIMPLE_ITEM_CODEC = ITEM_NON_AIR_CODEC.xmap(ItemStack::new, ItemStack::getItemHolder);
    PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> OPTIONAL_STREAM_CODEC = new PortStreamCodec<>() {
        public ItemStack decode(PortRegistryFriendlyByteBuf buffer) {
            CompoundTag tag = buffer.readAnySizeNbt();
            if (tag == null) return ItemStack.EMPTY;
            return ItemStack.of(tag);
        }

        public void encode(PortRegistryFriendlyByteBuf buffer, ItemStack value) {
            buffer.writeNbt(value.save(new CompoundTag()));
        }
    };
    PortStreamCodec<PortRegistryFriendlyByteBuf, ItemStack> STREAM_CODEC = new PortStreamCodec<>() {
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
    PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> OPTIONAL_LIST_STREAM_CODEC = OPTIONAL_STREAM_CODEC.apply(PortByteBufCodecs.collection(NonNullList::createWithCapacity));
    PortStreamCodec<PortRegistryFriendlyByteBuf, List<ItemStack>> LIST_STREAM_CODEC = STREAM_CODEC.apply(PortByteBufCodecs.collection(NonNullList::createWithCapacity));

    private static DataResult<ItemStack> validateStrict(ItemStack stack) {
        if (stack.isDamageableItem() && stack.getMaxStackSize() > 1) {
            return DataResult.error(() -> "Item cannot be both damageable and stackable");
        }
        CompoundTag data = IPortItemStackExtension.of(stack).getBlockEntityData(false);
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

    static boolean isSameItemSameComponents(ItemStack stack, ItemStack other) {
        return ItemStack.isSameItemSameTags(stack, other) && Objects.equals(IPortItemStack.of(stack).portlib$patch(), IPortItemStack.of(other).portlib$patch());
    }

    static int hashStackList(List<ItemStack> list) {
        int i = 0;
        for (ItemStack itemstack : list) {
            i = i * 31 + hashItemAndComponents(itemstack);
        }
        return i;
    }

    static boolean listMatches(List<ItemStack> list, List<ItemStack> other) {
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

    static int hashItemAndComponents(@Nullable ItemStack stack) {
        if (stack != null) {
            int i = 31 + stack.getItem().hashCode();
            return 31 * i + Objects.hashCode(stack.getTag());
        }
        return 0;
    }

    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    private static @Nullable CompoundTag getCustomData(ItemStack stack, boolean orDefault, boolean copy, String key) {
        CompoundTag data = orDefault ? stack.getOrCreateTagElement(key) : stack.getTagElement(key);
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    private static void setCustomData(ItemStack stack, CompoundTag data, String key) {
        stack.getOrCreateTag().put(key, data.copy());
    }

    default @Nullable CompoundTag getCustomData(ItemStack thiz, boolean orDefault, boolean copy) {
        return getCustomData(thiz, orDefault, copy, "portlib:custom_data");
    }

    default void setCustomData(ItemStack thiz, CompoundTag data) {
        setCustomData(thiz, data, "portlib:custom_data");
    }

    default @Nullable CompoundTag getCustomData(boolean orDefault, boolean copy) {
        return getCustomData(self(), orDefault, copy);
    }

    default void setCustomData(CompoundTag data) {
        setCustomData(self(), data);
    }

    default boolean getUnbreakable() {
        CompoundTag tag = self().getTag();
        if (tag != null && tag.contains("Unbreakable", Tag.TAG_BYTE)) {
            return tag.getBoolean("Unbreakable");
        }
        return false;
    }

    default void setCustomName(@Nullable Component name) {
        self().setHoverName(name);
    }

    default @Nullable Component getCustomName() {
        CompoundTag display = self().getTagElement("display");
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

    default Component getItemName() {
        return self().getItem().getName(self());
    }

    default List<Component> getLore() {
        CompoundTag tag = self().getTag();
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

    default void setLore(List<Component> list) {
        CompoundTag tag = self().getOrCreateTag();
        CompoundTag display;
        if (tag.contains("display", Tag.TAG_COMPOUND)) {
            display = tag.getCompound("display");
        } else {
            display = new CompoundTag();
            tag.put("display", display);
        }
        ListTag lore;
        if (display.contains("Lore", Tag.TAG_LIST)) {
            lore = display.getList("Lore", Tag.TAG_STRING);
        } else {
            lore = new ListTag();
            display.put("Lore", lore);
        }
        for (Component component : list) {
            lore.add(StringTag.valueOf(Component.Serializer.toJson(component)));
        }
    }

    default @Nullable PortItemEnchantments getPortEnchantments() {
        if (PortItemEnchantments.shouldGetStoredEnchantments(self())) {
            return null;
        }
        return new PortItemEnchantments(self().getEnchantmentTags(), getShowEnchantmentsTooltip());
    }

    default PortItemEnchantments getPortEnchantmentsOrDefault(PortItemEnchantments enchantments) {
        if (PortItemEnchantments.shouldGetStoredEnchantments(self())) {
            return enchantments;
        }
        return new PortItemEnchantments(self().getEnchantmentTags(), getShowEnchantmentsTooltip());
    }

    default boolean getCanPlaceOn(BlockInWorld block) {
        return self().hasAdventureModePlaceTagForBlock(block.getLevel().registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    default boolean getCanBreak(BlockInWorld block) {
        return self().hasAdventureModeBreakTagForBlock(block.getLevel().registryAccess().registryOrThrow(Registries.BLOCK), block);
    }

    default int getCustomModelData() {
        CompoundTag tag = self().getTag();
        return tag == null ? 0 : tag.getInt("CustomModelData");
    }

    default void setCustomModelData(int data) {
        self().getOrCreateTag().putInt("CustomModelData", data);
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

    default boolean getShowEnchantmentsTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.ENCHANTMENTS);
    }

    default void setShowEnchantmentsTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.ENCHANTMENTS, show);
    }

    @Private
    static final int HIDE_STORED_ENCHANTMENTS_MASK = 1 << ItemStack.TooltipPart.values().length;

    default boolean getShowStoredEnchantmentsTooltip() {
        return (self().getHideFlags() & HIDE_STORED_ENCHANTMENTS_MASK) == 0;
    }

    default void setShowStoredEnchantmentsTooltip(boolean show) {
        if (show) {
            CompoundTag tag = self().getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_STORED_ENCHANTMENTS_MASK);
            }
        } else {
            CompoundTag tag = self().getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_STORED_ENCHANTMENTS_MASK);
        }
    }

    default boolean getShowAttributeModifiersTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.MODIFIERS);
    }

    default void setShowAttributeModifiersTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.MODIFIERS, show);
    }

    default PortItemAttributeModifiers getPortAttributeModifiers() {
        ListTag listTag = null;
        CompoundTag tag = self().getTag();
        if (tag != null && tag.contains("AttributeModifiers", Tag.TAG_LIST)) {
            listTag = tag.getList("AttributeModifiers", Tag.TAG_COMPOUND);
        }
        if (listTag == null || listTag.isEmpty()) {
            return IPortItemExtension.of(self().getItem()).getDefaultPortAttributeModifiers(self());
        }
        return new PortItemAttributeModifiers(listTag, getShowAttributeModifiersTooltip());
    }

    default void setPortAttributeModifiers(PortItemAttributeModifiers value) {
        value.applyTo(self());
    }

    default boolean getShowUnbreakableTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.UNBREAKABLE);
    }

    default void setShowUnbreakableTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.UNBREAKABLE, show);
    }

    default boolean getShowCanBreakTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.CAN_DESTROY);
    }

    default void setShowCanBreakTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.CAN_DESTROY, show);
    }

    default boolean getShowCanPlaceOnTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.CAN_PLACE);
    }

    default void setShowCanPlaceOnTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.CAN_PLACE, show);
    }

    default boolean getShowAdditionalTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.ADDITIONAL);
    }

    default void setShowAdditionalTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.ADDITIONAL, show);
    }

    default boolean getShowDyeTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.DYE);
    }

    default void setShowDyeTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.DYE, show);
    }

    default boolean getShowTrimTooltip() {
        return getShowTooltipPart(self(), ItemStack.TooltipPart.UPGRADES);
    }

    default void setShowTrimTooltip(boolean show) {
        setShowTooltipPart(self(), ItemStack.TooltipPart.UPGRADES, show);
    }

    @Private
    static final int HIDE_ALL_MASK = HIDE_STORED_ENCHANTMENTS_MASK << 1;

    default boolean getShowTooltip() {
        return (self().getHideFlags() & HIDE_ALL_MASK) == 0;
    }

    default void setShowTooltip(boolean show) {
        if (show) {
            CompoundTag tag = self().getTag();
            if (tag != null) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~HIDE_ALL_MASK);
            }
        } else {
            CompoundTag tag = self().getOrCreateTag();
            tag.putInt("HideFlags", tag.getInt("HideFlags") | HIDE_ALL_MASK);
        }
    }

    default int getRepaireCost() {
        return self().getBaseRepairCost();
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

    default boolean getCreativeSlotLock() {
        return getUnit(self(), "CustomCreativeLock");
    }

    default void setCreativeSlotLock(boolean lock) {
        setUnit(self(), "CustomCreativeLock", lock);
    }

    default boolean getEnchantmentGlintOverride() {
        CompoundTag tag = self().getTag();
        return tag != null && tag.getBoolean("portlib:enchantment_glint_override");
    }

    default boolean hasEnchantmentGlintOverride() {
        CompoundTag tag = self().getTag();
        return tag != null && tag.contains("portlib:enchantment_glint_override", Tag.TAG_BYTE);
    }

    default void setEnchantmentGlintOverride(boolean override) {
        self().getOrCreateTag().putBoolean("portlib:enchantment_glint_override", override);
    }

    // todo AbstractArrow类
    default boolean getIntangibleProjectile() {
        return getUnit(self(), "portlib:intangible_projectile");
    }

    default void setIntangibleProjectile(boolean intangible) {
        setUnit(self(), "portlib:intangible_projectile", intangible);
    }

    default @Nullable FoodProperties getFood(@Nullable LivingEntity living) {
        FoodProperties food = ((IPortItemStack) this).portlib$getFood(living);
        if (food == null) {
            CompoundTag data = self().getTagElement(PortFoodProperties.KEY);
            if (data == null) {
                food = self().getFoodProperties(living);
            } else {
                food = PortFoodProperties.load(data);
            }
            ((IPortItemStack) this).portlib$setFood(food, false);
        }
        return food;
    }

    default void setFood(@Nullable FoodProperties food) {
        ((IPortItemStack) this).portlib$setFood(food, true);
    }

    default boolean getFireResistant() {
        return getUnit(self(), "portlib:fire_resistant");
    }

    default void setFireResistant(boolean resistant) {
        setUnit(self(), "portlib:fire_resistant", resistant);
    }

    default @Nullable PortTool getTool() {
        PortTool tool = ((IPortItemStack) this).portlib$getTool();
        if (tool == null) {
            CompoundTag data = self().getTagElement(PortTool.KEY);
            if (data != null) {
                tool = PortTool.load(data);
                ((IPortItemStack) this).portlib$setTool(tool, false);
            }
        }
        return tool;
    }

    default void setTool(@Nullable PortTool tool) {
        ((IPortItemStack) this).portlib$setTool(tool, true);
    }

    default @Nullable PortItemEnchantments getStoredEnchantments() {
        if (PortItemEnchantments.shouldGetStoredEnchantments(self())) {
            return new PortItemEnchantments(EnchantedBookItem.getEnchantments(self()), getShowStoredEnchantmentsTooltip());
        }
        return null;
    }

    default void setStoredEnchantments(PortItemEnchantments value) {
        self().getOrCreateTag().put(
                EnchantedBookItem.TAG_STORED_ENCHANTMENTS,
                value.getListTag().copy());
        setShowStoredEnchantmentsTooltip(value.showInTooltip);
    }

    /// rgb
    default int getDyedColor() {
        CompoundTag tag = self().getTag();
        if (tag != null && tag.contains("display", Tag.TAG_COMPOUND)) {
            CompoundTag display = tag.getCompound("display");
            if (display.contains("color", Tag.TAG_ANY_NUMERIC)) {
                return display.getInt("color");
            }
        }
        return -1;
    }

    default void setDyedColor(int rgb) {
        self().getOrCreateTagElement("display").putInt("color", rgb);
    }

    /// argb
    default int getMapColor() {
        return MapItem.getColor(self());
    }

    default void setMapColor(int argb) {
        self().getOrCreateTagElement("display").putInt("MapColor", argb);
    }

    default @Nullable Integer getMapId() {
        return MapItem.getMapId(self());
    }

    default void setMapId(int mapId) {
        self().getOrCreateTag().putInt("map", mapId);
    }

    // todo getMapDecortions MapItemSavedData#L201

    default @Nullable PortMapPostProcessing getMapPostProcessing() {
        CompoundTag tag = self().getTag();
        if (tag == null) return null;
        if (tag.contains(MapItem.MAP_SCALE_TAG, Tag.TAG_ANY_NUMERIC)) {
            return PortMapPostProcessing.SCALE;
        } else if (tag.contains(MapItem.MAP_LOCK_TAG, Tag.TAG_BYTE) && tag.getBoolean(MapItem.MAP_LOCK_TAG)) {
            return PortMapPostProcessing.LOCK;
        }
        return null;
    }

    default void setMapPostProcessing(PortMapPostProcessing value) {
        CompoundTag tag = self().getOrCreateTag();
        switch (value) {
            case LOCK -> tag.putBoolean(MapItem.MAP_LOCK_TAG, true);
            case SCALE -> tag.putInt(MapItem.MAP_SCALE_TAG, 1);
        }
    }

    default PortChargedProjectiles getChargedProjectiles() {
        return new PortChargedProjectiles(self());
    }

    default void setChargedProjectiles(PortChargedProjectiles value) {
        value.applyTo(self());
    }

    default PortBundleContents getBundleContents() {
        return new PortBundleContents(self());
    }

    default void setBundleContents(PortBundleContents value) {
        value.applyTo(self());
    }

    default PortPotionContents getPotionContents() {
        return new PortPotionContents(self());
    }

    default void setPotionContents(PortPotionContents value) {
        value.applyTo(self());
    }

    default PortSuspiciousStewEffects getSuspiciousStewEffects() {
        return new PortSuspiciousStewEffects(self());
    }

    default void setSuspiciousStewEffects(PortSuspiciousStewEffects value) {
        value.applyTo(self());
    }

    // todo getWritableBookContent
    // todo getWrittenBookContent

    default @Nullable ArmorTrim getTrim() {
        return ArmorTrim.getTrim(PortEnvironment.registryAccess(), self()).orElse(null);
    }

    default void setTrim(ArmorTrim value) {
        ArmorTrim.setTrim(PortEnvironment.registryAccess(), self(), value);
    }

    default PortDebugStickState getDebugStickState() {
        return new PortDebugStickState(self());
    }

    default void setDebugStickState(PortDebugStickState value) {
        value.applyTo(self());
    }

    default @Nullable CompoundTag getEntityData(boolean orDefault, boolean copy) {
        return getCustomData(self(), orDefault, copy, "EntityTag");
    }

    default void setEntityData(CompoundTag data) {
        setCustomData(self(), data, "EntityTag");
    }

    default @Nullable CompoundTag getBucketEntityData(boolean orDefault, boolean copy) {
        CompoundTag data = orDefault ? self().getOrCreateTag() : self().getTag();
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    default void setBucketEntityData(CompoundTag data) {
        self().setTag(data.copy());
    }

    default @Nullable CompoundTag getBlockEntityData(boolean copy) {
        CompoundTag data = BlockItem.getBlockEntityData(self());
        if (data == null) return null;
        return copy ? data.copy() : data;
    }

    default void setBlockEntityData(BlockEntityType<?> type, CompoundTag data) {
        BlockItem.setBlockEntityData(self(), type, data);
    }

    default @Nullable Holder<Instrument> getInstrument() {
        CompoundTag tag = self().getTag();
        if (tag != null && tag.contains("instrument", 8)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("instrument"));
            if (id != null) {
                return BuiltInRegistries.INSTRUMENT.getHolder(ResourceKey.create(Registries.INSTRUMENT, id)).orElse(null);
            }
        }
        return null;
    }

    default int getEnchantmentLevel(EnchantmentHolder enchantment) {
        int level = self().getEnchantmentLevel(enchantment.value());
        return PortGetEnchantmentLevelEvent.getEnchantmentLevelSpecific(level, self(), enchantment);
    }

    default PortItemEnchantments getAllPortEnchantments(HolderLookup.RegistryLookup<Enchantment> lookup) {
        return PortGetEnchantmentLevelEvent.getAllEnchantmentLevels(getPortEnchantments(), self(), lookup);
    }

    // region data component

    default <T> @Nullable T get(PortDataComponentType<T> type) {
        return ((IPortItemStack) this).portlib$patch().get(type);
    }

    default <T> @Nullable T get(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return get(type.get());
    }

    default <T> @Nullable T set(PortDataComponentType<T> type, T value) {
        T t = ((IPortItemStack) this).portlib$patch().set(type, value);
        ((IPortItemStack) this).updateTag();
        return t;
    }

    default <T> @Nullable T set(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
        return set(type.get(), value);
    }

    default <T> @Nullable T remove(PortDataComponentType<T> type) {
        T t = ((IPortItemStack) this).portlib$patch().remove(type);
        ((IPortItemStack) this).updateTag();
        return t;
    }

    default <T> @Nullable T remove(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return remove(type.get());
    }

    default <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
        return ((IPortItemStack) this).portlib$patch().getOrDefault(type, defaultValue);
    }

    default <T> T getOrDefault(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T defaultValue) {
        return getOrDefault(type.get(), defaultValue);
    }

    default <T> boolean has(PortDataComponentType<T> type) {
        return ((IPortItemStack) this).portlib$patch().has(type);
    }

    default <T> boolean has(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return has(type.get());
    }

    default PortDataComponentMap getPrototype() {
        return ((IPortItemStack) this).portlib$patch().getPrototype();
    }

    default PortDataComponentMap getComponents() {
        return ((IPortItemStack) this).portlib$patch();
    }

    // endregion data component

    @Diff
    default boolean is(RegistryObject<? extends Item> holder) {
        return self().is(holder.get());
    }

    default void hurtAndBreak(int amount, LivingEntity entity, EquipmentSlot slot) {
        self().hurtAndBreak(amount, entity, living -> living.level().broadcastEntityEvent(living, (byte) switch (slot) {
            case MAINHAND -> 47;
            case OFFHAND -> 48;
            case HEAD -> 49;
            case FEET -> 52;
            case LEGS -> 51;
            default -> 50;
        }));
    }

    default ItemStack transmuteCopy(ItemLike item) {
        return transmuteCopy(item, self().getCount());
    }

    default ItemStack transmuteCopy(ItemLike item, int count) {
        if (self().isEmpty()) return ItemStack.EMPTY;
        CompoundTag tag = self().getTag();
        CompoundTag serialized = self().save(new CompoundTag());
        CompoundTag capabilityData = serialized.contains("ForgeCaps", Tag.TAG_COMPOUND)
                ? serialized.getCompound("ForgeCaps") : null;
        ItemStack converted = new ItemStack(item, count, capabilityData);
        if (tag != null) {
            converted.setTag(tag.copy());
        }
        return converted;
    }

    default void consume(int amount, @Nullable LivingEntity living) {
        if (living == null || !IPortLivingEntity.of(living).hasInfiniteMaterials()) {
            self().shrink(amount);
        }
    }

    default void limitSize(int maxSize) {
        if (!self().isEmpty() && self().getCount() > maxSize) {
            self().setCount(maxSize);
        }
    }

    default int getUseDuration(LivingEntity living) {
        return IPortItemExtension.of(self().getItem()).getUseDuration(self(), living);
    }

    default void setRarity(@Nullable Rarity rarity) {
        ((IPortItemStack) this).portlib$setRarity(rarity);
    }

    static IPortItemStackExtension of(ItemStack stack) {
        return (IPortItemStackExtension) (Object) stack;
    }
}
