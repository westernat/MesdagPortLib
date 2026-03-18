package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

import java.util.EnumMap;
import java.util.Map;

public class PortArmorMaterialWrapper implements ArmorMaterial {
    private static final Map<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 13);
        map.put(ArmorItem.Type.LEGGINGS, 15);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 11);
    });

    private final PortArmorMaterial.Settings settings;

    public PortArmorMaterialWrapper(PortArmorMaterial.Settings settings) {
        this.settings = settings;
    }

    @Override
    public int getDurabilityForType(ArmorItem.@NotNull Type type) {
        Integer base = HEALTH_FUNCTION_FOR_TYPE.get(type);
        return (base == null ? 0 : base) * settings.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.@NotNull Type type) {
        return settings.defense.getOrDefault(type, 0);
    }

    @Override
    public int getEnchantmentValue() {
        return settings.enchantmentValue;
    }

    @NotNull
    @Override
    public SoundEvent getEquipSound() {
        return settings.equipSound.value();
    }

    @NotNull
    @Override
    public Ingredient getRepairIngredient() {
        return settings.repairIngredient.get();
    }

    @NotNull
    @Override
    public String getName() {
        return settings.assetId.getPath();
    }

    @Override
    public float getToughness() {
        return settings.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return settings.knockbackResistance;
    }
}