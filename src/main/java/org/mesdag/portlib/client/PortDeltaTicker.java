package org.mesdag.portlib.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PortDeltaTicker {
    private static final PortDeltaTicker INSTANCE = new PortDeltaTicker();
    private DeltaTracker delegate;

    public static final PortValue ZERO = new PortValue(0.0F);
    public static final PortValue ONE = new PortValue(1.0F);

    private PortDeltaTicker() {
    }

    public static PortDeltaTicker getInstance() {
        INSTANCE.delegate = Minecraft.getInstance().getTimer();
        return INSTANCE;
    }

    public static PortDeltaTicker wrap(DeltaTracker tracker) {
        INSTANCE.delegate = tracker != null ? tracker : Minecraft.getInstance().getTimer();
        return INSTANCE;
    }

    public float getGameTimeDeltaTicks() {
        return this.delegate != null ? this.delegate.getGameTimeDeltaTicks() : 0.0F;
    }

    public float getGameTimeDeltaPartialTick(boolean runsNormally) {
        return this.delegate != null ? this.delegate.getGameTimeDeltaPartialTick(runsNormally) : 0.0F;
    }

    public float getRealtimeDeltaTicks() {
        return this.delegate != null ? this.delegate.getRealtimeDeltaTicks() : 0.0F;
    }

    public static class PortValue {
        private final float value;

        public PortValue(float value) {
            this.value = value;
        }

        public float get() {
            return this.value;
        }
    }
}