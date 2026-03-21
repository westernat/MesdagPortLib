package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.neoforge.event.entity.player.TradeWithVillagerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortTradeWithVillagerEvent extends PortPlayerEvent {
    private final TradeWithVillagerEvent e;

    @Diff
    public PortTradeWithVillagerEvent(TradeWithVillagerEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public MerchantOffer getMerchantOffer() {
        return e.getMerchantOffer();
    }

    public AbstractVillager getAbstractVillager() {
        return e.getAbstractVillager();
    }

    static {
        PortEventHooks.register(TradeWithVillagerEvent.class, PortTradeWithVillagerEvent.class, PortTradeWithVillagerEvent::new);
    }
}
