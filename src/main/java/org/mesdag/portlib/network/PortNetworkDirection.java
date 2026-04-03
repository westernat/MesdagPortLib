package org.mesdag.portlib.network;

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
}
