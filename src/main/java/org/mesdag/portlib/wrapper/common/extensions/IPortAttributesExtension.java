package org.mesdag.portlib.wrapper.common.extensions;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.function.Supplier;

public interface IPortAttributesExtension {
    Supplier<Holder<Attribute>> BLOCK_BREAK_SPEED = Suppliers.memoize(() -> PortLib.BLOCK_BREAK_SPEED);
    Supplier<Holder<Attribute>> BLOCK_INTERACTION_RANGE = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.BLOCK_REACH));
    Supplier<Holder<Attribute>> BURNING_TIME = Suppliers.memoize(() -> PortLib.BURNING_TIME);
    Supplier<Holder<Attribute>> EXPLOSION_KNOCKBACK_RESISTANCE = Suppliers.memoize(() -> PortLib.EXPLOSION_KNOCKBACK_RESISTANCE);
    Supplier<Holder<Attribute>> ENTITY_INTERACTION_RANGE = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_REACH));
    Supplier<Holder<Attribute>> FALL_DAMAGE_MULTIPLIER = Suppliers.memoize(() -> PortLib.FALL_DAMAGE_MULTIPLIER);
    Supplier<Holder<Attribute>> FLYING_SPEED = Suppliers.memoize(() -> PortLib.FLYING_SPEED);
    Supplier<Holder<Attribute>> JUMP_STRENGTH = Suppliers.memoize(() -> PortLib.JUMP_STRENGTH);
    Supplier<Holder<Attribute>> GRAVITY = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_GRAVITY));
    Supplier<Holder<Attribute>> MAX_ABSORPTION = Suppliers.memoize(() -> PortLib.MAX_ABSORPTION);
    Supplier<Holder<Attribute>> MINING_EFFICIENCY = Suppliers.memoize(() -> PortLib.MINING_EFFICIENCY);
    Supplier<Holder<Attribute>> MOVEMENT_EFFICIENCY = Suppliers.memoize(() -> PortLib.MOVEMENT_EFFICIENCY);
    Supplier<Holder<Attribute>> OXYGEN_BONUS = Suppliers.memoize(() -> PortLib.OXYGEN_BONUS);
    Supplier<Holder<Attribute>> SAFE_FALL_DISTANCE = Suppliers.memoize(() -> PortLib.SAFE_FALL_DISTANCE);
    Supplier<Holder<Attribute>> SCALE = Suppliers.memoize(() -> PortLib.SCALE);
    Supplier<Holder<Attribute>> SNEAKING_SPEED = Suppliers.memoize(() -> PortLib.SNEAKING_SPEED);
    Supplier<Holder<Attribute>> STEP_HEIGHT = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.STEP_HEIGHT_ADDITION));
    Supplier<Holder<Attribute>> SUBMERGED_MINING_SPEED = Suppliers.memoize(() -> PortLib.SUBMERGED_MINING_SPEED);
    Supplier<Holder<Attribute>> SWEEPING_DAMAGE_RATIO = Suppliers.memoize(() -> PortLib.SWEEPING_DAMAGE_RATIO);
    Supplier<Holder<Attribute>> WATER_MOVEMENT_EFFICIENCY = Suppliers.memoize(() -> PortLib.WATER_MOVEMENT_EFFICIENCY);
    Supplier<Holder<Attribute>> SWIM_SPEED = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.SWIM_SPEED));
    Supplier<Holder<Attribute>> CREATIVE_FLIGHT = Suppliers.memoize(() -> PortLib.CREATIVE_FLIGHT);

    static Holder<Attribute> blockBreakSpeed() {
        return BLOCK_BREAK_SPEED.get();
    }

    static Holder<Attribute> blockInteractionRange() {
        return BLOCK_INTERACTION_RANGE.get();
    }

    static Holder<Attribute> burningTime() {
        return BURNING_TIME.get();
    }

    static Holder<Attribute> explosionKnockbackResistance() {
        return EXPLOSION_KNOCKBACK_RESISTANCE.get();
    }

    static Holder<Attribute> entityInteractionRange() {
        return ENTITY_INTERACTION_RANGE.get();
    }

    static Holder<Attribute> fallDamageMultiplier() {
        return FALL_DAMAGE_MULTIPLIER.get();
    }

    static Holder<Attribute> flyingSpeed() {
        return FLYING_SPEED.get();
    }

    static Holder<Attribute> jumpStrength() {
        return JUMP_STRENGTH.get();
    }

    static Holder<Attribute> gravity() {
        return GRAVITY.get();
    }

    static Holder<Attribute> maxAbsorption() {
        return MAX_ABSORPTION.get();
    }

    static Holder<Attribute> miningEfficiency() {
        return MINING_EFFICIENCY.get();
    }

    static Holder<Attribute> movementEfficiency() {
        return MOVEMENT_EFFICIENCY.get();
    }

    static Holder<Attribute> oxygenBonus() {
        return OXYGEN_BONUS.get();
    }

    static Holder<Attribute> safeFallDistance() {
        return SAFE_FALL_DISTANCE.get();
    }

    static Holder<Attribute> scale() {
        return SCALE.get();
    }

    static Holder<Attribute> sneakingSpeed() {
        return SNEAKING_SPEED.get();
    }

    static Holder<Attribute> stepHeight() {
        return STEP_HEIGHT.get();
    }

    // Invoked through coremod
    static Holder<Attribute> submergedMiningSpeed() {
        return SUBMERGED_MINING_SPEED.get();
    }

    static Holder<Attribute> sweepingDamageRatio() {
        return SWEEPING_DAMAGE_RATIO.get();
    }

    static Holder<Attribute> waterMovementEfficiency() {
        return WATER_MOVEMENT_EFFICIENCY.get();
    }

    static Holder<Attribute> swimSpeed() {
        return SWIM_SPEED.get();
    }

    static Holder<Attribute> creativeFlight() {
        return CREATIVE_FLIGHT.get();
    }
}
