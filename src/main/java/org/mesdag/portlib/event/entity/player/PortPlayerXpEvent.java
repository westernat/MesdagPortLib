package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortPlayerXpEvent<E extends PlayerXpEvent> extends PortPlayerEvent<E> {
    public PortPlayerXpEvent(E e) {
        super(e);
    }

    public static class PortPickupXp extends PortPlayerXpEvent<PlayerXpEvent.PickupXp> implements IPortCancellableEvent {
        @Diff
        public PortPickupXp(PlayerXpEvent.PickupXp e) {
            super(e);
        }

        public ExperienceOrb getOrb() {
            return e.getOrb();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortXpChange extends PortPlayerXpEvent<PlayerXpEvent.XpChange> implements IPortCancellableEvent {
        @Diff
        public PortXpChange(PlayerXpEvent.XpChange e) {
            super(e);
        }

        public int getAmount() {
            return e.getAmount();
        }

        public void setAmount(int amount) {
            e.setAmount(amount);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortLevelChange extends PortPlayerXpEvent<PlayerXpEvent.LevelChange> implements IPortCancellableEvent {
        @Diff
        public PortLevelChange(PlayerXpEvent.LevelChange e) {
            super(e);
        }

        public int getLevels() {
            return e.getLevels();
        }

        public void setLevels(int levels) {
            e.setLevels(levels);
        }

        static {
            PortEventHooks.register();
        }
    }
}
