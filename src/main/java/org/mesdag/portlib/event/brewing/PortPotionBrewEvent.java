package org.mesdag.portlib.event.brewing;

import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;

public abstract class PortPotionBrewEvent extends PortEvent {
    public abstract ItemStack getItem(int index);

    public abstract void setItem(int index, ItemStack stack);

    public abstract int getLength();

    public abstract static class PortPre extends PortPotionBrewEvent implements IPortCancellableEvent {}

    public abstract static class PortPost extends PortPotionBrewEvent {}
}
