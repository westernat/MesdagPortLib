package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;

@Cancelable
public class PortSweepAttackEvent extends PlayerEvent {
    private final Entity target;
    private final boolean isVanillaSweep;

    private boolean isSweeping;

    @Diff
    public PortSweepAttackEvent(Player player, Entity target, boolean isVanillaSweep) {
        super(player);
        this.target = target;
        this.isSweeping = this.isVanillaSweep = isVanillaSweep;
    }

    public Entity getTarget() {
        return target;
    }

    public boolean isVanillaSweep() {
        return isVanillaSweep;
    }

    public boolean isSweeping() {
        return isSweeping;
    }

    public void setSweeping(boolean sweep) {
        isSweeping = sweep;
    }
}
