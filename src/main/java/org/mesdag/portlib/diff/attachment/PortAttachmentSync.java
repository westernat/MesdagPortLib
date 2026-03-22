package org.mesdag.portlib.diff.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentSyncHandler;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortBundledPacket;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.level.PortChunkWatchEvent;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.registries.PortCustomRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.wrapper.server.level.PortChunkMap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Diff
public final class PortAttachmentSync {
    public static final PortCustomRegistration<PortAttachmentType<?>> SYNCED_ATTACHMENT_TYPES = PortRegisterHandler.custom(
            PortLib.MODID,
            ResourceKey.createRegistryKey(PortLib.asResource("synced_attachment_types")),
            maker -> maker.sync(true).onAdd((owner, id, key, value) -> {
                if (!PortRegistries.ATTACHMENT_TYPES.containsKey(key.location())
                        || !PortRegistries.ATTACHMENT_TYPES.containsValue(value)
                        || PortRegistries.ATTACHMENT_TYPES.get(key.location()) != value) {
                    throw new IllegalStateException("Cannot add entries to the SYNCED_ATTACHMENT_TYPES registry directly.");
                }
            })
    );

    public static final PortAddCallback<PortAttachmentType<?>> ATTACHMENT_TYPE_ADD_CALLBACK = (registry, id, key, value) -> {
        if (value.syncHandler != null) {
            SYNCED_ATTACHMENT_TYPES.register(key.location(), value);
        }
    };

    @Diff
    public static void init() {
        PortEventHandler.addListener((PortChunkWatchEvent.Sent event) -> {
            List<IPortPacket> packets = new ArrayList<>();
            var chunkPayload = syncInitialAttachments(CPortAttachmentHolder.of(event.getChunk()), event.getPlayer());
            if (chunkPayload != null) {
                packets.add(chunkPayload);
            }
            for (var blockEntity : event.getChunk().getBlockEntities().values()) {
                var blockEntityPayload = syncInitialAttachments(CPortAttachmentHolder.of(blockEntity), event.getPlayer());
                if (blockEntityPayload != null) {
                    packets.add(blockEntityPayload);
                }
            }
            if (!packets.isEmpty()) {
                PortLib.NETWORK_HANDLER.sendToPlayer(event.getPlayer(), PortBundledPacket.makePacket(packets));
            }
        });
    }

    private static PortSyncAttachmentsPayload.Target syncTarget(CPortAttachmentHolder holder) {
        if (holder instanceof BlockEntity blockEntity) {
            return new PortSyncAttachmentsPayload.BlockEntityTarget(blockEntity.getBlockPos());
        } else if (holder instanceof LevelChunk chunk) {
            return new PortSyncAttachmentsPayload.ChunkTarget(chunk.getPos());
        } else if (holder instanceof Entity entity) {
            return new PortSyncAttachmentsPayload.EntityTarget(entity.getId());
        } else if (holder instanceof Level) {
            return new PortSyncAttachmentsPayload.LevelTarget();
        }
        throw new UnsupportedOperationException("Attachment holder class is not supported: " + holder);
    }

    private static <T> void syncUpdate(CPortAttachmentHolder holder, PortAttachmentType<T> type, List<ServerPlayer> players) {
        RegistryAccess registryAccess = null;
        for (var player : players) {
            if (type.syncHandler.sendToPlayer(holder.getExposedHolder(), player)) {
                registryAccess = player.level().registryAccess();
                break;
            }
        }
        if (registryAccess == null) {
            return;
        }
        var data = writeCustomData(buf -> {
            var existingData = holder.getExistingDataOrNull(type);
            if (existingData != null) {
                buf.writeBoolean(true);
                type.syncHandler.write(buf, holder.getData(type), false);
            } else {
                buf.writeBoolean(false);
            }
        }, registryAccess);
        var packet = new PortSyncAttachmentsPayload(syncTarget(holder), List.of(type), data);
        for (var player : players) {
            if (type.syncHandler.sendToPlayer(holder.getExposedHolder(), player)) {
                PortLib.NETWORK_HANDLER.sendToPlayer(player, packet);
            }
        }
    }

