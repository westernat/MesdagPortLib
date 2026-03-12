package org.mesdag.portlib.wrapper.world.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

@SuppressWarnings("all")
public class EnchantmentHolder implements PortHolder<Enchantment> {
    private final Holder<Enchantment> delegate;

    private EnchantmentHolder(Holder<Enchantment> value) {
        this.delegate = value;
    }

    @Override
    public Holder<Enchantment> delegate() {
        return delegate;
    }

    @Diff
    public static EnchantmentHolder wrap(Holder<Enchantment> value) {
        return new EnchantmentHolder(value);
    }
}
