package PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeInstance;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class PortAttributeInstanceExtension {
    public static void addOrReplacePermanentModifier(AttributeInstance thiz, AttributeModifier modifier) {
        thiz.removeModifier(modifier.getId()); // 必须是#getId
        thiz.addPermanentModifier(modifier);
    }

    public static boolean hasModifier(AttributeInstance thiz, UUID id) {
        return thiz.getModifier(id) != null;
    }
}
