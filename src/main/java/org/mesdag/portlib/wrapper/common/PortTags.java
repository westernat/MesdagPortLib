package org.mesdag.portlib.wrapper.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import org.mesdag.portlib.PortLib;

public class PortTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> IS_MAGIC = common("is_magic");

        private static TagKey<DamageType> portlib(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, PortLib.asResource(name));
        }

        private static TagKey<DamageType> common(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> RANGED_WEAPON_TOOLS = common("tools/ranged_weapon");

        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
