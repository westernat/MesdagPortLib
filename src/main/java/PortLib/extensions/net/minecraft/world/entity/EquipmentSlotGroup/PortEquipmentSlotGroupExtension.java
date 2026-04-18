package PortLib.extensions.net.minecraft.world.entity.EquipmentSlotGroup;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.EquipmentSlotGroup;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.entity.PortEquipmentSlotGroup;

@Extension
public class PortEquipmentSlotGroupExtension {
    @Diff
    public static PortEquipmentSlotGroup wrap(@This EquipmentSlotGroup thiz) {
        return switch (thiz) {
            case ANY -> PortEquipmentSlotGroup.ANY;
            case MAINHAND -> PortEquipmentSlotGroup.MAINHAND;
            case OFFHAND -> PortEquipmentSlotGroup.OFFHAND;
            case HAND -> PortEquipmentSlotGroup.HAND;
            case FEET -> PortEquipmentSlotGroup.FEET;
            case LEGS -> PortEquipmentSlotGroup.LEGS;
            case CHEST -> PortEquipmentSlotGroup.CHEST;
            case HEAD -> PortEquipmentSlotGroup.HEAD;
            case ARMOR -> PortEquipmentSlotGroup.ARMOR;
            case BODY -> PortEquipmentSlotGroup.BODY;
        };
    }
}
