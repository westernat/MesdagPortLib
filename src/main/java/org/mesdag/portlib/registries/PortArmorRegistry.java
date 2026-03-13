package org.mesdag.portlib.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.armor.PortArmorMaterial;

/**
 * 1.20.1 侧的盔甲材质注册类
 */
@SuppressWarnings("all")
public class PortArmorRegistry {
    private static Registration<ArmorMaterial> REGISTRY;

    private static final ResourceKey<Registry<ArmorMaterial>> ARMOR_MATERIAL_KEY =
        ResourceKey.createRegistryKey(new ResourceLocation("minecraft", "armor_material"));

    @Diff
    public static void init(String namespace) {

        REGISTRY = PortRegisterHandler.registration(namespace, ARMOR_MATERIAL_KEY);
    }

    @Diff
    public static PortRegistryEntry<ArmorMaterial> register(String name, PortArmorMaterial material) {
        if (REGISTRY == null) {
            throw new IllegalStateException("PortArmorRegistry must be initialized! Please call init(namespace) first.");
        }
        PortRegistryEntry<ArmorMaterial> entry = REGISTRY.register(name, material::unwrap);
        entry.setRaw(material);
        return entry;
    }
}