package org.mesdag.portlib.wrapper.common.extensions;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public interface IPortInventoryScreenExtension {
    static void renderEntityInInventoryFollowsMouse(
            GuiGraphics guiGraphics,
            int x1,
            int y1,
            int x2,
            int y2,
            int scale,
            float yOffset,
            float mouseX,
            float mouseY,
            LivingEntity entity
    ) {
        float mx = (float) (x1 + x2) / 2.0F;
        float my = (float) (y1 + y2) / 2.0F;
        float rx = (float) Math.atan((mx - mouseX) / 40.0F);
        float ry = (float) Math.atan((my - mouseY) / 40.0F);
        renderEntityInInventoryFollowsAngle(guiGraphics, x1, y1, x2, y2, scale, yOffset, rx, ry, entity);
    }

    static void renderEntityInInventoryFollowsAngle(
            GuiGraphics guiGraphics,
            int x1,
            int y1,
            int x2,
            int y2,
            int scale,
            float yOffset,
            float rx,
            float ry,
            LivingEntity living
    ) {
        float mx = (float) (x1 + x2) / 2.0F;
        float my = (float) (y1 + y2) / 2.0F;
        guiGraphics.enableScissor(x1, y1, x2, y2);
        Quaternionf pose = new Quaternionf().rotateZ(Mth.PI);
        Quaternionf cameraOrientation = new Quaternionf().rotateX(ry * 20.0F * Mth.DEG_TO_RAD);
        pose.mul(cameraOrientation);
        float yBodyRot = living.yBodyRot;
        float yRot = living.getYRot();
        float xRot = living.getXRot();
        float yHeadRotO = living.yHeadRotO;
        float yHeadRot = living.yHeadRot;
        living.yBodyRot = 180.0F + rx * 20.0F;
        living.setYRot(180.0F + rx * 40.0F);
        living.setXRot(-ry * 20.0F);
        living.yHeadRot = living.getYRot();
        living.yHeadRotO = living.getYRot();
        float localScale = living.getScale();
        Vector3f vector3f = new Vector3f(0.0F, living.getBbHeight() / 2.0F + yOffset * localScale, 0.0F);
        float actualScale = (float) scale / localScale;
        renderEntityInInventory(guiGraphics, mx, my, actualScale, vector3f, pose, cameraOrientation, living);
        living.yBodyRot = yBodyRot;
        living.setYRot(yRot);
        living.setXRot(xRot);
        living.yHeadRotO = yHeadRotO;
        living.yHeadRot = yHeadRot;
        guiGraphics.disableScissor();
    }

    static void renderEntityInInventory(
            GuiGraphics guiGraphics,
            float x,
            float y,
            float scale,
            Vector3f translate,
            Quaternionf pose,
            @Nullable Quaternionf cameraOrientation,
            LivingEntity entity
    ) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 50.0F);
        guiGraphics.pose().scale(scale, scale, -scale);
        guiGraphics.pose().translate(translate.x, translate.y, translate.z);
        guiGraphics.pose().mulPose(pose);
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        if (cameraOrientation != null) {
            dispatcher.overrideCameraOrientation(cameraOrientation.conjugate(new Quaternionf()).rotateY((float) Math.PI));
        }

        dispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880));
        guiGraphics.flush();
        dispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
    }
}
