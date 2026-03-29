package org.mesdag.portlib.event.entity.player;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortItemTooltipEvent extends PortPlayerEvent<ItemTooltipEvent> {
    @Diff
    public PortItemTooltipEvent(ItemTooltipEvent e) {
        super(e);
    }

    public TooltipFlag getFlags() {
        return e.getFlags();
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public List<Component> getToolTip() {
        return e.getToolTip();
    }

    @Override
    public @Nullable Player getEntity() {
        return e.getEntity();
    }

    public TooltipContext getContext() {
        return e.getContext();
    }

    static {
        PortEventHooks.register(ItemTooltipEvent.class, PortItemTooltipEvent.class, PortItemTooltipEvent::new);
    }
}
