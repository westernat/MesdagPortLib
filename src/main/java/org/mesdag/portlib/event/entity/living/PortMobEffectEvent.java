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

public abstract class PortMobEffectEvent extends PortLivingEvent {
    private final MobEffectEvent e;

    protected PortMobEffectEvent(MobEffectEvent e) {
        super(e.getEntity());
        this.e = e;
    }

    public @Nullable MobEffectInstance getEffectInstance() {
        return e.getEffectInstance();
    }

    public static class PortRemove extends PortMobEffectEvent implements IPortCancellableEvent {
        private final MobEffectEvent.Remove e;

        @Diff
        public PortRemove(MobEffectEvent.Remove e) {
            super(e);
            this.e = e;
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
            PortEventHooks.register(MobEffectEvent.Remove.class, PortRemove.class, PortRemove::new);
        }
    }

    public static class PortApplicable extends PortMobEffectEvent {
        private final MobEffectEvent.Applicable e;

        @Diff
        public PortApplicable(MobEffectEvent.Applicable e) {
            super(e);
            this.e = e;
        }

        @Override
        public MobEffectInstance getEffectInstance() {
            return e.getEffectInstance();
        }

        public void setResult(PortResult result) {
            e.setResult(result.unwrap());
        }

        public PortResult getResult() {
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
            PortEventHooks.register(MobEffectEvent.Applicable.class, PortApplicable.class, PortApplicable::new);
        }
    }

    public static class PortAdded extends PortMobEffectEvent {
        private final MobEffectEvent.Added e;

        @Diff
        public PortAdded(MobEffectEvent.Added e) {
            super(e);
            this.e = e;
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

    public static class PortExpired extends PortMobEffectEvent implements IPortCancellableEvent {
        @Diff
        public PortExpired(MobEffectEvent.Expired e) {
            super(e);
        }

        static {
            PortEventHooks.register(MobEffectEvent.Expired.class, PortExpired.class, PortExpired::new);
        }
    }
}
