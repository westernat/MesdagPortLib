package PortLib.extensions.net.minecraft.world.entity.Entity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.Entity;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;

@Extension
public class PortEntityExtension {
    public static void copyAttachmentsFrom(@This Entity thiz, Entity other, boolean isDeath) {
        PortAttachmentInternals.copyEntityAttachments(other, thiz, isDeath);
    }
}
