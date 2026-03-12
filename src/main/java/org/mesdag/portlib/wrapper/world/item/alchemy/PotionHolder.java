package org.mesdag.portlib.wrapper.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

@SuppressWarnings("all")
public class PotionHolder implements PortHolder<Potion> {
    private final Holder<Potion> delegate;

    private PotionHolder(Holder<Potion> value) {
        this.delegate = value;
    }

    @Override
    public Holder<Potion> delegate() {
        return delegate;
    }

    @Diff
    public static PotionHolder wrap(Holder<Potion> value) {
        return new PotionHolder(value);
    }
}
