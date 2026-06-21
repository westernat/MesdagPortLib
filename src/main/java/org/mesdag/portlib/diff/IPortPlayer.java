package org.mesdag.portlib.diff;

import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.extensions.IPortPlayerExtension;

public interface IPortPlayer extends PortSelfGetter<Player>, IPortPlayerExtension {
    int portlib$getModelCustomisation();

    static IPortPlayer of(Player player) {
        return (IPortPlayer) player;
    }
}
