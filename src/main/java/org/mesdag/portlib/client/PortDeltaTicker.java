package org.mesdag.portlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PortDeltaTicker {

    public static final PortValue ZERO = new PortValue(0.0F);
    public static final PortValue ONE = new PortValue(1.0F);

    private PortDeltaTicker() {
    }


    private static Timer getRawTimer() {
        return Minecraft.getInstance().timer;
    }

    public static float getGameTimeDeltaTicks() {
        return getRawTimer().partialTick;
    }

    public static float getGameTimeDeltaPartialTick(boolean runsNormally) {
        return getRawTimer().partialTick;
    }

    public static float getRealtimeDeltaTicks() {
        return getRawTimer().partialTick;
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