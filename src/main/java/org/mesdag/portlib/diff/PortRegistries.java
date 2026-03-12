package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.wrapper.PortIdentifier;

@Diff
public class PortRegistries {
    public static final DeferredRegister<PortAttachmentType<?>> TYPES = DeferredRegister.create(Keys.ATTACHMENT_TYPES, PortLib.MODID);

    public static void init(IEventBus bus) {
        TYPES.register(bus);
    }

    public static final class Keys {
        public static final ResourceKey<Registry<PortAttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(PortIdentifier.fromNamespaceAndPath(PortLib.MODID, name));
        }
    }
}
