package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortItemTagsProvider extends ItemTagsProvider {
    protected static final String[] DYED_IDS = new String[]{
            "_banner",
            "_bed",
            "_candle",
            "_carpet",
            "_concrete",
            "_concrete_powder",
            "_glazed_terracotta",
            "_shulker_box",
            "_stained_glass",
            "_stained_glass_pane",
            "_terracotta",
            "_wool",
    };

    public PortItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PortLib.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.TOOLS_SHIELDS, PortTags.Items.TOOLS_SHIELD).add(Items.SHIELD);
        tag(Tags.Items.TOOLS_BOWS, PortTags.Items.TOOLS_BOW).add(Items.BOW);
        tag(PortTags.Items.TOOLS_BRUSH).add(Items.BRUSH);
        tag(Tags.Items.TOOLS_CROSSBOWS, PortTags.Items.TOOLS_CROSSBOW).add(Items.CROSSBOW);
        tag(Tags.Items.TOOLS_FISHING_RODS, PortTags.Items.TOOLS_FISHING_ROD).add(Items.FISHING_ROD);
        tag(PortTags.Items.TOOLS_SHEAR).add(Items.SHEARS);
        tag(Tags.Items.TOOLS_TRIDENTS, PortTags.Items.TOOLS_SPEAR).add(Items.TRIDENT);
        tag(PortTags.Items.TOOLS_IGNITER).add(Items.FLINT_AND_STEEL);
        tag(PortTags.Items.TOOLS_WRENCH);
        tag(PortTags.Items.MINING_TOOL_TOOLS).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.GOLDEN_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE);
        tag(PortTags.Items.MELEE_WEAPON_TOOLS).add(
                Items.TRIDENT,
                Items.WOODEN_SWORD, Items.STONE_SWORD, Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
                Items.WOODEN_AXE, Items.STONE_AXE, Items.GOLDEN_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);
        tag(PortTags.Items.RANGED_WEAPON_TOOLS).add(Items.BOW, Items.CROSSBOW, Items.TRIDENT);
        tag(Tags.Items.TOOLS, PortTags.Items.TOOLS)
                .addTags(ItemTags.AXES, ItemTags.HOES, ItemTags.PICKAXES, ItemTags.SHOVELS, ItemTags.SWORDS)
                .addTags(PortTags.Items.TOOLS_BOW, PortTags.Items.TOOLS_BRUSH, PortTags.Items.TOOLS_CROSSBOW, PortTags.Items.TOOLS_FISHING_ROD,
                        PortTags.Items.TOOLS_IGNITER, PortTags.Items.TOOLS_SHEAR, PortTags.Items.TOOLS_SHIELD, PortTags.Items.TOOLS_SPEAR,
                        PortTags.Items.TOOLS_WRENCH,
                        PortTags.Items.MINING_TOOL_TOOLS, PortTags.Items.MELEE_WEAPON_TOOLS, PortTags.Items.RANGED_WEAPON_TOOLS);
        tag(PortTags.Items.FOOT_ARMOR).add(
                Items.LEATHER_BOOTS,
                Items.CHAINMAIL_BOOTS,
                Items.GOLDEN_BOOTS,
                Items.IRON_BOOTS,
                Items.DIAMOND_BOOTS,
                Items.NETHERITE_BOOTS
        );
        tag(PortTags.Items.LEG_ARMOR).add(
                Items.LEATHER_LEGGINGS,
                Items.CHAINMAIL_LEGGINGS,
                Items.GOLDEN_LEGGINGS,
                Items.IRON_LEGGINGS,
                Items.DIAMOND_LEGGINGS,
                Items.NETHERITE_LEGGINGS
        );
        tag(PortTags.Items.CHEST_ARMOR).add(
                Items.LEATHER_CHESTPLATE,
                Items.CHAINMAIL_CHESTPLATE,
                Items.GOLDEN_CHESTPLATE,
                Items.IRON_CHESTPLATE,
                Items.DIAMOND_CHESTPLATE,
                Items.NETHERITE_CHESTPLATE
        );
        tag(PortTags.Items.HEAD_ARMOR).add(
                Items.LEATHER_HELMET,
                Items.CHAINMAIL_HELMET,
                Items.GOLDEN_HELMET,
                Items.IRON_HELMET,
                Items.DIAMOND_HELMET,
                Items.NETHERITE_HELMET,
                Items.TURTLE_HELMET
        );
        tag(Tags.Items.ARMORS, PortTags.Items.ARMORS).addTags(PortTags.Items.HEAD_ARMOR, PortTags.Items.CHEST_ARMOR, PortTags.Items.LEG_ARMOR, PortTags.Items.FOOT_ARMOR);

        tag(Tags.Items.SEEDS, PortTags.Items.SEEDS).addTags(Tags.Items.SEEDS_BEETROOT, Tags.Items.SEEDS_MELON, Tags.Items.SEEDS_PUMPKIN, PortTags.Items.SEEDS_TORCHFLOWER, Tags.Items.SEEDS_WHEAT);
        tag(Tags.Items.SEEDS_BEETROOT, PortTags.Items.SEEDS_BEETROOT).add(Items.BEETROOT_SEEDS);
        tag(Tags.Items.SEEDS_MELON, PortTags.Items.SEEDS_MELON).add(Items.MELON_SEEDS);
        tag(Tags.Items.SEEDS_PUMPKIN, PortTags.Items.SEEDS_PUMPKIN).add(Items.PUMPKIN_SEEDS);
        tag(PortTags.Items.SEEDS_TORCHFLOWER).add(Items.TORCHFLOWER_SEEDS);
        tag(Tags.Items.SEEDS_WHEAT, PortTags.Items.SEEDS_WHEAT).add(Items.WHEAT_SEEDS);

        copy(PortTags.Blocks.GLASS_BLOCKS, PortTags.Items.GLASS_BLOCKS);
        copy(PortTags.Blocks.GLASS_BLOCKS_COLORLESS, PortTags.Items.GLASS_BLOCKS_COLORLESS);
        copy(PortTags.Blocks.GLASS_BLOCKS_TINTED, PortTags.Items.GLASS_BLOCKS_TINTED);
        copy(PortTags.Blocks.GLASS_BLOCKS_CHEAP, PortTags.Items.GLASS_BLOCKS_CHEAP);

        tag(PortTags.Items.MUSIC_DISCS).add(Items.MUSIC_DISC_13, Items.MUSIC_DISC_CAT, Items.MUSIC_DISC_BLOCKS, Items.MUSIC_DISC_CHIRP,
                Items.MUSIC_DISC_FAR, Items.MUSIC_DISC_MALL, Items.MUSIC_DISC_MELLOHI, Items.MUSIC_DISC_STAL, Items.MUSIC_DISC_STRAD,
                Items.MUSIC_DISC_WARD, Items.MUSIC_DISC_11, Items.MUSIC_DISC_WAIT, Items.MUSIC_DISC_OTHERSIDE, Items.MUSIC_DISC_5,
                Items.MUSIC_DISC_PIGSTEP, Items.MUSIC_DISC_RELIC);

        copy(PortTags.Blocks.GRAVELS, PortTags.Items.GRAVELS);
        copy(PortTags.Blocks.STONES, PortTags.Items.STONES);

        tag(PortTags.Items.FERTILIZERS).add(Items.BONE_MEAL);
        tag(PortTags.Items.FOODS_FRUIT).add(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.CHORUS_FRUIT, Items.MELON_SLICE);
        tag(PortTags.Items.FOODS_VEGETABLE).add(Items.CARROT, Items.GOLDEN_CARROT, Items.POTATO, Items.BEETROOT);
        tag(PortTags.Items.FOODS_BERRY).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
        tag(PortTags.Items.FOODS_BREAD).add(Items.BREAD);
        tag(PortTags.Items.FOODS_COOKIE).add(Items.COOKIE);
        tag(PortTags.Items.FOODS_DOUGH);
        tag(PortTags.Items.FOODS_RAW_MEAT).add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT, Items.MUTTON);
        tag(PortTags.Items.FOODS_RAW_FISH).add(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);
        tag(PortTags.Items.FOODS_COOKED_MEAT).add(Items.COOKED_BEEF, Items.COOKED_PORKCHOP, Items.COOKED_CHICKEN, Items.COOKED_RABBIT, Items.COOKED_MUTTON);
        tag(PortTags.Items.FOODS_COOKED_FISH).add(Items.COOKED_COD, Items.COOKED_SALMON);
        tag(PortTags.Items.FOODS_SOUP).add(Items.BEETROOT_SOUP, Items.MUSHROOM_STEW, Items.RABBIT_STEW, Items.SUSPICIOUS_STEW);
        tag(PortTags.Items.FOODS_CANDY);
        tag(PortTags.Items.FOODS_PIE).add(Items.PUMPKIN_PIE);
        tag(PortTags.Items.FOODS_GOLDEN).add(Items.GOLDEN_APPLE).add(Items.ENCHANTED_GOLDEN_APPLE).add(Items.GOLDEN_CARROT);
        tag(PortTags.Items.FOODS_EDIBLE_WHEN_PLACED).add(Items.CAKE);
        tag(PortTags.Items.FOODS_FOOD_POISONING).add(Items.POISONOUS_POTATO, Items.PUFFERFISH, Items.SPIDER_EYE, Items.CHICKEN, Items.ROTTEN_FLESH);
        tag(PortTags.Items.FOODS)
                .add(Items.BAKED_POTATO, Items.PUMPKIN_PIE, Items.HONEY_BOTTLE/*, Items.OMINOUS_BOTTLE*/, Items.DRIED_KELP)
                .addTags(PortTags.Items.FOODS_FRUIT, PortTags.Items.FOODS_VEGETABLE, PortTags.Items.FOODS_BERRY, PortTags.Items.FOODS_BREAD,
                        PortTags.Items.FOODS_COOKIE, PortTags.Items.FOODS_DOUGH, PortTags.Items.FOODS_RAW_MEAT, PortTags.Items.FOODS_RAW_FISH,
                        PortTags.Items.FOODS_COOKED_MEAT, PortTags.Items.FOODS_COOKED_FISH, PortTags.Items.FOODS_SOUP, PortTags.Items.FOODS_CANDY,
                        PortTags.Items.FOODS_PIE, PortTags.Items.FOODS_GOLDEN, PortTags.Items.FOODS_EDIBLE_WHEN_PLACED, PortTags.Items.FOODS_FOOD_POISONING);

        tag(PortTags.Items.WOLF_FOOD).addTag(PortTags.Items.MEAT);
        tag(PortTags.Items.OCELOT_FOOD).add(Items.COD, Items.SALMON);
        tag(PortTags.Items.CAT_FOOD).add(Items.COD, Items.SALMON);
        tag(PortTags.Items.HORSE_FOOD)
                .add(Items.WHEAT, Items.SUGAR, Items.HAY_BLOCK, Items.APPLE, Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        tag(PortTags.Items.HORSE_TEMPT_ITEMS).add(Items.GOLDEN_CARROT, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE);
        tag(PortTags.Items.CAMEL_FOOD).add(Items.CACTUS);
        tag(PortTags.Items.ARMADILLO_FOOD).add(Items.SPIDER_EYE);
        tag(PortTags.Items.BEE_FOOD).addTag(ItemTags.FLOWERS);
        tag(PortTags.Items.CHICKEN_FOOD)
                .add(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
        tag(PortTags.Items.FROG_FOOD).add(Items.SLIME_BALL);
        tag(PortTags.Items.HOGLIN_FOOD).add(Items.CRIMSON_FUNGUS);
        tag(PortTags.Items.LLAMA_FOOD).add(Items.WHEAT, Items.HAY_BLOCK);
        tag(PortTags.Items.LLAMA_TEMPT_ITEMS).add(Items.HAY_BLOCK);
        tag(PortTags.Items.PANDA_FOOD).add(Items.BAMBOO);
        tag(PortTags.Items.PIG_FOOD).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
        tag(PortTags.Items.RABBIT_FOOD).add(Items.CARROT, Items.GOLDEN_CARROT, Items.DANDELION);
        tag(PortTags.Items.STRIDER_FOOD).add(Items.WARPED_FUNGUS);
        tag(PortTags.Items.STRIDER_TEMPT_ITEMS).addTag(PortTags.Items.STRIDER_FOOD).add(Items.WARPED_FUNGUS_ON_A_STICK);
        tag(PortTags.Items.TURTLE_FOOD).add(Items.SEAGRASS);
        tag(PortTags.Items.PARROT_FOOD)
                .add(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
        tag(PortTags.Items.PARROT_POISONOUS_FOOD).add(Items.COOKIE);
        tag(PortTags.Items.COW_FOOD).add(Items.WHEAT);
        tag(PortTags.Items.SHEEP_FOOD).add(Items.WHEAT);
        tag(PortTags.Items.GOAT_FOOD).add(Items.WHEAT);
        tag(PortTags.Items.AXOLOTL_FOOD).add(Items.TROPICAL_FISH_BUCKET);
        tag(PortTags.Items.ANIMAL_FOODS)
                .addTags(PortTags.Items.ARMADILLO_FOOD, PortTags.Items.AXOLOTL_FOOD, PortTags.Items.BEE_FOOD, PortTags.Items.CAMEL_FOOD,
                        PortTags.Items.CAT_FOOD, PortTags.Items.CHICKEN_FOOD, PortTags.Items.COW_FOOD, ItemTags.FOX_FOOD, PortTags.Items.FROG_FOOD,
                        PortTags.Items.GOAT_FOOD, PortTags.Items.HOGLIN_FOOD, PortTags.Items.HORSE_FOOD, PortTags.Items.LLAMA_FOOD, PortTags.Items.OCELOT_FOOD,
                        PortTags.Items.PANDA_FOOD, PortTags.Items.PARROT_FOOD, PortTags.Items.PIG_FOOD, ItemTags.PIGLIN_FOOD, PortTags.Items.RABBIT_FOOD,
                        PortTags.Items.SHEEP_FOOD, ItemTags.SNIFFER_FOOD, PortTags.Items.STRIDER_FOOD, PortTags.Items.TURTLE_FOOD, PortTags.Items.WOLF_FOOD);

        copy(PortTags.Blocks.SANDS, PortTags.Items.SANDS);
        copy(PortTags.Blocks.SANDS_COLORLESS, PortTags.Items.SANDS_COLORLESS);
        copy(PortTags.Blocks.SANDS_RED, PortTags.Items.SANDS_RED);

        tag(Tags.Items.BONES, PortTags.Items.BONES).add(Items.BONE);

        tag(PortTags.Items.BRICKS).addTags(PortTags.Items.BRICKS_NORMAL, PortTags.Items.BRICKS_NETHER);
        tag(PortTags.Items.BRICKS_NORMAL).add(Items.BRICK);
        tag(PortTags.Items.BRICKS_NETHER).add(Items.NETHER_BRICK);

        tag(PortTags.Items.BUCKETS_EMPTY).add(Items.BUCKET);
        tag(PortTags.Items.BUCKETS_WATER).add(Items.WATER_BUCKET);
        tag(PortTags.Items.BUCKETS_LAVA).add(Items.LAVA_BUCKET);
        tag(PortTags.Items.BUCKETS_MILK).add(Items.MILK_BUCKET);
        tag(PortTags.Items.BUCKETS_POWDER_SNOW).add(Items.POWDER_SNOW_BUCKET);
        tag(PortTags.Items.BUCKETS_ENTITY_WATER).add(Items.AXOLOTL_BUCKET, Items.COD_BUCKET, Items.PUFFERFISH_BUCKET, Items.TADPOLE_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.SALMON_BUCKET);
        tag(PortTags.Items.BUCKETS).addTags(PortTags.Items.BUCKETS_EMPTY, PortTags.Items.BUCKETS_WATER, PortTags.Items.BUCKETS_LAVA, PortTags.Items.BUCKETS_MILK, PortTags.Items.BUCKETS_POWDER_SNOW, PortTags.Items.BUCKETS_ENTITY_WATER);

        copy(PortTags.Blocks.BUDS, PortTags.Items.BUDS);

        copy(PortTags.Blocks.CHESTS, PortTags.Items.CHESTS);
        copy(PortTags.Blocks.CHESTS_ENDER, PortTags.Items.CHESTS_ENDER);
        copy(PortTags.Blocks.CHESTS_TRAPPED, PortTags.Items.CHESTS_TRAPPED);
        copy(PortTags.Blocks.CHESTS_WOODEN, PortTags.Items.CHESTS_WOODEN);

        tag(Tags.Items.CROPS, PortTags.Items.CROPS).addTags(
                PortTags.Items.CROPS_BEETROOT, PortTags.Items.CROPS_CACTUS, PortTags.Items.CROPS_CARROT,
                PortTags.Items.CROPS_COCOA_BEAN, PortTags.Items.CROPS_MELON, PortTags.Items.CROPS_NETHER_WART,
                PortTags.Items.CROPS_POTATO, PortTags.Items.CROPS_PUMPKIN, PortTags.Items.CROPS_SUGAR_CANE,
                PortTags.Items.CROPS_WHEAT);
        tag(Tags.Items.CROPS_BEETROOT, PortTags.Items.CROPS_BEETROOT).add(Items.BEETROOT);
        tag(PortTags.Items.CROPS_CACTUS).add(Items.CACTUS);
        tag(Tags.Items.CROPS_CARROT, PortTags.Items.CROPS_CARROT).add(Items.CARROT);
        tag(PortTags.Items.CROPS_COCOA_BEAN).add(Items.COCOA_BEANS);
        tag(PortTags.Items.CROPS_MELON).add(Items.MELON);
        tag(Tags.Items.CROPS_NETHER_WART, PortTags.Items.CROPS_NETHER_WART).add(Items.NETHER_WART);
        tag(Tags.Items.CROPS_POTATO, PortTags.Items.CROPS_POTATO).add(Items.POTATO);
        tag(PortTags.Items.CROPS_PUMPKIN).add(Items.PUMPKIN);
        tag(PortTags.Items.CROPS_SUGAR_CANE).add(Items.SUGAR_CANE);
        tag(Tags.Items.CROPS_WHEAT, PortTags.Items.CROPS_WHEAT).add(Items.WHEAT);

        tag(Tags.Items.DUSTS, PortTags.Items.DUSTS).addTags(Tags.Items.DUSTS_GLOWSTONE, Tags.Items.DUSTS_REDSTONE);
        tag(Tags.Items.DUSTS_GLOWSTONE, PortTags.Items.DUSTS_GLOWSTONE).add(Items.GLOWSTONE_DUST);
        tag(Tags.Items.DUSTS_REDSTONE, PortTags.Items.DUSTS_REDSTONE).add(Items.REDSTONE);

        IntrinsicTagAppender<Item> dyed = tag(PortTags.Items.DYED);
        for (DyeColor color : DyeColor.values()) {
            TagKey<Item> dyedColor = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + color.getName()));
            dyed.addTag(dyedColor);
            IntrinsicTagAppender<Item> appender = tag(dyedColor);
            for (String id : DYED_IDS) {
                ResourceLocation key = ResourceLocation.withDefaultNamespace(color.getName() + id);
                Item item = ForgeRegistries.ITEMS.getValue(key);
                if (item == null || item == Items.AIR) {
                    throw new IllegalStateException("Unknown vanilla item: " + key);
                }
                appender.add(item);
            }
        }

        copy(PortTags.Blocks.FENCE_GATES, PortTags.Items.FENCE_GATES);
        copy(PortTags.Blocks.FENCE_GATES_WOODEN, PortTags.Items.FENCE_GATES_WOODEN);
        copy(PortTags.Blocks.STRIPPED_LOGS, PortTags.Items.STRIPPED_LOGS);

        copy(PortTags.Blocks.CHAINS, PortTags.Items.CHAINS);

        tag(Tags.Items.GEMS, PortTags.Items.GEMS).addTags(PortTags.Items.GEMS_AMETHYST, PortTags.Items.GEMS_DIAMOND, PortTags.Items.GEMS_EMERALD, PortTags.Items.GEMS_LAPIS, PortTags.Items.GEMS_PRISMARINE, PortTags.Items.GEMS_QUARTZ);
        tag(Tags.Items.GEMS_AMETHYST, PortTags.Items.GEMS_AMETHYST).add(Items.AMETHYST_SHARD);
        tag(Tags.Items.GEMS_DIAMOND, PortTags.Items.GEMS_DIAMOND).add(Items.DIAMOND);
        tag(Tags.Items.GEMS_EMERALD, PortTags.Items.GEMS_EMERALD).add(Items.EMERALD);
        tag(Tags.Items.GEMS_LAPIS, PortTags.Items.GEMS_LAPIS).add(Items.LAPIS_LAZULI);
        tag(Tags.Items.GEMS_PRISMARINE, PortTags.Items.GEMS_PRISMARINE).add(Items.PRISMARINE_CRYSTALS);
        tag(Tags.Items.GEMS_QUARTZ, PortTags.Items.GEMS_QUARTZ).add(Items.QUARTZ);

        tag(Tags.Items.INGOTS, PortTags.Items.INGOTS).addTags(PortTags.Items.INGOTS_COPPER, PortTags.Items.INGOTS_GOLD, PortTags.Items.INGOTS_IRON, PortTags.Items.INGOTS_NETHERITE);
        tag(Tags.Items.INGOTS_COPPER, PortTags.Items.INGOTS_COPPER).add(Items.COPPER_INGOT);
        tag(Tags.Items.INGOTS_GOLD, PortTags.Items.INGOTS_GOLD).add(Items.GOLD_INGOT);
        tag(Tags.Items.INGOTS_IRON, PortTags.Items.INGOTS_IRON).add(Items.IRON_INGOT);
        tag(Tags.Items.INGOTS_NETHERITE, PortTags.Items.INGOTS_NETHERITE).add(Items.NETHERITE_INGOT);

        tag(Tags.Items.MUSHROOMS, PortTags.Items.MUSHROOMS).add(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);

        tag(Tags.Items.NUGGETS, PortTags.Items.NUGGETS).addTags(PortTags.Items.NUGGETS_GOLD, PortTags.Items.NUGGETS_IRON);
        tag(Tags.Items.NUGGETS_IRON, PortTags.Items.NUGGETS_IRON).add(Items.IRON_NUGGET);
        tag(Tags.Items.NUGGETS_GOLD, PortTags.Items.NUGGETS_GOLD).add(Items.GOLD_NUGGET);

        copy(PortTags.Blocks.ORE_BEARING_GROUND_DEEPSLATE, PortTags.Items.ORE_BEARING_GROUND_DEEPSLATE);
        copy(PortTags.Blocks.ORE_BEARING_GROUND_NETHERRACK, PortTags.Items.ORE_BEARING_GROUND_NETHERRACK);
        copy(PortTags.Blocks.ORE_BEARING_GROUND_STONE, PortTags.Items.ORE_BEARING_GROUND_STONE);
        copy(PortTags.Blocks.ORE_RATES_DENSE, PortTags.Items.ORE_RATES_DENSE);
        copy(PortTags.Blocks.ORE_RATES_SINGULAR, PortTags.Items.ORE_RATES_SINGULAR);
        copy(PortTags.Blocks.ORE_RATES_SPARSE, PortTags.Items.ORE_RATES_SPARSE);
        copy(PortTags.Blocks.ORES, PortTags.Items.ORES);
        copy(PortTags.Blocks.ORES_COAL, PortTags.Items.ORES_COAL);
        copy(PortTags.Blocks.ORES_COPPER, PortTags.Items.ORES_COPPER);
        copy(PortTags.Blocks.ORES_DIAMOND, PortTags.Items.ORES_DIAMOND);
        copy(PortTags.Blocks.ORES_EMERALD, PortTags.Items.ORES_EMERALD);
        copy(PortTags.Blocks.ORES_GOLD, PortTags.Items.ORES_GOLD);
        copy(PortTags.Blocks.ORES_IRON, PortTags.Items.ORES_IRON);
        copy(PortTags.Blocks.ORES_LAPIS, PortTags.Items.ORES_LAPIS);
        copy(PortTags.Blocks.ORES_QUARTZ, PortTags.Items.ORES_QUARTZ);
        copy(PortTags.Blocks.ORES_REDSTONE, PortTags.Items.ORES_REDSTONE);
        copy(PortTags.Blocks.ORES_NETHERITE_SCRAP, PortTags.Items.ORES_NETHERITE_SCRAP);
        copy(PortTags.Blocks.ORES_IN_GROUND_DEEPSLATE, PortTags.Items.ORES_IN_GROUND_DEEPSLATE);
        copy(PortTags.Blocks.ORES_IN_GROUND_NETHERRACK, PortTags.Items.ORES_IN_GROUND_NETHERRACK);
        copy(PortTags.Blocks.ORES_IN_GROUND_STONE, PortTags.Items.ORES_IN_GROUND_STONE);

        copy(PortTags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES, PortTags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES);
        copy(PortTags.Blocks.PLAYER_WORKSTATIONS_FURNACES, PortTags.Items.PLAYER_WORKSTATIONS_FURNACES);

        tag(PortTags.Items.POTION_BOTTLE).add(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);
        tag(PortTags.Items.POTIONS).addTags(PortTags.Items.POTION_BOTTLE);

        tag(Tags.Items.RAW_MATERIALS, PortTags.Items.RAW_MATERIALS).addTags(PortTags.Items.RAW_MATERIALS_COPPER, PortTags.Items.RAW_MATERIALS_GOLD, PortTags.Items.RAW_MATERIALS_IRON);
        tag(Tags.Items.RAW_MATERIALS_COPPER, PortTags.Items.RAW_MATERIALS_COPPER).add(Items.RAW_COPPER);
        tag(Tags.Items.RAW_MATERIALS_GOLD, PortTags.Items.RAW_MATERIALS_GOLD).add(Items.RAW_GOLD);
        tag(Tags.Items.RAW_MATERIALS_IRON, PortTags.Items.RAW_MATERIALS_IRON).add(Items.RAW_IRON);

        copy(PortTags.Blocks.ROPES, PortTags.Items.ROPES);

        copy(PortTags.Blocks.OBSIDIANS, PortTags.Items.OBSIDIANS);
        copy(PortTags.Blocks.OBSIDIANS_NORMAL, PortTags.Items.OBSIDIANS_NORMAL);
        copy(PortTags.Blocks.OBSIDIANS_CRYING, PortTags.Items.OBSIDIANS_CRYING);

        copy(PortTags.Blocks.SANDSTONE_BLOCKS, PortTags.Items.SANDSTONE_BLOCKS);
        copy(PortTags.Blocks.SANDSTONE_SLABS, PortTags.Items.SANDSTONE_SLABS);
        copy(PortTags.Blocks.SANDSTONE_STAIRS, PortTags.Items.SANDSTONE_STAIRS);
        copy(PortTags.Blocks.SANDSTONE_RED_BLOCKS, PortTags.Items.SANDSTONE_RED_BLOCKS);
        copy(PortTags.Blocks.SANDSTONE_RED_SLABS, PortTags.Items.SANDSTONE_RED_SLABS);
        copy(PortTags.Blocks.SANDSTONE_RED_STAIRS, PortTags.Items.SANDSTONE_RED_STAIRS);
        copy(PortTags.Blocks.SANDSTONE_UNCOLORED_BLOCKS, PortTags.Items.SANDSTONE_UNCOLORED_BLOCKS);
        copy(PortTags.Blocks.SANDSTONE_UNCOLORED_SLABS, PortTags.Items.SANDSTONE_UNCOLORED_SLABS);
        copy(PortTags.Blocks.SANDSTONE_UNCOLORED_STAIRS, PortTags.Items.SANDSTONE_UNCOLORED_STAIRS);

        copy(PortTags.Blocks.STORAGE_BLOCKS, PortTags.Items.STORAGE_BLOCKS);
        copy(PortTags.Blocks.STORAGE_BLOCKS_BONE_MEAL, PortTags.Items.STORAGE_BLOCKS_BONE_MEAL);
        copy(PortTags.Blocks.STORAGE_BLOCKS_COAL, PortTags.Items.STORAGE_BLOCKS_COAL);
        copy(PortTags.Blocks.STORAGE_BLOCKS_COPPER, PortTags.Items.STORAGE_BLOCKS_COPPER);
        copy(PortTags.Blocks.STORAGE_BLOCKS_DIAMOND, PortTags.Items.STORAGE_BLOCKS_DIAMOND);
        copy(PortTags.Blocks.STORAGE_BLOCKS_DRIED_KELP, PortTags.Items.STORAGE_BLOCKS_DRIED_KELP);
        copy(PortTags.Blocks.STORAGE_BLOCKS_EMERALD, PortTags.Items.STORAGE_BLOCKS_EMERALD);
        copy(PortTags.Blocks.STORAGE_BLOCKS_GOLD, PortTags.Items.STORAGE_BLOCKS_GOLD);
        copy(PortTags.Blocks.STORAGE_BLOCKS_IRON, PortTags.Items.STORAGE_BLOCKS_IRON);
        copy(PortTags.Blocks.STORAGE_BLOCKS_LAPIS, PortTags.Items.STORAGE_BLOCKS_LAPIS);
        copy(PortTags.Blocks.STORAGE_BLOCKS_NETHERITE, PortTags.Items.STORAGE_BLOCKS_NETHERITE);
        copy(PortTags.Blocks.STORAGE_BLOCKS_RAW_COPPER, PortTags.Items.STORAGE_BLOCKS_RAW_COPPER);
        copy(PortTags.Blocks.STORAGE_BLOCKS_RAW_GOLD, PortTags.Items.STORAGE_BLOCKS_RAW_GOLD);
        copy(PortTags.Blocks.STORAGE_BLOCKS_RAW_IRON, PortTags.Items.STORAGE_BLOCKS_RAW_IRON);
        copy(PortTags.Blocks.STORAGE_BLOCKS_REDSTONE, PortTags.Items.STORAGE_BLOCKS_REDSTONE);
        copy(PortTags.Blocks.STORAGE_BLOCKS_SLIME, PortTags.Items.STORAGE_BLOCKS_SLIME);
        copy(PortTags.Blocks.STORAGE_BLOCKS_WHEAT, PortTags.Items.STORAGE_BLOCKS_WHEAT);

        copy(PortTags.Blocks.COBBLESTONES, PortTags.Items.COBBLESTONES);
        copy(PortTags.Blocks.COBBLESTONES_NORMAL, PortTags.Items.COBBLESTONES_NORMAL);
        copy(PortTags.Blocks.COBBLESTONES_INFESTED, PortTags.Items.COBBLESTONES_INFESTED);
        copy(PortTags.Blocks.COBBLESTONES_MOSSY, PortTags.Items.COBBLESTONES_MOSSY);
        copy(PortTags.Blocks.COBBLESTONES_DEEPSLATE, PortTags.Items.COBBLESTONES_DEEPSLATE);

        tag(PortTags.Items.VILLAGER_JOB_SITES).add(
                Items.BARREL, Items.BLAST_FURNACE, Items.BREWING_STAND, Items.CARTOGRAPHY_TABLE,
                Items.CAULDRON, Items.COMPOSTER, Items.FLETCHING_TABLE, Items.GRINDSTONE,
                Items.LECTERN, Items.LOOM, Items.SMITHING_TABLE, Items.SMOKER, Items.STONECUTTER);

        tag(PortTags.Items.FOOT_ARMOR_ENCHANTABLE).addTag(PortTags.Items.FOOT_ARMOR);
        tag(PortTags.Items.LEG_ARMOR_ENCHANTABLE).addTag(PortTags.Items.LEG_ARMOR);
        tag(PortTags.Items.CHEST_ARMOR_ENCHANTABLE).addTag(PortTags.Items.CHEST_ARMOR);
        tag(PortTags.Items.HEAD_ARMOR_ENCHANTABLE).addTag(PortTags.Items.HEAD_ARMOR);
        tag(PortTags.Items.ARMOR_ENCHANTABLE)
                .addTag(PortTags.Items.FOOT_ARMOR_ENCHANTABLE)
                .addTag(PortTags.Items.LEG_ARMOR_ENCHANTABLE)
                .addTag(PortTags.Items.CHEST_ARMOR_ENCHANTABLE)
                .addTag(PortTags.Items.HEAD_ARMOR_ENCHANTABLE);
        tag(PortTags.Items.SWORD_ENCHANTABLE).addTag(ItemTags.SWORDS);
        tag(PortTags.Items.FIRE_ASPECT_ENCHANTABLE).addTag(PortTags.Items.SWORD_ENCHANTABLE)/*.add(Items.MACE)*/;
        tag(PortTags.Items.SHARP_WEAPON_ENCHANTABLE).addTag(ItemTags.SWORDS).addTag(ItemTags.AXES);
        tag(PortTags.Items.WEAPON_ENCHANTABLE).addTag(PortTags.Items.SHARP_WEAPON_ENCHANTABLE)/*.add(Items.MACE)*/;
//        tag(PortTags.Items.MACE_ENCHANTABLE).add(Items.MACE);
        tag(PortTags.Items.MINING_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES).add(Items.SHEARS);
        tag(PortTags.Items.MINING_LOOT_ENCHANTABLE).addTag(ItemTags.AXES).addTag(ItemTags.PICKAXES).addTag(ItemTags.SHOVELS).addTag(ItemTags.HOES);
        tag(PortTags.Items.FISHING_ENCHANTABLE).add(Items.FISHING_ROD);
        tag(PortTags.Items.TRIDENT_ENCHANTABLE).add(Items.TRIDENT);
        tag(PortTags.Items.DURABILITY_ENCHANTABLE)
                .addTag(PortTags.Items.FOOT_ARMOR)
                .addTag(PortTags.Items.LEG_ARMOR)
                .addTag(PortTags.Items.CHEST_ARMOR)
                .addTag(PortTags.Items.HEAD_ARMOR)
                .add(Items.ELYTRA)
                .add(Items.SHIELD)
                .addTag(ItemTags.SWORDS)
                .addTag(ItemTags.AXES)
                .addTag(ItemTags.PICKAXES)
                .addTag(ItemTags.SHOVELS)
                .addTag(ItemTags.HOES)
                .add(Items.BOW)
                .add(Items.CROSSBOW)
                .add(Items.TRIDENT)
                .add(Items.FLINT_AND_STEEL)
                .add(Items.SHEARS)
                .add(Items.BRUSH)
                .add(Items.FISHING_ROD)
                .add(Items.CARROT_ON_A_STICK, Items.WARPED_FUNGUS_ON_A_STICK)
        /*.add(Items.MACE)*/;
        tag(PortTags.Items.BOW_ENCHANTABLE).add(Items.BOW);
        tag(PortTags.Items.EQUIPPABLE_ENCHANTABLE)
                .addTag(PortTags.Items.FOOT_ARMOR)
                .addTag(PortTags.Items.LEG_ARMOR)
                .addTag(PortTags.Items.CHEST_ARMOR)
                .addTag(PortTags.Items.HEAD_ARMOR)
                .add(Items.ELYTRA)
                .addTag(PortTags.Items.SKULLS)
                .add(Items.CARVED_PUMPKIN);
        tag(PortTags.Items.CROSSBOW_ENCHANTABLE).add(Items.CROSSBOW);
        tag(PortTags.Items.SKULLS).add(
                Items.PLAYER_HEAD,
                Items.CREEPER_HEAD,
                Items.ZOMBIE_HEAD,
                Items.SKELETON_SKULL,
                Items.WITHER_SKELETON_SKULL,
                Items.DRAGON_HEAD,
                Items.PIGLIN_HEAD
        );
        tag(PortTags.Items.VANISHING_ENCHANTABLE).addTag(PortTags.Items.DURABILITY_ENCHANTABLE).add(Items.COMPASS).add(Items.CARVED_PUMPKIN).addTag(PortTags.Items.SKULLS);

        tag(PortTags.Items.MEAT).add(
                Items.BEEF,
                Items.CHICKEN,
                Items.COOKED_BEEF,
                Items.COOKED_CHICKEN,
                Items.COOKED_MUTTON,
                Items.COOKED_PORKCHOP,
                Items.COOKED_RABBIT,
                Items.MUTTON,
                Items.PORKCHOP,
                Items.RABBIT,
                Items.ROTTEN_FLESH
        );
    }

    protected IntrinsicTagAppender<Item> tag(TagKey<Item> forgeTag, TagKey<Item> commonTag) {
        super.tag(forgeTag).addTag(commonTag);
        return super.tag(commonTag);
    }
}
