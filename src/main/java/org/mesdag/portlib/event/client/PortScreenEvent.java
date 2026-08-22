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

    public static abstract class Init<E extends ScreenEvent.Init> extends PortScreenEvent<E> {
        @Diff
        public Init(E e) {
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

        public static class Pre extends Init<ScreenEvent.Init.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.Init.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends Init<ScreenEvent.Init.Post> {
            @Diff
            public Post(ScreenEvent.Init.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class Render<E extends ScreenEvent.Render> extends PortScreenEvent<E> {
        @Diff
        public Render(E e) {
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

        public static class Pre extends Render<ScreenEvent.Render.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.Render.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends Render<ScreenEvent.Render.Post> {
            @Diff
            public Post(ScreenEvent.Render.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static class RenderInventoryMobEffects extends PortScreenEvent<ScreenEvent.RenderInventoryMobEffects> implements IPortCancellableEvent {
        @Diff
        public RenderInventoryMobEffects(ScreenEvent.RenderInventoryMobEffects e) {
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

    public static abstract class MouseButtonPressed<E extends ScreenEvent.MouseButtonPressed> extends PortScreenEvent<E> {
        @Diff
        public MouseButtonPressed(E e) {
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

        public static class Pre extends MouseButtonPressed<ScreenEvent.MouseButtonPressed.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.MouseButtonPressed.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends MouseButtonPressed<ScreenEvent.MouseButtonPressed.Post> {
            @Diff
            public Post(ScreenEvent.MouseButtonPressed.Post e) {
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

    public static abstract class MouseButtonReleased<E extends ScreenEvent.MouseButtonReleased> extends PortScreenEvent<E> {
        @Diff
        public MouseButtonReleased(E e) {
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

        public static class Pre extends MouseButtonReleased<ScreenEvent.MouseButtonReleased.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.MouseButtonReleased.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends MouseButtonReleased<ScreenEvent.MouseButtonReleased.Post> {
            @Diff
            public Post(ScreenEvent.MouseButtonReleased.Post e) {
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

    public static abstract class MouseDragged<E extends ScreenEvent.MouseDragged> extends PortScreenEvent<E> {
        @Diff
        public MouseDragged(E e) {
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

        public static class Pre extends MouseDragged<ScreenEvent.MouseDragged.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.MouseDragged.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends MouseDragged<ScreenEvent.MouseDragged.Post> {
            @Diff
            public Post(ScreenEvent.MouseDragged.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class MouseScrolled<E extends ScreenEvent.MouseScrolled> extends PortScreenEvent<E> {
        @Diff
        public MouseScrolled(E e) {
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

        public static class Pre extends MouseScrolled<ScreenEvent.MouseScrolled.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.MouseScrolled.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends MouseScrolled<ScreenEvent.MouseScrolled.Post> {
            @Diff
            public Post(ScreenEvent.MouseScrolled.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class KeyPressed<E extends ScreenEvent.KeyPressed> extends PortScreenEvent<E> {
        @Diff
        public KeyPressed(E e) {
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

        public static class Pre extends KeyPressed<ScreenEvent.KeyPressed.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.KeyPressed.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends KeyPressed<ScreenEvent.KeyPressed.Post> implements IPortCancellableEvent {
            @Diff
            public Post(ScreenEvent.KeyPressed.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class KeyReleased<E extends ScreenEvent.KeyReleased> extends PortScreenEvent<E> {
        @Diff
        public KeyReleased(E e) {
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

        public static class Pre extends KeyReleased<ScreenEvent.KeyReleased.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.KeyReleased.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends KeyReleased<ScreenEvent.KeyReleased.Post> implements IPortCancellableEvent {
            @Diff
            public Post(ScreenEvent.KeyReleased.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static abstract class CharacterTyped<E extends ScreenEvent.CharacterTyped> extends PortScreenEvent<E> {
        @Diff
        public CharacterTyped(E e) {
            super(e);
        }

        public char getCodePoint() {
            return e.getCodePoint();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        public static class Pre extends CharacterTyped<ScreenEvent.CharacterTyped.Pre> implements IPortCancellableEvent {
            @Diff
            public Pre(ScreenEvent.CharacterTyped.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class Post extends CharacterTyped<ScreenEvent.CharacterTyped.Post> {
            @Diff
            public Post(ScreenEvent.CharacterTyped.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static class Opening extends PortScreenEvent<ScreenEvent.Opening> implements IPortCancellableEvent {
        @Diff
        public Opening(ScreenEvent.Opening e) {
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

    public static class Closing extends PortScreenEvent<ScreenEvent.Closing> {
        @Diff
        public Closing(ScreenEvent.Closing e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
