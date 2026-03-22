package org.mesdag.portlib.event;

import net.minecraft.world.Difficulty;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import org.mesdag.portlib.diff.Diff;


public class PortDifficultyChangeEvent extends PortEvent {
    private final DifficultyChangeEvent e;

    @Diff
    public PortDifficultyChangeEvent(DifficultyChangeEvent e) {
        super();
        this.e = e;
    }

    public Difficulty getDifficulty() {
        return e.getDifficulty();
    }

    public Difficulty getOldDifficulty() {
        return e.getOldDifficulty();
    }

    static {
        PortEventHooks.register(DifficultyChangeEvent.class, PortDifficultyChangeEvent.class, PortDifficultyChangeEvent::new);
    }
}