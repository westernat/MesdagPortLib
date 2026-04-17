package org.mesdag.portlib.event.entity.client.event;


import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.IItemDecorator;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterItemDecorationsEvent extends PortEvent {
    private final RegisterItemDecorationsEvent e;

    @Diff
    public PortRegisterItemDecorationsEvent(RegisterItemDecorationsEvent e) {
        super(e);
        this.e = e;
    }

    public void register(ItemLike itemLike, IItemDecorator decorator) {
        e.register(itemLike, decorator);
    }

    static {
        PortEventHooks.register(RegisterItemDecorationsEvent.class, PortRegisterItemDecorationsEvent.class, PortRegisterItemDecorationsEvent::new);
    }
}