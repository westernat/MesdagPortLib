package org.mesdag.portlib.wrapper.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.Diff;

public record PortAdvancementHolder(ResourceLocation id, Advancement value) {
    @Diff
    public static PortAdvancementHolder wrap(Advancement value) {
        return new PortAdvancementHolder(value.getId(), value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof PortAdvancementHolder holder && id.equals(holder.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
