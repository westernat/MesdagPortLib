package org.mesdag.portlib.event.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
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

    public static class PortTeleportCommand extends PortEntityTeleportEvent<EntityTeleportEvent.TeleportCommand> implements IPortCancellableEvent {
        @Diff
        public PortTeleportCommand(EntityTeleportEvent.TeleportCommand e) {
            super(e);
        }

        static {
            PortEventHooks.register(EntityTeleportEvent.TeleportCommand.class, PortTeleportCommand.class, PortTeleportCommand::new);
        }
    }

    public static class PortSpreadPlayersCommand extends PortEntityTeleportEvent<EntityTeleportEvent.SpreadPlayersCommand> implements IPortCancellableEvent {
        @Diff
        public PortSpreadPlayersCommand(EntityTeleportEvent.SpreadPlayersCommand e) {
            super(e);
        }

        static {
            PortEventHooks.register(EntityTeleportEvent.SpreadPlayersCommand.class, PortSpreadPlayersCommand.class, PortSpreadPlayersCommand::new);
        }
    }

    public static class PortEnderEntity extends PortEntityTeleportEvent<EntityTeleportEvent.EnderEntity> implements IPortCancellableEvent {
        @Diff
        public PortEnderEntity(EntityTeleportEvent.EnderEntity e) {
            super(e);
        }

        public LivingEntity getEntityLiving() {
            return e.getEntityLiving();
        }

        static {
            PortEventHooks.register(EntityTeleportEvent.EnderEntity.class, PortEnderEntity.class, PortEnderEntity::new);
        }
    }

    public static class PortEnderPearl extends PortEntityTeleportEvent<EntityTeleportEvent.EnderPearl> implements IPortCancellableEvent {
        @Diff
        public PortEnderPearl(EntityTeleportEvent.EnderPearl e) {
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
            PortEventHooks.register(EntityTeleportEvent.EnderPearl.class, PortEnderPearl.class, PortEnderPearl::new);
        }
    }

    public static class PortChorusFruit extends PortEntityTeleportEvent<EntityTeleportEvent.ChorusFruit> implements IPortCancellableEvent {
        @Diff
        public PortChorusFruit(EntityTeleportEvent.ChorusFruit e) {
            super(e);
        }

        public LivingEntity getEntityLiving() {
            return e.getEntityLiving();
        }

        static {
            PortEventHooks.register(EntityTeleportEvent.ChorusFruit.class, PortChorusFruit.class, PortChorusFruit::new);
        }
    }
}
