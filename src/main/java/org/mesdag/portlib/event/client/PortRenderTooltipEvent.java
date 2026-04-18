package org.mesdag.portlib.event.client;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public abstract class PortRenderTooltipEvent<E extends RenderTooltipEvent> extends PortEvent<E> {
    @Diff
    public PortRenderTooltipEvent(E e) {
        super(e);
    }

    public ItemStack getItemStack() {
        return e.getItemStack();
    }

    public GuiGraphics getGraphics() {
        return e.getGraphics();
    }

    public List<ClientTooltipComponent> getComponents() {
        return e.getComponents();
    }

    public int getX() {
        return e.getX();
    }

    public int getY() {
        return e.getY();
    }

    public Font getFont() {
        return e.getFont();
    }

    public static class PortGatherComponents extends PortEvent<RenderTooltipEvent.GatherComponents> implements IPortCancellableEvent {
        @Diff
        public PortGatherComponents(RenderTooltipEvent.GatherComponents e) {
            super(e);
        }

        public ItemStack getItemStack() {
            return e.getItemStack();
        }

        public int getScreenWidth() {
            return e.getScreenWidth();
        }

        public int getScreenHeight() {
            return e.getScreenHeight();
        }

        public List<Either<FormattedText, TooltipComponent>> getTooltipElements() {
            return e.getTooltipElements();
        }

        public int getMaxWidth() {
            return e.getMaxWidth();
        }

        public void setMaxWidth(int maxWidth) {
            e.setMaxWidth(maxWidth);
        }
    }

    public static class PortPre extends PortRenderTooltipEvent<RenderTooltipEvent.Pre> implements IPortCancellableEvent {
        @Diff
        public PortPre(RenderTooltipEvent.Pre e) {
            super(e);
        }

        public int getScreenWidth() {
            return e.getScreenWidth();
        }

        public int getScreenHeight() {
            return e.getScreenHeight();
        }

        public ClientTooltipPositioner getTooltipPositioner() {
            return e.getTooltipPositioner();
        }

        public void setFont(Font fr) {
            e.setFont(fr);
        }

        public void setX(int x) {
            e.setX(x);
        }

        public void setY(int y) {
            e.setY(y);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class PortColor extends PortRenderTooltipEvent<RenderTooltipEvent.Color> {
        @Diff
        public PortColor(RenderTooltipEvent.Color e) {
            super(e);
        }

        public int getBackgroundStart() {
            return e.getBackgroundStart();
        }

        public int getBackgroundEnd() {
            return e.getBackgroundEnd();
        }

        public void setBackground(int background) {
            e.setBackground(background);
        }

        public void setBackgroundStart(int backgroundStart) {
            e.setBackgroundStart(backgroundStart);
        }

        public void setBackgroundEnd(int backgroundEnd) {
            e.setBackgroundEnd(backgroundEnd);
        }

        public int getBorderStart() {
            return e.getBorderStart();
        }

        public void setBorderStart(int borderStart) {
            e.setBorderStart(borderStart);
        }

        public int getBorderEnd() {
            return e.getBorderEnd();
        }

        public void setBorderEnd(int borderEnd) {
            e.setBorderEnd(borderEnd);
        }

        public int getOriginalBackgroundStart() {
            return e.getOriginalBackgroundStart();
        }

        public int getOriginalBackgroundEnd() {
            return e.getOriginalBackgroundEnd();
        }

        public int getOriginalBorderStart() {
            return e.getOriginalBorderStart();
        }

        public int getOriginalBorderEnd() {
            return e.getOriginalBorderEnd();
        }

        static {
            PortEventHooks.register();
        }
    }
}
