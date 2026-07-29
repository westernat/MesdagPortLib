package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.item.ItemStack.PortItemStackExtension;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
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
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionContents;
import org.mesdag.portlib.wrapper.world.item.component.*;
import org.mesdag.portlib.wrapper.world.item.enchantment.EnchantmentHolder;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortItemEnchantments;

import java.util.List;

@SuppressWarnings("all")
public interface IPortItemStackExtension {
    private ItemStack self() {
        return (ItemStack) (Object) this;
    }

    default @Nullable CompoundTag getCustomData(boolean orDefault, boolean copy) {
        return PortItemStackExtension.getCustomData(self(), orDefault, copy);
    }

    default void setCustomData(CompoundTag data) {
        PortItemStackExtension.setCustomData(self(), data);
    }

    default boolean getUnbreakable() {
        return PortItemStackExtension.getUnbreakable(self());
    }

    default void setCustomName(@Nullable Component name) {
        PortItemStackExtension.setCustomName(self(), name);
    }

    default @Nullable Component getCustomName() {
        return PortItemStackExtension.getCustomName(self());
    }

    default Component getItemName() {
        return PortItemStackExtension.getItemName(self());
    }

    default List<Component> getLore() {
        return PortItemStackExtension.getLore(self());
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
        return PortItemStackExtension.getPortEnchantments(self());
    }

    default PortItemEnchantments getPortEnchantmentsOrDefault(PortItemEnchantments enchantments) {
        return PortItemStackExtension.getPortEnchantmentsOrDefault(self(), enchantments);
    }

    default boolean getCanPlaceOn(BlockInWorld block) {
        return PortItemStackExtension.getCanPlaceOn(self(), block);
    }

    default boolean getCanBreak(BlockInWorld block) {
        return PortItemStackExtension.getCanBreak(self(), block);
    }

    default int getCustomModelData() {
        return PortItemStackExtension.getCustomModelData(self());
    }

    default void setCustomModelData(int data) {
        PortItemStackExtension.setCustomModelData(self(), data);
    }

    default boolean getShowEnchantmentsTooltip() {
        return PortItemStackExtension.getShowEnchantmentsTooltip(self());
    }

    default void setShowEnchantmentsTooltip(boolean show) {
        PortItemStackExtension.setShowEnchantmentsTooltip(self(), show);
    }

    default boolean getShowStoredEnchantmentsTooltip() {
        return PortItemStackExtension.getShowStoredEnchantmentsTooltip(self());
    }

    default void setShowStoredEnchantmentsTooltip(boolean show) {
        PortItemStackExtension.setShowStoredEnchantmentsTooltip(self(), show);
    }

    default boolean getShowAttributeModifiersTooltip() {
        return PortItemStackExtension.getShowAttributeModifiersTooltip(self());
    }

    default void setShowAttributeModifiersTooltip(boolean show) {
        PortItemStackExtension.setShowAttributeModifiersTooltip(self(), show);
    }

    default PortItemAttributeModifiers getPortAttributeModifiers() {
        return PortItemStackExtension.getPortAttributeModifiers(self());
    }

    default void setPortAttributeModifiers(PortItemAttributeModifiers value) {
        PortItemStackExtension.setPortAttributeModifiers(self(), value);
    }

    default boolean getShowUnbreakableTooltip() {
        return PortItemStackExtension.getShowUnbreakableTooltip(self());
    }

    default void setShowUnbreakableTooltip(boolean show) {
        PortItemStackExtension.setShowUnbreakableTooltip(self(), show);
    }

    default boolean getShowCanBreakTooltip() {
        return PortItemStackExtension.getShowCanBreakTooltip(self());
    }

    default void setShowCanBreakTooltip(boolean show) {
        PortItemStackExtension.setShowCanBreakTooltip(self(), show);
    }

    default boolean getShowCanPlaceOnTooltip() {
        return PortItemStackExtension.getShowCanPlaceOnTooltip(self());
    }

    default void setShowCanPlaceOnTooltip(boolean show) {
        PortItemStackExtension.setShowCanPlaceOnTooltip(self(), show);
    }

    default boolean getShowAdditionalTooltip() {
        return PortItemStackExtension.getShowAdditionalTooltip(self());
    }

    default void setShowAdditionalTooltip(boolean show) {
        PortItemStackExtension.setShowAdditionalTooltip(self(), show);
    }

    default boolean getShowDyeTooltip() {
        return PortItemStackExtension.getShowDyeTooltip(self());
    }

    default void setShowDyeTooltip(boolean show) {
        PortItemStackExtension.setShowDyeTooltip(self(), show);
    }

