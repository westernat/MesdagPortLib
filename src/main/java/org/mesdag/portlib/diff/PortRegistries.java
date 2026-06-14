package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventPriority;
import org.mesdag.portlib.event.registries.PortModifyRegistriesEvent;
import org.mesdag.portlib.registries.PortCustomRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.common.crafting.PortIngredientType;

@Diff
public class PortRegistries {
    public static final PortCustomRegistration<PortAttachmentType<?>> ATTACHMENT_TYPES = PortRegisterHandler.custom(PortLib.MODID, Keys.ATTACHMENT_TYPES, maker -> {});
    public static final PortCustomRegistration<PortDataComponentType<?>> DATA_COMPONENTS = PortRegisterHandler.custom(PortLib.MODID, Keys.DATA_COMPONENTS, maker -> {});
    public static final PortCustomRegistration<PortIngredientType<?>> INGREDIENT_TYPES = PortRegisterHandler.custom(PortLib.MODID, Keys.INGREDIENT_TYPES, maker -> maker.sync(true));

    @ApiStatus.Internal
    public static void init() {
        PortEventHandler.addListener(PortEventPriority.LOWEST, (PortModifyRegistriesEvent event) -> ATTACHMENT_TYPES.addCallback(PortAttachmentSync.ATTACHMENT_TYPE_ADD_CALLBACK));
    }

    public static final class Keys {
        public static final ResourceKey<Registry<PortAttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");
        public static final ResourceKey<Registry<PortDataComponentType<?>>> DATA_COMPONENTS = key("data_components");
        public static final ResourceKey<Registry<ArmorMaterial>> ARMOR_MATERIALS = key("armor_materials");
        public static final ResourceKey<Registry<PortIngredientType<?>>> INGREDIENT_TYPES = key("ingredient_serializer");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(PortLib.asResource(name));
        }
    }
}
