package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.world.effect.MobEffectHolder;

public abstract class PortMobEffectEvent<E extends MobEffectEvent> extends PortLivingEvent<E> {
    @Diff
    public PortMobEffectEvent(E e) {
        super(e);
    }

    public @Nullable MobEffectInstance getEffectInstance() {
        return e.getEffectInstance();
    }

    public static class PortRemove extends PortMobEffectEvent<MobEffectEvent.Remove> implements IPortCancellableEvent {
        private final @Nullable PortEffectCure cure;

        @Diff
        public PortRemove(MobEffectEvent.Remove e, @Nullable PortEffectCure cure) {
            super(e);
            this.cure = cure;
        }

        public MobEffectHolder getEffect() {
            return MobEffectHolder.wrap(e.getEffect());
        }

        public @Nullable PortEffectCure getCure() {
            return cure;
        }

        @Override
        public @Nullable MobEffectInstance getEffectInstance() {
            return e.getEffectInstance();
        }

        @Diff
        public static boolean onEffectRemoved(LivingEntity entity, MobEffectInstance effectInstance, @Nullable PortEffectCure cure) {
            MobEffectEvent.Remove e = new MobEffectEvent.Remove(entity, effectInstance);
            return PortEventHandler.postEventWithReturn(new PortRemove(e, cure)).isCanceled();
        }

        static {
            PortEventHooks.register(MobEffectEvent.Remove.class, PortRemove.class, e -> new PortRemove(e, null));
        }
    }

    public static class PortApplicable extends MobEffectEvent {
        protected PortResult result = PortResult.DEFAULT;
        @Nullable
        private final Entity source;

        @Diff
        public PortApplicable(LivingEntity living, MobEffectInstance effectInstance, @Nullable Entity source) {
            super(living, effectInstance);
            this.source = source;
        }

        @Override
        public @NotNull MobEffectInstance getEffectInstance() {
            return super.getEffectInstance();
        }

        public void setPortResult(PortResult result) {
            this.result = result;
        }

        public PortResult getPortResult() {
            return result;
        }

        public @Nullable Entity getEffectSource() {
            return source;
        }

        public boolean getApplicationResult() {
            if (result == PortResult.APPLY) {
                return true;
            }
            return result == PortResult.DEFAULT && getEntity().canBeAffected(getEffectInstance());
        }

        public static boolean canMobEffectBeApplied(LivingEntity entity, MobEffectInstance effect, @Nullable Entity source) {
            var event = new PortApplicable(entity, effect, source);
            return PortEventHandler.postEventWithReturn(event).getApplicationResult();
        }

        public enum PortResult {
            APPLY,
            DEFAULT,
            DO_NOT_APPLY;
        }
    }

    public static class PortAdded extends PortMobEffectEvent<MobEffectEvent.Added> {
        @Diff
        public PortAdded(MobEffectEvent.Added e) {
            super(e);
        }

        @Override
        public MobEffectInstance getEffectInstance() {
            return e.getEffectInstance();
        }

        public @Nullable MobEffectInstance getOldEffectInstance() {
            return e.getOldEffectInstance();
        }

        public @Nullable Entity getEffectSource() {
            return e.getEffectSource();
        }

        static {
            PortEventHooks.register(MobEffectEvent.Added.class, PortAdded.class, PortAdded::new);
        }
    }

    public static class PortExpired extends PortMobEffectEvent<MobEffectEvent.Expired> implements IPortCancellableEvent {
        @Diff
        public PortExpired(MobEffectEvent.Expired e) {
            super(e);
        }

        static {
            PortEventHooks.register(MobEffectEvent.Expired.class, PortExpired.class, PortExpired::new);
        }
    }
}
