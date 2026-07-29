package org.mesdag.portlib.datamap.builtin;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * PortLib 可堆肥物品 Data Map 的值。
 *
 * @param chance 进入堆肥桶时提升层级的概率，范围为 {@code [0, 1]}
 * @param canVillagerCompost 是否允许农民村民把该物品投入堆肥桶
 */
public record PortCompostable(float chance, boolean canVillagerCompost) {
    public static final Codec<PortCompostable> CHANCE_CODEC = Codec.floatRange(0.0F, 1.0F)
            .xmap(PortCompostable::new, PortCompostable::chance);
    public static final Codec<PortCompostable> CODEC = PortCodecExtension.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(PortCompostable::chance),
                    Codec.BOOL.optionalFieldOf("can_villager_compost", false)
                            .forGetter(PortCompostable::canVillagerCompost)
            ).apply(instance, PortCompostable::new)),
            CHANCE_CODEC
    );

    public PortCompostable(float chance) {
        this(chance, false);
    }
}
