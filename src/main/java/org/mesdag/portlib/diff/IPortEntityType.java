package org.mesdag.portlib.diff;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityTypeBuilderExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityTypeExtension;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;

import java.util.function.UnaryOperator;

public interface IPortEntityType<T extends Entity> extends IPortEntityTypeExtension<T> {
    void portlib$setSpawnDimensionsScale(float scale);

    float portlib$getSpawnDimensionsScale();

    @SuppressWarnings("unchecked")
    static <T extends Entity> IPortEntityType<T> of(EntityType<T> type) {
        return (IPortEntityType<T>) type;
    }

    interface IPortBuilder<T extends Entity> extends IPortEntityTypeBuilderExtension<T> {
        void portlib$setSpawnDimensionsScale(float scale);

        void portlib$withAttachments(UnaryOperator<PortEntityAttachments.Builder> operator);
    }
}
