package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mesdag.portlib.diff.IPortRebuildTask;
import org.mesdag.portlib.diff.IPortRenderRegionCache;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortAddSectionGeometryEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//@Mixin(ChunkRenderDispatcher.class)
public abstract class ChunkRenderDispatcherMixin {
    @Mixin(ChunkRenderDispatcher.RenderChunk.class)
    public abstract static class RenderChunkMixin {
        @Shadow
        @Final
        BlockPos.MutableBlockPos origin;

        @WrapOperation(method = "createCompileTask", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/RenderRegionCache;createRegion(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;"))
        private RenderChunkRegion gatherAdditionalRenderers(
                RenderRegionCache instance,
                Level level,
                BlockPos start,
                BlockPos end,
                int padding,
                Operation<RenderChunkRegion> original,
                @Share("additionalRenderers") LocalRef<List<PortAddSectionGeometryEvent.PortAdditionalSectionRenderer>> additionalRenderers
        ) {
            var event = new PortAddSectionGeometryEvent(origin, level);
            PortEventHandler.postEvent(event);
            var renderers = event.getAdditionalRenderers();
            IPortRenderRegionCache.of(instance).portlib$setNullForEmpty(renderers.isEmpty());
            additionalRenderers.set(renderers);
            return original.call(instance, level, start, end, padding);
        }

        @ModifyExpressionValue(method = "createCompileTask", at = @At(value = "NEW", target = "net/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk$RebuildTask"))
        private ChunkRenderDispatcher.RenderChunk.RebuildTask store(
                ChunkRenderDispatcher.RenderChunk.RebuildTask original,
                @Share("additionalRenderers") LocalRef<List<PortAddSectionGeometryEvent.PortAdditionalSectionRenderer>> additionalRenderers
        ) {
            IPortRebuildTask.of(original).portlib$setAdditionalRenderers(additionalRenderers.get());
            return original;
        }

        @Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
        public abstract static class RebuildTaskMixin implements IPortRebuildTask {
            @Shadow
            @Nullable
            protected RenderChunkRegion region;
            @Unique
            private List<PortAddSectionGeometryEvent.PortAdditionalSectionRenderer> portlib$additionalRenderers = List.of();

            @Override
            public void portlib$setAdditionalRenderers(List<PortAddSectionGeometryEvent.PortAdditionalSectionRenderer> additionalRenderers) {
                this.portlib$additionalRenderers = additionalRenderers;
            }

            @WrapOperation(method = "compile", at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"))
            private <E> Iterator<E> addAdditionalGeometry(
                    Set<E> instance,
                    Operation<Iterator<E>> original,
                    @Local(argsOnly = true) ChunkBufferBuilderPack pack,
                    @Local(name = "posestack") PoseStack posestack
            ) {
                if (portlib$additionalRenderers.isEmpty()) {
                    return original.call(instance);
                }
                var context = new PortAddSectionGeometryEvent.PortSectionRenderingContext(
                        type -> {
                            instance.add((E) type);
                            return pack.builder(type);
                        },
                        region,
                        posestack
                );
                for (var renderer : portlib$additionalRenderers) {
                    renderer.render(context);
                }
                return original.call(instance);
            }
        }
    }
}
