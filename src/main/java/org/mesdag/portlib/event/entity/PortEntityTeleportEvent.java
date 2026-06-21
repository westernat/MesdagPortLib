package org.mesdag.portlib.event.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortEntityTeleportEvent<E extends EntityTeleportEvent> extends PortEntityEvent<E> implements IPortCancellableEvent {
    @Diff
    public PortEntityTeleportEvent(E e) {
        super(e);
    }

    public double getTargetX() {
        return e.getTargetX();
    }

    public void setTargetX(double targetX) {
        e.setTargetX(targetX);
    }

    public double getTargetY() {
        return e.getTargetY();
    }

    public void setTargetY(double targetY) {
        e.setTargetY(targetY);
    }

    public double getTargetZ() {
        return e.getTargetZ();
    }

    public void setTargetZ(double targetZ) {
        e.setTargetZ(targetZ);
    }

    public Vec3 getTarget() {
        return e.getTarget();
    }

    public double getPrevX() {
        return e.getPrevX();
    }

    public double getPrevY() {
        return e.getPrevY();
    }

    public double getPrevZ() {
        return e.getPrevZ();
    }

    public Vec3 getPrev() {
        return e.getPrev();
    }

    public static class TeleportCommand extends PortEntityTeleportEvent<EntityTeleportEvent.TeleportCommand> implements IPortCancellableEvent {
        @Diff
        public TeleportCommand(EntityTeleportEvent.TeleportCommand e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class SpreadPlayersCommand extends PortEntityTeleportEvent<EntityTeleportEvent.SpreadPlayersCommand> implements IPortCancellableEvent {
        @Diff
        public SpreadPlayersCommand(EntityTeleportEvent.SpreadPlayersCommand e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class EnderEntity extends PortEntityTeleportEvent<EntityTeleportEvent.EnderEntity> implements IPortCancellableEvent {
        @Diff
        public EnderEntity(EntityTeleportEvent.EnderEntity e) {
            super(e);
        }

        public LivingEntity getEntityLiving() {
            return e.getEntityLiving();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class EnderPearl extends PortEntityTeleportEvent<EntityTeleportEvent.EnderPearl> implements IPortCancellableEvent {
        @Diff
        public EnderPearl(EntityTeleportEvent.EnderPearl e) {
            super(e);
        }

        public ThrownEnderpearl getPearlEntity() {
            return e.getPearlEntity();
        }

        public ServerPlayer getPlayer() {
            return e.getPlayer();
        }

        public @Nullable HitResult getHitResult() {
            return e.getHitResult();
        }

        public float getAttackDamage() {
            return e.getAttackDamage();
        }

        public void setAttackDamage(float attackDamage) {
            e.setAttackDamage(attackDamage);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class ChorusFruit extends PortEntityTeleportEvent<EntityTeleportEvent.ChorusFruit> implements IPortCancellableEvent {
        @Diff
        public ChorusFruit(EntityTeleportEvent.ChorusFruit e) {
            super(e);
        }

        public LivingEntity getEntityLiving() {
            return e.getEntityLiving();
        }

        static {
            PortEventHooks.register();
        }
    }
}
