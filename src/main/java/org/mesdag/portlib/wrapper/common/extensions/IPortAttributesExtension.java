package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.ForgeMod;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;

public interface IPortAttributesExtension {
    Holder<Attribute> BLOCK_BREAK_SPEED = AttributeHolder.lazy(() -> PortLib.BLOCK_BREAK_SPEED, false);
    Holder<Attribute> BLOCK_INTERACTION_RANGE = AttributeHolder.lazy(() -> new PortRegistryEntry<>(ForgeMod.BLOCK_REACH), true);
    Holder<Attribute> BURNING_TIME = AttributeHolder.lazy(() -> PortLib.BURNING_TIME, false);
    Holder<Attribute> EXPLOSION_KNOCKBACK_RESISTANCE = AttributeHolder.lazy(() -> PortLib.EXPLOSION_KNOCKBACK_RESISTANCE, false);
    Holder<Attribute> ENTITY_INTERACTION_RANGE = AttributeHolder.lazy(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_REACH), true);
    Holder<Attribute> FALL_DAMAGE_MULTIPLIER = AttributeHolder.lazy(() -> PortLib.FALL_DAMAGE_MULTIPLIER, false);
    Holder<Attribute> JUMP_STRENGTH = AttributeHolder.lazy(() -> PortLib.JUMP_STRENGTH, false);
    Holder<Attribute> GRAVITY = AttributeHolder.lazy(() -> new PortRegistryEntry<>(ForgeMod.ENTITY_GRAVITY), true);
    Holder<Attribute> MAX_ABSORPTION = AttributeHolder.lazy(() -> PortLib.MAX_ABSORPTION, false);
    Holder<Attribute> MINING_EFFICIENCY = AttributeHolder.lazy(() -> PortLib.MINING_EFFICIENCY, false);
    Holder<Attribute> MOVEMENT_EFFICIENCY = AttributeHolder.lazy(() -> PortLib.MOVEMENT_EFFICIENCY, false);
    Holder<Attribute> OXYGEN_BONUS = AttributeHolder.lazy(() -> PortLib.OXYGEN_BONUS, false);
    Holder<Attribute> SAFE_FALL_DISTANCE = AttributeHolder.lazy(() -> PortLib.SAFE_FALL_DISTANCE, false);
    Holder<Attribute> SCALE = AttributeHolder.lazy(() -> PortLib.SCALE, false);
    Holder<Attribute> SNEAKING_SPEED = AttributeHolder.lazy(() -> PortLib.SNEAKING_SPEED, false);
    Holder<Attribute> STEP_HEIGHT = AttributeHolder.lazy(() -> new PortRegistryEntry<>(ForgeMod.STEP_HEIGHT_ADDITION), true);
    Holder<Attribute> SUBMERGED_MINING_SPEED = AttributeHolder.lazy(() -> PortLib.SUBMERGED_MINING_SPEED, false); // Invoked through coremod
    Holder<Attribute> SWEEPING_DAMAGE_RATIO = AttributeHolder.lazy(() -> PortLib.SWEEPING_DAMAGE_RATIO, false);
    Holder<Attribute> WATER_MOVEMENT_EFFICIENCY = AttributeHolder.lazy(() -> PortLib.WATER_MOVEMENT_EFFICIENCY, false);
    Holder<Attribute> SWIM_SPEED = AttributeHolder.lazy(() -> new PortRegistryEntry<>(ForgeMod.SWIM_SPEED), true);
    Holder<Attribute> CREATIVE_FLIGHT = AttributeHolder.lazy(() -> PortLib.CREATIVE_FLIGHT, false);
}
