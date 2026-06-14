package org.mesdag.portlib.wrapper.world.item;

import PortLib.extensions.net.minecraft.world.item.Item.PortItemExtension;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

public class PortItem extends Item {
    public PortItem(PortProperties properties) {
        super(properties);
    }

    public PortItemAttributeModifiers getDefaultPortAttributeModifiers(ItemStack stack) {
        return PortItemExtension.getDefaultPortAttributeModifiers(this, stack);
    }

    public int getDefaultMaxStackSize() {
        return PortItemExtension.getDefaultMaxStackSize(this);
    }

    public static class PortProperties extends Properties {
        public <T> PortProperties component(PortDataComponentType<T> type, T value) {
            PortItemExtension.Properties.component(this, type, value);
            return this;
        }

        public <T> PortProperties component(PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<T>> type, T value) {
            PortItemExtension.Properties.component(this, type, value);
            return this;
        }

        @Diff
        public PortProperties unbreakable() {
            PortItemExtension.Properties.unbreakable(this);
            return this;
        }

        public PortProperties attributes(PortItemAttributeModifiers modifiers) {
            PortItemExtension.Properties.attributes(this, modifiers);
            return this;
        }

        @Override
        public PortProperties fireResistant() {
            super.fireResistant();
            return this;
        }

        @Override
        public PortProperties durability(int maxDamage) {
            super.durability(maxDamage);
            return this;
        }
    }

    public interface PortTooltipContext {
        PortTooltipContext EMPTY = new PortTooltipContext() {
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

        static PortTooltipContext of(@Nullable Level level) {
            return level == null ? EMPTY : new PortTooltipContext() {
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

        static PortTooltipContext of(HolderLookup.Provider registries) {
            return new PortTooltipContext() {
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
