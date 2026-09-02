package org.mesdag.portlib.wrapper.world.entity.vehicle;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemStackExtension;

public abstract class PortVehicleEntity extends Entity {
    protected static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(PortVehicleEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(PortVehicleEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(PortVehicleEntity.class, EntityDataSerializers.FLOAT);

    public PortVehicleEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!level().isClientSide && !isRemoved()) {
            if (isInvulnerableTo(source)) {
                return false;
            }
            setHurtDir(-getHurtDir());
            setHurtTime(10);
            markHurt();
            setDamage(getDamage() + amount * 10.0F);
            gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
            boolean flag = source.getEntity() instanceof Player && ((Player) source.getEntity()).getAbilities().instabuild;
            if ((flag || !(getDamage() > 40.0F)) && !shouldSourceDestroy(source)) {
                if (flag) {
                    discard();
                }
            } else {
                destroy(source);
            }

            return true;
        }
        return true;
    }

    boolean shouldSourceDestroy(DamageSource source) {
        return false;
    }

    public void destroy(Item dropItem) {
        kill();
        if (level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack stack = dropItem.getDefaultInstance();
            IPortItemStackExtension.of(stack).setCustomName(getCustomName());
            spawnAtLocation(stack);
        }

    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_ID_HURT, 0);
        entityData.define(DATA_ID_HURTDIR, 1);
        entityData.define(DATA_ID_DAMAGE, 0.0F);
    }

    public void setHurtTime(int hurtTime) {
        entityData.set(DATA_ID_HURT, hurtTime);
    }

    public void setHurtDir(int hurtDir) {
        entityData.set(DATA_ID_HURTDIR, hurtDir);
    }

    public void setDamage(float damage) {
        entityData.set(DATA_ID_DAMAGE, damage);
    }

    public float getDamage() {
        return entityData.get(DATA_ID_DAMAGE);
    }

    public int getHurtTime() {
        return entityData.get(DATA_ID_HURT);
    }

    public int getHurtDir() {
        return entityData.get(DATA_ID_HURTDIR);
    }

    protected void destroy(DamageSource source) {
        destroy(getDropItem());
    }

    protected abstract Item getDropItem();
}
