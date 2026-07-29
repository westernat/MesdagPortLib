package PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes;

import com.google.common.base.Suppliers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.function.Supplier;

public class PortAttributesExtension {
    private static final Supplier<Holder<Attribute>> BLOCK_BREAK_SPEED = Suppliers.memoize(() -> PortLib.BLOCK_BREAK_SPEED);
    private static final Supplier<Holder<Attribute>> BLOCK_INTERACTION_RANGE = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.BLOCK_REACH));
    private static final Supplier<Holder<Attribute>> BURNING_TIME = Suppliers.memoize(() -> PortLib.BURNING_TIME);
    private static final Supplier<Holder<Attribute>> EXPLOSION_KNOCKBACK_RESISTANCE = Suppliers.memoize(() -> PortLib.EXPLOSION_KNOCKBACK_RESISTANCE);
    private static final Supplier<Holder<Attribute>> ENTITY_INTERACTION_RANGE = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_REACH));
    private static final Supplier<Holder<Attribute>> FALL_DAMAGE_MULTIPLIER = Suppliers.memoize(() -> PortLib.FALL_DAMAGE_MULTIPLIER);
    private static final Supplier<Holder<Attribute>> FLYING_SPEED = Suppliers.memoize(() -> PortLib.FLYING_SPEED);
    private static final Supplier<Holder<Attribute>> JUMP_STRENGTH = Suppliers.memoize(() -> PortLib.JUMP_STRENGTH);
    private static final Supplier<Holder<Attribute>> GRAVITY = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_GRAVITY));
    private static final Supplier<Holder<Attribute>> MAX_ABSORPTION = Suppliers.memoize(() -> PortLib.MAX_ABSORPTION);
    private static final Supplier<Holder<Attribute>> MINING_EFFICIENCY = Suppliers.memoize(() -> PortLib.MINING_EFFICIENCY);
    private static final Supplier<Holder<Attribute>> MOVEMENT_EFFICIENCY = Suppliers.memoize(() -> PortLib.MOVEMENT_EFFICIENCY);
    private static final Supplier<Holder<Attribute>> OXYGEN_BONUS = Suppliers.memoize(() -> PortLib.OXYGEN_BONUS);
    private static final Supplier<Holder<Attribute>> SAFE_FALL_DISTANCE = Suppliers.memoize(() -> PortLib.SAFE_FALL_DISTANCE);
    private static final Supplier<Holder<Attribute>> SCALE = Suppliers.memoize(() -> PortLib.SCALE);
    private static final Supplier<Holder<Attribute>> SNEAKING_SPEED = Suppliers.memoize(() -> PortLib.SNEAKING_SPEED);
    private static final Supplier<Holder<Attribute>> STEP_HEIGHT = Suppliers.memoize(() -> PortLib.STEP_HEIGHT);
    private static final Supplier<Holder<Attribute>> SUBMERGED_MINING_SPEED = Suppliers.memoize(() -> PortLib.SUBMERGED_MINING_SPEED);
    private static final Supplier<Holder<Attribute>> SWEEPING_DAMAGE_RATIO = Suppliers.memoize(() -> PortLib.SWEEPING_DAMAGE_RATIO);
    private static final Supplier<Holder<Attribute>> WATER_MOVEMENT_EFFICIENCY = Suppliers.memoize(() -> PortLib.WATER_MOVEMENT_EFFICIENCY);
    private static final Supplier<Holder<Attribute>> SWIM_SPEED = Suppliers.memoize(() -> new PortRegistryEntry<>(ForgeMod.SWIM_SPEED));
    private static final Supplier<Holder<Attribute>> CREATIVE_FLIGHT = Suppliers.memoize(() -> PortLib.CREATIVE_FLIGHT);

    public static Holder<Attribute> blockBreakSpeed() {
        return BLOCK_BREAK_SPEED.get();
    }

    public static Holder<Attribute> blockInteractionRange() {
        return BLOCK_INTERACTION_RANGE.get();
    }

    public static Holder<Attribute> burningTime() {
        return BURNING_TIME.get();
    }

    public static Holder<Attribute> explosionKnockbackResistance() {
        return EXPLOSION_KNOCKBACK_RESISTANCE.get();
    }

    public static Holder<Attribute> entityInteractionRange() {
        return ENTITY_INTERACTION_RANGE.get();
    }

    public static Holder<Attribute> fallDamageMultiplier() {
        return FALL_DAMAGE_MULTIPLIER.get();
    }

    public static Holder<Attribute> flyingSpeed() {
        return FLYING_SPEED.get();
    }

    public static Holder<Attribute> jumpStrength() {
        return JUMP_STRENGTH.get();
    }

    public static Holder<Attribute> gravity() {
        return GRAVITY.get();
    }

    public static Holder<Attribute> maxAbsorption() {
        return MAX_ABSORPTION.get();
    }

    public static Holder<Attribute> miningEfficiency() {
        return MINING_EFFICIENCY.get();
    }

    public static Holder<Attribute> movementEfficiency() {
        return MOVEMENT_EFFICIENCY.get();
    }

    public static Holder<Attribute> oxygenBonus() {
        return OXYGEN_BONUS.get();
    }

    public static Holder<Attribute> safeFallDistance() {
        return SAFE_FALL_DISTANCE.get();
    }

    public static Holder<Attribute> scale() {
        return SCALE.get();
    }

    public static Holder<Attribute> sneakingSpeed() {
        return SNEAKING_SPEED.get();
    }

    public static Holder<Attribute> stepHeight() {
        return STEP_HEIGHT.get();
    }

    // Invoked through coremod
    public static Holder<Attribute> submergedMiningSpeed() {
        return SUBMERGED_MINING_SPEED.get();
    }

    public static Holder<Attribute> sweepingDamageRatio() {
        return SWEEPING_DAMAGE_RATIO.get();
    }

    public static Holder<Attribute> waterMovementEfficiency() {
        return WATER_MOVEMENT_EFFICIENCY.get();
    }

    public static Holder<Attribute> swimSpeed() {
        return SWIM_SPEED.get();
    }

    public static Holder<Attribute> creativeFlight() {
        return CREATIVE_FLIGHT.get();
    }
}
