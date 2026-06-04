package org.mesdag.portlib.diff.attachment;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.IPortAttachmentCopyHandler;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventPriority;

import java.util.function.Predicate;

@Diff
public final class PortAttachmentInternals {
    private static <H extends CPortAttachmentHolder> void copyAttachments(HolderLookup.Provider provider, H from, H to, Predicate<PortAttachmentType<?>> filter) {
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

    public static void copyChunkAttachmentsOnPromotion(HolderLookup.Provider provider, CPortAttachmentHolder from, CPortAttachmentHolder to) {
        copyAttachments(provider, from, to, type -> true);
    }

    public static void copyEntityAttachments(Entity from, Entity to, boolean isDeath) {
        copyAttachments(from.level().registryAccess(), CPortAttachmentHolder.of(from), CPortAttachmentHolder.of(to), isDeath ? type -> type.copyOnDeath : type -> true);
    }

    @ApiStatus.Internal
    public static void init() {
        PortLib.NETWORK_HANDLER.registerInGameS2C(
                PortSyncAttachmentsPayload.class,
                PortSyncAttachmentsPayload.IDENTIFIER,
                PortSyncAttachmentsPayload.STREAM_CODEC,
                PortSyncAttachmentsPayload::handle
        );
        PortEventHandler.addListener(PortEventPriority.LOWEST, (PlayerEvent.Clone event) -> PortEntityExtension.copyAttachmentsFrom(event.getEntity(), event.getOriginal(), event.isWasDeath()));
        PortEventHandler.addListener(PortEventPriority.LOWEST, (LivingConversionEvent.Post event) -> PortEntityExtension.copyAttachmentsFrom(event.getOutcome(), event.getEntity(), true));
    }

    private PortAttachmentInternals() {}
}
