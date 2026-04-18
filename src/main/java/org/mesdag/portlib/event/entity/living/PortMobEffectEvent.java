package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
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
        @Diff
        public PortRemove(MobEffectEvent.Remove e) {
            super(e);
        }

        public MobEffectHolder getEffect() {
            return MobEffectHolder.wrap(e.getEffect());
        }

        public @Nullable PortEffectCure getCure() {
            return PortEffectCure.wrap(e.getCure());
        }

        @Override
        public @Nullable MobEffectInstance getEffectInstance() {
            return e.getEffectInstance();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortApplicable extends PortMobEffectEvent<MobEffectEvent.Applicable> {
        @Diff
        public PortApplicable(MobEffectEvent.Applicable e) {
            super(e);
        }

        @Override
        public MobEffectInstance getEffectInstance() {
            return e.getEffectInstance();
        }

        public void setPortResult(PortResult result) {
            e.setResult(result.unwrap());
        }

        public PortResult getPortResult() {
            return PortResult.wrap(e.getResult());
        }

        public @Nullable Entity getEffectSource() {
            return e.getEffectSource();
        }

        public boolean getApplicationResult() {
            return e.getApplicationResult();
        }

        public enum PortResult {
            APPLY,
            DEFAULT,
            DO_NOT_APPLY;

            @Diff
            public MobEffectEvent.Applicable.Result unwrap() {
                return switch (this) {
                    case APPLY -> MobEffectEvent.Applicable.Result.APPLY;
                    case DEFAULT -> MobEffectEvent.Applicable.Result.DEFAULT;
                    case DO_NOT_APPLY -> MobEffectEvent.Applicable.Result.DO_NOT_APPLY;
                };
            }

            @Diff
            public static PortResult wrap(MobEffectEvent.Applicable.Result result) {
                return switch (result) {
                    case APPLY -> APPLY;
                    case DEFAULT -> DEFAULT;
                    case DO_NOT_APPLY -> DO_NOT_APPLY;
                };
            }
        }

        static {
            PortEventHooks.register();
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
            PortEventHooks.register();
        }
    }

    public static class PortExpired extends PortMobEffectEvent<MobEffectEvent.Expired> implements IPortCancellableEvent {
        @Diff
        public PortExpired(MobEffectEvent.Expired e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }
}
