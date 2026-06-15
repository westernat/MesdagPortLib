package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.common.PortTags;

import java.util.concurrent.CompletableFuture;

public class PortDamageTypeTagsProvider extends TagsProvider<DamageType> {
    protected PortDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, PortLib.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(PortTags.DamageTypes.IS_MAGIC).add(
                DamageTypes.MAGIC,
                DamageTypes.INDIRECT_MAGIC,
                DamageTypes.THORNS,
                DamageTypes.DRAGON_BREATH
        );
        tag(PortTags.DamageTypes.PANIC_ENVIRONMENTAL_CAUSES).add(
                DamageTypes.CACTUS,
                DamageTypes.FREEZE,
                DamageTypes.HOT_FLOOR,
                DamageTypes.IN_FIRE,
                DamageTypes.LAVA,
                DamageTypes.LIGHTNING_BOLT,
                DamageTypes.ON_FIRE
        );
        tag(PortTags.DamageTypes.PANIC_CAUSES).addTag(PortTags.DamageTypes.PANIC_ENVIRONMENTAL_CAUSES).add(
                DamageTypes.ARROW,
                DamageTypes.DRAGON_BREATH,
                DamageTypes.EXPLOSION,
                DamageTypes.FIREBALL,
                DamageTypes.FIREWORKS,
                DamageTypes.INDIRECT_MAGIC,
                DamageTypes.MAGIC,
                DamageTypes.MOB_ATTACK,
                DamageTypes.MOB_PROJECTILE,
                DamageTypes.PLAYER_ATTACK,
                DamageTypes.PLAYER_EXPLOSION,
                DamageTypes.SONIC_BOOM,
                DamageTypes.STING,
                DamageTypes.THROWN,
                DamageTypes.TRIDENT,
                DamageTypes.UNATTRIBUTED_FIREBALL,
//                DamageTypes.WIND_CHARGE,
                DamageTypes.WITHER,
                DamageTypes.WITHER_SKULL
        );
        tag(PortTags.DamageTypes.IS_PLAYER_ATTACK).add(DamageTypes.PLAYER_ATTACK);
        tag(PortTags.DamageTypes.CAN_BREAK_ARMOR_STAND).add(DamageTypes.PLAYER_ATTACK, DamageTypes.PLAYER_EXPLOSION);
    }
}
