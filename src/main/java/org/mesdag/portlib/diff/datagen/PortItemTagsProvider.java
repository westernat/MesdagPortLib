package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortItemTagsProvider extends ItemTagsProvider {
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
    }

    protected IntrinsicTagAppender<Item> tag(TagKey<Item> forgeTag, TagKey<Item> commonTag) {
        super.tag(forgeTag).addTag(commonTag);
        return super.tag(commonTag);
    }
}
