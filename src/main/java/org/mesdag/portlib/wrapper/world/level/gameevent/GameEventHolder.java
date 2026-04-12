package org.mesdag.portlib.wrapper.world.level.gameevent;

import net.minecraft.core.Holder;
import net.minecraft.world.level.gameevent.GameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class GameEventHolder implements PortHolder<GameEvent> {
    private final Holder<GameEvent> delegate;

    private GameEventHolder(Holder<GameEvent> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Holder<GameEvent> delegate() {
        return delegate;
    }

    @Diff
    public static GameEventHolder wrap(Holder<GameEvent> delegate) {
        return new GameEventHolder(delegate);
    }
}
