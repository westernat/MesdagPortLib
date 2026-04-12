package org.mesdag.portlib.event.level;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

import java.util.List;

@Cancelable
public class PortBlockDropsEvent extends BlockEvent {
    @Nullable
    private final BlockEntity blockEntity;
    private final List<ItemEntity> drops;
    @Nullable
    private final Entity breaker;
    private final ItemStack tool;
    private int experience;

    @Diff
    public PortBlockDropsEvent(ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, List<ItemEntity> drops, @Nullable Entity breaker, ItemStack tool) {
        super(level, pos, state);
        this.blockEntity = blockEntity;
        this.drops = drops;
        this.breaker = breaker;
        this.tool = tool;

        int fortuneLevel = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        int silkTouchLevel = tool.getEnchantmentLevel(Enchantments.SILK_TOUCH);
        this.experience = state.getExpDrop(level, level.random, pos, fortuneLevel, silkTouchLevel);
    }

    public List<ItemEntity> getDrops() {
        return drops;
    }

    public @Nullable BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public @Nullable Entity getBreaker() {
        return breaker;
    }

    public ItemStack getTool() {
        return tool;
    }

    @Override
    public ServerLevel getLevel() {
        return (ServerLevel) super.getLevel();
    }

    public int getDroppedExperience() {
        return experience;
    }

    public void setDroppedExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "May not set a negative experience drop.");
        this.experience = experience;
    }

    public static void handleBlockDrops(ServerLevel level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, List<ItemEntity> drops, @Nullable Entity breaker, ItemStack tool, Runnable spawnAfterBreak) {
        PortBlockDropsEvent event = new PortBlockDropsEvent(level, pos, state, blockEntity, drops, breaker, tool);
        PortEventHandler.postEvent(event);
        if (!event.isCanceled()) {
            for (ItemEntity entity : event.getDrops()) {
                level.addFreshEntity(entity);
            }
            spawnAfterBreak.run();
            if (event.getDroppedExperience() > 0) {
                state.getBlock().popExperience(level, pos, event.getDroppedExperience());
            }
        }
    }
}
