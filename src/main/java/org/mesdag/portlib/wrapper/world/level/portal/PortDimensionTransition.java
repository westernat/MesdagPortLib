package org.mesdag.portlib.wrapper.world.level.portal;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.UnaryOperator;

public record PortDimensionTransition(
        ServerLevel newLevel,
        UnaryOperator<Vec3> pos,
        UnaryOperator<Vec3> speed,
        FloatUnaryOperator yRot,
        FloatUnaryOperator xRot,
        boolean missingRespawnBlock,
        PortPostDimensionTransition postDimensionTransition
) {
    public static final PortPostDimensionTransition DO_NOTHING = entity -> {};

    public PortDimensionTransition(ServerLevel newLevel, boolean missingRespawnBlock) {
        this(newLevel, UnaryOperator.identity(), UnaryOperator.identity(), FloatUnaryOperator.identity(), FloatUnaryOperator.identity(), missingRespawnBlock, DO_NOTHING);
    }

    public PortDimensionTransition(
            ServerLevel newLevel,
            UnaryOperator<Vec3> pos,
            UnaryOperator<Vec3> speed,
            FloatUnaryOperator yRot,
            FloatUnaryOperator xRot,
            PortPostDimensionTransition postDimensionTransition
    ) {
        this(newLevel, pos, speed, yRot, xRot, false, postDimensionTransition);
    }

    @FunctionalInterface
    public interface PortPostDimensionTransition {
        void onTransition(Entity entity);

        default PortPostDimensionTransition then(PortPostDimensionTransition transition) {
            return entity -> {
                onTransition(entity);
                transition.onTransition(entity);
            };
        }
    }
}
