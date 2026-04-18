package org.mesdag.portlib.wrapper.client.model.lighting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.lighting.QuadLighter;
import org.mesdag.portlib.diff.Diff;

public class PortQuadLighter {
    private final QuadLighter delegate;

    @Diff
    public PortQuadLighter(QuadLighter delegate) {
        this.delegate = delegate;
    }

    public void setup(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        delegate.setup(level, pos, state);
    }

    public final void reset() {
        delegate.reset();
    }

    public final void process(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad quad, int overlay) {
        delegate.process(consumer, pose, quad, overlay);
    }

    public static float calculateShade(float normalX, float normalY, float normalZ, boolean constantAmbientLight) {
        return QuadLighter.calculateShade(normalX, normalY, normalZ, constantAmbientLight);
    }
}
