package org.mesdag.portlib.network;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.Final;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public interface IPortPacket {
    void handle(Context context);

    ResourceLocation identifier();

    @Final
    default Packet<ClientGamePacketListener> toVanillaClientbound() {
        return PortPacketDistributor.distribute(this).toVanillaClientbound(this);
    }

    @Final
    default Packet<ServerGamePacketListener> toVanillaServerbound() {
        return PortPacketDistributor.distribute(this).toVanillaServerbound(this);
    }

    interface C2S extends IPortPacket {
        @Override
        default void handle(Context context) {
            if (context.player() instanceof ServerPlayer player) {
                context.enqueueWork(() -> work(player));
            }
        }

        @Final
        @Override
        default Packet<ClientGamePacketListener> toVanillaClientbound() {
            throw new UnsupportedOperationException();
        }

        void work(ServerPlayer player);
    }

    interface S2C extends IPortPacket {
        @Override
        default void handle(Context context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    work(player);
                }
            });
        }

        @Final
        @Override
        default Packet<ServerGamePacketListener> toVanillaServerbound() {
            throw new UnsupportedOperationException();
        }

        void work(Player player);
    }

    class Context {
        private final Supplier<@Nullable Player> player;
        private final Connection connection;
        private final Consumer<Runnable> executor;
        private final Consumer<IPortPacket> reply;
        private final Consumer<Component> disconnect;

        Context(
                Supplier<@Nullable Player> player,
                Connection connection,
                Consumer<Runnable> executor,
                Consumer<IPortPacket> reply,
                Consumer<Component> disconnect
        ) {
            this.player = player;
            this.connection = connection;
            this.executor = executor;
            this.reply = reply;
            this.disconnect = disconnect;
        }

        @Diff
        static Context wrap(Supplier<@Nullable Player> player, NetworkEvent.Context context, SimpleChannel channel) {
            return new Context(
                    player,
                    context.getNetworkManager(),
                    context::enqueueWork,
                    p1 -> channel.reply(p1, context),
                    context.getNetworkManager()::disconnect
            );
        }

        public @Nullable Player player() {
            return player.get();
        }

        public Connection connection() {
            return connection;
        }

        public ChannelHandlerContext channelHandlerContext() {
            return connection.channel().pipeline().lastContext();
        }

        public void enqueueWork(Runnable task) {
            executor.accept(task);
        }

        public void reply(IPortPacket packet) {
            reply.accept(packet);
        }

        public void disconnect(Component reason) {
            disconnect.accept(reason);
        }
    }
}
