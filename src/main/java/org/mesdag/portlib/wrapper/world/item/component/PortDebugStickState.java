package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DebugStickState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.level.block.BlockHolder;

public record PortDebugStickState(ItemStack debugStack) {
    public PortDebugStickState {
        if (debugStack.isEmpty()) {
            throw new IllegalArgumentException("DebugStack cannot be empty!");
        }
    }

    @Diff
    public DebugStickState unwrap() {
        return debugStack.getOrDefault(DataComponents.DEBUG_STICK_STATE, DebugStickState.EMPTY);
    }

    public @Nullable Property<?> getProperty(BlockHolder block) {
        return unwrap().properties().get(block.delegate());
    }

    public PortDebugStickState withProperty(BlockHolder block, Property<?> property) {
        debugStack.set(DataComponents.DEBUG_STICK_STATE, unwrap().withProperty(block.delegate(), property));
        return this;
    }

    public void applyTo(ItemStack stack) {
        if (stack == debugStack) return;
        stack.set(DataComponents.DEBUG_STICK_STATE, debugStack.get(DataComponents.DEBUG_STICK_STATE));
    }
}
