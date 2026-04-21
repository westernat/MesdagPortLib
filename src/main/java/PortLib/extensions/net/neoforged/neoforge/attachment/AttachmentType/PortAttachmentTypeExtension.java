package PortLib.extensions.net.neoforged.neoforge.attachment.AttachmentType;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.Diff;

@Extension
public class PortAttachmentTypeExtension {
    @Diff
    public static <T> PortAttachmentType<T> wrap(@This AttachmentType<T> thiz) {
        return new PortAttachmentType<>(thiz);
    }
}
