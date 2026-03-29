package org.mesdag.portlib.event.other;

import net.minecraft.world.Difficulty;
import net.neoforged.neoforge.event.DifficultyChangeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortDifficultyChangeEvent extends PortEvent<DifficultyChangeEvent> {
    @Diff
    public PortDifficultyChangeEvent(DifficultyChangeEvent e) {
        super(e);
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
