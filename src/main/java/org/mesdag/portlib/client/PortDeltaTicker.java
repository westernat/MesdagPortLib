package org.mesdag.portlib.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import org.mesdag.portlib.diff.Diff;

public enum PortDeltaTicker {
    INSTANCE;

    private DeltaTracker delegate;

    @Diff
    public DeltaTracker unwrap() {
        bind();
        return delegate;
    }

    @Diff
    public void bind(DeltaTracker delegate) {
        if (this.delegate == null) {
            this.delegate = delegate;
        }
    }

    private void bind() {
        bind(Minecraft.getInstance().getTimer());
    }

    public float getGameTimeDeltaTicks() {
        bind();
        return delegate.getGameTimeDeltaTicks();
    }

    public float getGameTimeDeltaPartialTick(boolean runsNormally) {
        bind();
        return delegate.getGameTimeDeltaPartialTick(runsNormally);
    }

    public float getRealtimeDeltaTicks() {
        bind();
        return delegate.getRealtimeDeltaTicks();
    }
}
