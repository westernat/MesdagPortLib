package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingGetProjectileEvent extends PortLivingEvent<LivingGetProjectileEvent> {
    @Diff
    public PortLivingGetProjectileEvent(LivingGetProjectileEvent e) {
        super(e);
    }

    public ItemStack getProjectileWeaponItemStack() {
        return e.getProjectileWeaponItemStack();
    }

    public ItemStack getProjectileItemStack() {
        return e.getProjectileItemStack();
    }

    public void setProjectileItemStack(ItemStack projectileItemStack) {
        e.setProjectileItemStack(projectileItemStack);
    }

    static {
        PortEventHooks.register();
    }
}
