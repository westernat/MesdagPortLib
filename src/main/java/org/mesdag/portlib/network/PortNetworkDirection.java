package org.mesdag.portlib.network;

import net.minecraftforge.network.NetworkDirection;
import org.mesdag.portlib.diff.Diff;

public enum PortNetworkDirection {
    PLAY_TO_SERVER,
    PLAY_TO_CLIENT,
    LOGIN_TO_SERVER,
    LOGIN_TO_CLIENT;

    public boolean isLogin() {
        return this == LOGIN_TO_CLIENT || this == LOGIN_TO_SERVER;
    }

    public boolean isPlay() {
        return this == PLAY_TO_CLIENT || this == PLAY_TO_SERVER;
    }

    public boolean toClient() {
        return this == PLAY_TO_CLIENT || this == LOGIN_TO_CLIENT;
    }

    public boolean toServer() {
        return this == PLAY_TO_SERVER || this == LOGIN_TO_SERVER;
    }

    @Diff
    NetworkDirection unwrap() {
        return switch (this) {
            case PLAY_TO_SERVER -> NetworkDirection.PLAY_TO_SERVER;
            case PLAY_TO_CLIENT -> NetworkDirection.PLAY_TO_CLIENT;
            case LOGIN_TO_SERVER -> NetworkDirection.LOGIN_TO_SERVER;
            case LOGIN_TO_CLIENT -> NetworkDirection.LOGIN_TO_CLIENT;
        };
    }
}
