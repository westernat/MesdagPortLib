package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortBlockTagsProvider extends BlockTagsProvider {
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
    }

    protected IntrinsicTagAppender<Block> tag(TagKey<Block> forgeTag, TagKey<Block> commonTag) {
        super.tag(forgeTag).addTag(commonTag);
        return super.tag(commonTag);
    }
}
