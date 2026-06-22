package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.mesdag.portlib.diff.IPortEntityType;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.UnaryOperator;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements IPortEntityType<T> {
    @Unique
    private float portlib$spawnDimensionsScale = 1.0F;

    @Override
    public void portlib$setSpawnDimensionsScale(float scale) {
        this.portlib$spawnDimensionsScale = scale;
    }

    @Override
    public float portlib$getSpawnDimensionsScale() {
        return portlib$spawnDimensionsScale;
    }

    @Mixin(EntityType.Builder.class)
    public abstract static class BuilderMixin<T extends Entity> implements IPortEntityType.IPortBuilder<T> {
        @Unique
        private float portlib$spawnDimensionsScale = 1.0F;
        @Unique
        private PortEntityAttachments.Builder portlib$attachments = PortEntityAttachments.builder();

        @Override
        public void portlib$setSpawnDimensionsScale(float scale) {
            this.portlib$spawnDimensionsScale = scale;
        }

        @Override
        public void portlib$withAttachments(UnaryOperator<PortEntityAttachments.Builder> operator) {
            this.portlib$attachments = operator.apply(portlib$attachments);
        }

        @ModifyReturnValue(method = "build", at = @At("RETURN"))
        private EntityType<T> modify(EntityType<T> original) {
            IPortEntityType<T> port = IPortEntityType.of(original);
            port.portlib$setSpawnDimensionsScale(portlib$spawnDimensionsScale);
            return original;
        }
    }
}
