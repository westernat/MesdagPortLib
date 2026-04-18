package org.mesdag.portlib.event.entity.player;

import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortTradeWithVillagerEvent extends PortPlayerEvent<TradeWithVillagerEvent> {
    @Diff
    public PortTradeWithVillagerEvent(TradeWithVillagerEvent e) {
        super(e);
    }

    public MerchantOffer getMerchantOffer() {
        return e.getMerchantOffer();
    }

    public AbstractVillager getAbstractVillager() {
        return e.getAbstractVillager();
    }

    static {
        PortEventHooks.register();
    }
}
