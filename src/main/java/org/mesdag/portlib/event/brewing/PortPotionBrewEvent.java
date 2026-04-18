package org.mesdag.portlib.event.brewing;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.brewing.PotionBrewEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortPotionBrewEvent extends PortEvent<PotionBrewEvent> {
    @Diff
    public PortPotionBrewEvent(PotionBrewEvent e) {
        super(e);
    }

    public ItemStack getItem(int index) {
        return e.getItem(index);
    }

    public void setItem(int index, ItemStack stack) {
        e.setItem(index, stack);
    }

    public int getLength() {
        return e.getLength();
    }

    public static class PortPre extends PortPotionBrewEvent implements IPortCancellableEvent {
        @Diff
        public PortPre(PotionBrewEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPost extends PortPotionBrewEvent {
        @Diff
        public PortPost(PotionBrewEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
