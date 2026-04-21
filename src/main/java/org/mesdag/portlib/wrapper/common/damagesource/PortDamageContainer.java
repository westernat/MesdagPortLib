package org.mesdag.portlib.wrapper.common.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.mesdag.portlib.diff.Diff;

public class PortDamageContainer {
    private final DamageContainer container;

    @Diff
    public PortDamageContainer(DamageContainer container) {
        this.container = container;
    }

    @Diff
    public DamageContainer unwrap() {
        return container;
    }

    public float getOriginalDamage() {
        return container.getOriginalDamage();
    }

    public DamageSource getSource() {
        return container.getSource();
    }

    public void setNewDamage(float damage) {
        container.setNewDamage(damage);
    }

    public float getNewDamage() {
        return container.getNewDamage();
    }

    public void addModifier(PortReduction type, IPortReductionFunction reductionFunction) {
        container.addModifier(type.unwrap(), reductionFunction.unwrap());
    }

    public float getBlockedDamage() {
        return container.getBlockedDamage();
    }

    public float getShieldDamage() {
        return container.getShieldDamage();
    }

    public void setPostAttackInvulnerabilityTicks(int ticks) {
        container.setPostAttackInvulnerabilityTicks(ticks);
    }

    public int getPostAttackInvulnerabilityTicks() {
        return container.getPostAttackInvulnerabilityTicks();
    }

    public float getReduction(PortReduction type) {
        return container.getReduction(type.unwrap());
    }

    @Diff
    public void setReduction(PortReduction reduction, float amount) {}

    public enum PortReduction {
        INVULNERABILITY,
        ARMOR,
        ENCHANTMENTS,
        MOB_EFFECTS,
        ABSORPTION,
        INNATE_RESISTANCE;

        @Diff
        public DamageContainer.Reduction unwrap() {
            return switch (this) {
                case INVULNERABILITY -> DamageContainer.Reduction.INVULNERABILITY;
                case ARMOR -> DamageContainer.Reduction.ARMOR;
                case ENCHANTMENTS -> DamageContainer.Reduction.ENCHANTMENTS;
                case MOB_EFFECTS -> DamageContainer.Reduction.MOB_EFFECTS;
                case ABSORPTION -> DamageContainer.Reduction.ABSORPTION;
                case INNATE_RESISTANCE -> DamageContainer.Reduction.INNATE_RESISTANCE;
            };
        }
    }
}
