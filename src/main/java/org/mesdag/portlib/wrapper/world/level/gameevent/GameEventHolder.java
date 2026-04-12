package org.mesdag.portlib.wrapper.world.level.gameevent;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gameevent.GameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortHolder;

public class GameEventHolder implements PortHolder<GameEvent> {
    private final GameEvent value;
    private final Holder<GameEvent> delegate;

    private GameEventHolder(GameEvent value) {
        this.value = value;
        this.delegate = BuiltInRegistries.GAME_EVENT.wrapAsHolder(value);
    }

    @Override
    public Holder<GameEvent> delegate() {
        return delegate;
    }

    @Override
    public GameEvent value() {
        return value;
    }

    @Diff
    public static GameEventHolder wrap(GameEvent value) {
        return new GameEventHolder(value);
    }
}
