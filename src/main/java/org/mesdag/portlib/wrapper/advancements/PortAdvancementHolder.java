package org.mesdag.portlib.wrapper.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.Diff;

public record PortAdvancementHolder(ResourceLocation id, Advancement value) {
    @Diff
    public AdvancementHolder unwrap() {
        return new AdvancementHolder(id, value);
    }

    @Diff
    public static PortAdvancementHolder wrap(AdvancementHolder holder) {
        return new PortAdvancementHolder(holder.id(), holder.value());
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
