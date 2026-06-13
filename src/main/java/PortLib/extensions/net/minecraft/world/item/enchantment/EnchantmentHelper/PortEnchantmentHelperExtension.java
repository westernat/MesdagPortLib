package PortLib.extensions.net.minecraft.world.item.enchantment.EnchantmentHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.util.Static;
import org.mesdag.portlib.wrapper.world.item.enchantment.PortEnchantmentHelper;

public final class PortEnchantmentHelperExtension {
    @Static
    public static boolean hasAnyEnchantments(ItemStack stack) {
        return !getEnchantmentTags(stack).isEmpty();
    }

    @Static
    public static void runIterationOnItem(ItemStack stack, PortEnchantmentHelper.PortEnchantmentVisitor visitor) {
        if (stack.isEmpty()) return;
        ListTag listTag = getEnchantmentTags(stack);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag tag = listTag.getCompound(i);
            ResourceLocation id = EnchantmentHelper.getEnchantmentId(tag);
            if (id == null) continue;
            ForgeRegistries.ENCHANTMENTS.getHolder(id).ifPresent(holder -> {
                visitor.accept(holder.value(), EnchantmentHelper.getEnchantmentLevel(tag));
            });
        }
    }

    @Static
    public static void doPostAttackEffects(ServerLevel level, Entity target, DamageSource damageSource) {
        Entity attacker = damageSource.getEntity();
        if (attacker instanceof LivingEntity livingAttacker) {
            EnchantmentHelper.doPostDamageEffects(livingAttacker, target);
        }
        if (attacker != null && target instanceof LivingEntity livingTarget) {
            EnchantmentHelper.doPostHurtEffects(livingTarget, attacker);
        }
    }

    private static ListTag getEnchantmentTags(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK)
                ? EnchantedBookItem.getEnchantments(stack)
                : stack.getEnchantmentTags();
    }
}
