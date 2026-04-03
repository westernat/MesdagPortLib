package org.mesdag.portlib.wrapper.common.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import org.mesdag.portlib.diff.Diff;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class PortDamageContainer {
    private final EnumMap<PortReduction, List<IPortReductionFunction>> reductionFunctions = new EnumMap<>(PortReduction.class);
    private final float originalDamage;
    private final DamageSource source;
    private float newDamage;
    private final EnumMap<PortReduction, Float> reductions = new EnumMap<>(PortReduction.class);
    private float blockedDamage = 0f;
    private float shieldDamage = 0;
    private int invulnerabilityTicksAfterAttack = 20;

    @Diff
    public PortDamageContainer(DamageSource source, float originalDamage) {
        this.source = source;
        this.originalDamage = originalDamage;
        this.newDamage = originalDamage;
    }

    public float getOriginalDamage() {
        return originalDamage;
    }

    public DamageSource getSource() {
        return source;
    }

    public void setNewDamage(float damage) {
        this.newDamage = damage;
    }

    public float getNewDamage() {
        return newDamage;
    }

    public void addModifier(PortReduction type, IPortReductionFunction reductionFunction) {
        reductionFunctions.computeIfAbsent(type, a -> new ArrayList<>()).add(reductionFunction);
    }

    public float getBlockedDamage() {
        return blockedDamage;
    }

    public float getShieldDamage() {
        return shieldDamage;
    }

    public void setPostAttackInvulnerabilityTicks(int ticks) {
        this.invulnerabilityTicksAfterAttack = ticks;
    }

    public int getPostAttackInvulnerabilityTicks() {
        return invulnerabilityTicksAfterAttack;
    }

    public float getReduction(PortReduction type) {
        return reductions.getOrDefault(type, 0f);
    }

    @Diff
    public void setReduction(PortReduction reduction, float amount) {
        for (var func : reductionFunctions.getOrDefault(reduction, List.of())) {
            amount = func.modify(this, amount);
        }
        this.reductions.put(reduction, amount);
        this.newDamage -= amount;
    }

    public enum PortReduction {
        INVULNERABILITY,
        ARMOR,
        ENCHANTMENTS,
        MOB_EFFECTS,
        ABSORPTION,
        INNATE_RESISTANCE
    }
}
