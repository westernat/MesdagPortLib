package org.mesdag.portlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.fml.ModContainer;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.config.PortConfigView;

import java.util.List;
import java.util.Locale;

/**
 * NeoForge 自动配置界面在 Forge 1.20 上的桥接实现。
 */
public class PortConfigurationScreen extends Screen {
    private static final int LIST_TOP = 32;
    private static final int LIST_BOTTOM_MARGIN = 32;
    private static final int ROW_HEIGHT = 26;
    private final ModContainer mod;
    private final Screen parent;

    public PortConfigurationScreen(ModContainer mod, Screen parent) {
        super(configurationTitle(mod));
        this.mod = mod;
        this.parent = parent;
    }

    @Override
    protected void init() {
        ConfigList list = addRenderableWidget(new ConfigList(minecraft, width, height, LIST_TOP, height - LIST_BOTTOM_MARGIN));
        List<PortConfigView> configs = PortConfigView.forMod(mod);
        String previousType = null;
        for (PortConfigView config : configs) {
            if (!config.type().equals(previousType)) {
                Component heading = Component.translatable("portlib.configuration.group." + config.type().toLowerCase(Locale.ROOT))
                        .withStyle(net.minecraft.ChatFormatting.UNDERLINE);
                list.addRow(ConfigRow.fullWidth(new StringWidget(300, 20, heading, font).alignLeft()));
                previousType = config.type();
            }
            Component label = configTitle(mod, config, "").copy().append("...");
            Button button = Button.builder(label, ignored -> {
                if (minecraft != null) {
                    minecraft.setScreen(new PortConfigValuesScreen(configs.size() == 1 ? parent : this, mod, config));
                }
            }).bounds(0, 0, 300, 20).build();
            boolean remoteServerConfig = "SERVER".equals(config.type()) && minecraft != null
                    && (minecraft.getCurrentServer() != null && !minecraft.isSingleplayer()
                    || minecraft.hasSingleplayerServer() && minecraft.getSingleplayerServer() != null && minecraft.getSingleplayerServer().isPublished());
            button.active = config.isLoaded() && !remoteServerConfig;
            if (!button.active)
                button.setTooltip(Tooltip.create(Component.translatable("portlib.configuration.not_loaded")));
            if (configs.size() == 1 && button.active) {
                button.onPress();
                return;
            }
            list.addRow(ConfigRow.fullWidth(button));
        }
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose()).bounds(width / 2 - 100, height - 27, 200, 20).build());
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    static Component configTitle(ModContainer mod, PortConfigView config, String suffix) {
        String fileKey = config.fileName().replaceAll("[^a-zA-Z0-9]+", ".").replaceAll("^\\.|\\.$", "").toLowerCase(Locale.ROOT);
        String key = mod.getModId() + ".configuration.section." + fileKey + suffix;
        return Component.translatable(I18n.exists(key) ? key : "portlib.configuration.type." + config.type().toLowerCase(Locale.ROOT),
                mod.getModInfo().getDisplayName());
    }

    private static Component configurationTitle(ModContainer mod) {
        String key = mod.getModId() + ".configuration.title";
        return I18n.exists(key) ? Component.translatable(key) : Component.translatable("portlib.configuration.title", mod.getModInfo().getDisplayName());
    }

    private static final class ConfigList extends ContainerObjectSelectionList<ConfigRow> {
        private ConfigList(Minecraft minecraft, int width, int height, int top, int bottom) {
            super(minecraft, width, height, top, bottom, ROW_HEIGHT);
            setRenderBackground(false);
            setRenderTopAndBottom(false);
        }

        private void addRow(ConfigRow row) {
            addEntry(row);
        }

        @Override
        public int getRowWidth() {
            return Math.min(360, width - 24);
        }

        @Override
        protected int getScrollbarPosition() {
            return width / 2 + getRowWidth() / 2 + 6;
        }
    }

    private static final class ConfigRow extends ContainerObjectSelectionList.Entry<ConfigRow> {
        private final StringWidget label;
        private final AbstractWidget widget;
        private final boolean fullWidth;

        private ConfigRow(Component label, AbstractWidget widget, boolean fullWidth) {
            this.label = new StringWidget(0, 20, label, Minecraft.getInstance().font) {
                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                    Component message = getMessage();
                    var font = getFont();
                    FormattedCharSequence text = message.getVisualOrderText();
                    if (font.width(message) > getWidth()) {
                        FormattedText clipped = font.substrByWidth(message, Math.max(0, getWidth() - font.width(CommonComponents.ELLIPSIS)));
                        text = Language.getInstance().getVisualOrder(FormattedText.composite(clipped, CommonComponents.ELLIPSIS));
                    }
                    graphics.drawString(font, text, getX(), getY() + (getHeight() - 9) / 2, 0xFFFFFF);
                }
            }.alignLeft();
            this.label.setTooltip(widget.getTooltip() != null ? widget.getTooltip() : Tooltip.create(label));
            this.widget = widget;
            this.fullWidth = fullWidth;
        }

        private static ConfigRow fullWidth(AbstractWidget widget) {
            return new ConfigRow(CommonComponents.EMPTY, widget, true);
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int rowWidth, int rowHeight,
                           int mouseX, int mouseY, boolean hovered, float partialTick) {
            if (fullWidth) {
                widget.setX(left + 4);
                widget.setWidth(rowWidth - 8);
            } else {
                int middle = left + rowWidth / 2;
                widget.setX(middle + 2);
                widget.setWidth(rowWidth / 2 - 6);
                label.setX(left + 6);
                label.setY(top + 1);
                label.setWidth(Math.max(0, rowWidth / 2 - 12));
                label.render(graphics, mouseX, mouseY, partialTick);
            }
            widget.setY(top + 1);
            widget.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return fullWidth ? List.of(widget) : List.of(label, widget);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return fullWidth ? List.of(widget) : List.of(label, widget);
        }
    }

}
