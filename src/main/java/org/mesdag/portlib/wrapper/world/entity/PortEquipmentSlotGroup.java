package org.mesdag.portlib.wrapper.world.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.function.IntFunction;
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

    public static final IntFunction<PortEquipmentSlotGroup> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<PortEquipmentSlotGroup> CODEC = StringRepresentable.fromEnum(PortEquipmentSlotGroup::values);
    public static final PortStreamCodec<ByteBuf, PortEquipmentSlotGroup> STREAM_CODEC = PortByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

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
