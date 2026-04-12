package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortBlockGrowFeatureEvent extends PortLevelEvent<SaplingGrowTreeEvent> implements IPortCancellableEvent {
    public PortBlockGrowFeatureEvent(SaplingGrowTreeEvent e) {
        super(e);
    }

    public RandomSource getRandom() {
        return e.getRandomSource();
    }

    public BlockPos getPos() {
        return e.getPos();
    }

    public @Nullable Holder<ConfiguredFeature<?, ?>> getFeature() {
        return e.getFeature();
    }

    public void setFeature(@Nullable Holder<ConfiguredFeature<?, ?>> feature) {
        e.setFeature(feature);
    }

    public void setFeature(ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
        e.setFeature(featureKey);
    }

    static {
        PortEventHooks.register(SaplingGrowTreeEvent.class, PortBlockGrowFeatureEvent.class, PortBlockGrowFeatureEvent::new);
    }
}
