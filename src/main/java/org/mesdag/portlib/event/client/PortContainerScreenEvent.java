package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ContainerScreenEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortContainerScreenEvent<E extends ContainerScreenEvent> extends PortEvent<E> {
    @Diff
    public PortContainerScreenEvent(E e) {
        super(e);
    }

    public AbstractContainerScreen<?> getContainerScreen() {
        return e.getContainerScreen();
    }

    public static abstract class PortRender<E extends ContainerScreenEvent.Render> extends PortContainerScreenEvent<E> {
        @Diff
        protected PortRender(E e) {
            super(e);
        }

        public GuiGraphics getGuiGraphics() {
            return e.getGuiGraphics();
        }

        public int getMouseX() {
            return e.getMouseX();
        }

        public int getMouseY() {
            return e.getMouseY();
        }

        public static class Foreground extends PortRender<ContainerScreenEvent.Render.Foreground> {
            @Diff
            public Foreground(ContainerScreenEvent.Render.Foreground e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Background extends PortRender<ContainerScreenEvent.Render.Background> {
            @Diff
            public Background(ContainerScreenEvent.Render.Background e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }
}
