package org.mesdag.portlib.event.level.block;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.block.CropGrowEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.level.PortBlockEvent;

public abstract class PortCropGrowEvent<E extends CropGrowEvent> extends PortBlockEvent<E> {
    @Diff
    public PortCropGrowEvent(E e) {
        super(e);
    }

    public static class PortPre extends PortCropGrowEvent<CropGrowEvent.Pre> {
        @Diff
        public PortPre(CropGrowEvent.Pre e) {
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
            public CropGrowEvent.Pre.Result unwrap() {
                return switch (this) {
                    case GROW -> CropGrowEvent.Pre.Result.GROW;
                    case DEFAULT -> CropGrowEvent.Pre.Result.DEFAULT;
                    case DO_NOT_GROW -> CropGrowEvent.Pre.Result.DO_NOT_GROW;
                };
            }

            @Diff
            public static PortResult wrap(CropGrowEvent.Pre.Result result) {
                return switch (result) {
                    case GROW -> GROW;
                    case DEFAULT -> DEFAULT;
                    case DO_NOT_GROW -> DO_NOT_GROW;
                };
            }
        }

        static {
            PortEventHooks.register(CropGrowEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortCropGrowEvent<CropGrowEvent.Post> {
        @Diff
        public PortPost(CropGrowEvent.Post e) {
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
            PortEventHooks.register(CropGrowEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
