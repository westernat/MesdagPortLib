package org.mesdag.portlib.wrapper.world.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class MobEffectHolder implements PortHolder<MobEffect> {
    private final Holder<MobEffect> delegate;

    private MobEffectHolder(Holder<MobEffect> value) {
        this.delegate = value;
    }

    @Override
    public Holder<MobEffect> delegate() {
        return delegate;
    }

    @Diff
    public static MobEffectHolder wrap(Holder<MobEffect> value) {
        return new MobEffectHolder(value);
    }
}
