package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentSync;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortPriority;
import org.mesdag.portlib.registries.CustomRegistration;
import org.mesdag.portlib.registries.PortModifyRegistriesEvent;
import org.mesdag.portlib.registries.PortRegisterHandler;

@Diff
public class PortRegistries {
    public static final CustomRegistration<PortAttachmentType<?>> ATTACHMENT_TYPES = PortRegisterHandler.custom(PortLib.MODID, Keys.ATTACHMENT_TYPES, maker -> {});

    public static void init() {
        PortEventHandler.addListener(PortPriority.LOWEST, (RegisterCapabilitiesEvent event) -> PortEventHandler.postEvent(new PortModifyRegistriesEvent()));
        PortEventHandler.addListener(PortPriority.LOWEST, (PortModifyRegistriesEvent event) -> ATTACHMENT_TYPES.addCallback(PortAttachmentSync.ATTACHMENT_TYPE_ADD_CALLBACK));
    }

    public static final class Keys {
        public static final ResourceKey<Registry<PortAttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(PortLib.asResource(name));
        }
    }
}
