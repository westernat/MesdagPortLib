package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortBlockTagsProvider extends BlockTagsProvider {
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
            "_wall_banner",
            "_wool",
    };

    public PortBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PortLib.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Blocks.FENCE_GATES, PortTags.Blocks.FENCE_GATES).addTags(PortTags.Blocks.FENCE_GATES_WOODEN);
        tag(Tags.Blocks.FENCE_GATES_WOODEN, PortTags.Blocks.FENCE_GATES_WOODEN).add(Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE, Blocks.CHERRY_FENCE_GATE);
        tag(Tags.Blocks.FENCES, PortTags.Blocks.FENCES).addTags(PortTags.Blocks.FENCES_NETHER_BRICK, PortTags.Blocks.FENCES_WOODEN);
        tag(Tags.Blocks.FENCES_NETHER_BRICK, PortTags.Blocks.FENCES_NETHER_BRICK).add(Blocks.NETHER_BRICK_FENCE);
        tag(Tags.Blocks.FENCES_WOODEN, PortTags.Blocks.FENCES_WOODEN).addTag(BlockTags.WOODEN_FENCES);

        tag(PortTags.Blocks.STRIPPED_LOGS).add(
                Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_BAMBOO_BLOCK, Blocks.STRIPPED_BIRCH_LOG,
                Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_JUNGLE_LOG,
                Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG,
                Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_WARPED_STEM);

        tag(PortTags.Blocks.GLASS_BLOCKS).addTags(PortTags.Blocks.GLASS_BLOCKS_COLORLESS, PortTags.Blocks.GLASS_BLOCKS_CHEAP, PortTags.Blocks.GLASS_BLOCKS_TINTED);
        tag(PortTags.Blocks.GLASS_BLOCKS_COLORLESS).add(Blocks.GLASS);
        tag(PortTags.Blocks.GLASS_BLOCKS_CHEAP).add(Blocks.GLASS, Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS);
        tag(PortTags.Blocks.GLASS_BLOCKS_TINTED).add(Blocks.TINTED_GLASS);

        tag(Tags.Blocks.ORE_BEARING_GROUND_DEEPSLATE, PortTags.Blocks.ORE_BEARING_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE);
        tag(Tags.Blocks.ORE_BEARING_GROUND_NETHERRACK, PortTags.Blocks.ORE_BEARING_GROUND_NETHERRACK).add(Blocks.NETHERRACK);
        tag(Tags.Blocks.ORE_BEARING_GROUND_STONE, PortTags.Blocks.ORE_BEARING_GROUND_STONE).add(Blocks.STONE);
        tag(Tags.Blocks.ORE_RATES_DENSE, PortTags.Blocks.ORE_RATES_DENSE).add(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);
        tag(Tags.Blocks.ORE_RATES_SINGULAR, PortTags.Blocks.ORE_RATES_SINGULAR).add(Blocks.ANCIENT_DEBRIS, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.NETHER_QUARTZ_ORE);
        tag(Tags.Blocks.ORE_RATES_SPARSE, PortTags.Blocks.ORE_RATES_SPARSE).add(Blocks.NETHER_GOLD_ORE);
        tag(Tags.Blocks.ORES, PortTags.Blocks.ORES).addTags(PortTags.Blocks.ORES_COAL, PortTags.Blocks.ORES_COPPER, PortTags.Blocks.ORES_DIAMOND, PortTags.Blocks.ORES_EMERALD, PortTags.Blocks.ORES_GOLD, PortTags.Blocks.ORES_IRON, PortTags.Blocks.ORES_LAPIS, PortTags.Blocks.ORES_NETHERITE_SCRAP, PortTags.Blocks.ORES_REDSTONE, PortTags.Blocks.ORES_QUARTZ);
        tag(Tags.Blocks.ORES_COAL, PortTags.Blocks.ORES_COAL).addTag(BlockTags.COAL_ORES);
        tag(Tags.Blocks.ORES_COPPER, PortTags.Blocks.ORES_COPPER).addTag(BlockTags.COPPER_ORES);
        tag(Tags.Blocks.ORES_DIAMOND, PortTags.Blocks.ORES_DIAMOND).addTag(BlockTags.DIAMOND_ORES);
        tag(Tags.Blocks.ORES_EMERALD, PortTags.Blocks.ORES_EMERALD).addTag(BlockTags.EMERALD_ORES);
        tag(Tags.Blocks.ORES_GOLD, PortTags.Blocks.ORES_GOLD).addTag(BlockTags.GOLD_ORES);
        tag(Tags.Blocks.ORES_IRON, PortTags.Blocks.ORES_IRON).addTag(BlockTags.IRON_ORES);
        tag(Tags.Blocks.ORES_LAPIS, PortTags.Blocks.ORES_LAPIS).addTag(BlockTags.LAPIS_ORES);
        tag(Tags.Blocks.ORES_QUARTZ, PortTags.Blocks.ORES_QUARTZ).add(Blocks.NETHER_QUARTZ_ORE);
        tag(Tags.Blocks.ORES_REDSTONE, PortTags.Blocks.ORES_REDSTONE).addTag(BlockTags.REDSTONE_ORES);
        tag(Tags.Blocks.ORES_NETHERITE_SCRAP, PortTags.Blocks.ORES_NETHERITE_SCRAP).add(Blocks.ANCIENT_DEBRIS);
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE, PortTags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
        tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK, PortTags.Blocks.ORES_IN_GROUND_NETHERRACK).add(Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);
        tag(Tags.Blocks.ORES_IN_GROUND_STONE, PortTags.Blocks.ORES_IN_GROUND_STONE).add(Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE);

        tag(PortTags.Blocks.GRAVELS).add(Blocks.GRAVEL);
        tag(PortTags.Blocks.STONES).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF);
        tag(PortTags.Blocks.SANDS).addTags(PortTags.Blocks.SANDS_COLORLESS, PortTags.Blocks.SANDS_RED);
        tag(PortTags.Blocks.SANDS_COLORLESS).add(Blocks.SAND);
        tag(PortTags.Blocks.SANDS_RED).add(Blocks.RED_SAND);

        tag(PortTags.Blocks.BUDS).add(Blocks.SMALL_AMETHYST_BUD).add(Blocks.MEDIUM_AMETHYST_BUD).add(Blocks.LARGE_AMETHYST_BUD);
        tag(PortTags.Blocks.CHAINS).add(Blocks.CHAIN);
        tag(Tags.Blocks.CHESTS, PortTags.Blocks.CHESTS).addTags(Tags.Blocks.CHESTS_ENDER, Tags.Blocks.CHESTS_TRAPPED, Tags.Blocks.CHESTS_WOODEN);
        tag(Tags.Blocks.CHESTS_ENDER, PortTags.Blocks.CHESTS_ENDER).add(Blocks.ENDER_CHEST);
        tag(Tags.Blocks.CHESTS_TRAPPED, PortTags.Blocks.CHESTS_TRAPPED).add(Blocks.TRAPPED_CHEST);
        tag(Tags.Blocks.CHESTS_WOODEN, PortTags.Blocks.CHESTS_WOODEN).add(Blocks.CHEST, Blocks.TRAPPED_CHEST);

        tag(PortTags.Blocks.PLAYER_WORKSTATIONS_CRAFTING_TABLES).add(Blocks.CRAFTING_TABLE);
        tag(PortTags.Blocks.PLAYER_WORKSTATIONS_FURNACES).add(Blocks.FURNACE);

        tag(PortTags.Blocks.ROPES);

        tag(PortTags.Blocks.OBSIDIANS_NORMAL).add(Blocks.OBSIDIAN);
        tag(PortTags.Blocks.OBSIDIANS_CRYING).add(Blocks.CRYING_OBSIDIAN);
        tag(PortTags.Blocks.OBSIDIANS).addTags(PortTags.Blocks.OBSIDIANS_NORMAL, PortTags.Blocks.OBSIDIANS_CRYING);

        tag(PortTags.Blocks.SANDSTONE_RED_BLOCKS).add(Blocks.RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);
        tag(PortTags.Blocks.SANDSTONE_UNCOLORED_BLOCKS).add(Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE, Blocks.SMOOTH_SANDSTONE);
        tag(PortTags.Blocks.SANDSTONE_BLOCKS).addTags(PortTags.Blocks.SANDSTONE_RED_BLOCKS, PortTags.Blocks.SANDSTONE_UNCOLORED_BLOCKS);
        tag(PortTags.Blocks.SANDSTONE_RED_SLABS).add(Blocks.RED_SANDSTONE_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB);
        tag(PortTags.Blocks.SANDSTONE_UNCOLORED_SLABS).add(Blocks.SANDSTONE_SLAB, Blocks.CUT_SANDSTONE_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB);
        tag(PortTags.Blocks.SANDSTONE_SLABS).addTags(PortTags.Blocks.SANDSTONE_RED_SLABS, PortTags.Blocks.SANDSTONE_UNCOLORED_SLABS);
        tag(PortTags.Blocks.SANDSTONE_RED_STAIRS).add(Blocks.RED_SANDSTONE_STAIRS, Blocks.SMOOTH_RED_SANDSTONE_STAIRS);
        tag(PortTags.Blocks.SANDSTONE_UNCOLORED_STAIRS).add(Blocks.SANDSTONE_STAIRS, Blocks.SMOOTH_SANDSTONE_STAIRS);
        tag(PortTags.Blocks.SANDSTONE_STAIRS).addTags(PortTags.Blocks.SANDSTONE_RED_STAIRS, PortTags.Blocks.SANDSTONE_UNCOLORED_STAIRS);

        tag(Tags.Blocks.STORAGE_BLOCKS, PortTags.Blocks.STORAGE_BLOCKS).addTags(
                PortTags.Blocks.STORAGE_BLOCKS_BONE_MEAL, PortTags.Blocks.STORAGE_BLOCKS_COAL,
                PortTags.Blocks.STORAGE_BLOCKS_COPPER, PortTags.Blocks.STORAGE_BLOCKS_DIAMOND, PortTags.Blocks.STORAGE_BLOCKS_DRIED_KELP,
                PortTags.Blocks.STORAGE_BLOCKS_EMERALD, PortTags.Blocks.STORAGE_BLOCKS_GOLD, PortTags.Blocks.STORAGE_BLOCKS_IRON,
                PortTags.Blocks.STORAGE_BLOCKS_LAPIS, PortTags.Blocks.STORAGE_BLOCKS_NETHERITE, PortTags.Blocks.STORAGE_BLOCKS_RAW_COPPER,
                PortTags.Blocks.STORAGE_BLOCKS_RAW_GOLD, PortTags.Blocks.STORAGE_BLOCKS_RAW_IRON, PortTags.Blocks.STORAGE_BLOCKS_REDSTONE,
                PortTags.Blocks.STORAGE_BLOCKS_SLIME, PortTags.Blocks.STORAGE_BLOCKS_WHEAT);
        tag(PortTags.Blocks.STORAGE_BLOCKS_BONE_MEAL).add(Blocks.BONE_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_COAL, PortTags.Blocks.STORAGE_BLOCKS_COAL).add(Blocks.COAL_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_COPPER, PortTags.Blocks.STORAGE_BLOCKS_COPPER).add(Blocks.COPPER_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_DIAMOND, PortTags.Blocks.STORAGE_BLOCKS_DIAMOND).add(Blocks.DIAMOND_BLOCK);
        tag(PortTags.Blocks.STORAGE_BLOCKS_DRIED_KELP).add(Blocks.DRIED_KELP_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_EMERALD, PortTags.Blocks.STORAGE_BLOCKS_EMERALD).add(Blocks.EMERALD_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_GOLD, PortTags.Blocks.STORAGE_BLOCKS_GOLD).add(Blocks.GOLD_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_IRON, PortTags.Blocks.STORAGE_BLOCKS_IRON).add(Blocks.IRON_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_LAPIS, PortTags.Blocks.STORAGE_BLOCKS_LAPIS).add(Blocks.LAPIS_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_NETHERITE, PortTags.Blocks.STORAGE_BLOCKS_NETHERITE).add(Blocks.NETHERITE_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER, PortTags.Blocks.STORAGE_BLOCKS_RAW_COPPER).add(Blocks.RAW_COPPER_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD, PortTags.Blocks.STORAGE_BLOCKS_RAW_GOLD).add(Blocks.RAW_GOLD_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON, PortTags.Blocks.STORAGE_BLOCKS_RAW_IRON).add(Blocks.RAW_IRON_BLOCK);
        tag(Tags.Blocks.STORAGE_BLOCKS_REDSTONE, PortTags.Blocks.STORAGE_BLOCKS_REDSTONE).add(Blocks.REDSTONE_BLOCK);
        tag(PortTags.Blocks.STORAGE_BLOCKS_SLIME).add(Blocks.SLIME_BLOCK);
        tag(PortTags.Blocks.STORAGE_BLOCKS_WHEAT).add(Blocks.HAY_BLOCK);

        tag(PortTags.Blocks.COBBLESTONES).addTags(PortTags.Blocks.COBBLESTONES_NORMAL, PortTags.Blocks.COBBLESTONES_INFESTED, PortTags.Blocks.COBBLESTONES_MOSSY, PortTags.Blocks.COBBLESTONES_DEEPSLATE);
        tag(PortTags.Blocks.COBBLESTONES_NORMAL).add(Blocks.COBBLESTONE);
        tag(PortTags.Blocks.COBBLESTONES_INFESTED).add(Blocks.INFESTED_COBBLESTONE);
        tag(PortTags.Blocks.COBBLESTONES_MOSSY).add(Blocks.MOSSY_COBBLESTONE);
        tag(PortTags.Blocks.COBBLESTONES_DEEPSLATE).add(Blocks.COBBLED_DEEPSLATE);

        IntrinsicTagAppender<Block> dyed = tag(PortTags.Blocks.DYED);
        for (DyeColor color : DyeColor.values()) {
            TagKey<Block> dyedColor = BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "dyed/" + color.getName()));
            dyed.addTag(dyedColor);
            IntrinsicTagAppender<Block> appender = tag(dyedColor);
            for (String id : DYED_IDS) {
                ResourceLocation key = ResourceLocation.withDefaultNamespace(color.getName() + id);
                Block block = ForgeRegistries.BLOCKS.getValue(key);
                if (block == null || block == Blocks.AIR) {
                    throw new IllegalStateException("Unknown vanilla block: " + key);
                }
                appender.add(block);
            }
        }

        tag(PortTags.Blocks.VILLAGER_JOB_SITES).add(
                Blocks.BARREL, Blocks.BLAST_FURNACE, Blocks.BREWING_STAND, Blocks.CARTOGRAPHY_TABLE,
                Blocks.CAULDRON, Blocks.WATER_CAULDRON, Blocks.LAVA_CAULDRON, Blocks.POWDER_SNOW_CAULDRON,
                Blocks.COMPOSTER, Blocks.FLETCHING_TABLE, Blocks.GRINDSTONE, Blocks.LECTERN,
                Blocks.LOOM, Blocks.SMITHING_TABLE, Blocks.SMOKER, Blocks.STONECUTTER);

        tag(PortTags.Blocks.HIDDEN_FROM_RECIPE_VIEWERS);
    }

    protected IntrinsicTagAppender<Block> tag(TagKey<Block> forgeTag, TagKey<Block> commonTag) {
        super.tag(forgeTag).addTag(commonTag);
        return super.tag(commonTag);
    }
}
