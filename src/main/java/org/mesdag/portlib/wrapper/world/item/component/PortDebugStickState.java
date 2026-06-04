package org.mesdag.portlib.wrapper.world.item.component;

import PortLib.extensions.net.minecraft.core.Holder.PortHolderExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.level.block.BlockHolder;

public record PortDebugStickState(ItemStack debugStack) {
    public PortDebugStickState {
        if (debugStack.isEmpty()) {
            throw new IllegalArgumentException("DebugStack cannot be empty!");
        }
    }

    public @Nullable Property<?> getProperty(BlockHolder block) {
        CompoundTag tag = debugStack.getTagElement("DebugProperty");
        if (tag != null) {
            StateDefinition<Block, BlockState> stateDefinition = block.value().getStateDefinition();
            return stateDefinition.getProperty(tag.getString(PortHolderExtension.getRegisteredName(block)));
        }
        return null;
    }

    public PortDebugStickState withProperty(BlockHolder block, Property<?> property) {
        CompoundTag tag = debugStack.getOrCreateTagElement("DebugProperty");
        tag.putString(PortHolderExtension.getRegisteredName(block), property.getName());
        return this;
    }

    public void applyTo(ItemStack stack) {
        if (stack == debugStack) return;
        CompoundTag tag = debugStack.getTagElement("DebugProperty");
        if (tag != null) {
            stack.getOrCreateTag().put("DebugProperty", tag.copy());
        }
    }
}
