package org.mesdag.portlib.network.login;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.IntSupplier;

public abstract class PortLoginPacket implements IntSupplier {
    private int loginIndex;

    @ApiStatus.Internal
    public void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }

    @ApiStatus.Internal
    public int getLoginIndex() {
        return loginIndex;
    }

    @Override
    public int getAsInt() {
        return getLoginIndex();
    }
}
