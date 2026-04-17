package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Consumer;

public class PortItem {
    public interface PortTooltipContext {
        PortTooltipContext EMPTY = wrap(Item.TooltipContext.EMPTY);

        @Nullable HolderLookup.Provider registries();

        float tickRate();

        @Nullable MapItemSavedData mapData(String mapName);

        default @Nullable Level level() {
            return null;
        }

        static PortTooltipContext of(@Nullable Level level) {
            return wrap(Item.TooltipContext.of(level));
        }

        static PortTooltipContext of(HolderLookup.Provider registries) {
            return wrap(Item.TooltipContext.of(registries));
        }

        @Diff
        default Item.TooltipContext unwrap() {
            return new Item.TooltipContext() {
                @Override
                public @Nullable HolderLookup.Provider registries() {
                    return PortTooltipContext.this.registries();
                }

                @Override
                public float tickRate() {
                    return PortTooltipContext.this.tickRate();
                }

                @Override
                public @Nullable MapItemSavedData mapData(MapId mapId) {
                    return PortTooltipContext.this.mapData(mapId.key());
                }
            };
        }

        @Diff
        static PortTooltipContext wrap(Item.TooltipContext context) {
            return new Delegate(context);
        }

        @Diff
        record Delegate(Item.TooltipContext delegate) implements PortTooltipContext {
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

            @Diff
            @Override
            public Item.TooltipContext unwrap() {
                return delegate;
            }
        }
    }
}
