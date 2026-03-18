package org.mesdag.portlib.event.furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortFurnaceFuelBurnTimeEvent extends PortEvent implements IPortCancellableEvent {
    private final FurnaceFuelBurnTimeEvent internal;

    public PortFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent internal) {
        this.internal = internal;
    }

    public ItemStack getItemStack() {
        return internal.getItemStack();
    }

    @Nullable
    public RecipeType<?> getRecipeType() {
        return internal.getRecipeType();
    }

    public void setBurnTime(int burnTime) {
        internal.setBurnTime(burnTime);
    }

    public int getBurnTime() {
        return internal.getBurnTime();
    }

    static {
        PortEventHooks.register(FurnaceFuelBurnTimeEvent.class, PortFurnaceFuelBurnTimeEvent.class, PortFurnaceFuelBurnTimeEvent::new);
    }
}