    public static void syncBlockEntityUpdate(BlockEntity blockEntity, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(blockEntity.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        syncUpdate(CPortAttachmentHolder.of(blockEntity), type, serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockEntity.getBlockPos()), false));
    }

    public static void syncChunkUpdate(LevelChunk chunk, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(chunk.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        syncUpdate(CPortAttachmentHolder.of(chunk), type, serverLevel.getChunkSource().chunkMap.getPlayers(chunk.getPos(), false));
    }

    public static void syncEntityUpdate(Entity entity, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        var players = PortChunkMap.getPlayersWatching(serverLevel.getChunkSource().chunkMap, entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            // Players do not track themselves
            var newPlayers = new ArrayList<ServerPlayer>(players.size() + 1);
            newPlayers.addAll(players);
            newPlayers.add(serverPlayer);
            players = newPlayers;
        }
        syncUpdate(CPortAttachmentHolder.of(entity), type, players);
    }

    public static void syncLevelUpdate(ServerLevel level, PortAttachmentType<?> type) {
        if (type.syncHandler == null) {
            return;
        }
        syncUpdate(CPortAttachmentHolder.of(level), type, level.players());
    }

    @Nullable
    private static PortSyncAttachmentsPayload syncInitialAttachments(CPortAttachmentHolder holder, ServerPlayer to) {
        if (holder.portlib$attachments() == null) {
            return null;
        }
        boolean anySyncableAttachment = false;
        for (var attachment : holder.portlib$attachments().keySet()) {
            anySyncableAttachment = anySyncableAttachment | attachment.syncHandler != null;
        }
        if (!anySyncableAttachment) {
            return null;
        }
        List<PortAttachmentType<?>> syncedTypes = new ArrayList<>();
        var data = writeCustomData(buf -> {
            for (var entry : holder.portlib$attachments().entrySet()) {
                PortAttachmentType<?> type = entry.getKey();
                @SuppressWarnings("unchecked")
                var syncHandler = (PortAttachmentSyncHandler<Object>) type.syncHandler;
                if (syncHandler != null) {
                    int indexBefore = buf.writerIndex();
                    buf.writeBoolean(true);
                    int indexBetween = buf.writerIndex();
                    syncHandler.write(buf, entry.getValue(), true);
                    if (indexBetween < buf.writerIndex()) {
                        // Actually wrote something
                        syncedTypes.add(type);
                    } else {
                        buf.writerIndex(indexBefore);
                    }
                }
            }
        }, to.level().registryAccess());
        return new PortSyncAttachmentsPayload(syncTarget(holder), syncedTypes, data);
    }

    public static void syncInitialEntityAttachments(Entity entity, ServerPlayer to, Consumer<Packet<? super ClientGamePacketListener>> packetConsumer) {
        var packet = syncInitialAttachments(CPortAttachmentHolder.of(entity), to);
        if (packet != null) {
            PortLib.NETWORK_HANDLER.sendToPlayer(to, packet);
        }
    }

    public static void syncInitialPlayerAttachments(ServerPlayer player) {
        var packet = syncInitialAttachments(CPortAttachmentHolder.of(player), player);
        if (packet != null) {
            PortLib.NETWORK_HANDLER.sendToPlayer(player, packet);
        }
    }

    public static void syncInitialLevelAttachments(ServerLevel level, ServerPlayer to) {
        var packet = syncInitialAttachments(CPortAttachmentHolder.of(level), to);
        if (packet != null) {
            PortLib.NETWORK_HANDLER.sendToPlayer(to, packet);
        }
    }

    public static void receiveSyncedDataAttachments(CPortAttachmentHolder holder, RegistryAccess registryAccess, List<PortAttachmentType<?>> types, byte[] bytes) {
        var buf = new PortRegistryFriendlyByteBuf(Unpooled.wrappedBuffer(bytes), registryAccess, PortConnectionType.MODDED);
        try {
            for (var type : types) {
                @SuppressWarnings("unchecked")
                var syncHandler = (PortAttachmentSyncHandler<Object>) type.syncHandler;
                if (syncHandler == null) {
                    throw new IllegalArgumentException("Received synced attachment type without a sync handler registered: " + PortRegistries.ATTACHMENT_TYPES.getKey(type));
                }
                var previousValue = holder.portlib$attachments() == null ? null : holder.portlib$attachments().get(type);
                boolean hasAttachment = buf.readBoolean();
                var result = hasAttachment ? syncHandler.read(holder.getExposedHolder(), buf, previousValue) : null;
                if (result == null) {
                    if (holder.portlib$attachments() != null) {
                        holder.portlib$attachments().remove(type);
                    }
                } else {
                    holder.getAttachmentMap().put(type, result);
                }
            }
        } catch (Exception exception) {
            throw new RuntimeException("Encountered exception when reading synced data attachments: " + types, exception);
        } finally {
            buf.release();
        }
    }

    private PortAttachmentSync() {}

    public static byte[] writeCustomData(Consumer<PortRegistryFriendlyByteBuf> dataWriter, RegistryAccess registryAccess) {
        PortRegistryFriendlyByteBuf buf = new PortRegistryFriendlyByteBuf(Unpooled.buffer(), registryAccess, PortConnectionType.MODDED);
        try {
            dataWriter.accept(buf);
            buf.readerIndex(0);
            final byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            return data;
        } finally {
            buf.release();
        }
    }
}
