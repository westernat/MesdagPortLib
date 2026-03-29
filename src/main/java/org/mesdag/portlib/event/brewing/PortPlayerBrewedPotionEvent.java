package org.mesdag.portlib.event.brewing;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public class PortPlayerBrewedPotionEvent extends PortPlayerEvent<PlayerBrewedPotionEvent> {
    @Diff
    public PortPlayerBrewedPotionEvent(PlayerBrewedPotionEvent e) {
        super(e);
    }

    public ItemStack getStack() {
        return e.getStack();
    }

    static {
        PortEventHooks.register(PlayerBrewedPotionEvent.class, PortPlayerBrewedPotionEvent.class, PortPlayerBrewedPotionEvent::new);
    }
}
