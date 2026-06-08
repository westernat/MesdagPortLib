package org.mesdag.portlib.wrapper.world.entity;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public enum PortEquipmentSlotGroup implements StringRepresentable {
    ANY("any", s -> true),
    MAINHAND("mainhand", EquipmentSlot.MAINHAND),
    OFFHAND("offhand", EquipmentSlot.OFFHAND),
    HAND("hand", s -> s.getType() == EquipmentSlot.Type.HAND),
    FEET("feet", EquipmentSlot.FEET),
    LEGS("legs", EquipmentSlot.LEGS),
    CHEST("chest", EquipmentSlot.CHEST),
    HEAD("head", EquipmentSlot.HEAD),
    ARMOR("armor", EquipmentSlot::isArmor),
    BODY("body", EquipmentSlot.CHEST);

    private final String key;
    private final Predicate<EquipmentSlot> predicate;

    PortEquipmentSlotGroup(String key, Predicate<EquipmentSlot> predicate) {
        this.key = key;
        this.predicate = predicate;
    }

    PortEquipmentSlotGroup(String key, EquipmentSlot slot) {
        this(key, s -> s == slot);
    }

    public static @Nullable PortEquipmentSlotGroup fromSlot(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> PortEquipmentSlotGroup.MAINHAND;
            case OFFHAND -> PortEquipmentSlotGroup.OFFHAND;
            case FEET -> PortEquipmentSlotGroup.FEET;
            case LEGS -> PortEquipmentSlotGroup.LEGS;
            case CHEST -> PortEquipmentSlotGroup.CHEST;
            case HEAD -> PortEquipmentSlotGroup.HEAD;
            default -> null;
        };
    }

    @Override
    public String getSerializedName() {
        return key;
    }

    public boolean test(EquipmentSlot slot) {
        return predicate.test(slot);
    }
}
