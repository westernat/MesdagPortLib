package org.mesdag.portlib.event.entity.item;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortItemTossEvent extends PortItemEvent<ItemTossEvent> implements IPortCancellableEvent {
    @Diff
    public PortItemTossEvent(ItemTossEvent e) {
        super(e);
    }

    public Player getPlayer() {
        return e.getPlayer();
    }

    static {
        PortEventHooks.register();
    }
}
