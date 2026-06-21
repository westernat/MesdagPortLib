package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import org.mesdag.portlib.client.PortDeltaTicker;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortCustomizeGuiOverlayEvent<E extends CustomizeGuiOverlayEvent> extends PortEvent<E> {
    @Diff
    public PortCustomizeGuiOverlayEvent(E e) {
        super(e);
    }

    public Window getWindow() {
        return e.getWindow();
    }

    public GuiGraphics getGuiGraphics() {
        return e.getGuiGraphics();
    }

    public PortDeltaTicker getPartialTick() {
        return PortDeltaTicker.INSTANCE;
    }

    public static class BossEventProgress extends PortCustomizeGuiOverlayEvent<CustomizeGuiOverlayEvent.BossEventProgress> implements IPortCancellableEvent {
        @Diff
        public BossEventProgress(CustomizeGuiOverlayEvent.BossEventProgress e) {
            super(e);
        }

        public LerpingBossEvent getBossEvent() {
            return e.getBossEvent();
        }

        public int getX() {
            return e.getX();
        }

        public int getY() {
            return e.getY();
        }

        public int getIncrement() {
            return e.getIncrement();
        }

        public void setIncrement(int increment) {
            e.setIncrement(increment);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class DebugText extends PortCustomizeGuiOverlayEvent<CustomizeGuiOverlayEvent.DebugText> {
        @Diff
        public DebugText(CustomizeGuiOverlayEvent.DebugText e) {
            super(e);
        }

        public List<String> getLeft() {
            return e.getLeft();
        }

        public List<String> getRight() {
            return e.getRight();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Chat extends PortCustomizeGuiOverlayEvent<CustomizeGuiOverlayEvent.Chat> {
        @Diff
        public Chat(CustomizeGuiOverlayEvent.Chat e) {
            super(e);
        }

        public int getPosX() {
            return e.getPosX();
        }

        public void setPosX(int posX) {
            e.setPosX(posX);
        }

        public int getPosY() {
            return e.getPosY();
        }

        public void setPosY(int posY) {
            e.setPosY(posY);
        }

        static {
            PortEventHooks.register();
        }
    }
}
