package org.mesdag.portlib.wrapper.world.entity.projectile;

import PortLib.extensions.net.minecraft.world.entity.projectile.AbstractArrow.PortAbstractArrowExtension;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.mesdag.portlib.wrapper.common.extensions.IPortProjectileExtension;

import javax.annotation.Nullable;

public abstract class PortAbstractArrow extends AbstractArrow implements IPortProjectileExtension {
    protected PortAbstractArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    protected PortAbstractArrow(
            EntityType<? extends AbstractArrow> entityType,
            double x,
            double y,
            double z,
            Level level,
            ItemStack pickupItemStack,
            @Nullable ItemStack firedFromWeapon
    ) {
        this(entityType, level);
        setPos(x, y, z);
        PortAbstractArrowExtension.setup(this, pickupItemStack, firedFromWeapon);
    }

    protected PortAbstractArrow(
            EntityType<? extends AbstractArrow> entityType, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon
    ) {
        this(entityType, owner.getX(), owner.getEyeY() - 0.1F, owner.getZ(), level, pickupItemStack, firedFromWeapon);
        this.setOwner(owner);
    }

    @Override
    protected ItemStack getPickupItem() {
        return PortAbstractArrowExtension.pickupItem(this);
    }

    public ItemStack getPickupItemStackOrigin() {
        return PortAbstractArrowExtension.pickupItemStackOrigin(this);
    }

    @Override
    public @Nullable ItemStack getWeaponItem() {
        return PortAbstractArrowExtension.weaponItem(this);
    }

    protected void setPickupItemStack(ItemStack pickupItemStack) {
        if (pickupItemStack.isEmpty()) {
            IPortAbstractArrow.of(this).portlib$setPickupItem(getDefaultPickupItem());
        } else {
            IPortAbstractArrow.of(this).portlib$setPickupItem(pickupItemStack);
        }
    }

    protected abstract ItemStack getDefaultPickupItem();

    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
        if (getKnockback() > 0.0) {
            double remains = Math.max(0.0, 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            Vec3 vec3 = getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(getKnockback() * 0.6 * remains);
            if (vec3.lengthSqr() > 0.0) {
                entity.push(vec3.x, 0.1, vec3.z);
            }
        }
    }
}
