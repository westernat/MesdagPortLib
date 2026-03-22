package org.mesdag.portlib.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.function.BiPredicate;

public class PortBlockEntityTypeAddBlocksEvent extends PortEvent implements IPortModBusEvent {
    private final BlockEntityTypeAddBlocksEvent e;

    @Diff
    public PortBlockEntityTypeAddBlocksEvent(BlockEntityTypeAddBlocksEvent e) {
        this.e = e;
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
        PortEventHooks.register(BlockEntityTypeAddBlocksEvent.class, PortBlockEntityTypeAddBlocksEvent.class, PortBlockEntityTypeAddBlocksEvent::new);
    }
}