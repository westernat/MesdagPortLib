package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.wrapper.common.crafting.PortIngredientType;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

@Diff
public class PortRegistries {
    public static final class Keys {
        public static final ResourceKey<Registry<PortAttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");
        public static final ResourceKey<Registry<PortDataComponentType<?>>> DATA_COMPONENTS = key("data_components");
        public static final ResourceKey<Registry<PortArmorMaterial>> ARMOR_MATERIALS = key("armor_materials");
        public static final ResourceKey<Registry<PortIngredientType<?>>> INGREDIENT_TYPES = key("ingredient_serializer");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(PortLib.asResource(name));
        }
    }
}
