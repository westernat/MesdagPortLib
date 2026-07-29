package org.mesdag.portlib.network;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Consumer;

@SuppressWarnings("all")
public interface IPortPacket {
    void handle(Context context);

    ResourceLocation identifier();

    interface C2S extends IPortPacket {
        @Override
        default void handle(Context context) {
            if (context.player instanceof ServerPlayer player) {
                work(player);
            }
        }

        void work(ServerPlayer player);
    }

    interface S2C extends IPortPacket {
        @Override
        default void handle(Context context) {
            if (context.player != null) {
                work(context.player);
            }
        }

        void work(Player player);
    }

    class Context {
        @Nullable
        private final Player player;
        private final Connection connection;
        private final Consumer<Runnable> executor;
        private final Consumer<IPortPacket> reply;
        private final Consumer<Component> disconnect;

        Context(
                @Nullable Player player,
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
        static Context wrap(@Nullable Player player, NetworkEvent.Context context, SimpleChannel channel) {
            return new Context(
                    player,
                    context.getNetworkManager(),
                    context::enqueueWork,
                    p1 -> channel.reply(p1, context),
                    context.getNetworkManager()::disconnect
            );
        }

        public @Nullable Player player() {
            return player;
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
