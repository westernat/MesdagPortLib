package org.mesdag.portlib.component;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.component.PortItemAttributeModifiers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface PortDataComponentMap {
    PortDataComponentMap EMPTY = new PortDataComponentMap() {
        @Override
        public <T> @Nullable T portlib$get(PortDataComponentType<T> type) {
            return null;
        }

        @Override
        public Set<PortDataComponentType<?>> portlib$keySet() {
            return Set.of();
        }
    };

    <T> @Nullable T portlib$get(PortDataComponentType<T> type);

    default <T> @Nullable T get(PortDataComponentType<T> type) {
        return portlib$get(type);
    }

    Set<PortDataComponentType<?>> portlib$keySet();

    default Set<PortDataComponentType<?>> keySet() {
        return portlib$keySet();
    }

    static PortBuilder builder() {
        return new PortBuilder();
    }

    class PortBuilder {
        private final Map<PortDataComponentType<?>, Object> map = new Reference2ObjectOpenHashMap<>();
        private PortItemAttributeModifiers modifiers = PortItemAttributeModifiers.EMPTY;
        private @Nullable CompoundTag defaultTag;

        PortBuilder() {}

        public <T> PortBuilder set(PortDataComponentType<T> component, T value) {
            map.put(component, value);
            return this;
        }

        @Diff
        public PortBuilder unbreakable() {
            getOrCreateTag().putBoolean("Unbreakable", true);
            return this;
        }

        @Diff
        public void setModifiers(PortItemAttributeModifiers modifiers) {
            this.modifiers = modifiers;
        }

        @Diff
        public Map<PortDataComponentType<?>, Object> getMap() {
            return Collections.unmodifiableMap(map);
        }

        @Diff
        public PortItemAttributeModifiers getModifiers() {
            return modifiers;
        }

        private CompoundTag getOrCreateTag() {
            if (defaultTag == null) defaultTag = new CompoundTag();
            return defaultTag;
        }

        @Diff
        public void dyedColor(int rgb, boolean showInTooltip) {
            CompoundTag display = new CompoundTag();
            display.putInt("color", rgb);
            CompoundTag tag = getOrCreateTag();
            tag.put("display", display);
            ItemStack.TooltipPart part = ItemStack.TooltipPart.DYE;
            if (showInTooltip) {
                tag.putInt("HideFlags", tag.getInt("HideFlags") & ~part.getMask());
            } else {
                tag.putInt("HideFlags", tag.getInt("HideFlags") | part.getMask());
            }
        }

        @Diff
        public @Nullable CompoundTag getDefaultTag() {
            return defaultTag;
        }
    }
}
