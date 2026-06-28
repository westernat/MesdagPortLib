package org.mesdag.portlib.wrapper.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import org.mesdag.portlib.PortLib;

public record PortAddCarversBiomeModifier(
        HolderSet<Biome> biomes,
        HolderSet<ConfiguredWorldCarver<?>> carvers,
        GenerationStep.Carving step
) implements BiomeModifier {
    public static final Codec<PortAddCarversBiomeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(PortAddCarversBiomeModifier::biomes),
            ConfiguredWorldCarver.LIST_CODEC.fieldOf("carvers").forGetter(PortAddCarversBiomeModifier::carvers),
            GenerationStep.Carving.CODEC.fieldOf("step").forGetter(PortAddCarversBiomeModifier::step)
    ).apply(instance, PortAddCarversBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            carvers.forEach(holder -> settings.addCarver(this.step, holder));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return PortLib.ADD_CARVERS_BIOME_MODIFIER_TYPE.get();
    }
}
