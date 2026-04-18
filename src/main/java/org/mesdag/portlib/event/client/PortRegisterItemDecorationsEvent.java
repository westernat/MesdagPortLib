package org.mesdag.portlib.event.client;

import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterItemDecorationsEvent extends PortEvent<RegisterItemDecorationsEvent> {
    @Diff
    public PortRegisterItemDecorationsEvent(RegisterItemDecorationsEvent e) {
        super(e);
    }

    public void register(ItemLike itemLike, IItemDecorator decorator) {
        e.register(itemLike, decorator);
    }

    static {
        PortEventHooks.register();
    }
}
