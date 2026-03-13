package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.world.item.ArmorItem;

public enum PortArmorType {
    HELMET(ArmorItem.Type.HELMET),
    CHESTPLATE(ArmorItem.Type.CHESTPLATE),
    LEGGINGS(ArmorItem.Type.LEGGINGS),
    BOOTS(ArmorItem.Type.BOOTS),
    BODY(safeGetBodyType());

    private final ArmorItem.Type vanillaType;

    PortArmorType(ArmorItem.Type vanillaType) {
        this.vanillaType = vanillaType;
    }

    public ArmorItem.Type getVanilla() {
        return vanillaType;
    }

    public boolean isBody() {
        return this == BODY;
    }

    private static ArmorItem.Type safeGetBodyType() {
        try {
            return ArmorItem.Type.valueOf("BODY");
        } catch (Exception e) {
            return null;
        }
    }
}