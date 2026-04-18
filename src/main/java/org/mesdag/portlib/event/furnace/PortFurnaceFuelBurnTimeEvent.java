package org.mesdag.portlib.event.furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortFurnaceFuelBurnTimeEvent extends PortEvent<FurnaceFuelBurnTimeEvent> implements IPortCancellableEvent {
    @Diff
    public PortFurnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent e) {
        super(e);
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public @Nullable RecipeType<?> getRecipeType() {
        return e.getRecipeType();
    }

    public void setBurnTime(int burnTime) {
        e.setBurnTime(burnTime);
    }

    public int getBurnTime() {
        return e.getBurnTime();
    }

    static {
        PortEventHooks.register();
    }
}
