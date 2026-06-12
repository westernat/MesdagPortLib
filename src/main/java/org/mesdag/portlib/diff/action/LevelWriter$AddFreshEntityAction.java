package org.mesdag.portlib.diff.action;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelWriter;

import java.util.Objects;

public record LevelWriter$AddFreshEntityAction(LevelWriter instance, Entity entity,
                                               Operation<Boolean> original) {
    public boolean call() {
        return original.call(instance, entity);
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof LevelWriter$AddFreshEntityAction action && Objects.equals(action.entity, entity));
    }

    @Override
    public int hashCode() {
        return entity.hashCode();
    }
}
