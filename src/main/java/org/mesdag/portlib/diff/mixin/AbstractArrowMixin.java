package org.mesdag.portlib.diff.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortAbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin implements IPortAbstractArrow {
    @Unique
    private static final String portlib$ROOT_TAG = "PortLibArrowData";
    @Unique
    private static final String portlib$VERSION_TAG = "Version";
    @Unique
    private static final String portlib$PICKUP_ITEM_TAG = "PickupItem";
    @Unique
    private static final String portlib$FIRED_FROM_WEAPON_TAG = "FiredFromWeapon";
    @Unique
    private static final int portlib$CURRENT_FORMAT_VERSION = 1;

    @Unique
    private @Nullable ItemStack portlib$pickupItemStack;
    @Unique
    private @Nullable ItemStack portlib$firedFromWeapon;

    @Override
    public @Nullable ItemStack portlib$getPickupItem() {
        return portlib$pickupItemStack;
    }

    @Override
    public void portlib$setPickupItem(ItemStack stack) {
        this.portlib$pickupItemStack = stack.isEmpty() ? null : stack.copyWithCount(1);
    }

    @Override
    public @Nullable ItemStack portlib$getFiredFromWeapon() {
        return portlib$firedFromWeapon;
    }

    @Override
    public void portlib$setFiredFromWeapon(@Nullable ItemStack stack) {
        this.portlib$firedFromWeapon = stack == null || stack.isEmpty() ? null : stack.copyWithCount(1);
    }

    /**
     * 1.21 的箭矢本体保存实际拾取物和发射武器；1.20 没有这两个字段，因此桥层必须补齐。
     * 这里只保存平台差异，不保存穿透、命中特效等具体玩法状态。
     */
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void portlib$saveCompatibilityData(CompoundTag entityTag, CallbackInfo ci) {
        if (portlib$pickupItemStack == null && portlib$firedFromWeapon == null) {
            return;
        }
        CompoundTag bridgeTag = new CompoundTag();
        bridgeTag.putInt(portlib$VERSION_TAG, portlib$CURRENT_FORMAT_VERSION);
        if (portlib$pickupItemStack != null) {
            bridgeTag.put(portlib$PICKUP_ITEM_TAG, portlib$pickupItemStack.save(new CompoundTag()));
        }
        if (portlib$firedFromWeapon != null) {
            bridgeTag.put(portlib$FIRED_FROM_WEAPON_TAG, portlib$firedFromWeapon.save(new CompoundTag()));
        }
        entityTag.put(portlib$ROOT_TAG, bridgeTag);
    }

    /** 当前格式缺失或损坏时清空桥接缓存，由具体箭矢按自身默认物品恢复。 */
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void portlib$loadCompatibilityData(CompoundTag entityTag, CallbackInfo ci) {
        portlib$pickupItemStack = null;
        portlib$firedFromWeapon = null;
        if (!entityTag.contains(portlib$ROOT_TAG, Tag.TAG_COMPOUND)) {
            return;
        }
        CompoundTag bridgeTag = entityTag.getCompound(portlib$ROOT_TAG);
        if (!bridgeTag.contains(portlib$VERSION_TAG, Tag.TAG_INT)
                || bridgeTag.getInt(portlib$VERSION_TAG) != portlib$CURRENT_FORMAT_VERSION) {
            return;
        }
        if (bridgeTag.contains(portlib$PICKUP_ITEM_TAG, Tag.TAG_COMPOUND)) {
            ItemStack pickup = ItemStack.of(bridgeTag.getCompound(portlib$PICKUP_ITEM_TAG));
            portlib$setPickupItem(pickup);
        }
        if (bridgeTag.contains(portlib$FIRED_FROM_WEAPON_TAG, Tag.TAG_COMPOUND)) {
            ItemStack weapon = ItemStack.of(bridgeTag.getCompound(portlib$FIRED_FROM_WEAPON_TAG));
            portlib$setFiredFromWeapon(weapon);
        }
    }
}
