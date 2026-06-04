package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortBlock;
import org.mesdag.portlib.event.level.PortBlockDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin implements IPortBlock {
    @Shadow(remap = false)
    private Object renderProperties;
    @Unique
    private static @Nullable List<ItemEntity> portlib$capturedDrops = null;

    @Unique
    private static void portlib$beginCapturingDrops() {
        portlib$capturedDrops = new ArrayList<>();
    }

    @Unique
    private static List<ItemEntity> portlib$stopCapturingDrops() {
        List<ItemEntity> drops = portlib$capturedDrops;
        portlib$capturedDrops = null;
        return drops;
    }

    @Override
    public void portlib$setRenderPropertiesInternal(Object properties) {
        this.renderProperties = properties;
    }

    @WrapOperation(method = "popResource(Lnet/minecraft/world/level/Level;Ljava/util/function/Supplier;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private static boolean capture(Level instance, Entity entity, Operation<Boolean> original) {
        if (portlib$capturedDrops == null) {
            return original.call(instance, entity);
        }
        return portlib$capturedDrops.add((ItemEntity) entity);
    }

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/List;"))
    private static void begin1(CallbackInfo ci) {
        portlib$beginCapturingDrops();
    }

    @WrapOperation(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
    private static void stop1(BlockState instance, ServerLevel level, BlockPos pos, ItemStack stack, boolean b, Operation<Void> original) {
        List<ItemEntity> captured = portlib$stopCapturingDrops();
        PortBlockDropsEvent.handleBlockDrops(level, pos, instance, null, captured, null, stack, () -> original.call(instance, level, pos, stack, false));
    }

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)Ljava/util/List;"))
    private static void begin2(CallbackInfo ci) {
        portlib$beginCapturingDrops();
    }

    @WrapOperation(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
    private static void stop2(BlockState instance, ServerLevel level, BlockPos pos, ItemStack stack, boolean b, Operation<Void> original, @Local(argsOnly = true) @Nullable BlockEntity blockEntity) {
        List<ItemEntity> captured = portlib$stopCapturingDrops();
        PortBlockDropsEvent.handleBlockDrops(level, pos, instance, blockEntity, captured, null, stack, () -> original.call(instance, level, pos, stack, false));
    }

    @Inject(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
    private static void begin3(CallbackInfo ci) {
        portlib$beginCapturingDrops();
    }

    @WrapOperation(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"))
    private static void stop3(BlockState instance, ServerLevel level, BlockPos pos, ItemStack stack, boolean b, Operation<Void> original, @Local(argsOnly = true) @Nullable BlockEntity blockEntity, @Local(argsOnly = true) @Nullable Entity entity) {
        List<ItemEntity> captured = portlib$stopCapturingDrops();
        PortBlockDropsEvent.handleBlockDrops(level, pos, instance, blockEntity, captured, entity, stack, () -> original.call(instance, level, pos, stack, false));
    }
}
