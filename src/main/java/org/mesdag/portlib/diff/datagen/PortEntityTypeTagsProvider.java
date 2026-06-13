package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public PortEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, PortLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PortTags.EntityTypes.ZOMBIES).add(
                EntityType.ZOMBIE_HORSE,
                EntityType.ZOMBIE,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.ZOGLIN,
                EntityType.DROWNED,
                EntityType.HUSK
        );
        tag(PortTags.EntityTypes.UNDEAD).addTag(EntityTypeTags.SKELETONS).addTag(PortTags.EntityTypes.ZOMBIES).add(EntityType.WITHER).add(EntityType.PHANTOM);
    }
}
