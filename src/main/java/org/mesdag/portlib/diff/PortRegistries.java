package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;

@Diff
public class PortRegistries {
    public static final class Keys {
        public static final ResourceKey<Registry<PortAttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");

        private static <T> ResourceKey<Registry<T>> key(String name) {
            return ResourceKey.createRegistryKey(PortLib.asResource(name));
        }
    }
}