    default boolean getShowTrimTooltip() {
        return PortItemStackExtension.getShowTrimTooltip(self());
    }

    default void setShowTrimTooltip(boolean show) {
        PortItemStackExtension.setShowTrimTooltip(self(), show);
    }

    default boolean getShowTooltip() {
        return PortItemStackExtension.getShowTooltip(self());
    }

    default void setShowTooltip(boolean show) {
        PortItemStackExtension.setShowTooltip(self(), show);
    }

    default int getRepaireCost() {
        return PortItemStackExtension.getRepaireCost(self());
    }

    default boolean getCreativeSlotLock() {
        return PortItemStackExtension.getCreativeSlotLock(self());
    }

    default void setCreativeSlotLock(boolean lock) {
        PortItemStackExtension.setCreativeSlotLock(self(), lock);
    }

    default boolean getEnchantmentGlintOverride() {
        return PortItemStackExtension.getEnchantmentGlintOverride(self());
    }

    default void setEnchantmentGlintOverride(boolean override) {
        PortItemStackExtension.setEnchantmentGlintOverride(self(), override);
    }

    default boolean getIntangibleProjectile() {
        return PortItemStackExtension.getIntangibleProjectile(self());
    }

    default void setIntangibleProjectile(boolean intangible) {
        PortItemStackExtension.setIntangibleProjectile(self(), intangible);
    }

    default @Nullable FoodProperties getFood(@Nullable LivingEntity living) {
        return PortItemStackExtension.getFood(self(), living);
    }

    default void setFood(@Nullable FoodProperties food) {
        PortItemStackExtension.setFood(self(), food);
    }

    default boolean getFireResistant() {
        return PortItemStackExtension.getFireResistant(self());
    }

    default void setFireResistant(boolean resistant) {
        PortItemStackExtension.setFireResistant(self(), resistant);
    }

    default @Nullable PortTool getTool() {
        return PortItemStackExtension.getTool(self());
    }

    default void setTool(@Nullable PortTool tool) {
        PortItemStackExtension.setTool(self(), tool);
    }

    default @Nullable PortItemEnchantments getStoredEnchantments() {
        return PortItemStackExtension.getStoredEnchantments(self());
    }

    default void setStoredEnchantments(PortItemEnchantments value) {
        PortItemStackExtension.setStoredEnchantments(self(), value);
    }

    default int getDyedColor() {
        return PortItemStackExtension.getDyedColor(self());
    }

    default void setDyedColor(int rgb) {
        PortItemStackExtension.setDyedColor(self(), rgb);
    }

    default int getMapColor() {
        return PortItemStackExtension.getMapColor(self());
    }

    default void setMapColor(int argb) {
        PortItemStackExtension.setMapColor(self(), argb);
    }

    default @Nullable Integer getMapId() {
        return PortItemStackExtension.getMapId(self());
    }

    default void setMapId(int mapId) {
        PortItemStackExtension.setMapId(self(), mapId);
    }

    default @Nullable PortMapPostProcessing getMapPostProcessing() {
        return PortItemStackExtension.getMapPostProcessing(self());
    }

    default void setMapPostProcessing(PortMapPostProcessing value) {
        PortItemStackExtension.setMapPostProcessing(self(), value);
    }

    default PortChargedProjectiles getChargedProjectiles() {
        return PortItemStackExtension.getChargedProjectiles(self());
    }

    default void setChargedProjectiles(PortChargedProjectiles value) {
        PortItemStackExtension.setChargedProjectiles(self(), value);
    }

    default PortBundleContents getBundleContents() {
        return PortItemStackExtension.getBundleContents(self());
    }

    default void setBundleContents(PortBundleContents value) {
        PortItemStackExtension.setBundleContents(self(), value);
    }

    default PortPotionContents getPotionContents() {
        return PortItemStackExtension.getPotionContents(self());
    }

    default void setPotionContents(PortPotionContents value) {
        PortItemStackExtension.setPotionContents(self(), value);
    }

    default PortSuspiciousStewEffects getSuspiciousStewEffects() {
        return PortItemStackExtension.getSuspiciousStewEffects(self());
    }

    default void setSuspiciousStewEffects(PortSuspiciousStewEffects value) {
        PortItemStackExtension.setSuspiciousStewEffects(self(), value);
    }

    default @Nullable ArmorTrim getTrim() {
        return PortItemStackExtension.getTrim(self());
    }

    default void setTrim(ArmorTrim value) {
        PortItemStackExtension.setTrim(self(), value);
    }

    default PortDebugStickState getDebugStickState() {
        return PortItemStackExtension.getDebugStickState(self());
    }

