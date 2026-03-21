package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerDestroyItemEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortPlayerDestroyItemEvent extends PortPlayerEvent {
    private final PlayerDestroyItemEvent e;

    @Diff
    public PortPlayerDestroyItemEvent(PlayerDestroyItemEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public ItemStack getOriginal() {
        return e.getOriginal();
    }

    @Nullable
    public InteractionHand getHand() {
        return e.getHand();
    }

    static {
        PortEventHooks.register(PlayerDestroyItemEvent.class, PortPlayerDestroyItemEvent.class, PortPlayerDestroyItemEvent::new);
    }
}
