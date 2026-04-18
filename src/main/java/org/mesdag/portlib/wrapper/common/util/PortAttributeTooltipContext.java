package org.mesdag.portlib.wrapper.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.world.item.PortItem;

public interface PortAttributeTooltipContext extends PortItem.PortTooltipContext {
    @Nullable Player player();

    TooltipFlag flag();

    static PortAttributeTooltipContext of(@Nullable Player player, PortItem.PortTooltipContext itemCtx, TooltipFlag flag) {
        return new PortAttributeTooltipContext() {

            @Override
            public HolderLookup.Provider registries() {
                return itemCtx.registries();
            }

            @Override
            public float tickRate() {
                return itemCtx.tickRate();
            }

            @Override
            public MapItemSavedData mapData(String mapName) {
                return itemCtx.mapData(mapName);
            }

            @Override
            public Level level() {
                return itemCtx.level();
            }

            @Override
            public Player player() {
                return player;
            }

            @Override
            public TooltipFlag flag() {
                return flag;
            }
        };
    }
}
