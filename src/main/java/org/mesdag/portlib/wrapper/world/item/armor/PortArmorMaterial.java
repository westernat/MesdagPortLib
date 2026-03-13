package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.EnumMap;
import java.util.List;
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

        Map<ArmorItem.Type, Integer> finalDefense = new EnumMap<>(ArmorItem.Type.class);
        finalDefense.putAll(defenseMap);
        finalDefense.put(ArmorItem.Type.BODY, bodyDefense);

        List<ArmorMaterial.Layer> layers = List.of(
            new ArmorMaterial.Layer(assetId)
        );

        return new ArmorMaterial(
            finalDefense,
            enchantmentValue,
            equipSound,
            repairIngredient,
            layers,
            toughness,
            knockbackResistance
        );
    }
}