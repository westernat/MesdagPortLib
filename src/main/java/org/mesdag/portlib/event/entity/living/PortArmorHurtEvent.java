package org.mesdag.portlib.event.entity.living;

import com.google.common.collect.Maps;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.ArmorHurtEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.Map;

public class PortArmorHurtEvent extends PortLivingEvent<ArmorHurtEvent> implements IPortCancellableEvent {
    @Diff
    public PortArmorHurtEvent(ArmorHurtEvent e) {
        super(e);
    }

    public ItemStack getArmorItemStack(EquipmentSlot slot) {
        return e.getArmorItemStack(slot);
    }

    public Float getOriginalDamage(EquipmentSlot slot) {
        return e.getOriginalDamage(slot);
    }

    public Float getNewDamage(EquipmentSlot slot) {
        return e.getNewDamage(slot);
    }

    public void setNewDamage(EquipmentSlot slot, float damage) {
        e.setNewDamage(slot, damage);
    }

    public Map<EquipmentSlot, PortArmorEntry> getArmorMap() {
        return Maps.transformValues(e.getArmorMap(), PortArmorEntry::wrap);
    }

    public DamageSource getDamageSource() {
        return e.getDamageSource();
    }

    public static class PortArmorEntry {
        public ItemStack armorItemStack;
        public final float originalDamage;
        public float newDamage;

        public PortArmorEntry(ItemStack armorStack, float damageIn) {
            this.armorItemStack = armorStack;
            this.originalDamage = damageIn;
            this.newDamage = damageIn;
        }

        @Diff
        public ArmorHurtEvent.ArmorEntry unwrap() {
            ArmorHurtEvent.ArmorEntry entry = new ArmorHurtEvent.ArmorEntry(armorItemStack, originalDamage);
            entry.newDamage = newDamage;
            return entry;
        }

        @Diff
        public static PortArmorEntry wrap(ArmorHurtEvent.ArmorEntry entry) {
            PortArmorEntry entry1 = new PortArmorEntry(entry.armorItemStack, entry.originalDamage);
            entry1.newDamage = entry.newDamage;
            return entry1;
        }
    }

    static {
        PortEventHooks.register();
    }
}
