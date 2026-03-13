package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;

public class PortArmorItem {

    @Diff
    public static ArmorItem create(PortRegistryEntry<ArmorMaterial> materialEntry, PortArmorType portType, Item.Properties properties) {
        Object raw = materialEntry.getRaw();
        ArmorItem.Type vanillaType = portType.getVanilla();

        if (raw instanceof PortArmorMaterial portMaterial) {
            int multiplier = portMaterial.durabilityMultiplier();
            int durability = switch (vanillaType) {
                case HELMET -> multiplier * 11;
                case LEGGINGS -> multiplier * 15;
                case BOOTS -> multiplier * 13;
                case CHESTPLATE, BODY -> multiplier * 16;
            };
            properties.durability(durability);
        }

        return new ArmorItem(materialEntry.asHolder(), vanillaType, properties);
    }
}