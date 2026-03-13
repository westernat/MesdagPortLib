package org.mesdag.portlib.network;

import net.minecraftforge.network.NetworkDirection;

public enum PacketDirection {
    PLAY_TO_SERVER,
    PLAY_TO_CLIENT,
    LOGIN_TO_SERVER,
    LOGIN_TO_CLIENT;

    NetworkDirection unwrap() {
        return switch (this) {
            case PLAY_TO_SERVER -> NetworkDirection.PLAY_TO_SERVER;
            case PLAY_TO_CLIENT -> NetworkDirection.PLAY_TO_CLIENT;
            case LOGIN_TO_SERVER -> NetworkDirection.LOGIN_TO_SERVER;
            case LOGIN_TO_CLIENT -> NetworkDirection.LOGIN_TO_CLIENT;
        };
    }
}
