package org.mesdag.portlib.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.PortIdentifier;

@SuppressWarnings("all")
public interface IPortPacket {
    void handle(Context context);

    PortIdentifier identifier();

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

    record Context(@Nullable Player player) {}
}
