package org.mesdag.portlib.wrapper.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
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
        public static final TagKey<Item> TOOLS = common("tools");
        public static final TagKey<Item> TOOLS_SHIELD = common("tools/shield");
        public static final TagKey<Item> TOOLS_BOW = common("tools/bow");
        public static final TagKey<Item> TOOLS_CROSSBOW = common("tools/crossbow");
        public static final TagKey<Item> TOOLS_FISHING_ROD = common("tools/fishing_rod");
        public static final TagKey<Item> TOOLS_SPEAR = common("tools/spear");
        public static final TagKey<Item> TOOLS_SHEAR = common("tools/shear");
        public static final TagKey<Item> TOOLS_BRUSH = common("tools/brush");
        public static final TagKey<Item> TOOLS_IGNITER = common("tools/igniter");
        public static final TagKey<Item> TOOLS_WRENCH = common("tools/wrench");
        public static final TagKey<Item> MELEE_WEAPON_TOOLS = common("tools/melee_weapon");
        public static final TagKey<Item> RANGED_WEAPON_TOOLS = common("tools/ranged_weapon");
        public static final TagKey<Item> MINING_TOOL_TOOLS = common("tools/mining_tool");

        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> DEFLECTS_PROJECTILES = mc("deflects_projectiles");

        private static TagKey<EntityType<?>> mc(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(name));
        }
    }
}
