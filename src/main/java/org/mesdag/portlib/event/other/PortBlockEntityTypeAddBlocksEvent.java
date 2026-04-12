package org.mesdag.portlib.event.other;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.mixin.BlockEntityTypeAccessor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class PortBlockEntityTypeAddBlocksEvent extends Event implements IModBusEvent {
    private final Function<BlockEntityType<?>, ? extends Class<?>> memoizedCommonSuperClass = Util.memoize((BlockEntityType<?> blockEntityType) -> getCommonSuperClassForExistingValidBlocks(((BlockEntityTypeAccessor) blockEntityType).getValidBlocks()));

    @Diff
    public PortBlockEntityTypeAddBlocksEvent() {}

    public void modify(BlockEntityType<?> blockEntityType, Block... blocksToAdd) {
        if (blocksToAdd.length == 0) {
            return;
        }

        Set<Block> currentValidBlocks = new HashSet<>(((BlockEntityTypeAccessor) blockEntityType).getValidBlocks());

        for (Block block : blocksToAdd) {
            addValidBlock(block, memoizedCommonSuperClass.apply(blockEntityType), currentValidBlocks);
        }

        ((BlockEntityTypeAccessor) blockEntityType).setValidBlocks(currentValidBlocks);
    }

    public void modify(ResourceKey<BlockEntityType<?>> blockEntityTypeKey, Block... blocksToAdd) {
        BlockEntityType<?> value = ForgeRegistries.BLOCK_ENTITY_TYPES.getValue(blockEntityTypeKey.location());
        if (value != null) {
            modify(value, blocksToAdd);
        }
    }

    public void modify(BiPredicate<ResourceKey<BlockEntityType<?>>, BlockEntityType<?>> blockEntityTypeToMatch, Block... blocksToAdd) {
        for (Map.Entry<ResourceKey<BlockEntityType<?>>, BlockEntityType<?>> blockEntityTypeEntry : BuiltInRegistries.BLOCK_ENTITY_TYPE.entrySet()) {
            if (blockEntityTypeToMatch.test(blockEntityTypeEntry.getKey(), blockEntityTypeEntry.getValue())) {
                modify(blockEntityTypeEntry.getValue(), blocksToAdd);
            }
        }
    }

    private void addValidBlock(Block block, @Nullable Class<?> baseClass, Set<Block> currentValidBlocks) {
        if (baseClass == null || baseClass.isAssignableFrom(block.getClass())) {
            currentValidBlocks.add(block);
        } else {
            throw new IllegalArgumentException("Given block " + block + " does not derive from existing valid block's common superclass of " + baseClass);
        }
    }

    @Nullable
    private Class<?> getCommonSuperClassForExistingValidBlocks(Set<Block> validBlocks) {
        Class<?> calculatedBaseClass = null;

        for (Block existingBlock : validBlocks) {
            if (calculatedBaseClass != null) {
                calculatedBaseClass = findClosestCommonSuper(calculatedBaseClass, existingBlock.getClass());
            } else {
                calculatedBaseClass = existingBlock.getClass();
            }
        }

        return calculatedBaseClass;
    }

    private static Class<?> findClosestCommonSuper(Class<?> superClass, Class<?> childClass) {
        while (!superClass.isAssignableFrom(childClass)) {
            superClass = superClass.getSuperclass();
        }
        return superClass;
    }
}