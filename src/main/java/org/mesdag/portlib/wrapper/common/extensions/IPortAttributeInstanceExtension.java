package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.AttributeInstance.PortAttributeInstanceExtension;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public interface IPortAttributeInstanceExtension {
    private AttributeInstance self() {
        return (AttributeInstance) this;
    }

    default void addOrReplacePermanentModifier(AttributeModifier modifier) {
        PortAttributeInstanceExtension.addOrReplacePermanentModifier(self(), modifier);
    }

    default boolean hasModifier(UUID id) {
        return PortAttributeInstanceExtension.hasModifier(self(), id);
    }
}
