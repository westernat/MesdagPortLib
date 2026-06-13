package org.mesdag.portlib.wrapper.common;

import com.google.common.collect.Sets;
import net.minecraftforge.common.ToolActions;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PortItemAbilities {
    public static final PortItemAbility AXE_DIG = PortItemAbility.wrap(ToolActions.AXE_DIG);
    public static final PortItemAbility PICKAXE_DIG = PortItemAbility.wrap(ToolActions.PICKAXE_DIG);
    public static final PortItemAbility SHOVEL_DIG = PortItemAbility.wrap(ToolActions.SHOVEL_DIG);
    public static final PortItemAbility HOE_DIG = PortItemAbility.wrap(ToolActions.HOE_DIG);
    public static final PortItemAbility SWORD_DIG = PortItemAbility.wrap(ToolActions.SWORD_DIG);
    public static final PortItemAbility SHEARS_DIG = PortItemAbility.wrap(ToolActions.SHEARS_DIG);
    public static final PortItemAbility AXE_STRIP = PortItemAbility.wrap(ToolActions.AXE_STRIP);
    public static final PortItemAbility AXE_SCRAPE = PortItemAbility.wrap(ToolActions.AXE_SCRAPE);
    public static final PortItemAbility AXE_WAX_OFF = PortItemAbility.wrap(ToolActions.AXE_WAX_OFF);
    public static final PortItemAbility SHOVEL_FLATTEN = PortItemAbility.wrap(ToolActions.SHOVEL_FLATTEN);
    public static final PortItemAbility SHOVEL_DOUSE = PortItemAbility.get("shovel_douse");
    public static final PortItemAbility SWORD_SWEEP = PortItemAbility.wrap(ToolActions.SWORD_SWEEP);
    public static final PortItemAbility SHEARS_HARVEST = PortItemAbility.wrap(ToolActions.SHEARS_HARVEST);
    public static final PortItemAbility SHEARS_REMOVE_ARMOR = PortItemAbility.get("shears_remove_armor");
    public static final PortItemAbility SHEARS_CARVE = PortItemAbility.wrap(ToolActions.SHEARS_CARVE);
    public static final PortItemAbility SHEARS_DISARM = PortItemAbility.wrap(ToolActions.SHEARS_DISARM);
    public static final PortItemAbility SHEARS_TRIM = PortItemAbility.get("shears_trim");
    public static final PortItemAbility HOE_TILL = PortItemAbility.wrap(ToolActions.HOE_TILL);
    public static final PortItemAbility SHIELD_BLOCK = PortItemAbility.wrap(ToolActions.SHIELD_BLOCK);
    public static final PortItemAbility FISHING_ROD_CAST = PortItemAbility.wrap(ToolActions.FISHING_ROD_CAST);
    public static final PortItemAbility TRIDENT_THROW = PortItemAbility.get("trident_throw");
    public static final PortItemAbility BRUSH_BRUSH = PortItemAbility.get("brush_brush");
    public static final PortItemAbility FIRESTARTER_LIGHT = PortItemAbility.get("firestarter_light");
    public static final PortItemAbility SPYGLASS_SCOPE = PortItemAbility.get("spyglass_scope");

    public static final Set<PortItemAbility> DEFAULT_AXE_ACTIONS = of(AXE_DIG, AXE_STRIP, AXE_SCRAPE, AXE_WAX_OFF);
    public static final Set<PortItemAbility> DEFAULT_HOE_ACTIONS = of(HOE_DIG, HOE_TILL);
    public static final Set<PortItemAbility> DEFAULT_SHOVEL_ACTIONS = of(SHOVEL_DIG, SHOVEL_FLATTEN, SHOVEL_DOUSE);
    public static final Set<PortItemAbility> DEFAULT_PICKAXE_ACTIONS = of(PICKAXE_DIG);
    public static final Set<PortItemAbility> DEFAULT_SWORD_ACTIONS = of(SWORD_DIG, SWORD_SWEEP);
    public static final Set<PortItemAbility> DEFAULT_SHEARS_ACTIONS = of(SHEARS_DIG, SHEARS_HARVEST, SHEARS_REMOVE_ARMOR, SHEARS_CARVE, SHEARS_DISARM, SHEARS_TRIM);
    public static final Set<PortItemAbility> DEFAULT_SHIELD_ACTIONS = of(SHIELD_BLOCK);
    public static final Set<PortItemAbility> DEFAULT_FISHING_ROD_ACTIONS = of(FISHING_ROD_CAST);
    public static final Set<PortItemAbility> DEFAULT_TRIDENT_ACTIONS = of(TRIDENT_THROW);
    public static final Set<PortItemAbility> DEFAULT_BRUSH_ACTIONS = of(BRUSH_BRUSH);
    public static final Set<PortItemAbility> DEFAULT_FLINT_ACTIONS = of(FIRESTARTER_LIGHT);
    public static final Set<PortItemAbility> DEFAULT_FIRECHARGE_ACTIONS = of(FIRESTARTER_LIGHT);
    public static final Set<PortItemAbility> DEFAULT_SPYGLASS_ACTIONS = of(SPYGLASS_SCOPE);

    private static Set<PortItemAbility> of(PortItemAbility... actions) {
        return Stream.of(actions).collect(Collectors.toCollection(Sets::newIdentityHashSet));
    }
}
