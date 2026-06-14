package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortBiomeTagsProvider extends BiomeTagsProvider {
    public PortBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, PortLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PortTags.Biomes.IS_ICY).add(Biomes.ICE_SPIKES).add(Biomes.FROZEN_PEAKS);
        tag(Tags.Biomes.IS_SNOWY, PortTags.Biomes.IS_SNOWY).add(Biomes.SNOWY_BEACH).add(Biomes.SNOWY_PLAINS).add(Biomes.ICE_SPIKES).add(Biomes.SNOWY_TAIGA).add(Biomes.GROVE).add(Biomes.SNOWY_SLOPES).add(Biomes.JAGGED_PEAKS).add(Biomes.FROZEN_PEAKS);
        tag(PortTags.Biomes.IS_JUNGLE).addTag(BiomeTags.IS_JUNGLE);
        tag(PortTags.Biomes.IS_SAVANNA).addTag(BiomeTags.IS_SAVANNA);
        tag(PortTags.Biomes.IS_TAIGA).addTag(BiomeTags.IS_TAIGA);
        tag(Tags.Biomes.IS_DESERT, PortTags.Biomes.IS_DESERT).add(Biomes.DESERT);
        tag(PortTags.Biomes.IS_AQUATIC).addTag(PortTags.Biomes.IS_OCEAN).addTag(PortTags.Biomes.IS_RIVER);
        tag(Tags.Biomes.IS_UNDERGROUND, PortTags.Biomes.IS_UNDERGROUND).addTag(Tags.Biomes.IS_CAVE);
        tag(PortTags.Biomes.IS_RIVER).addTag(BiomeTags.IS_RIVER);
        tag(PortTags.Biomes.IS_OCEAN).addTag(BiomeTags.IS_OCEAN).addTag(PortTags.Biomes.IS_SHALLOW_OCEAN).addTag(PortTags.Biomes.IS_DEEP_OCEAN);
        tag(PortTags.Biomes.IS_SHALLOW_OCEAN).add(Biomes.OCEAN).add(Biomes.LUKEWARM_OCEAN).add(Biomes.WARM_OCEAN).add(Biomes.COLD_OCEAN).add(Biomes.FROZEN_OCEAN);
        tag(PortTags.Biomes.IS_DEEP_OCEAN).addTag(BiomeTags.IS_DEEP_OCEAN);
        tag(Tags.Biomes.IS_LUSH, PortTags.Biomes.IS_LUSH).add(Biomes.LUSH_CAVES);
        tag(PortTags.Biomes.IS_STONY_SHORES).add(Biomes.STONY_SHORE);

        tag(PortTags.Biomes.IS_CONIFEROUS_TREE).addTag(PortTags.Biomes.IS_TAIGA).add(Biomes.GROVE);
        tag(PortTags.Biomes.IS_SAVANNA_TREE).addTag(PortTags.Biomes.IS_SAVANNA);
        tag(PortTags.Biomes.IS_JUNGLE_TREE).addTag(PortTags.Biomes.IS_JUNGLE);
        tag(PortTags.Biomes.IS_DECIDUOUS_TREE).add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.BIRCH_FOREST).add(Biomes.DARK_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.WINDSWEPT_FOREST);
    }

    protected TagAppender<Biome> tag(TagKey<Biome> forgeTag, TagKey<Biome> commonTag) {
        super.tag(forgeTag).addTag(commonTag);
        return super.tag(commonTag);
    }
}
