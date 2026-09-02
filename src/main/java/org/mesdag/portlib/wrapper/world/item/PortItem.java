package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;

public class PortItem {
    public interface TooltipContext {
        TooltipContext EMPTY = new TooltipContext() {
            @Override
            public @Nullable HolderLookup.Provider registries() {
                return null;
            }

            @Override
            public float tickRate() {
                return 20.0F;
            }

            @Override
            public @Nullable MapItemSavedData mapData(String mapName) {
                return null;
            }
        };

        @Nullable HolderLookup.Provider registries();

        float tickRate();

        @Nullable MapItemSavedData mapData(String mapName);

        default @Nullable Level level() {
            return null;
        }

        static TooltipContext of(@Nullable Level level) {
            return level == null ? EMPTY : new TooltipContext() {
                @Override
                public HolderLookup.Provider registries() {
                    return level.registryAccess();
                }

                @Override
                public float tickRate() {
                    return 20.0F;
                }

                @Override
                public MapItemSavedData mapData(String mapName) {
                    return level.getMapData(mapName);
                }

                @Override
                public Level level() {
                    return level;
                }
            };
        }

        static TooltipContext of(HolderLookup.Provider registries) {
            return new TooltipContext() {
                @Override
                public HolderLookup.Provider registries() {
                    return registries;
                }

                @Override
                public float tickRate() {
                    return 20.0F;
                }

                @Override
                public @Nullable MapItemSavedData mapData(String mapName) {
                    return null;
                }
            };
        }
    }
}
