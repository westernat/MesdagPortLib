package org.mesdag.portlib.datamap.builtin;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record PortFurnaceFuel(int burnTime) {
    public static final Codec<PortFurnaceFuel> BURN_TIME_CODEC = ExtraCodecs.POSITIVE_INT
            .xmap(PortFurnaceFuel::new, PortFurnaceFuel::burnTime);
    public static final Codec<PortFurnaceFuel> CODEC = PortCodecExtension.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("burn_time").forGetter(PortFurnaceFuel::burnTime)
            ).apply(instance, PortFurnaceFuel::new)),
            BURN_TIME_CODEC
    );
}
