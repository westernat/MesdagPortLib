package PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes;

import cpw.mods.util.Lazy;
import manifold.ext.rt.api.Extension;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.mesdag.portlib.PortLib;

@Extension
public class PortAttributesExtension {
    private static final Lazy<Holder<Attribute>> BLOCK_BREAK_SPEED = Lazy.of(() -> PortLib.BLOCK_BREAK_SPEED);
    private static final Lazy<Holder<Attribute>> BLOCK_INTERACTION_RANGE = Lazy.of(() -> ForgeMod.BLOCK_REACH.get().wrap());
    private static final Lazy<Holder<Attribute>> BURNING_TIME = Lazy.of(() -> PortLib.BURNING_TIME);
    private static final Lazy<Holder<Attribute>> EXPLOSION_KNOCKBACK_RESISTANCE = Lazy.of(() -> PortLib.EXPLOSION_KNOCKBACK_RESISTANCE);
    private static final Lazy<Holder<Attribute>> ENTITY_INTERACTION_RANGE = Lazy.of(() -> ForgeMod.ENTITY_REACH.get().wrap());
    private static final Lazy<Holder<Attribute>> FALL_DAMAGE_MULTIPLIER = Lazy.of(() -> PortLib.FALL_DAMAGE_MULTIPLIER);
    private static final Lazy<Holder<Attribute>> FLYING_SPEED = Lazy.of(() -> PortLib.FLYING_SPEED);
    private static final Lazy<Holder<Attribute>> GRAVITY = Lazy.of(() -> ForgeMod.ENTITY_GRAVITY.get().wrap());
    private static final Lazy<Holder<Attribute>> MAX_ABSORPTION = Lazy.of(() -> PortLib.MAX_ABSORPTION);
    private static final Lazy<Holder<Attribute>> MINING_EFFICIENCY = Lazy.of(() -> PortLib.MINING_EFFICIENCY);
    private static final Lazy<Holder<Attribute>> MOVEMENT_EFFICIENCY = Lazy.of(() -> PortLib.MOVEMENT_EFFICIENCY);
    private static final Lazy<Holder<Attribute>> OXYGEN_BONUS = Lazy.of(() -> PortLib.OXYGEN_BONUS);
    private static final Lazy<Holder<Attribute>> SAFE_FALL_DISTANCE = Lazy.of(() -> PortLib.SAFE_FALL_DISTANCE);
    private static final Lazy<Holder<Attribute>> SCALE = Lazy.of(() -> PortLib.SCALE);
    private static final Lazy<Holder<Attribute>> SNEAKING_SPEED = Lazy.of(() -> PortLib.SNEAKING_SPEED);
    private static final Lazy<Holder<Attribute>> STEP_HEIGHT = Lazy.of(() -> ForgeMod.SWIM_SPEED.get().wrap());
    private static final Lazy<Holder<Attribute>> SUBMERGED_MINING_SPEED = Lazy.of(() -> PortLib.SUBMERGED_MINING_SPEED);
    private static final Lazy<Holder<Attribute>> SWEEPING_DAMAGE_RATIO = Lazy.of(() -> PortLib.SWEEPING_DAMAGE_RATIO);
    private static final Lazy<Holder<Attribute>> WATER_MOVEMENT_EFFICIENCY = Lazy.of(() -> PortLib.WATER_MOVEMENT_EFFICIENCY);
    private static final Lazy<Holder<Attribute>> SWIM_SPEED = Lazy.of(() -> ForgeMod.SWIM_SPEED.get().wrap());
    private static final Lazy<Holder<Attribute>> CREATIVE_FLIGHT = Lazy.of(() -> PortLib.CREATIVE_FLIGHT);

    @Extension
    public static Holder<Attribute> blockBreakSpeed() {
        return BLOCK_BREAK_SPEED.get();
    }

    @Extension
    public static Holder<Attribute> blockInteractionRange() {
        return BLOCK_INTERACTION_RANGE.get();
    }

    @Extension
    public static Holder<Attribute> burningTime() {
        return BURNING_TIME.get();
    }

    @Extension
    public static Holder<Attribute> explosionKnockbackResistance() {
        return EXPLOSION_KNOCKBACK_RESISTANCE.get();
    }

    @Extension
    public static Holder<Attribute> entityInteractionRange() {
        return ENTITY_INTERACTION_RANGE.get();
    }

    @Extension
    public static Holder<Attribute> fallDamageMultiplier() {
        return FALL_DAMAGE_MULTIPLIER.get();
    }

    @Extension
    public static Holder<Attribute> flyingSpeed() {
        return FLYING_SPEED.get();
    }

    @Extension
    public static Holder<Attribute> gravity() {
        return GRAVITY.get();
    }

    @Extension
    public static Holder<Attribute> maxAbsorption() {
        return MAX_ABSORPTION.get();
    }

    @Extension
    public static Holder<Attribute> miningEfficiency() {
        return MINING_EFFICIENCY.get();
    }

    @Extension
    public static Holder<Attribute> movementEfficiency() {
        return MOVEMENT_EFFICIENCY.get();
    }

    @Extension
    public static Holder<Attribute> oxygenBonus() {
        return OXYGEN_BONUS.get();
    }

    @Extension
    public static Holder<Attribute> safeFallDistance() {
        return SAFE_FALL_DISTANCE.get();
    }

    @Extension
    public static Holder<Attribute> scale() {
        return SCALE.get();
    }

    @Extension
    public static Holder<Attribute> sneakingSpeed() {
        return SNEAKING_SPEED.get();
    }

    @Extension
    public static Holder<Attribute> stepHeight() {
        return STEP_HEIGHT.get();
    }

    @Extension
    public static Holder<Attribute> submergedMiningSpeed() {
        return SUBMERGED_MINING_SPEED.get();
    }

    @Extension
    public static Holder<Attribute> sweepingDamageRatio() {
        return SWEEPING_DAMAGE_RATIO.get();
    }

    @Extension
    public static Holder<Attribute> waterMovementEfficiency() {
        return WATER_MOVEMENT_EFFICIENCY.get();
    }

    @Extension
    public static Holder<Attribute> swimSpeed() {
        return SWIM_SPEED.get();
    }

    @Extension
    public static Holder<Attribute> creativeFlight() {
        return CREATIVE_FLIGHT.get();
    }
}
