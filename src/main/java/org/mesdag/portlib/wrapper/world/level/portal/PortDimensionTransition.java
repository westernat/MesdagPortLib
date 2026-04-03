package org.mesdag.portlib.wrapper.world.level.portal;

import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.Diff;

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

    @Diff
    public DimensionTransition unwrap() {
        return new DimensionTransition(
                newLevel,
                pos.apply(Vec3.ZERO),
                speed.apply(Vec3.ZERO),
                yRot.apply(0),
                xRot.apply(0),
                missingRespawnBlock,
                postDimensionTransition.unwrap()
        );
    }

    @Diff
    public static PortDimensionTransition wrap(DimensionTransition transition) {
        return new PortDimensionTransition(
                transition.newLevel(),
                v -> transition.pos(),
                v -> transition.speed(),
                f -> transition.yRot(),
                f -> transition.xRot(),
                transition.missingRespawnBlock(),
                PortPostDimensionTransition.wrap(transition.postDimensionTransition())
        );
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

        @Diff
        default DimensionTransition.PostDimensionTransition unwrap() {
            return this::onTransition;
        }

        @Diff
        static PortPostDimensionTransition wrap(DimensionTransition.PostDimensionTransition delegate) {
            return delegate::onTransition;
        }
    }
}
