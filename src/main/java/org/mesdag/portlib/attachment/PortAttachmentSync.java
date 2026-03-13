package org.mesdag.portlib.attachment;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
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
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.diff.PortSyncAttachmentsPayload;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.registries.CustomRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.callback.PortAddCallback;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Diff
public final class PortAttachmentSync {
    public static final CustomRegistration<PortAttachmentType<?>> SYNCED_ATTACHMENT_TYPES = PortRegisterHandler.custom(
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

    private static PortSyncAttachmentsPayload.Target syncTarget(PortAttachmentHolder holder) {
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

    private static <T> void syncUpdate(PortAttachmentHolder holder, PortAttachmentType<T> type, List<ServerPlayer> players) {
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
                player.connection.send(packet);
            }
        }
    }

    public static void syncBlockEntityUpdate(BlockEntity blockEntity, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(blockEntity.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        syncUpdate(PortAttachmentHolder.of(blockEntity), type, serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockEntity.getBlockPos()), false));
    }

    public static void syncChunkUpdate(LevelChunk chunk, AttachmentHolder.AsField holder, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(chunk.getLevel() instanceof ServerLevel serverLevel)) {
            return;
        }
        syncUpdate(PortAttachmentHolder.of(chunk), type, serverLevel.getChunkSource().chunkMap.getPlayers(chunk.getPos(), false));
    }

    public static void syncEntityUpdate(Entity entity, PortAttachmentType<?> type) {
        if (type.syncHandler == null || !(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        var players = serverLevel.getChunkSource().chunkMap.getPlayersWatching(entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            // Players do not track themselves
            var newPlayers = new ArrayList<ServerPlayer>(players.size() + 1);
            newPlayers.addAll(players);
            newPlayers.add(serverPlayer);
            players = newPlayers;
        }
        syncUpdate(PortAttachmentHolder.of(entity), type, players);
    }

    public static void syncLevelUpdate(ServerLevel level, PortAttachmentType<?> type) {
        if (type.syncHandler == null) {
            return;
        }
        syncUpdate(PortAttachmentHolder.of(level), type, level.players());
    }

    /**
     * Constructs a payload to sync all syncable attachments to a player, if any.
     */
    @Nullable
    private static PortSyncAttachmentsPayload syncInitialAttachments(PortAttachmentHolder holder, ServerPlayer to) {
        if (holder.attachments == null) {
            return null;
        }
        boolean anySyncableAttachment = false;
        for (var attachment : holder.attachments.keySet()) {
            anySyncableAttachment = anySyncableAttachment | attachment.syncHandler != null;
        }
        if (!anySyncableAttachment) {
            return null;
        }
        List<PortAttachmentType<?>> syncedTypes = new ArrayList<>();
        var data = FriendlyByteBufUtil.writeCustomData(buf -> {
            for (var entry : holder.attachments.entrySet()) {
                PortAttachmentType<?> type = entry.getKey();
                @SuppressWarnings("unchecked")
                var syncHandler = (AttachmentSyncHandler<Object>) type.syncHandler;
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
        }, to.registryAccess());
        return new SyncAttachmentsPayload(syncTarget(holder), syncedTypes, data);
    }

    /**
     * Handles initial syncing of block entity and chunk attachments.
     */
    @SubscribeEvent
    public static void onChunkSent(ChunkWatchEvent.Sent event) {
        List<Packet<? super ClientGamePacketListener>> packets = new ArrayList<>();
        var chunkPayload = syncInitialAttachments(event.getChunk().getAttachmentHolder(), event.getPlayer());
        if (chunkPayload != null) {
            packets.add(chunkPayload.toVanillaClientbound());
        }
        for (var blockEntity : event.getChunk().getBlockEntities().values()) {
            var blockEntityPayload = syncInitialAttachments(blockEntity, event.getPlayer());
            if (blockEntityPayload != null) {
                packets.add(blockEntityPayload.toVanillaClientbound());
            }
        }
        if (!packets.isEmpty()) {
            event.getPlayer().connection.send(new ClientboundBundlePacket(packets));
        }
    }

    /**
     * Handles initial syncing of entity attachments, except for a player's own attachments.
     */
    public static void syncInitialEntityAttachments(Entity entity, ServerPlayer to, Consumer<Packet<? super ClientGamePacketListener>> packetConsumer) {
        var packet = syncInitialAttachments(entity, to);
        if (packet != null) {
            packetConsumer.accept(packet.toVanillaClientbound());
        }
    }

    /**
     * Handles initial syncing of a player's own attachments.
     */
    public static void syncInitialPlayerAttachments(ServerPlayer player) {
        var packet = syncInitialAttachments(player, player);
        if (packet != null) {
            player.connection.send(packet.toVanillaClientbound());
        }
    }

    /**
     * Handles initial syncing of level attachments. Needs to be called for login, respawn and teleports.
     */
    public static void syncInitialLevelAttachments(ServerLevel level, ServerPlayer to) {
        var packet = syncInitialAttachments(level, to);
        if (packet != null) {
            to.connection.send(packet.toVanillaClientbound());
        }
    }

    public static void receiveSyncedDataAttachments(PortAttachmentHolder holder, RegistryAccess registryAccess, List<PortAttachmentType<?>> types, byte[] bytes) {
        var buf = new PortRegistryFriendlyByteBuf(Unpooled.wrappedBuffer(bytes), registryAccess, PortConnectionType.MODDED);
        try {
            for (var type : types) {
                @SuppressWarnings("unchecked")
                var syncHandler = (PortAttachmentSyncHandler<Object>) type.syncHandler;
                if (syncHandler == null) {
                    throw new IllegalArgumentException("Received synced attachment type without a sync handler registered: " + NeoForgeRegistries.ATTACHMENT_TYPES.getKey(type));
                }
                var previousValue = holder.attachments == null ? null : holder.attachments.get(type);
                boolean hasAttachment = buf.readBoolean();
                var result = hasAttachment ? syncHandler.read(holder.getExposedHolder(), buf, previousValue) : null;
                if (result == null) {
                    if (holder.attachments != null) {
                        holder.attachments.remove(type);
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
