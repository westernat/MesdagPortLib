package org.mesdag.portlib.diff;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentHolder;
import org.mesdag.portlib.attachment.PortAttachmentSync;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.PortIdentifier;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;

import java.util.List;

@Diff
public record PortSyncAttachmentsPayload(
        Target target,
        List<PortAttachmentType<?>> types,
        byte[] syncPayload
) implements IPortPacket {
    public static final PortIdentifier IDENTIFIER = PortLib.asResource("sync_attachments");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortSyncAttachmentsPayload> STREAM_CODEC = new PortStreamCodec<>() {
//        private static final PortStreamCodec<PortRegistryFriendlyByteBuf, List<PortAttachmentType<?>>> TYPES_CODEC = PortByteBufCodecs.registry()

        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, PortSyncAttachmentsPayload value) {
            Target.STREAM_CODEC.encode(buffer, value.target);

        }

        @Override
        public PortSyncAttachmentsPayload decode(PortRegistryFriendlyByteBuf buffer) {
            return null;
        }
    };

    @Override
    public void handle(Context context) {
        Player player = context.player();
        if (player == null) return;
        RegistryAccess registryAccess = player.level().registryAccess();
        if (target instanceof BlockEntityTarget t) {
            var blockEntity = player.level().getBlockEntity(t.pos);
            if (blockEntity == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown block entity");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(PortAttachmentHolder.of(blockEntity), registryAccess, types, syncPayload);
            }
        } else if (target instanceof ChunkTarget t) {
            var chunk = player.level().getChunk(t.pos.x, t.pos.z, ChunkStatus.FULL, false);
            if (chunk == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown chunk");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(PortAttachmentHolder.of(chunk), registryAccess, types, syncPayload);
            }
        } else if (target instanceof EntityTarget t) {
            var entity = context.player().level().getEntity(t.entity);
            if (entity == null) {
                PortLib.LOGGER.warn("Received synced attachments from unknown entity");
            } else {
                PortAttachmentSync.receiveSyncedDataAttachments(PortAttachmentHolder.of(entity), registryAccess, types, syncPayload);
            }
        } else if (target instanceof LevelTarget) {
            PortAttachmentSync.receiveSyncedDataAttachments(PortAttachmentHolder.of(player.level()), registryAccess, types, syncPayload);
        }
    }

    @Override
    public PortIdentifier identifier() {
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
