package org.mesdag.portlib.wrapper.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
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

        public static final TagKey<Item> SEEDS = common("seeds");
        public static final TagKey<Item> SEEDS_BEETROOT = common("seeds/beetroot");
        public static final TagKey<Item> SEEDS_MELON = common("seeds/melon");
        public static final TagKey<Item> SEEDS_PUMPKIN = common("seeds/pumpkin");
        public static final TagKey<Item> SEEDS_TORCHFLOWER = common("seeds/torchflower");
        public static final TagKey<Item> SEEDS_WHEAT = common("seeds/wheat");

        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> FENCES = common("fences");
        public static final TagKey<Block> FENCES_NETHER_BRICK = common("fences/nether_brick");
        public static final TagKey<Block> FENCES_WOODEN = common("fences/wooden");

        public static final TagKey<Block> FENCE_GATES = common("fence_gates");
        public static final TagKey<Block> FENCE_GATES_WOODEN = common("fence_gates/wooden");

        public static final TagKey<Block> STRIPPED_LOGS = common("stripped_logs");

        private static TagKey<Block> common(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> DEFLECTS_PROJECTILES = mc("deflects_projectiles");
        public static final TagKey<EntityType<?>> UNDEAD = mc("undead");
        public static final TagKey<EntityType<?>> ZOMBIES = mc("zombies");

        private static TagKey<EntityType<?>> mc(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_ICY = common("is_icy");
        public static final TagKey<Biome> IS_SNOWY = common("is_snowy");
        public static final TagKey<Biome> IS_JUNGLE = common("is_jungle");
        public static final TagKey<Biome> IS_SAVANNA = common("is_savanna");
        public static final TagKey<Biome> IS_TAIGA = common("is_taiga");
        public static final TagKey<Biome> IS_DESERT = common("is_desert");
        public static final TagKey<Biome> IS_AQUATIC = common("is_aquatic");
        public static final TagKey<Biome> IS_UNDERGROUND = common("is_underground");
        public static final TagKey<Biome> IS_RIVER = common("is_river");
        public static final TagKey<Biome> IS_OCEAN = common("is_ocean");
        public static final TagKey<Biome> IS_DEEP_OCEAN = common("is_deep_ocean");
        public static final TagKey<Biome> IS_SHALLOW_OCEAN = common("is_shallow_ocean");
        public static final TagKey<Biome> IS_LUSH = common("is_lush");

        public static final TagKey<Biome> IS_CONIFEROUS_TREE = common("is_tree/coniferous");
        public static final TagKey<Biome> IS_SAVANNA_TREE = common("is_tree/savanna");
        public static final TagKey<Biome> IS_JUNGLE_TREE = common("is_tree/jungle");
        public static final TagKey<Biome> IS_DECIDUOUS_TREE = common("is_tree/deciduous");

        private static TagKey<Biome> common(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
