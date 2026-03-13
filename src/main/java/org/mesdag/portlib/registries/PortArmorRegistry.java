package org.mesdag.portlib.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.armor.PortArmorMaterial;

@SuppressWarnings("all")
public class PortArmorRegistry {
    private static Registration<ArmorMaterial> REGISTRY;

    @Diff
    public static void init(String namespace) {
        REGISTRY = new Registration<>(namespace, Registries.ARMOR_MATERIAL);
    }

    @Diff
    public static PortRegistryEntry<ArmorMaterial> register(String name, PortArmorMaterial material) {
        PortRegistryEntry<ArmorMaterial> entry = REGISTRY.register(name, material::unwrap);
        entry.setRaw(material);
        return entry;
    }
}