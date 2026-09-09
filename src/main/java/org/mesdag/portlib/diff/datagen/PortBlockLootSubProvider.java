package org.mesdag.portlib.diff.datagen;

import com.google.common.collect.Iterables;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Set;

public class PortBlockLootSubProvider extends BlockLootSubProvider {
    protected PortBlockLootSubProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(PortLib.CHISELED_TUFF.get());
        dropSelf(PortLib.TUFF_STAIRS.get());
        dropSelf(PortLib.TUFF_WALL.get());
        dropSelf(PortLib.POLISHED_TUFF.get());
        dropSelf(PortLib.POLISHED_TUFF_STAIRS.get());
        dropSelf(PortLib.POLISHED_TUFF_WALL.get());
        dropSelf(PortLib.TUFF_BRICKS.get());
        dropSelf(PortLib.TUFF_BRICK_STAIRS.get());
        dropSelf(PortLib.TUFF_BRICK_WALL.get());
        dropSelf(PortLib.CHISELED_TUFF_BRICKS.get());
        add(PortLib.TUFF_SLAB.get(), this::createSlabItemTable);
        add(PortLib.TUFF_BRICK_SLAB.get(), this::createSlabItemTable);
        add(PortLib.POLISHED_TUFF_SLAB.get(), this::createSlabItemTable);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Iterables.transform(PortLib.BLOCKS.getEntries(), PortRegistryEntry::get);
    }
}
