package org.mesdag.portlib.wrapper.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class PortTags {
    public static class Fluids {
        public static final TagKey<Fluid> WATER = common("water");
        public static final TagKey<Fluid> LAVA = common("lava");
        public static final TagKey<Fluid> MILK = common("milk");
        public static final TagKey<Fluid> HONEY = common("honey");

        private static TagKey<Fluid> common(String name) {
            return TagKey.create(Registries.FLUID, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }

    public static class DamageTypes {
        public static final TagKey<DamageType> IS_MAGIC = common("is_magic");
        public static final TagKey<DamageType> PANIC_CAUSES = mc("panic_causes");
        public static final TagKey<DamageType> PANIC_ENVIRONMENTAL_CAUSES = mc("panic_environmental_causes");
        public static final TagKey<DamageType> IS_PLAYER_ATTACK = mc("is_player_attack");
        public static final TagKey<DamageType> CAN_BREAK_ARMOR_STAND = mc("can_break_armor_stand");

        private static TagKey<DamageType> common(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<DamageType> mc(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.withDefaultNamespace(name));
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

        public static final TagKey<Item> GLASS_BLOCKS = common("glass_blocks");
        public static final TagKey<Item> GLASS_BLOCKS_COLORLESS = common("glass_blocks/colorless");
        public static final TagKey<Item> GLASS_BLOCKS_CHEAP = common("glass_blocks/cheap");
        public static final TagKey<Item> GLASS_BLOCKS_TINTED = common("glass_blocks/tinted");

        public static final TagKey<Item> MUSIC_DISCS = common("music_discs");

        public static final TagKey<Item> GRAVELS = common("gravels");

        public static final TagKey<Item> ARMORS = common("armors"); // todo

        public static final TagKey<Item> STONES = common("stones"); // todo

        public static final TagKey<Item> FOODS_RAW_FISH = common("foods/raw_fish"); // todo
        public static final TagKey<Item> FOODS_COOKED_FISH = common("foods/cooked_fish"); // todo
        public static final TagKey<Item> FOODS_FRUIT = common("foods/fruit"); // todo
        public static final TagKey<Item> FOODS_RAW_MEAT = common("foods/raw_meat"); // todo
        public static final TagKey<Item> FOODS_COOKED_MEAT = common("foods/cooked_meat"); // todo
        public static final TagKey<Item> FOODS_VEGETABLE = common("foods/vegetable"); // todo
        public static final TagKey<Item> FOODS_BREAD = common("foods/bread"); // todo
        public static final TagKey<Item> FOODS_PIE = common("foods/pie"); // todo
        public static final TagKey<Item> FOODS_COOKIE = common("foods/cookie"); // todo
        public static final TagKey<Item> FOODS_SOUP = common("foods/soup"); // todo
        public static final TagKey<Item> FOODS_GOLDEN = common("foods/golden"); // todo
        public static final TagKey<Item> FOODS_EDIBLE_WHEN_PLACED = common("foods/edible_when_placed"); // todo

        public static final TagKey<Item> SANDS = common("sands"); // todo

        public static final TagKey<Item> BONES = common("bones"); // todo
        public static final TagKey<Item> BRICKS = common("bricks"); // todo
        public static final TagKey<Item> BUCKETS = common("buckets"); // todo
        public static final TagKey<Item> BUCKETS_LAVA = common("buckets/lava"); // todo
        public static final TagKey<Item> BUCKETS_WATER = common("buckets/water"); // todo
        public static final TagKey<Item> CHAINS = common("chains"); // todo
        public static final TagKey<Item> CHESTS_TRAPPED = common("chests/trapped"); // todo

        public static final TagKey<Item> CROPS = common("crops"); // todo
        public static final TagKey<Item> CROPS_WHEAT = common("crops/wheat"); // todo

        public static final TagKey<Item> DUSTS = common("dusts"); // todo

        public static final TagKey<Item> DYED = common("dyed"); // todo
        public static final TagKey<Item> DYED_BLACK = common("dyed/black"); // todo
        public static final TagKey<Item> DYED_BLUE = common("dyed/blue"); // todo
        public static final TagKey<Item> DYED_BROWN = common("dyed/brown"); // todo
        public static final TagKey<Item> DYED_CYAN = common("dyed/cyan"); // todo
        public static final TagKey<Item> DYED_GRAY = common("dyed/gray"); // todo
        public static final TagKey<Item> DYED_GREEN = common("dyed/green"); // todo
        public static final TagKey<Item> DYED_LIGHT_BLUE = common("dyed/light_blue"); // todo
        public static final TagKey<Item> DYED_LIGHT_GRAY = common("dyed/light_gray"); // todo
        public static final TagKey<Item> DYED_LIME = common("dyed/lime"); // todo
        public static final TagKey<Item> DYED_MAGENTA = common("dyed/magenta"); // todo
        public static final TagKey<Item> DYED_ORANGE = common("dyed/orange"); // todo
        public static final TagKey<Item> DYED_PINK = common("dyed/pink"); // todo
        public static final TagKey<Item> DYED_PURPLE = common("dyed/purple"); // todo
        public static final TagKey<Item> DYED_RED = common("dyed/red"); // todo
        public static final TagKey<Item> DYED_WHITE = common("dyed/white"); // todo
        public static final TagKey<Item> DYED_YELLOW = common("dyed/yellow"); // todo

        public static final TagKey<Item> FENCE_GATES = common("fence_gates"); // todo
        public static final TagKey<Item> FENCE_GATES_WOODEN = common("fence_gates/wooden"); // todo
        public static final TagKey<Item> FERTILIZERS = common("fertilizers"); // todo

        public static final TagKey<Item> FOODS = common("foods"); // todo

        public static final TagKey<Item> GEMS = common("gems"); // todo

        public static final TagKey<Item> INGOTS = common("ingots"); // todo
        public static final TagKey<Item> INGOTS_GOLD = common("ingots/gold"); // todo
        public static final TagKey<Item> INGOTS_IRON = common("ingots/iron"); // todo

        public static final TagKey<Item> MUSHROOMS = common("mushrooms"); // todo
        public static final TagKey<Item> NUGGETS = common("nuggets"); // todo

        public static final TagKey<Item> ORES = common("ores"); // todo
        public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = common("ores_in_ground/deepslate"); // todo
        public static final TagKey<Item> ORES_IN_GROUND_NETHERRACK = common("ores_in_ground/netherrack"); // todo
        public static final TagKey<Item> ORES_IN_GROUND_STONE = common("ores_in_ground/stone"); // todo
        public static final TagKey<Item> ORE_RATES_DENSE = common("ore_rates/dense"); // todo
        public static final TagKey<Item> ORE_RATES_SINGULAR = common("ore_rates/singular"); // todo

        public static final TagKey<Item> PLAYER_WORKSTATIONS_CRAFTING_TABLES = common("player_workstations/crafting_tables"); // todo
        public static final TagKey<Item> PLAYER_WORKSTATIONS_FURNACES = common("player_workstations/furnaces"); // todo
        public static final TagKey<Item> POTIONS = common("potions"); // todo
        public static final TagKey<Item> POTION_BOTTLE = common("potion_bottle"); // todo

        public static final TagKey<Item> RAW_MATERIALS = common("raw_materials"); // todo
        public static final TagKey<Item> ROPES = common("ropes"); // todo
        public static final TagKey<Item> SANDSTONE_BLOCKS = common("sandstone_blocks"); // todo
        public static final TagKey<Item> STORAGE_BLOCKS = common("storage_blocks"); // todo
        public static final TagKey<Item> STRIPPED_LOGS = common("stripped_logs"); // todo
        public static final TagKey<Item> VILLAGER_JOB_SITES = common("villager_job_sites"); // todo

        public static final TagKey<Item> MACE_ENCHANTABLE = mc("mace_enchantable"); // todo

        public static final TagKey<Item> SWORDS = mc("swords"); // todo
        public static final TagKey<Item> AXES = mc("axes"); // todo
        public static final TagKey<Item> HOES = mc("hoes"); // todo
        public static final TagKey<Item> PICKAXES = mc("pickaxes"); // todo
        public static final TagKey<Item> SHOVELS = mc("shovels"); // todo

        public static final TagKey<Item> HEAD_ARMOR = mc("head_armor"); // todo
        public static final TagKey<Item> CHEST_ARMOR = mc("chest_armor"); // todo
        public static final TagKey<Item> LEG_ARMOR = mc("leg_armor"); // todo
        public static final TagKey<Item> FOOT_ARMOR = mc("foot_armor"); // todo

        public static final TagKey<Item> BOW_ENCHANTABLE = mc("bow_enchantable"); // todo
        public static final TagKey<Item> CROSSBOW_ENCHANTABLE = mc("crossbow_enchantable"); // todo
        public static final TagKey<Item> DURABILITY_ENCHANTABLE = mc("durability_enchantable"); // todo
        public static final TagKey<Item> FISHING_ENCHANTABLE = mc("fishing_enchantable"); // todo
        public static final TagKey<Item> MINING_ENCHANTABLE = mc("mining_enchantable"); // todo
        public static final TagKey<Item> MINING_LOOT_ENCHANTABLE = mc("mining_loot_enchantable"); // todo
        public static final TagKey<Item> SHARP_WEAPON_ENCHANTABLE = mc("sharp_weapon_enchantable"); // todo
        public static final TagKey<Item> WEAPON_ENCHANTABLE = mc("weapon_enchantable"); // todo

        public static final TagKey<Item> COAL_ORES = mc("coal_ores"); // todo
        public static final TagKey<Item> COPPER_ORES = mc("copper_ores"); // todo
        public static final TagKey<Item> DIAMOND_ORES = mc("diamond_ores"); // todo
        public static final TagKey<Item> EMERALD_ORES = mc("emerald_ores"); // todo
        public static final TagKey<Item> GOLD_ORES = mc("gold_ores"); // todo
        public static final TagKey<Item> IRON_ORES = mc("iron_ores"); // todo
        public static final TagKey<Item> LAPIS_ORES = mc("lapis_ores"); // todo
        public static final TagKey<Item> REDSTONE_ORES = mc("redstone_ores"); // todo

        public static final TagKey<Item> LOGS = mc("logs"); // todo
        public static final TagKey<Item> LOGS_THAT_BURN = mc("logs_that_burn"); // todo
        public static final TagKey<Item> PLANKS = mc("planks"); // todo
        public static final TagKey<Item> NON_FLAMMABLE_WOOD = mc("non_flammable_wood"); // todo
        public static final TagKey<Item> SAPLINGS = mc("saplings"); // todo
        public static final TagKey<Item> LEAVES = mc("leaves"); // todo
        public static final TagKey<Item> WOODEN_BUTTONS = mc("wooden_buttons"); // todo
        public static final TagKey<Item> WOODEN_DOORS = mc("wooden_doors"); // todo
        public static final TagKey<Item> WOODEN_FENCES = mc("wooden_fences"); // todo
        public static final TagKey<Item> WOODEN_PRESSURE_PLATES = mc("wooden_pressure_plates"); // todo
        public static final TagKey<Item> WOODEN_SLABS = mc("wooden_slabs"); // todo
        public static final TagKey<Item> WOODEN_STAIRS = mc("wooden_stairs"); // todo
        public static final TagKey<Item> WOODEN_TRAPDOORS = mc("wooden_trapdoors"); // todo

        public static final TagKey<Item> MEAT = mc("meat"); // todo
        public static final TagKey<Item> FISHES = mc("fishes"); // todo
        public static final TagKey<Item> CAT_FOOD = mc("cat_food"); // todo
        public static final TagKey<Item> GOAT_FOOD = mc("goat_food"); // todo
        public static final TagKey<Item> LLAMA_FOOD = mc("llama_food"); // todo
        public static final TagKey<Item> LLAMA_TEMPT_ITEMS = mc("llama_tempt_items"); // todo
        public static final TagKey<Item> PARROT_FOOD = mc("parrot_food"); // todo
        public static final TagKey<Item> PARROT_POISONOUS_FOOD = mc("parrot_poisonous_food"); // todo
        public static final TagKey<Item> PIGLIN_LOVED = mc("piglin_loved"); // todo
        public static final TagKey<Item> VILLAGER_PLANTABLE_SEEDS = mc("villager_plantable_seeds"); // todo
        public static final TagKey<Item> SHEEP_FOOD = mc("sheep_food"); // todo

        public static final TagKey<Item> ANVIL = mc("anvil"); // todo
        public static final TagKey<Item> ARROWS = mc("arrows"); // todo
        public static final TagKey<Item> BEACON_PAYMENT_ITEMS = mc("beacon_payment_items"); // todo
        public static final TagKey<Item> BOATS = mc("boats"); // todo
        public static final TagKey<Item> BOOKSHELF_BOOKS = mc("bookshelf_books"); // todo
        public static final TagKey<Item> CHEST_BOATS = mc("chest_boats"); // todo
        public static final TagKey<Item> CLUSTER_MAX_HARVESTABLES = mc("cluster_max_harvestables"); // todo
        public static final TagKey<Item> COMPASSES = mc("compasses"); // todo
        public static final TagKey<Item> DIRT = mc("dirt"); // todo
        public static final TagKey<Item> HANGING_SIGNS = mc("hanging_signs"); // todo
        public static final TagKey<Item> RAILS = mc("rails"); // todo
        public static final TagKey<Item> SIGNS = mc("signs"); // todo
        public static final TagKey<Item> STONE_BRICKS = mc("stone_bricks"); // todo

        private static TagKey<Item> common(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Item> mc(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> FENCES = common("fences");
        public static final TagKey<Block> FENCES_NETHER_BRICK = common("fences/nether_brick");
        public static final TagKey<Block> FENCES_WOODEN = common("fences/wooden");

        public static final TagKey<Block> FENCE_GATES = common("fence_gates");
        public static final TagKey<Block> FENCE_GATES_WOODEN = common("fence_gates/wooden");

        public static final TagKey<Block> STRIPPED_LOGS = common("stripped_logs");

        public static final TagKey<Block> GLASS_BLOCKS = common("glass_blocks");
        public static final TagKey<Block> GLASS_BLOCKS_COLORLESS = common("glass_blocks/colorless");
        public static final TagKey<Block> GLASS_BLOCKS_CHEAP = common("glass_blocks/cheap");
        public static final TagKey<Block> GLASS_BLOCKS_TINTED = common("glass_blocks/tinted");

        public static final TagKey<Block> ORE_BEARING_GROUND_DEEPSLATE = common("ore_bearing_ground/deepslate");
        public static final TagKey<Block> ORE_BEARING_GROUND_NETHERRACK = common("ore_bearing_ground/netherrack");
        public static final TagKey<Block> ORE_BEARING_GROUND_STONE = common("ore_bearing_ground/stone");
        public static final TagKey<Block> ORE_RATES_DENSE = common("ore_rates/dense");
        public static final TagKey<Block> ORE_RATES_SINGULAR = common("ore_rates/singular");
        public static final TagKey<Block> ORE_RATES_SPARSE = common("ore_rates/sparse");
        public static final TagKey<Block> ORES = common("ores");
        public static final TagKey<Block> ORES_COAL = common("ores/coal");
        public static final TagKey<Block> ORES_COPPER = common("ores/copper");
        public static final TagKey<Block> ORES_DIAMOND = common("ores/diamond");
        public static final TagKey<Block> ORES_EMERALD = common("ores/emerald");
        public static final TagKey<Block> ORES_GOLD = common("ores/gold");
        public static final TagKey<Block> ORES_IRON = common("ores/iron");
        public static final TagKey<Block> ORES_LAPIS = common("ores/lapis");
        public static final TagKey<Block> ORES_NETHERITE_SCRAP = common("ores/netherite_scrap");
        public static final TagKey<Block> ORES_QUARTZ = common("ores/quartz");
        public static final TagKey<Block> ORES_REDSTONE = common("ores/redstone");
        public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = common("ores_in_ground/deepslate");
        public static final TagKey<Block> ORES_IN_GROUND_NETHERRACK = common("ores_in_ground/netherrack");
        public static final TagKey<Block> ORES_IN_GROUND_STONE = common("ores_in_ground/stone");

        public static final TagKey<Block> GRAVELS = common("gravels");

        public static final TagKey<Block> SANDSTONE_BLOCKS = common("sandstone_blocks"); // todo
        public static final TagKey<Block> SANDSTONE_RED_BLOCKS = common("sandstone_red_blocks"); // todo

        public static final TagKey<Block> PLAYER_WORKSTATIONS_FURNACES = common("player_workstations/furnaces"); // todo
        public static final TagKey<Block> PLAYER_WORKSTATIONS_CRAFTING_TABLES = common("player_workstations/crafting_tables"); // todo

        public static final TagKey<Block> CHAINS = common("chains"); // todo

        public static final TagKey<Block> ROPES = common("ropes"); // todo

        public static final TagKey<Block> VILLAGER_JOB_SITES = common("villager_job_sites"); // todo

        public static final TagKey<Block> OBSIDIANS = common("obsidians"); // todo

        public static final TagKey<Block> STORAGE_BLOCKS = common("storage_blocks"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_NETHERITE = common("storage_blocks/netherite"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_DIAMOND = common("storage_blocks/diamond");
        public static final TagKey<Block> STORAGE_BLOCKS_COAL = common("storage_blocks/coal"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = common("storage_blocks/raw_copper"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_COPPER = common("storage_blocks/copper"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRON = common("storage_blocks/raw_iron"); // todo
        public static final TagKey<Block> STORAGE_BLOCKS_IRON = common("storage_blocks/iron"); // todo

        public static final TagKey<Block> CHESTS = common("chests"); // todo
        public static final TagKey<Block> CHESTS_WOODEN = common("chests/wooden"); // todo
        public static final TagKey<Block> CHESTS_TRAPPED = common("chests/trapped"); // todo

        public static final TagKey<Block> COBBLESTONES_NORMAL = common("cobblestones_normal"); // todo

        public static final TagKey<Block> DYED = common("dyed"); // todo
        public static final TagKey<Block> DYED_BLACK = common("dyed/black"); // todo
        public static final TagKey<Block> DYED_BLUE = common("dyed/blue"); // todo
        public static final TagKey<Block> DYED_BROWN = common("dyed/brown"); // todo
        public static final TagKey<Block> DYED_CYAN = common("dyed/cyan"); // todo
        public static final TagKey<Block> DYED_GRAY = common("dyed/gray"); // todo
        public static final TagKey<Block> DYED_GREEN = common("dyed/green"); // todo
        public static final TagKey<Block> DYED_LIGHT_BLUE = common("dyed/light_blue"); // todo
        public static final TagKey<Block> DYED_LIGHT_GRAY = common("dyed/light_gray"); // todo
        public static final TagKey<Block> DYED_LIME = common("dyed/lime"); // todo
        public static final TagKey<Block> DYED_MAGENTA = common("dyed/magenta"); // todo
        public static final TagKey<Block> DYED_ORANGE = common("dyed/orange"); // todo
        public static final TagKey<Block> DYED_PINK = common("dyed/pink"); // todo
        public static final TagKey<Block> DYED_PURPLE = common("dyed/purple"); // todo
        public static final TagKey<Block> DYED_RED = common("dyed/red"); // todo
        public static final TagKey<Block> DYED_WHITE = common("dyed/white"); // todo
        public static final TagKey<Block> DYED_YELLOW = common("dyed/yellow"); // todo

        public static final TagKey<Block> NEEDS_WOOD_TOOL = forge("needs_wood_tool"); // todo
        public static final TagKey<Block> NEEDS_GOLD_TOOL = forge("needs_gold_tool"); // todo
        public static final TagKey<Block> NEEDS_NETHERITE_TOOL = forge("needs_netherite_tool"); // todo

        public static final TagKey<Block> INCORRECT_FOR_WOODEN_TOOL = mc("incorrect_for_wooden_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = mc("incorrect_for_gold_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_STONE_TOOL = mc("incorrect_for_stone_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = mc("incorrect_for_iron_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_DIAMOND_TOOL = mc("incorrect_for_diamond_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_NETHERITE_TOOL = mc("incorrect_for_netherite_tool"); // todo

        public static final TagKey<Block> BUDS = common("buds"); // todo

        public static final TagKey<Block> SANDS = common("sands"); // todo

        public static final TagKey<Block> STONES = common("stones"); // todo

        public static final TagKey<Block> HIDDEN_FROM_RECIPE_VIEWERS = common("hidden_from_recipe_viewers"); // todo

        private static TagKey<Block> common(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<Block> mc(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name));
        }

        private static TagKey<Block> forge(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("forge", name));
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
        public static final TagKey<Biome> IS_CONIFEROUS_TREE = common("is_tree/coniferous");
        public static final TagKey<Biome> IS_SAVANNA_TREE = common("is_tree/savanna");
        public static final TagKey<Biome> IS_JUNGLE_TREE = common("is_tree/jungle");
        public static final TagKey<Biome> IS_DECIDUOUS_TREE = common("is_tree/deciduous");

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
        public static final TagKey<Biome> IS_STONY_SHORES = common("is_stony_shores");
        public static final TagKey<Biome> IS_BADLANDS = common("is_badlands"); // todo
        public static final TagKey<Biome> IS_OVERWORLD = common("is_overworld"); // todo
        public static final TagKey<Biome> IS_SWAMP = common("is_swamp"); // todo
        public static final TagKey<Biome> IS_BEACH = common("is_beach"); // todo
        public static final TagKey<Biome> IS_NETHER = common("is_nether"); // todo
        public static final TagKey<Biome> IS_FLOWER_FOREST = common("is_flower_forest"); // todo
        public static final TagKey<Biome> IS_COLD_OVERWORLD = common("is_cold_overworld"); // todo
        public static final TagKey<Biome> IS_MUSHROOM = common("is_mushroom"); // todo
        public static final TagKey<Biome> IS_FOREST = common("is_forest"); // todo
        public static final TagKey<Biome> IS_PLAINS = common("is_plains"); // todo
        public static final TagKey<Biome> IS_WINDSWEPT = common("is_windswept"); // todo
        public static final TagKey<Biome> IS_OLD_GROWTH = common("is_old_growth"); // todo
        public static final TagKey<Biome> IS_NETHER_FOREST = common("is_nether_forest"); // todo
        public static final TagKey<Biome> IS_BIRCH_FOREST = common("is_birch_forest"); // todo

        private static TagKey<Biome> common(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
