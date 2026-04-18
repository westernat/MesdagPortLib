package org.mesdag.portlib.event.level.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.level.PortBlockEvent;

public abstract class PortCropGrowEvent<E extends BlockEvent.CropGrowEvent> extends PortBlockEvent<E> {
    @Diff
    public PortCropGrowEvent(E e) {
        super(e);
    }

    public static class PortPre extends PortCropGrowEvent<BlockEvent.CropGrowEvent.Pre> {
        @Diff
        public PortPre(BlockEvent.CropGrowEvent.Pre e) {
            super(e);
        }

        public void setPortResult(PortResult result) {
            e.setResult(result.unwrap());
        }

        public PortResult getPortResult() {
            return PortResult.wrap(e.getResult());
        }

        public enum PortResult {
            GROW,
            DEFAULT,
            DO_NOT_GROW;

            @Diff
            public Result unwrap() {
                return switch (this) {
                    case GROW -> Result.ALLOW;
                    case DEFAULT -> Result.DEFAULT;
                    case DO_NOT_GROW -> Result.DENY;
                };
            }

            @Diff
            public static PortResult wrap(Result result) {
                return switch (result) {
                    case ALLOW -> GROW;
                    case DEFAULT -> DEFAULT;
                    case DENY -> DO_NOT_GROW;
                };
            }
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortCropGrowEvent<BlockEvent.CropGrowEvent.Post> {
        @Diff
        public PortPost(BlockEvent.CropGrowEvent.Post e) {
            super(e);
        }

        public BlockState getOriginalState() {
            return e.getOriginalState();
        }

        @Override
        public BlockState getState() {
            return e.getState();
        }

        static {
            PortEventHooks.register();
        }
    }
}
