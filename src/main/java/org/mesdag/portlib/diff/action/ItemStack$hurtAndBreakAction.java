package org.mesdag.portlib.diff.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.entity.living.PortArmorHurtEvent;

import java.util.function.Consumer;

@Diff
public class ItemStack$hurtAndBreakAction {
    private ItemStack instance;
    private int amount;
    private LivingEntity entity;
    private Consumer<? extends LivingEntity> onBroken;
    private Operation<Void> original;

    public ItemStack$hurtAndBreakAction() {}

    public <T extends LivingEntity> void prepare(
            ItemStack instance,
            int amount,
            T entity,
            Consumer<T> onBroken,
            Operation<Void> original
    ) {
        this.instance = instance;
        this.amount = amount;
        this.entity = entity;
        this.onBroken = onBroken;
        this.original = original;
    }

    public PortArmorHurtEvent.ArmorEntry toEntry() {
        return new PortArmorHurtEvent.ArmorEntry(instance, amount);
    }

    public boolean notPrepared() {
        return instance == null;
    }

    public void call(ItemStack armorItemStack, int newDamage) {
        original.call(armorItemStack, newDamage, entity, onBroken);
    }
}
