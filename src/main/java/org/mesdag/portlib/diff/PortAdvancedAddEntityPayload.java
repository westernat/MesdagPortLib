package org.mesdag.portlib.diff;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.entity.IPortEntityWithComplexSpawn;

@Diff
public record PortAdvancedAddEntityPayload(
        int entityId,
        byte[] customPayload
) implements IPortPacket.S2C {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("portlib", "advanced_add_entity");
    public static final PortStreamCodec<FriendlyByteBuf, PortAdvancedAddEntityPayload> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.VAR_INT,
            PortAdvancedAddEntityPayload::entityId,
            PortByteBufCodecs.UNBOUNDED_BYTE_ARRAY,
            PortAdvancedAddEntityPayload::customPayload,
            PortAdvancedAddEntityPayload::new);

    public PortAdvancedAddEntityPayload(Entity e) {
        this(e.getId(), writeCustomData(e));
    }

    private static byte[] writeCustomData(Entity entity) {
        if (!(entity instanceof IPortEntityWithComplexSpawn port)) {
            return new byte[0];
        }
        PortRegistryFriendlyByteBuf buf = new PortRegistryFriendlyByteBuf(Unpooled.buffer(), PortEntityExtension.registryAccess(entity), PortConnectionType.MODDED);
        try {
            port.writeSpawnData(buf);
            buf.readerIndex(0);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            return data;
        } finally {
            buf.release();
        }
    }

    @Override
    public void handle(Context context) {
        Player player = context.player();
        if (player == null) return;
        try {
            Entity entity = player.level().getEntity(entityId);
            if (entity instanceof IPortEntityWithComplexSpawn port) {
                PortRegistryFriendlyByteBuf buf = new PortRegistryFriendlyByteBuf(
                        Unpooled.wrappedBuffer(customPayload()),
                        PortEntityExtension.registryAccess(entity),
                        PortConnectionType.MODDED
                );
                try {
                    port.readSpawnData(buf);
                } finally {
                    buf.release();
                }
            }
        } catch (Throwable t) {
            PortLib.LOGGER.error("Failed to handle advanced add entity from server.", t);
            context.disconnect(Component.translatable("portlib.network.advanced_add_entity.failed", t.toString()));
        }
    }

    @Override
    public void work(Player player) {}

    @Override
    public ResourceLocation identifier() {
        return ID;
    }
}
