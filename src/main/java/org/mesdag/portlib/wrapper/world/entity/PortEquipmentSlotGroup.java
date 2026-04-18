package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import org.mesdag.portlib.diff.Diff;

public enum PortEquipmentSlotGroup implements StringRepresentable {
    ANY,
    MAINHAND,
    OFFHAND,
    HAND,
    FEET,
    LEGS,
    CHEST,
    HEAD,
    ARMOR,
    BODY;

    @Diff
    public EquipmentSlotGroup unwrap() {
        return switch (this) {
            case ANY -> EquipmentSlotGroup.ANY;
            case MAINHAND -> EquipmentSlotGroup.MAINHAND;
            case OFFHAND -> EquipmentSlotGroup.OFFHAND;
            case HAND -> EquipmentSlotGroup.HAND;
            case FEET -> EquipmentSlotGroup.FEET;
            case LEGS -> EquipmentSlotGroup.LEGS;
            case CHEST -> EquipmentSlotGroup.CHEST;
            case HEAD -> EquipmentSlotGroup.HEAD;
            case ARMOR -> EquipmentSlotGroup.ARMOR;
            case BODY -> EquipmentSlotGroup.BODY;
        };
    }

    @Override
    public String getSerializedName() {
        return unwrap().getSerializedName();
    }

    public boolean test(EquipmentSlot slot) {
        return unwrap().test(slot);
    }
}
