package org.mesdag.portlib.event.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.client.event.InputEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortInputEvent<E extends InputEvent> extends PortEvent<E> {
    @Diff
    public PortInputEvent(E e) {
        super(e);
    }

    public static abstract class PortMouseButton<E extends InputEvent.MouseButton> extends PortInputEvent<E> {
        @Diff
        public PortMouseButton(E e) {
            super(e);
        }

        public int getButton() {
            return e.getButton();
        }

        public int getAction() {
            return e.getAction();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        public static class PortPre extends PortMouseButton<InputEvent.MouseButton.Pre> implements IPortCancellableEvent {
            @Diff
            public PortPre(InputEvent.MouseButton.Pre e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }

        public static class PortPost extends PortMouseButton<InputEvent.MouseButton.Post> {
            @Diff
            public PortPost(InputEvent.MouseButton.Post e) {
                super(e);
            }

            static {
                PortEventHooks.register();
            }
        }
    }

    public static class PortMouseScrollingEvent extends PortInputEvent<InputEvent.MouseScrollingEvent> implements IPortCancellableEvent {
        @Diff
        public PortMouseScrollingEvent(InputEvent.MouseScrollingEvent e) {
            super(e);
        }

        public double getScrollDeltaX() {
            return e.getScrollDeltaX();
        }

        public double getScrollDeltaY() {
            return e.getScrollDeltaY();
        }

        public boolean isLeftDown() {
            return e.isLeftDown();
        }

        public boolean isRightDown() {
            return e.isRightDown();
        }

        public boolean isMiddleDown() {
            return e.isMiddleDown();
        }

        public double getMouseX() {
            return e.getMouseX();
        }

        public double getMouseY() {
            return e.getMouseY();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortKey extends PortInputEvent<InputEvent.Key> {
        @Diff
        public PortKey(InputEvent.Key e) {
            super(e);
        }

        public int getKey() {
            return e.getKey();
        }

        public int getScanCode() {
            return e.getScanCode();
        }

        public int getAction() {
            return e.getAction();
        }

        public int getModifiers() {
            return e.getModifiers();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortInteractionKeyMappingTriggered extends PortInputEvent<InputEvent.InteractionKeyMappingTriggered> implements IPortCancellableEvent {
        @Diff
        public PortInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered e) {
            super(e);
        }

        public void setSwingHand(boolean value) {
            e.setSwingHand(value);
        }

        public boolean shouldSwingHand() {
            return e.shouldSwingHand();
        }

        public InteractionHand getHand() {
            return e.getHand();
        }

        public boolean isAttack() {
            return e.isAttack();
        }

        public boolean isUseItem() {
            return e.isUseItem();
        }

        public boolean isPickBlock() {
            return e.isPickBlock();
        }

        public KeyMapping getKeyMapping() {
            return e.getKeyMapping();
        }

        static {
            PortEventHooks.register();
        }
    }
}
