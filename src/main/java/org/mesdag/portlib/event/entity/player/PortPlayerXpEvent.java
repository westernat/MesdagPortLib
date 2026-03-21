package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortPlayerXpEvent extends PortPlayerEvent {
    public PortPlayerXpEvent(Player player) {
        super(player);
    }

    public static class PortPickupXp extends PortPlayerXpEvent implements IPortCancellableEvent {
        private final PlayerXpEvent.PickupXp e;

        @Diff
        public PortPickupXp(PlayerXpEvent.PickupXp e) {
            super(e.getEntity());
            this.e = e;
        }

        public ExperienceOrb getOrb() {
            return e.getOrb();
        }

        static {
            PortEventHooks.register(PlayerXpEvent.PickupXp.class, PortPickupXp.class, PortPickupXp::new);
        }
    }

    public static class PortXpChange extends PortPlayerXpEvent implements IPortCancellableEvent {
        private final PlayerXpEvent.XpChange e;

        @Diff
        public PortXpChange(PlayerXpEvent.XpChange e) {
            super(e.getEntity());
            this.e = e;
        }

        public int getAmount() {
            return e.getAmount();
        }

        public void setAmount(int amount) {
            e.setAmount(amount);
        }

        static {
            PortEventHooks.register(PlayerXpEvent.XpChange.class, PortXpChange.class, PortXpChange::new);
        }
    }

    public static class PortLevelChange extends PortPlayerXpEvent implements IPortCancellableEvent {
        private final PlayerXpEvent.LevelChange e;

        @Diff
        public PortLevelChange(PlayerXpEvent.LevelChange e) {
            super(e.getEntity());
            this.e = e;
        }

        public int getLevels() {
            return e.getLevels();
        }

        public void setLevels(int levels) {
            e.setLevels(levels);
        }

        static {
            PortEventHooks.register(PlayerXpEvent.LevelChange.class, PortLevelChange.class, PortLevelChange::new);
        }
    }
}
