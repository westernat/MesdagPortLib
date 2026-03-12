package org.mesdag.portlib.wrapper.world.item.alchemy;

import net.minecraft.core.Holder;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

@SuppressWarnings("all")
public class PotionHolder implements PortHolder<Potion> {
    private final Holder<Potion> delegate;

    private PotionHolder(Potion value) {
        this.delegate = PortHolder.getDelegate(ForgeRegistries.POTIONS, value);
    }

    @Override
    public Holder<Potion> delegate() {
        return delegate;
    }

    @Diff
    public static PotionHolder wrap(Potion value) {
        return new PotionHolder(value);
    }
}
