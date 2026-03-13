package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial; // 1.20.1 中这是一个接口
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.Map;
import java.util.function.Supplier;

public record PortArmorMaterial(
    Map<ArmorItem.Type, Integer> defenseMap,
    int bodyDefense,
    int enchantmentValue,
    Holder<SoundEvent> equipSound,
    Supplier<Ingredient> repairIngredient,
    float toughness,
    float knockbackResistance,
    PortIdentifier assetId,
    @Diff int durabilityMultiplier
) {
    @Diff
    public ArmorMaterial unwrap() {
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return switch (type.name()) {
                    case "HELMET" -> durabilityMultiplier * 11;
                    case "LEGGINGS" -> durabilityMultiplier * 15;
                    case "BOOTS" -> durabilityMultiplier * 13;
                    case "CHESTPLATE", "BODY" -> durabilityMultiplier * 16;
                    default -> durabilityMultiplier * 16;
                };
            }

            @Override
            public int getDefenseForType(ArmorItem.@NotNull Type type) {
                if ("BODY".equals(type.name())) {
                    return bodyDefense;
                }
                return defenseMap.getOrDefault(type, 0);
            }

            @Override
            public int getEnchantmentValue() {
                return enchantmentValue;
            }

            @NotNull
            @Override
            public SoundEvent getEquipSound() {
                return equipSound.get();
            }

            @NotNull
            @Override
            public Ingredient getRepairIngredient() {
                return repairIngredient.get();
            }

            @NotNull
            @Override
            public String getName() {
                return assetId.toString();
            }

            @Override
            public float getToughness() {
                return toughness;
            }

            @Override
            public float getKnockbackResistance() {
                return knockbackResistance;
            }
        };
    }
}