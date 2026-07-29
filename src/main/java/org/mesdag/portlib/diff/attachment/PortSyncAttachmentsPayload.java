package org.mesdag.portlib.diff.attachment;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.List;

@Diff
public record PortSyncAttachmentsPayload(
        Target target,
        List<PortAttachmentType<?>> types,
        byte[] syncPayload
) implements IPortPacket.S2C {
    private static final int MAX_SYNCED_TYPES = 1_024;
    private static final int MAX_SYNC_PAYLOAD_BYTES = 1_048_576;
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("sync_attachments");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortSyncAttachmentsPayload> STREAM_CODEC = PortStreamCodec.composite(
            Target.STREAM_CODEC, PortSyncAttachmentsPayload::target,
            PortByteBufCodecs.registry(PortAttachmentSync.SYNCED_ATTACHMENT_TYPES.key()).apply(PortByteBufCodecs.list(MAX_SYNCED_TYPES)), PortSyncAttachmentsPayload::types,
            PortByteBufCodecs.byteArray(MAX_SYNC_PAYLOAD_BYTES), PortSyncAttachmentsPayload::syncPayload,
            PortSyncAttachmentsPayload::new
    );

    @Override
    public void work(Player player) {
        RegistryAccess registryAccess = player.level().registryAccess();
        if (target instanceof BlockEntityTarget t) {
            var blockEntity = player.level().getBlockEntity(t.pos);
            if (blockEntity == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown block entity");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(CPortAttachmentHolder.of(blockEntity), registryAccess, types, syncPayload);
            }
        } else if (target instanceof ChunkTarget t) {
            var chunk = player.level().getChunk(t.pos.x, t.pos.z, ChunkStatus.FULL, false);
            if (chunk == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown chunk");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(CPortAttachmentHolder.of(chunk), registryAccess, types, syncPayload);
            }
        } else if (target instanceof EntityTarget t) {
            var entity = player.level().getEntity(t.entity);
            if (entity == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown entity");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(CPortAttachmentHolder.of(entity), registryAccess, types, syncPayload);
            }
        } else if (target instanceof LevelTarget) {
            PortAttachmentSync.receiveSyncedDataAttachments(CPortAttachmentHolder.of(player.level()), registryAccess, types, syncPayload);
        }
    }

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }

    public sealed interface Target {
        PortStreamCodec<PortRegistryFriendlyByteBuf, Target> STREAM_CODEC = new PortStreamCodec<>() {
            @Override
            public void encode(PortRegistryFriendlyByteBuf buf, Target target) {
                if (target instanceof BlockEntityTarget t) {
                    buf.writeByte(0);
                    buf.writeBlockPos(t.pos);
                } else if (target instanceof ChunkTarget t) {
                    buf.writeByte(1);
                    buf.writeChunkPos(t.pos);
                } else if (target instanceof EntityTarget t) {
                    buf.writeByte(2);
                    buf.writeVarInt(t.entity);
                } else if (target instanceof LevelTarget) {
                    buf.writeByte(3);
                }
            }

            @Override
            public Target decode(PortRegistryFriendlyByteBuf buf) {
                int type = buf.readByte();
                switch (type) {
                    case 0 -> {
                        return new BlockEntityTarget(buf.readBlockPos());
                    }
                    case 1 -> {
                        return new ChunkTarget(buf.readChunkPos());
                    }
                    case 2 -> {
                        return new EntityTarget(buf.readVarInt());
                    }
                    case 3 -> {
                        return new LevelTarget();
                    }
                    default -> throw new IllegalArgumentException("Unknown target type: " + type);
                }
            }
        };
    }

    public record BlockEntityTarget(BlockPos pos) implements Target {}

    public record ChunkTarget(ChunkPos pos) implements Target {}

    public record EntityTarget(int entity) implements Target {}

    public record LevelTarget() implements Target {}
}
