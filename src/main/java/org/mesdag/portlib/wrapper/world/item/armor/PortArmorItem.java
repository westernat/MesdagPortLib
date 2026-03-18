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
        properties.durability(type.getDurability(settings.durabilityMultiplier));
        return new ArmorItem(materialEntry.asHolder(), type, properties);
    }
}