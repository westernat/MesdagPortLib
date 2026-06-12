package PortLib.extensions.net.minecraft.world.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.mesdag.portlib.diff.Diff;

import java.util.LinkedHashMap;
import java.util.Map;

@Diff
public final class PortEnchantmentHelper {

    public static boolean hasAnyEnchantments(ItemStack thiz) {
        return !getEnchantments(thiz).isEmpty();
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack thiz) {
        ListTag tags = thiz.is(Items.ENCHANTED_BOOK)
                ? EnchantedBookItem.getEnchantments(thiz)
                : thiz.getEnchantmentTags();
        return deserializeEnchantments(tags);
    }


    public static void setEnchantments(ItemStack thiz, Map<Enchantment, Integer> enchantments) {
        ListTag listTag = new ListTag();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(entry.getKey());
            if (id != null) {
                listTag.add(EnchantmentHelper.storeEnchantment(id, entry.getValue()));
            }
        }
        if (listTag.isEmpty()) {
            thiz.removeTagKey("Enchantments");
        } else if (!thiz.is(Items.ENCHANTED_BOOK)) {
            thiz.addTagElement("Enchantments", listTag);
        }
    }

    public static void updateEnchantment(ItemStack thiz, Enchantment enchantment, int level) {
        Map<Enchantment, Integer> enchants = getEnchantments(thiz);
        if (level <= 0) {
            enchants.remove(enchantment);
        } else {
            enchants.put(enchantment, level);
        }
        setEnchantments(thiz, enchants);
    }

    @Diff
    public static void runIterationOnItem(ItemStack thiz, EnchantmentVisitor visitor) {
        if (thiz.isEmpty()) return;
        Map<Enchantment, Integer> enchants = getEnchantments(thiz);
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            visitor.accept(entry.getKey(), entry.getValue());
        }
    }

    @Diff
    public static void doPostAttackEffects(ServerLevel level, Entity target, DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            EnchantmentHelper.doPostDamageEffects(livingAttacker, target);
        }
        if (target instanceof LivingEntity livingTarget) {
            EnchantmentHelper.doPostHurtEffects(livingTarget, attacker);
        }
    }

    @Diff
    public static int getMobLooting(LivingEntity thiz) {
        return EnchantmentHelper.getMobLooting(thiz);
    }

    private static Map<Enchantment, Integer> deserializeEnchantments(ListTag listTag) {
        Map<Enchantment, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(tag);
            if (id != null) {
                BuiltInRegistries.ENCHANTMENT.getOptional(id).ifPresent(
                        ench -> map.put(ench, EnchantmentHelper.getEnchantmentLevel(tag))
                );
            }
        }
        return map;
    }


    @FunctionalInterface
    public interface EnchantmentVisitor {
        void accept(Enchantment enchantment, int level);
    }

    @FunctionalInterface
    public interface EnchantmentSlotVisitor {
        void accept(Enchantment enchantment, int level, ItemStack stack, EquipmentSlot slot, LivingEntity entity);
    }
}
