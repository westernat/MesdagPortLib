package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

public class PortArmorItem {

    @Diff
    public static ArmorItem create(
        PortRegistryEntry<ArmorMaterial> materialEntry,
        ArmorItem.Type type,
        Item.Properties properties,
        PortArmorMaterial.Settings settings
    ) {
        int baseDurability = switch (type) {
            case BOOTS -> 13;
            case LEGGINGS -> 15;
            case CHESTPLATE -> 16;
            case HELMET -> 11;
        };
        properties.durability(baseDurability * settings.durabilityMultiplier);
        return new ArmorItem(materialEntry.get(), type, properties);
    }
}