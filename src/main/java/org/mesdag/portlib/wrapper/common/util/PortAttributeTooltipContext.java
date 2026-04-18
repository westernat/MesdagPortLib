package org.mesdag.portlib.wrapper.common.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.PortItem;

public interface PortAttributeTooltipContext extends PortItem.PortTooltipContext {
    @Nullable Player player();

    TooltipFlag flag();

    @Diff
    default AttributeTooltipContext unwrap() {
        return new AttributeTooltipContext() {
            @Override
            public @Nullable Player player() {
                return PortAttributeTooltipContext.this.player();
            }

            @Override
            public TooltipFlag flag() {
                return PortAttributeTooltipContext.this.flag();
            }

            @Override
            public @Nullable HolderLookup.Provider registries() {
                return PortAttributeTooltipContext.this.registries();
            }

            @Override
            public float tickRate() {
                return PortAttributeTooltipContext.this.tickRate();
            }

            @Override
            public @Nullable MapItemSavedData mapData(MapId mapId) {
                return PortAttributeTooltipContext.this.mapData(mapId.key());
            }

            @Override
            public @Nullable Level level() {
                return PortAttributeTooltipContext.this.level();
            }
        };
    }

    @Diff
    record Delegate(AttributeTooltipContext delegate) implements PortAttributeTooltipContext {
        @Override
        public @Nullable Player player() {
            return delegate.player();
        }

        @Override
        public TooltipFlag flag() {
            return delegate.flag();
        }

        @Override
        public @Nullable HolderLookup.Provider registries() {
            return delegate.registries();
        }

        @Override
        public float tickRate() {
            return delegate.tickRate();
        }

        @Override
        public @Nullable MapItemSavedData mapData(String mapName) {
            try {
                int id = Integer.parseInt(mapName.substring("map_".length()));
                return delegate.mapData(new MapId(id));
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public @Nullable Level level() {
            return delegate.level();
        }

        @Override
        public AttributeTooltipContext unwrap() {
            return delegate;
        }
    }

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
