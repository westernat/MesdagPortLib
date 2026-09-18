package org.mesdag.portlib.network;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.PortBundledPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public final class PortPacketDistributor {
    static final Map<ResourceLocation, PortNetworkHandler> PACKET_OWNER = new HashMap<>();

    private PortPacketDistributor() {}

    static PortNetworkHandler distribute(IPortPacket packet) {
        ResourceLocation id = packet.identifier();
        PortNetworkHandler handler = PACKET_OWNER.get(id);
        if (handler == null) {
            throw new IllegalStateException("packet '" + id + "' has not register in any of handlers");
        }
        return handler;
    }

    private static void distribute(BiConsumer<PortNetworkHandler, IPortPacket> consumer, IPortPacket packet, IPortPacket... packets) {
        if (packets.length == 0) {
            consumer.accept(distribute(packet), packet);
        } else {
            var map = new Reference2ObjectLinkedOpenHashMap<PortNetworkHandler, ObjectList<IPortPacket>>();
            var list = new ObjectArrayList<IPortPacket>();
            list.add(packet);
            map.put(distribute(packet), list);
            for (var packet1 : packets) {
                map.computeIfAbsent(distribute(packet1), h -> new ObjectArrayList<>()).add(packet1);
            }
            for (var entry : map.reference2ObjectEntrySet()) {
                consumer.accept(entry.getKey(), PortBundledPacket.makePacket(entry.getValue()));
            }
        }
    }

    public static void sendToServer(IPortPacket packet, IPortPacket... packets) {
        distribute(PortNetworkHandler::sendToServer, packet, packets);
    }

    public static void sendToPlayer(ServerPlayer player, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayer(player, packet1), packet, packets);
    }

    public static void sendToPlayersInDimension(ResourceKey<Level> dimension, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayersInDimension(dimension, packet1), packet, packets);
    }

    public static void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayersNear(dimension, excluded, x, y, z, radius, packet1), packet, packets);
    }

    public static void sendToAllPlayers(IPortPacket packet, IPortPacket... packets) {
        distribute(PortNetworkHandler::sendToAllPlayers, packet, packets);
    }

    public static void sendToPlayersTrackingEntity(Entity entity, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayersTrackingEntity(entity, packet1), packet, packets);
    }

    public static void sendToPlayersTrackingEntityAndSelf(Entity entity, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayersTrackingEntityAndSelf(entity, packet1), packet, packets);
    }

    public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPortPacket packet, IPortPacket... packets) {
        distribute((handler, packet1) -> handler.sendToPlayersTrackingChunk(level, pos, packet1), packet, packets);
    }
}
