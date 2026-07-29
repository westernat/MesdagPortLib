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

/**
 * PortLib 跨版本业务包契约。
 *
 * <p>{@link C2S} 与 {@link S2C} 用类型表达固定方向，基础接口只用于确实需要双向复用编码的
 * 少量消息。{@link Context} 隐藏 Forge/NeoForge 上下文差异，并提供玩家、回复、断开连接和
 * 主线程任务入口；具体玩法校验仍必须在业务包中以服务端状态为准。</p>
 */
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
        private final boolean serverbound;

        Context(
                @Nullable Player player,
                Connection connection,
                Consumer<Runnable> executor,
                Consumer<IPortPacket> reply,
                Consumer<Component> disconnect,
                boolean serverbound
        ) {
            this.player = player;
            this.connection = connection;
            this.executor = executor;
            this.reply = reply;
            this.disconnect = disconnect;
            this.serverbound = serverbound;
        }

        @Diff
        static Context wrap(@Nullable Player player, NetworkEvent.Context context, SimpleChannel channel) {
            boolean serverbound = context.getDirection().getReceptionSide().isServer();
            return new Context(
                    player,
                    context.getNetworkManager(),
                    context::enqueueWork,
                    packet -> {
                        PortNetworkHandler.validateReplyDirection(packet, serverbound);
                        channel.reply(packet, context);
                    },
                    context.getNetworkManager()::disconnect,
                    serverbound
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

        /** 当前消息是否由客户端发往服务器。 */
        public boolean isServerbound() {
            return serverbound;
        }
    }
}
