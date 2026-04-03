package org.mesdag.portlib.diff;

import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.wrapper.PortSelfGetter;

public interface IPortPlayer extends PortSelfGetter<Player> {
    int portlib$getModelCustomisation();

    static IPortPlayer of(Player player) {
        return (IPortPlayer) player;
    }
}
