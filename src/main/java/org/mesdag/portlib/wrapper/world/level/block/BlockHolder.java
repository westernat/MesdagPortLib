package org.mesdag.portlib.wrapper.world.level.block;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import org.mesdag.portlib.wrapper.core.PortHolder;

@SuppressWarnings("all")
public class BlockHolder implements PortHolder<Block> {
    private final Block value;

    private BlockHolder(Block value) {
        this.value = value;
    }

    @Override
    public Holder<Block> delegate() {
        return value.builtInRegistryHolder();
    }

    @Override
    public Block value() {
        return value;
    }

    public static BlockHolder wrap(Block value) {
        return new BlockHolder(value);
    }
}
