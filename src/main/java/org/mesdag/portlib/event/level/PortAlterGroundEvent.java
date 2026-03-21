package org.mesdag.portlib.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.neoforged.neoforge.event.level.AlterGroundEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortAlterGroundEvent extends PortEvent {
    private final AlterGroundEvent e;

    @Diff
    public PortAlterGroundEvent(AlterGroundEvent e) {
        this.e = e;
    }

    public TreeDecorator.Context getContext() {
        return e.getContext();
    }

    public List<BlockPos> getPositions() {
        return e.getPositions();
    }

    public PortStateProvider getStateProvider() {
        return PortStateProvider.wrap(e.getStateProvider());
    }

    public void setStateProvider(PortStateProvider provider) {
        e.setStateProvider(provider.unwrap());
    }

    @FunctionalInterface
    public interface PortStateProvider {
        BlockState getState(RandomSource random, BlockPos state);

        @Diff
        default AlterGroundEvent.StateProvider unwrap() {
            return this::getState;
        }

        @Diff
        static PortStateProvider wrap(AlterGroundEvent.StateProvider provider) {
            return new Delegate(provider);
        }

        @Diff
        record Delegate(AlterGroundEvent.StateProvider delegate) implements PortStateProvider {
            @Override
            public BlockState getState(RandomSource random, BlockPos state) {
                return delegate.getState(random, state);
            }

            @Override
            public AlterGroundEvent.StateProvider unwrap() {
                return delegate;
            }
        }
    }

    static {
        PortEventHooks.register(AlterGroundEvent.class, PortAlterGroundEvent.class, PortAlterGroundEvent::new);
    }
}
