package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortMouseHandler;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortScreenEvent<E extends ScreenEvent> extends PortEvent<E> {
    @Diff
    public PortScreenEvent(E e) {
        super(e);
    }

    public Screen getScreen() {
        return e.getScreen();
    }

    public static abstract class PortInit<E extends ScreenEvent.Init> extends PortScreenEvent<E> {
        @Diff
        public PortInit(E e) {
            super(e);
        }

        public List<GuiEventListener> getListenersList() {
            return e.getListenersList();
        }

        public void addListener(GuiEventListener listener) {
            e.addListener(listener);
        }

        public void removeListener(GuiEventListener listener) {
            e.removeListener(listener);
        }

        public static class PortPre extends PortInit<ScreenEvent.Init.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.Init.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortInit<ScreenEvent.Init.Post> {
            @Diff
            public PortPost(ScreenEvent.Init.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class PortRender<E extends ScreenEvent.Render> extends PortScreenEvent<E> {
        @Diff
        public PortRender(E e) {
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

        public float getPartialTick() {
            return e.getPartialTick();
        }

        public static class PortPre extends PortRender<ScreenEvent.Render.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.Render.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortRender<ScreenEvent.Render.Post> {
            @Diff
            public PortPost(ScreenEvent.Render.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static class PortRenderInventoryMobEffects extends PortScreenEvent<ScreenEvent.RenderInventoryMobEffects> implements IPortCancellableEvent {
        @Diff
        public PortRenderInventoryMobEffects(ScreenEvent.RenderInventoryMobEffects e) {
            super(e);
        }

        public int getAvailableSpace() {
            return e.getAvailableSpace();
        }

        public boolean isCompact() {
            return e.isCompact();
        }

        public int getHorizontalOffset() {
            return e.getHorizontalOffset();
        }

        public void setHorizontalOffset(int offset) {
            e.setHorizontalOffset(offset);
        }

        public void addHorizontalOffset(int offset) {
            e.addHorizontalOffset(offset);
        }

        public void setCompact(boolean compact) {
            e.setCompact(compact);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static abstract class PortMouseButtonPressed<E extends ScreenEvent.MouseButtonPressed> extends PortScreenEvent<E> {
        @Diff
        public PortMouseButtonPressed(E e) {
            super(e);
        }

        public double getMouseX() {
            return e.getMouseX();
        }

        public double getMouseY() {
            return e.getMouseY();
        }

        public int getButton() {
            return e.getButton();
        }

        public static class PortPre extends PortMouseButtonPressed<ScreenEvent.MouseButtonPressed.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.MouseButtonPressed.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortMouseButtonPressed<ScreenEvent.MouseButtonPressed.Post> {
            @Diff
            public PortPost(ScreenEvent.MouseButtonPressed.Post e) {
                super(e);
            }

            public boolean wasClickHandled() {
                return e.wasHandled();
            }

            public void setPortResult(PortResult result) {
                e.setResult(result.unwrap());
            }

            public PortResult getPortResult() {
                return PortResult.wrap(e.getResult());
            }

            public boolean getClickResult() {
                if (getResult().isAllowed()) {
                    return true;
                }
                return getResult().isDefault() && wasClickHandled();
            }

            static {
                PortEventHooks.register();
            }

            public enum PortResult {
                FORCE_HANDLED,
                DEFAULT,
                FORCE_UNHANDLED;

                @Diff
                public Result unwrap() {
                    if (this == FORCE_HANDLED) {
                        return Result.ALLOW;
                    } else if (this == FORCE_UNHANDLED) {
                        return Result.DENY;
                    }
                    return Result.DEFAULT;
                }

                @Diff
                public static PortResult wrap(Result result) {
                    if (result.isAllowed()) return FORCE_HANDLED;
                    if (result.isDenied()) return FORCE_UNHANDLED;
                    return DEFAULT;
                }
            }
        }
    }

    public static abstract class PortMouseButtonReleased<E extends ScreenEvent.MouseButtonReleased> extends PortScreenEvent<E> {
        @Diff
        public PortMouseButtonReleased(E e) {
            super(e);
        }

        public double getMouseX() {
            return e.getMouseX();
        }

        public double getMouseY() {
            return e.getMouseY();
        }

        public int getButton() {
            return e.getButton();
        }

        public static class PortPre extends PortMouseButtonReleased<ScreenEvent.MouseButtonReleased.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.MouseButtonReleased.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortMouseButtonReleased<ScreenEvent.MouseButtonReleased.Post> {
            @Diff
            public PortPost(ScreenEvent.MouseButtonReleased.Post e) {
                super(e);
            }

            public boolean wasReleaseHandled() {
                return e.wasHandled();
            }

            public void setPortResult(PortResult result) {
                e.setResult(result.unwrap());
            }

            public PortResult getPortResult() {
                return PortResult.wrap(e.getResult());
            }

            public boolean getReleaseResult() {
                if (getResult().isAllowed()) {
                    return true;
                }
                return getResult().isDefault() && wasReleaseHandled();
            }

            static {
                PortEventHooks.register();
            }

            public enum PortResult {
                FORCE_HANDLED,
                DEFAULT,
                FORCE_UNHANDLED;

                @Diff
                public Result unwrap() {
                    if (this == FORCE_HANDLED) {
                        return Result.ALLOW;
                    } else if (this == FORCE_UNHANDLED) {
                        return Result.DENY;
                    }
                    return Result.DEFAULT;
                }

                @Diff
                public static PortResult wrap(Result result) {
                    if (result.isAllowed()) return FORCE_HANDLED;
                    if (result.isDenied()) return FORCE_UNHANDLED;
                    return DEFAULT;
                }
            }
        }
    }

    public static abstract class PortMouseDragged<E extends ScreenEvent.MouseDragged> extends PortScreenEvent<E> {
        @Diff
        public PortMouseDragged(E e) {
            super(e);
        }

        public double getMouseX() {
            return e.getMouseX();
        }

        public double getMouseY() {
            return e.getMouseY();
        }

        public int getMouseButton() {
            return e.getMouseButton();
        }

        public double getDragX() {
            return e.getDragX();
        }

        public double getDragY() {
            return e.getDragY();
        }

        public static class PortPre extends PortMouseDragged<ScreenEvent.MouseDragged.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.MouseDragged.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortMouseDragged<ScreenEvent.MouseDragged.Post> {
            @Diff
            public PortPost(ScreenEvent.MouseDragged.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class PortMouseScrolled<E extends ScreenEvent.MouseScrolled> extends PortScreenEvent<E> {
        @Diff
        public PortMouseScrolled(E e) {
            super(e);
        }

        public double getMouseX() {
            return e.getMouseX();
        }

        public double getMouseY() {
            return e.getMouseY();
        }

        public double getScrollDeltaX() {
            return PortMouseHandler.scrollDeltaX;
        }

        public double getScrollDeltaY() {
            return PortMouseHandler.scrollDeltaY;
        }

        public static class PortPre extends PortMouseScrolled<ScreenEvent.MouseScrolled.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.MouseScrolled.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortMouseScrolled<ScreenEvent.MouseScrolled.Post> {
            @Diff
            public PortPost(ScreenEvent.MouseScrolled.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class PortKeyPressed<E extends ScreenEvent.KeyPressed> extends PortScreenEvent<E> {
        @Diff
        public PortKeyPressed(E e) {
            super(e);
        }

        public int getKeyCode() {
            return e.getKeyCode();
        }

        public int getScanCode() {
            return e.getScanCode();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        public static class PortPre extends PortKeyPressed<ScreenEvent.KeyPressed.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.KeyPressed.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortKeyPressed<ScreenEvent.KeyPressed.Post> implements IPortCancellableEvent {
            @Diff
            public PortPost(ScreenEvent.KeyPressed.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class PortKeyReleased<E extends ScreenEvent.KeyReleased> extends PortScreenEvent<E> {
        @Diff
        public PortKeyReleased(E e) {
            super(e);
        }

        public int getKeyCode() {
            return e.getKeyCode();
        }

        public int getScanCode() {
            return e.getScanCode();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        public static class PortPre extends PortKeyReleased<ScreenEvent.KeyReleased.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.KeyReleased.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortKeyReleased<ScreenEvent.KeyReleased.Post> implements IPortCancellableEvent {
            @Diff
            public PortPost(ScreenEvent.KeyReleased.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class PortCharacterTyped<E extends ScreenEvent.CharacterTyped> extends PortScreenEvent<E> {
        @Diff
        public PortCharacterTyped(E e) {
            super(e);
        }

        public char getCodePoint() {
            return e.getCodePoint();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        public static class PortPre extends PortCharacterTyped<ScreenEvent.CharacterTyped.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(ScreenEvent.CharacterTyped.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortCharacterTyped<ScreenEvent.CharacterTyped.Post> {
            @Diff
            public PortPost(ScreenEvent.CharacterTyped.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static class PortOpening extends PortScreenEvent<ScreenEvent.Opening> implements IPortCancellableEvent {
        @Diff
        public PortOpening(ScreenEvent.Opening e) {
            super(e);
        }

        public @Nullable Screen getCurrentScreen() {
            return e.getCurrentScreen();
        }

        public @Nullable Screen getNewScreen() {
            return e.getNewScreen();
        }

        public void setNewScreen(Screen newScreen) {
            e.setNewScreen(newScreen);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortClosing extends PortScreenEvent<ScreenEvent.Closing> {
        @Diff
        public PortClosing(ScreenEvent.Closing e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
