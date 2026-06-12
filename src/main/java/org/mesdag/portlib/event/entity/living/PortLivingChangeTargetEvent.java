package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortLivingChangeTargetEvent extends PortLivingEvent<LivingChangeTargetEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingChangeTargetEvent(LivingChangeTargetEvent e) {
        super(e);
    }

    public @Nullable LivingEntity getNewAboutToBeSetTarget() {
        return e.getNewTarget();
    }

    public void setNewAboutToBeSetTarget(@Nullable LivingEntity newAboutToBeSetTarget) {
        e.setNewTarget(newAboutToBeSetTarget);
    }

    public IPortLivingTargetType getTargetType() {
        return IPortLivingTargetType.wrap(e.getTargetType());
    }

    public @Nullable LivingEntity getOriginalAboutToBeSetTarget() {
        return e.getOriginalTarget();
    }

    public interface IPortLivingTargetType extends LivingChangeTargetEvent.ILivingTargetType {
        @Diff
        default LivingChangeTargetEvent.ILivingTargetType unwrap() {
            return this;
        }

        @Diff
        static Delegate wrap(LivingChangeTargetEvent.ILivingTargetType delegate) {
            return new Delegate(delegate);
        }

        @Diff
        record Delegate(
                LivingChangeTargetEvent.ILivingTargetType delegate) implements IPortLivingTargetType {
            @Override
            public LivingChangeTargetEvent.ILivingTargetType unwrap() {
                return delegate;
            }
        }
    }

    public enum PortLivingTargetType implements IPortLivingTargetType {
        MOB_TARGET,
        BEHAVIOR_TARGET;

        @Diff
        public LivingChangeTargetEvent.LivingTargetType unwrap() {
            return this == MOB_TARGET
                    ? LivingChangeTargetEvent.LivingTargetType.MOB_TARGET
                    : LivingChangeTargetEvent.LivingTargetType.BEHAVIOR_TARGET;
        }

        @Diff
        public static PortLivingTargetType wrap(LivingChangeTargetEvent.LivingTargetType type) {
            return type == LivingChangeTargetEvent.LivingTargetType.MOB_TARGET ? MOB_TARGET : BEHAVIOR_TARGET;
        }
    }

    static {
        PortEventHooks.register();
    }
}
