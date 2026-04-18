package org.mesdag.portlib.event.other;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.BiPredicate;

public class PortBlockEntityTypeAddBlocksEvent extends PortEvent<BlockEntityTypeAddBlocksEvent> implements IPortModBusEvent {
    @Diff
    public PortBlockEntityTypeAddBlocksEvent(BlockEntityTypeAddBlocksEvent e) {
        super(e);
    }

    public void modify(BlockEntityType<?> blockEntityType, Block... blocksToAdd) {
        e.modify(blockEntityType, blocksToAdd);
    }

    public void modify(ResourceKey<BlockEntityType<?>> blockEntityTypeKey, Block... blocksToAdd) {
        e.modify(blockEntityTypeKey, blocksToAdd);
    }

    public void modify(BiPredicate<ResourceKey<BlockEntityType<?>>, BlockEntityType<?>> blockEntityTypeToMatch, Block... blocksToAdd) {
        e.modify(blockEntityTypeToMatch, blocksToAdd);
    }

    static {
        PortEventHooks.register();
    }
}