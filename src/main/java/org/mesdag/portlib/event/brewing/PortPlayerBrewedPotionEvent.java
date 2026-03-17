package org.mesdag.portlib.event.brewing;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public abstract class PortPlayerBrewedPotionEvent extends PortPlayerEvent {
    public abstract ItemStack getStack();

    static {
        PortEventHooks.register(PlayerBrewedPotionEvent.class, PortPlayerBrewedPotionEvent.class, e -> new PortPlayerBrewedPotionEvent() {
            @Override
            public ItemStack getStack() {
                return e.getStack();
            }

            @Override
            public Player getEntity() {
                return e.getEntity();
            }
        });
    }
}