    default void setDebugStickState(PortDebugStickState value) {
        PortItemStackExtension.setDebugStickState(self(), value);
    }

    default @Nullable CompoundTag getEntityData(boolean orDefault, boolean copy) {
        return PortItemStackExtension.getEntityData(self(), orDefault, copy);
    }

    default void setEntityData(CompoundTag data) {
        PortItemStackExtension.setEntityData(self(), data);
    }

    default @Nullable CompoundTag getBucketEntityData(boolean orDefault, boolean copy) {
        return PortItemStackExtension.getBucketEntityData(self(), orDefault, copy);
    }

    default void setBucketEntityData(CompoundTag data) {
        PortItemStackExtension.setBucketEntityData(self(), data);
    }

    default @Nullable CompoundTag getBlockEntityData(boolean copy) {
        return PortItemStackExtension.getBlockEntityData(self(), copy);
    }

    default void setBlockEntityData(BlockEntityType<?> type, CompoundTag data) {
        PortItemStackExtension.setBlockEntityData(self(), type, data);
    }

    default @Nullable Holder<Instrument> getInstrument() {
        return PortItemStackExtension.getInstrument(self());
    }

    default int getEnchantmentLevel(EnchantmentHolder enchantment) {
        return PortItemStackExtension.getEnchantmentLevel(self(), enchantment);
    }

    default PortItemEnchantments getAllPortEnchantments(HolderLookup.RegistryLookup<Enchantment> lookup) {
        return PortItemStackExtension.getAllPortEnchantments(self(), lookup);
    }

    // region data component

    default <T> @Nullable T get(PortDataComponentType<T> type) {
        return PortItemStackExtension.getData(self(), type);
    }

    default <T> @Nullable T get(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return PortItemStackExtension.getData(self(), type);
    }

    default <T> @Nullable T set(PortDataComponentType<T> type, T value) {
        return PortItemStackExtension.setData(self(), type, value);
    }

    default <T> @Nullable T set(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
        return PortItemStackExtension.setData(self(), type, value);
    }

    default <T> @Nullable T remove(PortDataComponentType<T> type) {
        return PortItemStackExtension.removeData(self(), type);
    }

    default <T> @Nullable T remove(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return PortItemStackExtension.removeData(self(), type);
    }

    default <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
        return PortItemStackExtension.getDataOrDefault(self(), type, defaultValue);
    }

    default <T> T getOrDefault(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T defaultValue) {
        return PortItemStackExtension.getDataOrDefault(self(), type, defaultValue);
    }

    default <T> boolean has(PortDataComponentType<T> type) {
        return PortItemStackExtension.hasData(self(), type);
    }

    default <T> boolean has(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type) {
        return PortItemStackExtension.hasData(self(), type);
    }

    default PortDataComponentMap getPrototype() {
        return PortItemStackExtension.getPrototypeData(self());
    }

    default PortDataComponentMap getComponents() {
        return ((IPortItemStack) this).portlib$patch();
    }

    // endregion data component

    @Diff
    default boolean is(RegistryObject<? extends Item> holder) {
        return PortItemStackExtension.is(self(), holder);
    }

    default void hurtAndBreak(int amount, LivingEntity entity, EquipmentSlot slot) {
        PortItemStackExtension.hurtAndBreak(self(), amount, entity, slot);
    }

    default ItemStack transmuteCopy(ItemLike item) {
        return PortItemStackExtension.transmuteCopy(self(), item);
    }

    default ItemStack transmuteCopy(ItemLike item, int count) {
        return PortItemStackExtension.transmuteCopy(self(), item, count);
    }

    default void consume(int amount, @Nullable LivingEntity living) {
        PortItemStackExtension.consume(self(), amount, living);
    }

    default void limitSize(int maxSize) {
        PortItemStackExtension.limitSize(self(), maxSize);
    }

    /**
     * 返回当前使用者上下文中的物品使用时长。
     *
     * <p>1.21 的物品栈会把物品栈和使用者一起交给物品。1.20 原版只有无使用者参数的
     * 入口，因此桥接层必须显式转发到 {@link IPortItemExtension}；退回原版方法会让需要
     * 玩家上下文的移植物品丢失自定义时长。</p>
     */
    default int getUseDuration(LivingEntity living) {
        return IPortItemExtension.of(self().getItem()).getUseDuration(self(), living);
    }

    default void setRarity(Rarity rarity) {
        // todo
    }

    static IPortItemStackExtension of(ItemStack stack) {
        return (IPortItemStackExtension) (Object) stack;
    }
}
