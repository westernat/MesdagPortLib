package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nonnegative;

public class PortItemFishedEvent extends PortPlayerEvent implements IPortCancellableEvent {
    private final ItemFishedEvent e;

    @Diff
    public PortItemFishedEvent(ItemFishedEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public int getRodDamage() {
        return e.getRodDamage();
    }

    public void damageRodBy(@Nonnegative int rodDamage) {
        e.damageRodBy(rodDamage);
    }

    public NonNullList<ItemStack> getDrops() {
        return e.getDrops();
    }

    public FishingHook getHookEntity() {
        return e.getHookEntity();
    }

    static {
        PortEventHooks.register(ItemFishedEvent.class, PortItemFishedEvent.class, PortItemFishedEvent::new);
    }
}
