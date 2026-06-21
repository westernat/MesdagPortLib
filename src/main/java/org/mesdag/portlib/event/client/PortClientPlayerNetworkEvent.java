package org.mesdag.portlib.event.client;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortClientPlayerNetworkEvent<E extends ClientPlayerNetworkEvent> extends PortEvent<E> {
    @Diff
    public PortClientPlayerNetworkEvent(E e) {
        super(e);
    }

    public MultiPlayerGameMode getMultiPlayerGameMode() {
        return e.getMultiPlayerGameMode();
    }

    public LocalPlayer getPlayer() {
        return e.getPlayer();
    }

    public Connection getConnection() {
        return e.getConnection();
    }

    public static class LoggingIn extends PortClientPlayerNetworkEvent<ClientPlayerNetworkEvent.LoggingIn> {
        @Diff
        public LoggingIn(ClientPlayerNetworkEvent.LoggingIn e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class LoggingOut extends PortClientPlayerNetworkEvent<ClientPlayerNetworkEvent.LoggingOut> {
        @Diff
        public LoggingOut(ClientPlayerNetworkEvent.LoggingOut e) {
            super(e);
        }

        @Override
        public @Nullable MultiPlayerGameMode getMultiPlayerGameMode() {
            return super.getMultiPlayerGameMode();
        }

        @Override
        public @Nullable LocalPlayer getPlayer() {
            return super.getPlayer();
        }

        @Override
        public @Nullable Connection getConnection() {
            return super.getConnection();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Clone extends PortClientPlayerNetworkEvent<ClientPlayerNetworkEvent.Clone> {
        @Diff
        public Clone(ClientPlayerNetworkEvent.Clone e) {
            super(e);
        }

        public LocalPlayer getOldPlayer() {
            return e.getOldPlayer();
        }

        public LocalPlayer getNewPlayer() {
            return e.getNewPlayer();
        }

        static {
            PortEventHooks.register();
        }
    }
}
