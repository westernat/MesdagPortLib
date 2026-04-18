package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraftforge.event.level.PistonEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortPistonEvent<E extends PistonEvent> extends PortBlockEvent<E> {
    @Diff
    public PortPistonEvent(E e) {
        super(e);
    }

    public Direction getDirection() {
        return e.getDirection();
    }

    public BlockPos getFaceOffsetPos() {
        return e.getFaceOffsetPos();
    }

    public PortPistonMoveType getPistonMoveType() {
        return PortPistonMoveType.wrap(e.getPistonMoveType());
    }

    public @Nullable PistonStructureResolver getStructureHelper() {
        return e.getStructureHelper();
    }

    public static class PortPost extends PortPistonEvent<PistonEvent.Post> {
        @Diff
        public PortPost(PistonEvent.Post e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPre extends PortPistonEvent<PistonEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(PistonEvent.Pre e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public enum PortPistonMoveType {
        EXTEND(true),
        RETRACT(false);

        public final boolean isExtend;

        PortPistonMoveType(boolean isExtend) {
            this.isExtend = isExtend;
        }

        @Diff
        public PistonEvent.PistonMoveType unwrap() {
            return this == EXTEND ? PistonEvent.PistonMoveType.EXTEND : PistonEvent.PistonMoveType.RETRACT;
        }

        @Diff
        public static PortPistonMoveType wrap(PistonEvent.PistonMoveType type) {
            return type == PistonEvent.PistonMoveType.EXTEND ? EXTEND : RETRACT;
        }
    }
}
