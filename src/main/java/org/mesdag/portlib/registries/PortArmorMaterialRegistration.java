package org.mesdag.portlib.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterial.Layer;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

import java.util.List;
import java.util.Objects;

public class PortArmorMaterialRegistration extends PortRegistration<ArmorMaterial> {

    PortArmorMaterialRegistration(String namespace) {
        super(namespace, BuiltInRegistries.ARMOR_MATERIAL.key());
    }

    public PortRegistryEntry<ArmorMaterial> register(PortArmorMaterial.Settings settings) {
        Objects.requireNonNull(settings.assetId, "Armor assetId must not be null for registration!");

        return register(settings.assetId.getPath(), () -> {
            List<Layer> layers = List.of(new Layer(settings.assetId));
            return new ArmorMaterial(
                settings.defense,
                settings.enchantmentValue,
                settings.equipSound,
                settings.repairIngredient,
                layers,
                settings.toughness,
                settings.knockbackResistance
            );
        });
    }
}