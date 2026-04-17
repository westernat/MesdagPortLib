package org.mesdag.portlib.wrapper.world.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class MobEffectHolder implements PortHolder<MobEffect> {
    private final Holder<MobEffect> delegate;

    private MobEffectHolder(MobEffect value) {
        this.delegate = PortHolder.getDelegate(ForgeRegistries.MOB_EFFECTS, value);
    }

    @Override
    public Holder<MobEffect> delegate() {
        return delegate;
    }

    @Diff
    public static MobEffectHolder wrap(MobEffect value) {
        return new MobEffectHolder(value);
    }
}
