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

        public static final TagKey<Item> STONES = common("stones");

        public static final TagKey<Item> FERTILIZERS = common("fertilizers");

        public static final TagKey<Item> FOODS = common("foods");
        public static final TagKey<Item> FOODS_FRUIT = common("foods/fruit");
        public static final TagKey<Item> FOODS_VEGETABLE = common("foods/vegetable");
        public static final TagKey<Item> FOODS_BERRY = common("foods/berry");
        public static final TagKey<Item> FOODS_BREAD = common("foods/bread");
        public static final TagKey<Item> FOODS_COOKIE = common("foods/cookie");
        public static final TagKey<Item> FOODS_DOUGH = common("foods/dough");
        public static final TagKey<Item> FOODS_RAW_MEAT = common("foods/raw_meat");
        public static final TagKey<Item> FOODS_COOKED_MEAT = common("foods/cooked_meat");
        public static final TagKey<Item> FOODS_RAW_FISH = common("foods/raw_fish");
        public static final TagKey<Item> FOODS_COOKED_FISH = common("foods/cooked_fish");
        public static final TagKey<Item> FOODS_SOUP = common("foods/soup");
        public static final TagKey<Item> FOODS_CANDY = common("foods/candy");
        public static final TagKey<Item> FOODS_PIE = common("foods/pie");
        public static final TagKey<Item> FOODS_GOLDEN = common("foods/golden");
        public static final TagKey<Item> FOODS_EDIBLE_WHEN_PLACED = common("foods/edible_when_placed");
        public static final TagKey<Item> FOODS_FOOD_POISONING = common("foods/food_poisoning");
        public static final TagKey<Item> ANIMAL_FOODS = common("animal_foods");
        public static final TagKey<Item> ARMADILLO_FOOD = mc("armadilld_food");
        public static final TagKey<Item> AXOLOTL_FOOD = mc("axolotl_food");
        public static final TagKey<Item> BEE_FOOD = mc("bee_food");
        public static final TagKey<Item> CAMEL_FOOD = mc("camel_food");
        public static final TagKey<Item> CAT_FOOD = mc("cat_food");
        public static final TagKey<Item> GOAT_FOOD = mc("goat_food");
        public static final TagKey<Item> LLAMA_FOOD = mc("llama_food");
        public static final TagKey<Item> PARROT_FOOD = mc("parrot_food");
        public static final TagKey<Item> SHEEP_FOOD = mc("sheep_food");
        public static final TagKey<Item> CHICKEN_FOOD = mc("sheep_food");
        public static final TagKey<Item> COW_FOOD = mc("sheep_food");
        public static final TagKey<Item> FROG_FOOD = mc("sheep_food");
        public static final TagKey<Item> HOGLIN_FOOD = mc("sheep_food");
        public static final TagKey<Item> HORSE_FOOD = mc("sheep_food");
        public static final TagKey<Item> HORSE_TEMPT_ITEMS = mc("horse_tempt_items");
        public static final TagKey<Item> OCELOT_FOOD = mc("sheep_food");
        public static final TagKey<Item> PANDA_FOOD = mc("sheep_food");
        public static final TagKey<Item> PIG_FOOD = mc("sheep_food");
        public static final TagKey<Item> RABBIT_FOOD = mc("sheep_food");
        public static final TagKey<Item> STRIDER_FOOD = mc("sheep_food");
        public static final TagKey<Item> STRIDER_TEMPT_ITEMS = mc("strider_tempt_items");
        public static final TagKey<Item> TURTLE_FOOD = mc("sheep_food");
        public static final TagKey<Item> WOLF_FOOD = mc("sheep_food");

        public static final TagKey<Item> SANDS = common("sands");
        public static final TagKey<Item> SANDS_COLORLESS = common("sands/colorless");
        public static final TagKey<Item> SANDS_RED = common("sands/red");

        public static final TagKey<Item> BONES = common("bones");

        public static final TagKey<Item> BRICKS = common("bricks");
        public static final TagKey<Item> BRICKS_NORMAL = common("bricks/normal");
        public static final TagKey<Item> BRICKS_NETHER = common("bricks/nether");

        public static final TagKey<Item> BUCKETS = common("buckets");
        public static final TagKey<Item> BUCKETS_EMPTY = common("buckets/empty");
        public static final TagKey<Item> BUCKETS_WATER = common("buckets/water");
        public static final TagKey<Item> BUCKETS_LAVA = common("buckets/lava");
        public static final TagKey<Item> BUCKETS_MILK = common("buckets/milk");
        public static final TagKey<Item> BUCKETS_POWDER_SNOW = common("buckets/powder_snow");
        public static final TagKey<Item> BUCKETS_ENTITY_WATER = common("buckets/entity_water");

        public static final TagKey<Item> CHAINS = common("chains");

        public static final TagKey<Item> BUDS = common("buds");

        public static final TagKey<Item> CHESTS = common("chests");
        public static final TagKey<Item> CHESTS_ENDER = common("chests/ender");
        public static final TagKey<Item> CHESTS_TRAPPED = common("chests/trapped");
        public static final TagKey<Item> CHESTS_WOODEN = common("chests/wooden");

        public static final TagKey<Item> CROPS = common("crops");
        public static final TagKey<Item> CROPS_BEETROOT = common("crops/beetroot");
        public static final TagKey<Item> CROPS_CACTUS = common("crops/cactus");
        public static final TagKey<Item> CROPS_CARROT = common("crops/carrot");
        public static final TagKey<Item> CROPS_COCOA_BEAN = common("crops/cocoa_bean");
        public static final TagKey<Item> CROPS_MELON = common("crops/melon");
        public static final TagKey<Item> CROPS_NETHER_WART = common("crops/nether_wart");
        public static final TagKey<Item> CROPS_POTATO = common("crops/potato");
        public static final TagKey<Item> CROPS_PUMPKIN = common("crops/pumpkin");
        public static final TagKey<Item> CROPS_SUGAR_CANE = common("crops/sugar_cane");
        public static final TagKey<Item> CROPS_WHEAT = common("crops/wheat");

        public static final TagKey<Item> DUSTS = common("dusts");
        public static final TagKey<Item> DUSTS_REDSTONE = common("dusts/redstone");
        public static final TagKey<Item> DUSTS_GLOWSTONE = common("dusts/glowstone");

        public static final TagKey<Item> DYED = common("dyed");
        public static final TagKey<Item> DYED_BLACK = common("dyed/black");
        public static final TagKey<Item> DYED_BLUE = common("dyed/blue");
        public static final TagKey<Item> DYED_BROWN = common("dyed/brown");
        public static final TagKey<Item> DYED_CYAN = common("dyed/cyan");
        public static final TagKey<Item> DYED_GRAY = common("dyed/gray");
        public static final TagKey<Item> DYED_GREEN = common("dyed/green");
        public static final TagKey<Item> DYED_LIGHT_BLUE = common("dyed/light_blue");
        public static final TagKey<Item> DYED_LIGHT_GRAY = common("dyed/light_gray");
        public static final TagKey<Item> DYED_LIME = common("dyed/lime");
        public static final TagKey<Item> DYED_MAGENTA = common("dyed/magenta");
        public static final TagKey<Item> DYED_ORANGE = common("dyed/orange");
        public static final TagKey<Item> DYED_PINK = common("dyed/pink");
        public static final TagKey<Item> DYED_PURPLE = common("dyed/purple");
        public static final TagKey<Item> DYED_RED = common("dyed/red");
        public static final TagKey<Item> DYED_WHITE = common("dyed/white");
        public static final TagKey<Item> DYED_YELLOW = common("dyed/yellow");

        public static final TagKey<Item> FENCE_GATES = common("fence_gates");
        public static final TagKey<Item> FENCE_GATES_WOODEN = common("fence_gates/wooden");

        public static final TagKey<Item> GEMS = common("gems");
        public static final TagKey<Item> GEMS_DIAMOND = common("gems/diamond");
        public static final TagKey<Item> GEMS_EMERALD = common("gems/emerald");
        public static final TagKey<Item> GEMS_AMETHYST = common("gems/amethyst");
        public static final TagKey<Item> GEMS_LAPIS = common("gems/lapis");
        public static final TagKey<Item> GEMS_PRISMARINE = common("gems/prismarine");
        public static final TagKey<Item> GEMS_QUARTZ = common("gems/quartz");

        public static final TagKey<Item> INGOTS = common("ingots");
        public static final TagKey<Item> INGOTS_COPPER = common("ingots/copper");
        public static final TagKey<Item> INGOTS_GOLD = common("ingots/gold");
        public static final TagKey<Item> INGOTS_IRON = common("ingots/iron");
        public static final TagKey<Item> INGOTS_NETHERITE = common("ingots/netherite");

        public static final TagKey<Item> MUSHROOMS = common("mushrooms");

        public static final TagKey<Item> NUGGETS = common("nuggets");
        public static final TagKey<Item> NUGGETS_GOLD = common("nuggets/gold");
        public static final TagKey<Item> NUGGETS_IRON = common("nuggets/iron");

        public static final TagKey<Item> ORE_BEARING_GROUND_DEEPSLATE = common("ore_bearing_ground/deepslate");
        public static final TagKey<Item> ORE_BEARING_GROUND_NETHERRACK = common("ore_bearing_ground/netherrack");
        public static final TagKey<Item> ORE_BEARING_GROUND_STONE = common("ore_bearing_ground/stone");
        public static final TagKey<Item> ORE_RATES_DENSE = common("ore_rates/dense");
        public static final TagKey<Item> ORE_RATES_SINGULAR = common("ore_rates/singular");
        public static final TagKey<Item> ORE_RATES_SPARSE = common("ore_rates/sparse");
        public static final TagKey<Item> ORES = common("ores");
        public static final TagKey<Item> ORES_COAL = common("ores/coal");
        public static final TagKey<Item> ORES_COPPER = common("ores/copper");
        public static final TagKey<Item> ORES_DIAMOND = common("ores/diamond");
        public static final TagKey<Item> ORES_EMERALD = common("ores/emerald");
        public static final TagKey<Item> ORES_GOLD = common("ores/gold");
        public static final TagKey<Item> ORES_IRON = common("ores/iron");
        public static final TagKey<Item> ORES_LAPIS = common("ores/lapis");
        public static final TagKey<Item> ORES_NETHERITE_SCRAP = common("ores/netherite_scrap");
        public static final TagKey<Item> ORES_QUARTZ = common("ores/quartz");
        public static final TagKey<Item> ORES_REDSTONE = common("ores/redstone");
        public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = common("ores_in_ground/deepslate");
        public static final TagKey<Item> ORES_IN_GROUND_NETHERRACK = common("ores_in_ground/netherrack");
        public static final TagKey<Item> ORES_IN_GROUND_STONE = common("ores_in_ground/stone");

        public static final TagKey<Item> PLAYER_WORKSTATIONS_CRAFTING_TABLES = common("player_workstations/crafting_tables");
        public static final TagKey<Item> PLAYER_WORKSTATIONS_FURNACES = common("player_workstations/furnaces");

        public static final TagKey<Item> POTIONS = common("potions");
        public static final TagKey<Item> POTION_BOTTLE = common("potion_bottle");

        public static final TagKey<Item> RAW_MATERIALS = common("raw_materials");
        public static final TagKey<Item> RAW_MATERIALS_COPPER = common("raw_materials/copper");
        public static final TagKey<Item> RAW_MATERIALS_GOLD = common("raw_materials/gold");
        public static final TagKey<Item> RAW_MATERIALS_IRON = common("raw_materials/iron");

        public static final TagKey<Item> ROPES = common("ropes");

        public static final TagKey<Item> SANDSTONE_BLOCKS = common("sandstone/blocks");
        public static final TagKey<Item> SANDSTONE_SLABS = common("sandstone/slabs");
        public static final TagKey<Item> SANDSTONE_STAIRS = common("sandstone/stairs");
        public static final TagKey<Item> SANDSTONE_RED_BLOCKS = common("sandstone/red_blocks");
        public static final TagKey<Item> SANDSTONE_RED_SLABS = common("sandstone/red_slabs");
        public static final TagKey<Item> SANDSTONE_RED_STAIRS = common("sandstone/red_stairs");
        public static final TagKey<Item> SANDSTONE_UNCOLORED_BLOCKS = common("sandstone/uncolored_blocks");
        public static final TagKey<Item> SANDSTONE_UNCOLORED_SLABS = common("sandstone/uncolored_slabs");
        public static final TagKey<Item> SANDSTONE_UNCOLORED_STAIRS = common("sandstone/uncolored_stairs");

        public static final TagKey<Item> STORAGE_BLOCKS = common("storage_blocks");
        public static final TagKey<Item> STORAGE_BLOCKS_BONE_MEAL = common("storage_blocks/bone_meal");
        public static final TagKey<Item> STORAGE_BLOCKS_COAL = common("storage_blocks/coal");
        public static final TagKey<Item> STORAGE_BLOCKS_COPPER = common("storage_blocks/copper");
        public static final TagKey<Item> STORAGE_BLOCKS_DIAMOND = common("storage_blocks/diamond");
        public static final TagKey<Item> STORAGE_BLOCKS_DRIED_KELP = common("storage_blocks/dried_kelp");
        public static final TagKey<Item> STORAGE_BLOCKS_EMERALD = common("storage_blocks/emerald");
        public static final TagKey<Item> STORAGE_BLOCKS_GOLD = common("storage_blocks/gold");
        public static final TagKey<Item> STORAGE_BLOCKS_IRON = common("storage_blocks/iron");
        public static final TagKey<Item> STORAGE_BLOCKS_LAPIS = common("storage_blocks/lapis");
        public static final TagKey<Item> STORAGE_BLOCKS_NETHERITE = common("storage_blocks/netherite");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_COPPER = common("storage_blocks/raw_copper");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_GOLD = common("storage_blocks/raw_gold");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_IRON = common("storage_blocks/raw_iron");
        public static final TagKey<Item> STORAGE_BLOCKS_REDSTONE = common("storage_blocks/redstone");
        public static final TagKey<Item> STORAGE_BLOCKS_SLIME = common("storage_blocks/slime");
        public static final TagKey<Item> STORAGE_BLOCKS_WHEAT = common("storage_blocks/wheat");

        public static final TagKey<Item> STRIPPED_LOGS = common("stripped_logs");

        public static final TagKey<Item> VILLAGER_JOB_SITES = common("villager_job_sites");


        public static final TagKey<Item> ARMORS = common("armors");
        public static final TagKey<Item> HEAD_ARMOR = mc("head_armor");
        public static final TagKey<Item> CHEST_ARMOR = mc("chest_armor");
        public static final TagKey<Item> LEG_ARMOR = mc("leg_armor");
        public static final TagKey<Item> FOOT_ARMOR = mc("foot_armor");

        public static final TagKey<Item> FOOT_ARMOR_ENCHANTABLE = mc("enchantable/foot_armor");
        public static final TagKey<Item> LEG_ARMOR_ENCHANTABLE = mc("enchantable/leg_armor");
        public static final TagKey<Item> CHEST_ARMOR_ENCHANTABLE = mc("enchantable/chest_armor");
        public static final TagKey<Item> HEAD_ARMOR_ENCHANTABLE = mc("enchantable/head_armor");
        public static final TagKey<Item> ARMOR_ENCHANTABLE = mc("enchantable/armor");
        public static final TagKey<Item> SWORD_ENCHANTABLE = mc("enchantable/sword");
        public static final TagKey<Item> FIRE_ASPECT_ENCHANTABLE = mc("enchantable/fire_aspect");
        public static final TagKey<Item> SHARP_WEAPON_ENCHANTABLE = mc("enchantable/sharp_weapon");
        public static final TagKey<Item> WEAPON_ENCHANTABLE = mc("enchantable/weapon");
        public static final TagKey<Item> MINING_ENCHANTABLE = mc("enchantable/mining");
        public static final TagKey<Item> MINING_LOOT_ENCHANTABLE = mc("enchantable/mining_loot");
        public static final TagKey<Item> FISHING_ENCHANTABLE = mc("enchantable/fishing");
        public static final TagKey<Item> TRIDENT_ENCHANTABLE = mc("enchantable/trident");
        public static final TagKey<Item> DURABILITY_ENCHANTABLE = mc("enchantable/durability");
        public static final TagKey<Item> BOW_ENCHANTABLE = mc("enchantable/bow");
        public static final TagKey<Item> EQUIPPABLE_ENCHANTABLE = mc("enchantable/equippable");
        public static final TagKey<Item> CROSSBOW_ENCHANTABLE = mc("enchantable/crossbow");
        public static final TagKey<Item> VANISHING_ENCHANTABLE = mc("enchantable/vanishing");
