package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortModelManager;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.Map;

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

        @Diff
        public Map<ResourceLocation, BakedModel> getModels() {
            return e.getModels();
        }

        public @Nullable BakedModel setModel(ModelResourceLocation location, BakedModel model) {
            return e.getModels().put(location, model);
        }

        public TextureAtlasSprite getTexture(Material material) {
            if (PortModelManager.atlasPreparations == null) {
                throw new IllegalStateException("Unable to catching atlasPreparations");
            }
            AtlasSet.StitchResult stitchResult = PortModelManager.atlasPreparations.get(material.atlasLocation());
            TextureAtlasSprite sprite = stitchResult.getSprite(material.texture());
            if (sprite != null) {
                return sprite;
            }
            PortLib.LOGGER.warn("Failed to retrieve texture '{}' from atlas '{}'", material.texture(), material.atlasLocation(), new Throwable());
            return stitchResult.missing();
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
            e.register(key.getPath(), loader);
        }

        static {
            PortEventHooks.register();
        }
    }
}
