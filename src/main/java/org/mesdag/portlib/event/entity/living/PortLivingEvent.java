package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.PortEntityEvent;

public abstract class PortLivingEvent<E extends LivingEvent> extends PortEntityEvent<E> {
    public PortLivingEvent(E e) {
        super(e);
    }

    @Override
    public LivingEntity getEntity() {
        return e.getEntity();
    }

    public static class LivingJumpEvent extends PortLivingEvent<LivingEvent.LivingJumpEvent> {
        public LivingJumpEvent(LivingEvent.LivingJumpEvent e) {
            super(e);
        }

        public static void onLivingJump(LivingEntity entity) {
            PortEventHandler.postEvent(new LivingEvent.LivingJumpEvent(entity));
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class LivingVisibilityEvent extends PortLivingEvent<LivingEvent.LivingVisibilityEvent> {
        @Diff
        public LivingVisibilityEvent(LivingEvent.LivingVisibilityEvent e) {
            super(e);
        }

        public void modifyVisibility(double mod) {
            e.modifyVisibility(mod);
        }

        public double getVisibilityModifier() {
            return e.getVisibilityModifier();
        }

        public @Nullable Entity getLookingEntity() {
            return e.getLookingEntity();
        }

        static {
            PortEventHooks.register();
        }
    }
}
