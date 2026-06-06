package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import org.mesdag.portlib.diff.Diff;

public class PortArmorItem {
    public enum PortType {
        HELMET(11),
        CHESTPLATE(16),
        LEGGINGS(15),
        BOOTS(13);

        private final int durability;

        PortType(int durability) {
            this.durability = durability;
        }

        public int getDurability(int durabilityFactor) {
            return durability * durabilityFactor;
        }

        public EquipmentSlot getSlot() {
            return unwrap().getSlot();
        }

        public String getName() {
            return unwrap().getName();
        }

        public boolean hasTrims() {
            return this == HELMET || this == CHESTPLATE || this == LEGGINGS || this == BOOTS;
        }

        @Diff
        public ArmorItem.Type unwrap() {
            return switch (this) {
                case HELMET -> ArmorItem.Type.HELMET;
                case LEGGINGS -> ArmorItem.Type.LEGGINGS;
                case BOOTS -> ArmorItem.Type.BOOTS;
                default -> ArmorItem.Type.CHESTPLATE;
            };
        }

        public static PortType wrap(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> HELMET;
                case LEGGINGS -> LEGGINGS;
                case BOOTS -> BOOTS;
                default -> CHESTPLATE;
            };
        }
    }
}
