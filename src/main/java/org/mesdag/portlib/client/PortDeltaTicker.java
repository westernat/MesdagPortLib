package org.mesdag.portlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.mesdag.portlib.diff.Diff;

public enum PortDeltaTicker {
    INSTANCE;

    private Timer delegate;

    @Diff
    public Timer unwrap() {
        bind();
        return delegate;
    }

    @Diff
    public void bind(Timer delegate) {
        if (this.delegate == null) {
            this.delegate = delegate;
        }
    }

    private void bind() {
        bind(Minecraft.getInstance().timer);
    }

    public float getGameTimeDeltaTicks() {
        bind();
        return delegate.tickDelta;
    }

    public float getGameTimeDeltaPartialTick(boolean runsNormally) {
        bind();
        return delegate.partialTick;
    }

    public float getRealtimeDeltaTicks() {
        bind();
        return delegate.tickDelta;
    }
}
