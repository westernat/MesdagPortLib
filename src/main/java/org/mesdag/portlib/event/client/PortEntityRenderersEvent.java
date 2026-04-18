package org.mesdag.portlib.event.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.util.PortSets;

import java.util.Set;
import java.util.function.Supplier;

public abstract class PortEntityRenderersEvent<E extends EntityRenderersEvent> extends PortEvent<E> implements IPortModBusEvent {
    @Diff
    public PortEntityRenderersEvent(E e) {
        super(e);
    }

    public static class PortRegisterLayerDefinitions extends PortEntityRenderersEvent<EntityRenderersEvent.RegisterLayerDefinitions> {
        @Diff
        public PortRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions e) {
            super(e);
        }

        public void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
            e.registerLayerDefinition(layerLocation, supplier);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortRegisterRenderers extends PortEntityRenderersEvent<EntityRenderersEvent.RegisterRenderers> {
        @Diff
        public PortRegisterRenderers(EntityRenderersEvent.RegisterRenderers e) {
            super(e);
        }

        public <T extends Entity> void registerEntityRenderer(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider) {
            e.registerEntityRenderer(entityType, entityRendererProvider);
        }

        public <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRendererProvider) {
            e.registerBlockEntityRenderer(blockEntityType, blockEntityRendererProvider);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortAddLayers extends PortEntityRenderersEvent<EntityRenderersEvent.AddLayers> {
        @Diff
        public PortAddLayers(EntityRenderersEvent.AddLayers e) {
            super(e);
        }

        public Set<PortModel> getSkins() {
            return PortSets.immutableTransform(e.getSkins(), name -> {
                if (name.equals("slim")) {
                    return PortModel.SLIM;
                }
                return PortModel.WIDE;
            });
        }

        public <R extends EntityRenderer<? extends Player>> @Nullable R getSkin(PortModel skinModel) {
            return (R) e.getSkin(skinModel.id);
        }

        public Set<EntityType<?>> getEntityTypes() {
            return Minecraft.getInstance().getEntityRenderDispatcher().renderers.keySet();
        }

        public <T extends Entity, R extends EntityRenderer<T>> @Nullable R getRenderer(EntityType<? extends T> entityType) {
            return e.getEntityRenderer(entityType);
        }

        public EntityModelSet getEntityModels() {
            return e.getEntityModels();
        }

        public EntityRendererProvider.Context getContext() {
            return e.getContext();
        }

        static {
            PortEventHooks.register();
        }

        public enum PortModel {
            SLIM("slim"),
            WIDE("default");

            private final String id;

            PortModel(String id) {
                this.id = id;
            }
        }
    }

    public static class PortCreateSkullModels extends PortEntityRenderersEvent<EntityRenderersEvent.CreateSkullModels> {
        @Diff
        public PortCreateSkullModels(EntityRenderersEvent.CreateSkullModels e) {
            super(e);
        }

        public EntityModelSet getEntityModelSet() {
            return e.getEntityModelSet();
        }

        public void registerSkullModel(SkullBlock.Type type, SkullModelBase model) {
            e.registerSkullModel(type, model);
        }

        static {
            PortEventHooks.register();
        }
    }
}
