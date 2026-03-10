package org.mesdag.portlib.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.Identifier;

public interface IPacket {
    void handle(Context context);

    Identifier identifier();

    interface C2S extends IPacket {
        @Override
        default void handle(Context context) {
            if (context.player instanceof ServerPlayer player) {
                work(player);
            }
        }

        void work(ServerPlayer player);
    }

    interface S2C extends IPacket {
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
