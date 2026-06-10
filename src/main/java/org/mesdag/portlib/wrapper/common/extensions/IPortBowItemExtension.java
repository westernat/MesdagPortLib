package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IPortBowItemExtension extends IPortProjectileWeaponItemExtension {
    private BowItem self() {
        return (BowItem) this;
    }

    /// 只能手动调用
    default void releaseUsing1211(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (living instanceof Player player) {
            ItemStack itemstack = player.getProjectile(stack);
            if (!itemstack.isEmpty()) {
                int i = self().getUseDuration(stack) - timeLeft;
                i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, level, player, i, !itemstack.isEmpty());
                if (i < 0) return;
                float f = BowItem.getPowerForTime(i);
                if (!((double) f < 0.1)) {
                    List<ItemStack> list = IPortProjectileWeaponItemExtension.draw(stack, itemstack, player);
                    if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                        this.shoot(serverlevel, player, player.getUsedItemHand(), stack, list, f * 3.0F, 1.0F, f == 1.0F, null);
                    }

                    level.playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.ARROW_SHOOT,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                    );
                    player.awardStat(Stats.ITEM_USED.get(self()));
                }
            }
        }
    }

    static IPortBowItemExtension of(BowItem item) {
        return (IPortBowItemExtension) item;
    }
}