//        public static final TagKey<Item> MACE_ENCHANTABLE = mc("mace_enchantable");

        public static final TagKey<Item> MEAT = mc("meat");
        public static final TagKey<Item> LLAMA_TEMPT_ITEMS = mc("llama_tempt_items");
        public static final TagKey<Item> PARROT_POISONOUS_FOOD = mc("parrot_poisonous_food");

        public static final TagKey<Item> OBSIDIANS = common("obsidians");
        public static final TagKey<Item> OBSIDIANS_NORMAL = common("obsidians/normal");
        public static final TagKey<Item> OBSIDIANS_CRYING = common("obsidians/crying");

        public static final TagKey<Item> COBBLESTONES = common("cobblestones");
        public static final TagKey<Item> COBBLESTONES_NORMAL = common("cobblestones/normal");
        public static final TagKey<Item> COBBLESTONES_INFESTED = common("cobblestones/infested");
        public static final TagKey<Item> COBBLESTONES_MOSSY = common("cobblestones/mossy");
        public static final TagKey<Item> COBBLESTONES_DEEPSLATE = common("cobblestones/deepslate");

        public static final TagKey<Item> SKULLS = mc("skulls");

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

        public static final TagKey<Block> SANDSTONE_BLOCKS = common("sandstone/blocks");
        public static final TagKey<Block> SANDSTONE_SLABS = common("sandstone/slabs");
        public static final TagKey<Block> SANDSTONE_STAIRS = common("sandstone/stairs");
        public static final TagKey<Block> SANDSTONE_RED_BLOCKS = common("sandstone/red_blocks");
        public static final TagKey<Block> SANDSTONE_RED_SLABS = common("sandstone/red_slabs");
        public static final TagKey<Block> SANDSTONE_RED_STAIRS = common("sandstone/red_stairs");
        public static final TagKey<Block> SANDSTONE_UNCOLORED_BLOCKS = common("sandstone/uncolored_blocks");
        public static final TagKey<Block> SANDSTONE_UNCOLORED_SLABS = common("sandstone/uncolored_slabs");
        public static final TagKey<Block> SANDSTONE_UNCOLORED_STAIRS = common("sandstone/uncolored_stairs");

        public static final TagKey<Block> PLAYER_WORKSTATIONS_FURNACES = common("player_workstations/furnaces");
        public static final TagKey<Block> PLAYER_WORKSTATIONS_CRAFTING_TABLES = common("player_workstations/crafting_tables");

        public static final TagKey<Block> ROPES = common("ropes");

        public static final TagKey<Block> VILLAGER_JOB_SITES = common("villager_job_sites");

        public static final TagKey<Block> OBSIDIANS = common("obsidians");
        public static final TagKey<Block> OBSIDIANS_NORMAL = common("obsidians/normal");
        public static final TagKey<Block> OBSIDIANS_CRYING = common("obsidians/crying");

        public static final TagKey<Block> STORAGE_BLOCKS = common("storage_blocks");
        public static final TagKey<Block> STORAGE_BLOCKS_BONE_MEAL = common("storage_blocks/bone_meal");
        public static final TagKey<Block> STORAGE_BLOCKS_COAL = common("storage_blocks/coal");
        public static final TagKey<Block> STORAGE_BLOCKS_COPPER = common("storage_blocks/copper");
        public static final TagKey<Block> STORAGE_BLOCKS_DIAMOND = common("storage_blocks/diamond");
        public static final TagKey<Block> STORAGE_BLOCKS_DRIED_KELP = common("storage_blocks/dried_kelp");
        public static final TagKey<Block> STORAGE_BLOCKS_EMERALD = common("storage_blocks/emerald");
        public static final TagKey<Block> STORAGE_BLOCKS_GOLD = common("storage_blocks/gold");
        public static final TagKey<Block> STORAGE_BLOCKS_IRON = common("storage_blocks/iron");
        public static final TagKey<Block> STORAGE_BLOCKS_LAPIS = common("storage_blocks/lapis");
        public static final TagKey<Block> STORAGE_BLOCKS_NETHERITE = common("storage_blocks/netherite");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = common("storage_blocks/raw_copper");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = common("storage_blocks/raw_gold");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRON = common("storage_blocks/raw_iron");
        public static final TagKey<Block> STORAGE_BLOCKS_REDSTONE = common("storage_blocks/redstone");
        public static final TagKey<Block> STORAGE_BLOCKS_SLIME = common("storage_blocks/slime");
        public static final TagKey<Block> STORAGE_BLOCKS_WHEAT = common("storage_blocks/wheat");

        public static final TagKey<Block> COBBLESTONES = common("cobblestones");
        public static final TagKey<Block> COBBLESTONES_NORMAL = common("cobblestones/normal");
        public static final TagKey<Block> COBBLESTONES_INFESTED = common("cobblestones/infested");
        public static final TagKey<Block> COBBLESTONES_MOSSY = common("cobblestones/mossy");
        public static final TagKey<Block> COBBLESTONES_DEEPSLATE = common("cobblestones/deepslate");

        public static final TagKey<Block> DYED = common("dyed");
        public static final TagKey<Block> DYED_BLACK = common("dyed/black");
        public static final TagKey<Block> DYED_BLUE = common("dyed/blue");
        public static final TagKey<Block> DYED_BROWN = common("dyed/brown");
        public static final TagKey<Block> DYED_CYAN = common("dyed/cyan");
        public static final TagKey<Block> DYED_GRAY = common("dyed/gray");
        public static final TagKey<Block> DYED_GREEN = common("dyed/green");
        public static final TagKey<Block> DYED_LIGHT_BLUE = common("dyed/light_blue");
        public static final TagKey<Block> DYED_LIGHT_GRAY = common("dyed/light_gray");
        public static final TagKey<Block> DYED_LIME = common("dyed/lime");
        public static final TagKey<Block> DYED_MAGENTA = common("dyed/magenta");
        public static final TagKey<Block> DYED_ORANGE = common("dyed/orange");
        public static final TagKey<Block> DYED_PINK = common("dyed/pink");
        public static final TagKey<Block> DYED_PURPLE = common("dyed/purple");
        public static final TagKey<Block> DYED_RED = common("dyed/red");
        public static final TagKey<Block> DYED_WHITE = common("dyed/white");
        public static final TagKey<Block> DYED_YELLOW = common("dyed/yellow");

        public static final TagKey<Block> NEEDS_WOOD_TOOL = forge("needs_wood_tool"); // todo
        public static final TagKey<Block> NEEDS_GOLD_TOOL = forge("needs_gold_tool"); // todo
        public static final TagKey<Block> NEEDS_NETHERITE_TOOL = forge("needs_netherite_tool"); // todo

        public static final TagKey<Block> INCORRECT_FOR_WOODEN_TOOL = mc("incorrect_for_wooden_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_GOLD_TOOL = mc("incorrect_for_gold_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_STONE_TOOL = mc("incorrect_for_stone_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_IRON_TOOL = mc("incorrect_for_iron_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_DIAMOND_TOOL = mc("incorrect_for_diamond_tool"); // todo
        public static final TagKey<Block> INCORRECT_FOR_NETHERITE_TOOL = mc("incorrect_for_netherite_tool"); // todo

        public static final TagKey<Block> BUDS = common("buds");

        public static final TagKey<Block> CHAINS = common("chains");

        public static final TagKey<Block> CHESTS = common("chests");
        public static final TagKey<Block> CHESTS_ENDER = common("chests/ender");
        public static final TagKey<Block> CHESTS_TRAPPED = common("chests/trapped");
        public static final TagKey<Block> CHESTS_WOODEN = common("chests/wooden");

        public static final TagKey<Block> SANDS = common("sands");
        public static final TagKey<Block> SANDS_COLORLESS = common("sands/colorless");
        public static final TagKey<Block> SANDS_RED = common("sands/red");

        public static final TagKey<Block> STONES = common("stones");

        public static final TagKey<Block> HIDDEN_FROM_RECIPE_VIEWERS = common("hidden_from_recipe_viewers");

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
        public static final TagKey<Biome> IS_BADLANDS = common("is_badlands");
        public static final TagKey<Biome> IS_OVERWORLD = common("is_overworld");
        public static final TagKey<Biome> IS_SWAMP = common("is_swamp");
        public static final TagKey<Biome> IS_BEACH = common("is_beach");
        public static final TagKey<Biome> IS_NETHER = common("is_nether");
        public static final TagKey<Biome> IS_FLOWER_FOREST = common("is_flower_forest");
        public static final TagKey<Biome> IS_COLD_OVERWORLD = common("is_cold_overworld");
        public static final TagKey<Biome> IS_MUSHROOM = common("is_mushroom");
        public static final TagKey<Biome> IS_FOREST = common("is_forest");
        public static final TagKey<Biome> IS_PLAINS = common("is_plains");
        public static final TagKey<Biome> IS_WINDSWEPT = common("is_windswept");
        public static final TagKey<Biome> IS_OLD_GROWTH = common("is_old_growth");
        public static final TagKey<Biome> IS_NETHER_FOREST = common("is_nether_forest");
        public static final TagKey<Biome> IS_BIRCH_FOREST = common("is_birch_forest");

        private static TagKey<Biome> common(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
