package org.mesdag.portlib.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.living.PortLivingEvent;

import java.io.File;
import java.util.Optional;

public abstract class PortPlayerEvent<E extends PlayerEvent> extends PortLivingEvent<E> {
    @Diff
    protected PortPlayerEvent(E e) {
        super(e);
    }

    @Override
    public Player getEntity() {
        return e.getEntity();
    }

    public static class PortHarvestCheck extends PlayerEvent {
        private final BlockState state;
        private final BlockGetter level;
        private final BlockPos pos;
        private boolean success;

        @Diff
        public PortHarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos, boolean success) {
            super(player);
            this.state = state;
            this.level = level;
            this.pos = pos;
            this.success = success;
        }

        public BlockState getTargetBlock() {
            return state;
        }

        public BlockGetter getLevel() {
            return level;
        }

        public BlockPos getPos() {
            return pos;
        }

        public boolean canHarvest() {
            return success;
        }

        public void setCanHarvest(boolean success) {
            this.success = success;
        }

        public static boolean doPlayerHarvestCheck(Player player, BlockState state, BlockGetter level, BlockPos pos, boolean success) {
            PortHarvestCheck event = new PortHarvestCheck(player, state, level, pos, success);
            PortEventHandler.postEvent(event);
            return event.canHarvest();
        }
    }

    public static class PortBreakSpeed extends PortPlayerEvent<PlayerEvent.BreakSpeed> implements IPortCancellableEvent {
        @Diff
        public PortBreakSpeed(PlayerEvent.BreakSpeed e) {
            super(e);
        }

        public BlockState getState() {
            return e.getState();
        }

        public float getOriginalSpeed() {
            return e.getOriginalSpeed();
        }

        public float getNewSpeed() {
            return e.getNewSpeed();
        }

        public void setNewSpeed(float newSpeed) {
            e.setNewSpeed(newSpeed);
        }

        public Optional<BlockPos> getPosition() {
            return e.getPosition();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortNameFormat extends PortPlayerEvent<PlayerEvent.NameFormat> {
        @Diff
        public PortNameFormat(PlayerEvent.NameFormat e) {
            super(e);
        }

        public Component getUsername() {
            return e.getUsername();
        }

        public Component getDisplayname() {
            return e.getDisplayname();
        }

        public void setDisplayname(Component displayname) {
            e.setDisplayname(displayname);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortTabListNameFormat extends PortPlayerEvent<PlayerEvent.TabListNameFormat> {
        @Diff
        public PortTabListNameFormat(PlayerEvent.TabListNameFormat e) {
            super(e);
        }

        @Nullable
        public Component getDisplayName() {
            return e.getDisplayName();
        }

        public void setDisplayName(@Nullable Component displayName) {
            e.setDisplayName(displayName);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortClone extends PortPlayerEvent<PlayerEvent.Clone> {
        @Diff
        public PortClone(PlayerEvent.Clone e) {
            super(e);
        }

        public Player getOriginal() {
            return e.getOriginal();
        }

        public boolean isWasDeath() {
            return e.isWasDeath();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortStartTracking extends PortPlayerEvent<PlayerEvent.StartTracking> {
        @Diff
        public PortStartTracking(PlayerEvent.StartTracking e) {
            super(e);
        }

        public Entity getTarget() {
            return e.getTarget();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortStopTracking extends PortPlayerEvent<PlayerEvent.StopTracking> {
        @Diff
        public PortStopTracking(PlayerEvent.StopTracking e) {
            super(e);
        }

        public Entity getTarget() {
            return e.getTarget();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortLoadFromFile extends PortPlayerEvent<PlayerEvent.LoadFromFile> {
        @Diff
        public PortLoadFromFile(PlayerEvent.LoadFromFile e) {
            super(e);
        }

        public File getPlayerFile(String suffix) {
            return e.getPlayerFile(suffix);
        }

        public File getPlayerDirectory() {
            return e.getPlayerDirectory();
        }

        public String getPlayerUUID() {
            return e.getPlayerUUID();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortSaveToFile extends PortPlayerEvent<PlayerEvent.SaveToFile> {
        @Diff
        public PortSaveToFile(PlayerEvent.SaveToFile e) {
            super(e);
        }

        public File getPlayerFile(String suffix) {
            return e.getPlayerFile(suffix);
        }

        public File getPlayerDirectory() {
            return e.getPlayerDirectory();
        }

        public String getPlayerUUID() {
            return e.getPlayerUUID();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortItemCraftedEvent extends PortPlayerEvent<PlayerEvent.ItemCraftedEvent> {
        @Diff
        public PortItemCraftedEvent(PlayerEvent.ItemCraftedEvent e) {
            super(e);
        }

        public ItemStack getCrafting() {
            return e.getCrafting();
        }

        public Container getInventory() {
            return e.getInventory();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortItemSmeltedEvent extends PortPlayerEvent<PlayerEvent.ItemSmeltedEvent> {
        @Diff
        public PortItemSmeltedEvent(PlayerEvent.ItemSmeltedEvent e) {
            super(e);
        }

        public ItemStack getSmelting() {
            return e.getSmelting();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPlayerLoggedInEvent extends PortPlayerEvent<PlayerEvent.PlayerLoggedInEvent> {
        @Diff
        public PortPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPlayerLoggedOutEvent extends PortPlayerEvent<PlayerEvent.PlayerLoggedOutEvent> {
        @Diff
        public PortPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
            super(e);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPlayerRespawnEvent extends PortPlayerEvent<PlayerEvent.PlayerRespawnEvent> {
        @Diff
        public PortPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent e) {
            super(e);
        }

        public boolean isEndConquered() {
            return e.isEndConquered();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPlayerChangedDimensionEvent extends PortPlayerEvent<PlayerEvent.PlayerChangedDimensionEvent> {
        @Diff
        public PortPlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
            super(e);
        }

        public ResourceKey<Level> getFrom() {
            return e.getFrom();
        }

        public ResourceKey<Level> getTo() {
            return e.getTo();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortPlayerChangeGameModeEvent extends PortPlayerEvent<PlayerEvent.PlayerChangeGameModeEvent> implements IPortCancellableEvent {
        @Diff
        public PortPlayerChangeGameModeEvent(PlayerEvent.PlayerChangeGameModeEvent e) {
            super(e);
        }

        public GameType getCurrentGameMode() {
            return e.getCurrentGameMode();
        }

        public GameType getNewGameMode() {
            return e.getNewGameMode();
        }

        public void setNewGameMode(GameType newGameMode) {
            e.setNewGameMode(newGameMode);
        }

        static {
            PortEventHooks.register();
        }
    }
}
