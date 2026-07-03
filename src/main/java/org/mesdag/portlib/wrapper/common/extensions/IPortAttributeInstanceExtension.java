package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public interface IPortAttributeInstanceExtension {
    private AttributeInstance self() {
        return (AttributeInstance) this;
    }

    default void addOrReplacePermanentModifier(AttributeModifier modifier) {
        AttributeInstance self = self();
        self.removeModifier(modifier.getId());
        self.addModifier(modifier);
        self.permanentModifiers.add(modifier);
    }

    default boolean hasModifier(UUID id) {
        return self().modifierById.get(id) != null;
    }

    default void addOrUpdateTransientModifier(AttributeModifier modifier) {
        AttributeInstance self = self();
        AttributeModifier attributemodifier = self.modifierById.put(modifier.getId(), modifier);
        if (modifier != attributemodifier) {
            self.getModifiers(modifier.getOperation()).add(modifier);
            self.setDirty();
        }
    }
}
