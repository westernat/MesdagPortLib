package org.mesdag.portlib.event.brewing;

import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.event.entity.player.PortPlayerEvent;

public abstract class PortPlayerBrewedPotionEvent extends PortPlayerEvent {
    public abstract ItemStack getStack();
}
