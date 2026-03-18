package org.mesdag.portlib.diff;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.mesdag.portlib.attachment.IPortAttachmentCopyHandler;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventPriority;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityExtension;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.function.Predicate;

@Diff
public final class PortAttachmentInternals {
    private static <H extends CPortAttachmentHolder> void copyAttachments(PortRegistryAccess provider, H from, H to, Predicate<PortAttachmentType<?>> filter) {
        if (from.portlib$attachments() == null) {
            return;
        }
        for (var entry : from.portlib$attachments().entrySet()) {
            PortAttachmentType<?> type = entry.getKey();
            if (type.serializer == null) {
                continue;
            }
            @SuppressWarnings("unchecked")
            var copyHandler = (IPortAttachmentCopyHandler<Object>) type.copyHandler;
            if (filter.test(type)) {
                Object copy = copyHandler.copy(entry.getValue(), to.getExposedHolder(), provider);
                if (copy != null) {
                    to.getAttachmentMap().put(type, copy);
                }
            }
        }
    }

    public static void copyChunkAttachmentsOnPromotion(PortRegistryAccess provider, CPortAttachmentHolder from, CPortAttachmentHolder to) {
        copyAttachments(provider, from, to, type -> true);
    }

    public static void copyEntityAttachments(Entity from, Entity to, boolean isDeath) {
        copyAttachments(new PortRegistryAccess(from.level().registryAccess()), CPortAttachmentHolder.of(from), CPortAttachmentHolder.of(to), isDeath ? type -> type.copyOnDeath : type -> true);
    }

    public static void init() {
        PortEventHandler.addListener(PortEventPriority.LOWEST, (PlayerEvent.Clone event) -> IPortEntityExtension.copyAttachmentsFrom(event.getEntity(), event.getOriginal(), event.isWasDeath()));
        PortEventHandler.addListener(PortEventPriority.LOWEST, (LivingConversionEvent.Post event) -> IPortEntityExtension.copyAttachmentsFrom(event.getOutcome(), event.getEntity(), true));
    }

    private PortAttachmentInternals() {}
}
