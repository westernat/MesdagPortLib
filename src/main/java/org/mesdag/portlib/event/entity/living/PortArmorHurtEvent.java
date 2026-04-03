package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.mesdag.portlib.diff.Diff;

import java.util.EnumMap;
import java.util.Map;

@Cancelable
public class PortArmorHurtEvent extends LivingEvent {
    private final DamageSource source;
    private final EnumMap<EquipmentSlot, PortArmorEntry> armorEntries;

    @Diff
    public PortArmorHurtEvent(EnumMap<EquipmentSlot, PortArmorEntry> armorMap, LivingEntity player, DamageSource source) {
        super(player);
        this.armorEntries = armorMap;
        this.source = source;
    }

    public ItemStack getArmorItemStack(EquipmentSlot slot) {
        return armorEntries.containsKey(slot) ? armorEntries.get(slot).armorItemStack : ItemStack.EMPTY;
    }

    public Float getOriginalDamage(EquipmentSlot slot) {
        return armorEntries.containsKey(slot) ? armorEntries.get(slot).originalDamage : 0f;
    }

    public Float getNewDamage(EquipmentSlot slot) {
        return armorEntries.containsKey(slot) ? armorEntries.get(slot).newDamage : 0f;
    }

    public void setNewDamage(EquipmentSlot slot, float damage) {
        if (this.armorEntries.containsKey(slot)) this.armorEntries.get(slot).newDamage = damage;
    }

    public Map<EquipmentSlot, PortArmorEntry> getArmorMap() {
        return armorEntries;
    }

    public DamageSource getDamageSource() {
        return source;
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
    }
}
