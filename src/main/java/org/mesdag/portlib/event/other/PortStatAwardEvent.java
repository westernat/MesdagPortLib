package org.mesdag.portlib.event.other;

import net.minecraft.stats.Stat;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;

@Cancelable
public class PortStatAwardEvent extends PlayerEvent {
    private Stat<?> stat;
    private int value;

    @Diff
    public PortStatAwardEvent(Player player, Stat<?> stat, int value) {
        super(player);
        this.stat = stat;
        this.value = value;
    }

    public Stat<?> getStat() {
        return stat;
    }

    public void setStat(Stat<?> stat) {
        this.stat = stat;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
