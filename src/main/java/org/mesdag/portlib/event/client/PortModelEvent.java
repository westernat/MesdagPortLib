package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortModelEvent<E extends ModelEvent> extends PortEvent<E> {
    @Diff
    public PortModelEvent(E e) {
        super(e);
    }

    public static class PortModifyBakingResult extends PortModelEvent<ModelEvent.ModifyBakingResult> implements IPortModBusEvent {
        @Diff
        public PortModifyBakingResult(ModelEvent.ModifyBakingResult e) {
            super(e);
        }

        public @Nullable BakedModel getModel(ModelResourceLocation location) {
            return e.getModels().get(location);
        }

        public @Nullable BakedModel setModel(ModelResourceLocation location, BakedModel model) {
            return e.getModels().put(location, model);
        }

        public TextureAtlasSprite getTexture(Material material) {
            return e.getTextureGetter().apply(material);
        }

        public ModelBakery getModelBakery() {
            return e.getModelBakery();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortBakingCompleted extends PortModelEvent<ModelEvent.BakingCompleted> implements IPortModBusEvent {
        @Diff
        public PortBakingCompleted(ModelEvent.BakingCompleted e) {
            super(e);
        }

        public ModelManager getModelManager() {
            return e.getModelManager();
        }

        public @Nullable BakedModel getModel(ModelResourceLocation location) {
            return e.getModels().get(location);
        }

        public ModelBakery getModelBakery() {
            return e.getModelBakery();
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortRegisterAdditional extends PortModelEvent<ModelEvent.RegisterAdditional> implements IPortModBusEvent {
        @Diff
        public PortRegisterAdditional(ModelEvent.RegisterAdditional e) {
            super(e);
        }

        public void register(ModelResourceLocation model) {
            e.register(model);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortRegisterGeometryLoaders extends PortModelEvent<ModelEvent.RegisterGeometryLoaders> implements IPortModBusEvent {
        @Diff
        public PortRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders e) {
            super(e);
        }

        public void register(ResourceLocation key, IGeometryLoader<?> loader) {
            e.register(key, loader);
        }

        static {
            PortEventHooks.register();
        }
    }
}
