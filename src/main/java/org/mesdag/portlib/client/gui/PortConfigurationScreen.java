package org.mesdag.portlib.client.gui;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.mesdag.portlib.wrapper.common.PortTranslatableEnum;
import org.mesdag.portlib.wrapper.common.extensions.IPortConfigValueExtension;

import java.util.*;

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
        ConfigTracker.INSTANCE.configSets().values().stream()
                .flatMap(Collection::stream)
                .filter(config -> config.getModId().equals(mod.getModId()))
                .sorted(Comparator.comparing(ModConfig::getType).thenComparing(ModConfig::getFileName))
                .forEach(config -> list.addRow(configRow(config)));
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onClose()).bounds(width / 2 - 100, height - 27, 200, 20).build());
    }

    private ConfigRow configRow(ModConfig config) {
        Button button = Button.builder(configLabel(config), ignored -> open(config)).bounds(0, 0, 300, 20).build();
        if (!(config.getSpec() instanceof ForgeConfigSpec spec) || !spec.isLoaded()) {
            button.active = false;
            button.setTooltip(Tooltip.create(Component.translatable("portlib.configuration.not_loaded")));
        }
        return ConfigRow.fullWidth(button);
    }

    private void open(ModConfig config) {
        if (config.getSpec() instanceof ForgeConfigSpec spec && spec.isLoaded()) {
            minecraft.setScreen(new ConfigValuesScreen(this, mod, config, spec, spec.getValues(), spec.getSpec(), List.of(), new EditHistory()));
        }
    }

    private Component configLabel(ModConfig config) {
        String key = mod.getModId() + ".configuration.section." + config.getFileName();
        if (I18n.exists(key)) return Component.translatable(key);
        return Component.translatable("portlib.configuration.type." + config.getType().name().toLowerCase(Locale.ROOT), mod.getModInfo().getDisplayName());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }

    private static Component configurationTitle(ModContainer mod) {
        String key = mod.getModId() + ".configuration.title";
        return I18n.exists(key) ? Component.translatable(key) : Component.translatable("portlib.configuration.title", mod.getModInfo().getDisplayName());
    }

    private static final class ConfigValuesScreen extends Screen {
        private final Screen parent;
        private final ModContainer mod;
        private final ModConfig modConfig;
        private final ForgeConfigSpec forgeSpec;
        private final UnmodifiableConfig values;
        private final UnmodifiableConfig spec;
        private final List<String> path;
        private final EditHistory history;
        private Button undoButton;
        private Button resetButton;

        private ConfigValuesScreen(Screen parent, ModContainer mod, ModConfig modConfig, ForgeConfigSpec forgeSpec,
                                   UnmodifiableConfig values, UnmodifiableConfig spec, List<String> path, EditHistory history) {
            super(sectionTitle(mod, modConfig, forgeSpec, path));
            this.parent = parent;
            this.mod = mod;
            this.modConfig = modConfig;
            this.forgeSpec = forgeSpec;
            this.values = values;
            this.spec = spec;
            this.path = path;
            this.history = history;
        }

        @Override
        protected void init() {
            ConfigList list = addRenderableWidget(new ConfigList(minecraft, width, height, LIST_TOP, height - LIST_BOTTOM_MARGIN));
            for (Map.Entry<String, Object> entry : values.valueMap().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Object valueSpec = spec.valueMap().get(key);
                List<String> childPath = append(path, key);
                if (value instanceof UnmodifiableConfig childValues && valueSpec instanceof UnmodifiableConfig childSpec) {
                    list.addRow(sectionRow(key, childValues, childSpec, childPath));
                } else if (value instanceof ForgeConfigSpec.ConfigValue<?> configValue && valueSpec instanceof ForgeConfigSpec.ValueSpec configSpec) {
                    list.addRow(valueRow(key, configValue, configSpec, childPath));
                }
            }
            undoButton = addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.undo"), ignored -> undo()).bounds(width / 2 - 154, height - 27, 100, 20).build());
            resetButton = addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.reset"), ignored -> reset()).bounds(width / 2 - 50, height - 27, 100, 20).build());
            addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, ignored -> onClose()).bounds(width / 2 + 54, height - 27, 100, 20).build());
            updateActions();
        }

        private ConfigRow sectionRow(String key, UnmodifiableConfig childValues, UnmodifiableConfig childSpec, List<String> childPath) {
            String translationKey = forgeSpec.getLevelTranslationKey(childPath);
            Component label = translatedSectionButton(mod.getModId(), key, translationKey);
            Button button = Button.builder(label, ignored -> minecraft.setScreen(new ConfigValuesScreen(
                    this, mod, modConfig, forgeSpec, childValues, childSpec, childPath, history
            ))).bounds(0, 0, 300, 20).build();
            applyTooltip(button, translatedTooltip(mod.getModId(), key, translationKey, forgeSpec.getLevelComment(childPath)), false);
            return ConfigRow.fullWidth(button);
        }

        private ConfigRow valueRow(String key, ForgeConfigSpec.ConfigValue<?> value, ForgeConfigSpec.ValueSpec valueSpec, List<String> valuePath) {
            Component label = translatedLabel(mod.getModId(), key, valueSpec.getTranslationKey());
            Object current = raw(value);
            AbstractWidget widget;
            if (current instanceof Boolean bool) {
                widget = CycleButton.onOffBuilder(bool).displayOnlyValue().create(0, 0, 150, 20, CommonComponents.EMPTY, (button, selected) -> change(value, selected));
            } else if (current instanceof Enum<?> selected) {
                widget = enumButton(value, selected);
            } else if (current instanceof Integer integer && smallIntegerRange(valueSpec)) {
                widget = new IntegerSlider(value, integer, valueSpec.getRange());
            } else if (isEditable(current)) {
                widget = editBox(value, valueSpec, current);
            } else {
                Button unsupported = Button.builder(Component.literal(String.valueOf(current)), ignored -> {}).bounds(0, 0, 150, 20).build();
                unsupported.active = false;
                widget = unsupported;
            }
            applyTooltip(widget, translatedTooltip(mod.getModId(), key, valueSpec.getTranslationKey(), valueSpec.getComment()), valueSpec.needsWorldRestart());
            return new ConfigRow(label, widget, false);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private AbstractWidget enumButton(ForgeConfigSpec.ConfigValue<?> value, Enum<?> selected) {
            List constants = Arrays.asList(selected.getDeclaringClass().getEnumConstants());
            return CycleButton.builder(PortConfigurationScreen::enumName).withValues(constants).withInitialValue(selected).displayOnlyValue()
                    .create(0, 0, 150, 20, CommonComponents.EMPTY, (button, choice) -> change(value, choice));
        }

        private EditBox editBox(ForgeConfigSpec.ConfigValue<?> value, ForgeConfigSpec.ValueSpec valueSpec, Object current) {
            EditBox editBox = new EditBox(font, 0, 0, 150, 20, Component.empty());
            editBox.setMaxLength(1024);
            editBox.setValue(formatValue(current));
            editBox.setResponder(text -> {
                Object parsed = parseValue(text, current);
                boolean valid = parsed != InvalidValue.INSTANCE && valueSpec.test(parsed);
                editBox.setTextColor(valid ? 0xE0E0E0 : 0xFF5555);
                if (valid && !Objects.equals(raw(value), parsed)) change(value, parsed);
            });
            return editBox;
        }

        private void change(ForgeConfigSpec.ConfigValue<?> value, Object next) {
            Object previous = raw(value);
            if (Objects.equals(previous, next)) return;
            history.record(value, previous);
            set(value, next);
            updateActions();
        }

        private void undo() {
            history.undo();
            rebuildWidgets();
        }

        private void reset() {
            resetValues(values);
            rebuildWidgets();
        }

        private void resetValues(UnmodifiableConfig config) {
            for (Object object : config.valueMap().values()) {
                if (object instanceof UnmodifiableConfig child) {
                    resetValues(child);
                } else if (object instanceof ForgeConfigSpec.ConfigValue<?> value) {
                    Object current = raw(value);
                    Object defaultValue = value.getDefault();
                    if (!Objects.equals(current, defaultValue)) change(value, defaultValue);
                }
            }
        }

        private void updateActions() {
            if (undoButton != null) undoButton.active = history.canUndo();
            if (resetButton != null) resetButton.active = hasNonDefault(values);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            renderBackground(graphics);
            updateActions();
            super.render(graphics, mouseX, mouseY, partialTick);
            graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
        }

        @Override
        public void onClose() {
            if (parent instanceof PortConfigurationScreen && history.dirty) modConfig.save();
            minecraft.setScreen(parent);
        }

        private final class IntegerSlider extends AbstractSliderButton {
            private final ForgeConfigSpec.ConfigValue<?> configValue;
            private final int min;
            private final int max;

            private IntegerSlider(ForgeConfigSpec.ConfigValue<?> configValue, int current, ForgeConfigSpec.Range<?> range) {
                super(0, 0, 150, 20, Component.empty(), sliderValue(current, range));
                this.configValue = configValue;
                this.min = ((Number) range.getMin()).intValue();
                this.max = ((Number) range.getMax()).intValue();
                updateMessage();
            }

            @Override
            protected void updateMessage() {
                setMessage(Component.literal(Integer.toString(sliderInteger(value, min, max))));
            }

            @Override
            protected void applyValue() {
                change(configValue, sliderInteger(value, min, max));
            }
        }
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
        private final Component label;
        private final AbstractWidget widget;
        private final boolean fullWidth;

        private ConfigRow(Component label, AbstractWidget widget, boolean fullWidth) {
            this.label = label;
            this.widget = widget;
            this.fullWidth = fullWidth;
        }

        private static ConfigRow fullWidth(AbstractWidget widget) {
            return new ConfigRow(CommonComponents.EMPTY, widget, true);
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int rowWidth, int rowHeight,
                           int mouseX, int mouseY, boolean hovered, float partialTick) {
            if (fullWidth) {
                widget.setX(left + 4);
                widget.setWidth(rowWidth - 8);
            } else {
                int middle = left + rowWidth / 2;
                widget.setX(middle + 2);
                widget.setWidth(rowWidth / 2 - 6);
                graphics.drawString(Minecraft.getInstance().font, label, left + 6, top + 6, 0xFFFFFF);
            }
            widget.setY(top + 1);
            widget.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(widget);
        }
    }

    private static final class EditHistory {
        private final Deque<Change> changes = new ArrayDeque<>();
        private boolean dirty;

        private void record(ForgeConfigSpec.ConfigValue<?> value, Object previous) {
            changes.push(new Change(value, previous));
            dirty = true;
        }

        private boolean canUndo() {
            return !changes.isEmpty();
        }

        private void undo() {
            if (!changes.isEmpty()) {
                Change change = changes.pop();
                set(change.value, change.previous);
                dirty = true;
            }
        }
    }

    private record Change(ForgeConfigSpec.ConfigValue<?> value, Object previous) {}

    private enum InvalidValue {INSTANCE}

    private static Component translatedLabel(String modId, String key, String explicitKey) {
        if (explicitKey != null && I18n.exists(explicitKey))
            return Component.translatable(explicitKey);
        String generatedKey = modId + ".configuration." + key;
        return I18n.exists(generatedKey) ? Component.translatable(generatedKey) : Component.literal(humanize(key));
    }

    private static Component translatedSectionButton(String modId, String key, String explicitKey) {
        String baseKey = explicitKey == null ? modId + ".configuration." + key : explicitKey;
        return I18n.exists(baseKey + ".button") ? Component.translatable(baseKey + ".button") : translatedLabel(modId, key, explicitKey);
    }

    private static Component translatedTooltip(String modId, String key, String explicitKey, String comment) {
        String baseKey = explicitKey == null ? modId + ".configuration." + key : explicitKey;
        return I18n.exists(baseKey + ".tooltip") ? Component.translatable(baseKey + ".tooltip")
                : comment == null || comment.isBlank() ? null : Component.literal(comment);
    }

    private static Component sectionTitle(ModContainer mod, ModConfig config, ForgeConfigSpec spec, List<String> path) {
        if (path.isEmpty()) {
            String key = mod.getModId() + ".configuration.section." + config.getFileName() + ".title";
            return I18n.exists(key) ? Component.translatable(key) : configurationTitle(mod);
        }
        String key = path.get(path.size() - 1);
        return translatedLabel(mod.getModId(), key, spec.getLevelTranslationKey(path));
    }

    private static Component enumName(Object value) {
        if (value instanceof PortTranslatableEnum translated) return translated.getTranslatedName();
        return Component.literal(humanize(((Enum<?>) value).name()));
    }

    private static void applyTooltip(AbstractWidget widget, Component description, boolean restart) {
        Component tooltip = description;
        if (restart) {
            Component warning = Component.translatable("portlib.configuration.requires_world_restart").withStyle(ChatFormatting.YELLOW);
            tooltip = tooltip == null ? warning : tooltip.copy().append("\n").append(warning);
        }
        if (tooltip != null) widget.setTooltip(Tooltip.create(tooltip));
    }

    private static List<String> append(List<String> path, String key) {
        List<String> result = new ArrayList<>(path.size() + 1);
        result.addAll(path);
        result.add(key);
        return List.copyOf(result);
    }

    private static String humanize(String value) {
        String spaced = value.replace('_', ' ').replace('-', ' ');
        StringBuilder result = new StringBuilder(spaced.length() + 4);
        for (int i = 0; i < spaced.length(); i++) {
            char character = spaced.charAt(i);
            if (i > 0 && Character.isUpperCase(character) && Character.isLowerCase(spaced.charAt(i - 1)))
                result.append(' ');
            result.append(character);
        }
        if (result.isEmpty()) return value;
        result.setCharAt(0, Character.toUpperCase(result.charAt(0)));
        return result.toString();
    }

    private static boolean smallIntegerRange(ForgeConfigSpec.ValueSpec spec) {
        ForgeConfigSpec.Range<?> range = spec.getRange();
        return range != null && range.getMin() instanceof Number min && range.getMax() instanceof Number max
                && max.longValue() >= min.longValue() && max.longValue() - min.longValue() <= 256L;
    }

    private static double sliderValue(int current, ForgeConfigSpec.Range<?> range) {
        int min = ((Number) range.getMin()).intValue();
        int max = ((Number) range.getMax()).intValue();
        return max == min ? 0.0D : (double) (current - min) / (max - min);
    }

    private static int sliderInteger(double value, int min, int max) {
        return min + (int) Math.round(value * (max - min));
    }

    private static boolean isEditable(Object value) {
        return value instanceof Number || value instanceof String || value instanceof List<?>;
    }

    private static String formatValue(Object value) {
        if (value instanceof List<?> list)
            return String.join(", ", list.stream().map(String::valueOf).toList());
        return String.valueOf(value);
    }

    private static Object parseValue(String text, Object template) {
        try {
            if (template instanceof Integer) return Integer.valueOf(text);
            if (template instanceof Long) return Long.valueOf(text);
            if (template instanceof Double) return Double.valueOf(text);
            if (template instanceof Float) return Float.valueOf(text);
            if (template instanceof String) return text;
            if (template instanceof List<?> list) return parseList(text, list);
        } catch (RuntimeException ignored) {
            return InvalidValue.INSTANCE;
        }
        return InvalidValue.INSTANCE;
    }

    private static List<?> parseList(String text, List<?> template) {
        if (text.isBlank()) return List.of();
        Object elementTemplate = template.isEmpty() ? "" : template.get(0);
        List<Object> result = new ArrayList<>();
        for (String element : text.split(",")) {
            Object parsed = parseValue(element.trim(), elementTemplate);
            if (parsed == InvalidValue.INSTANCE)
                throw new IllegalArgumentException("Invalid list value");
            result.add(parsed);
        }
        return List.copyOf(result);
    }

    private static boolean hasNonDefault(UnmodifiableConfig values) {
        for (Object object : values.valueMap().values()) {
            if (object instanceof UnmodifiableConfig child && hasNonDefault(child)) return true;
            if (object instanceof ForgeConfigSpec.ConfigValue<?> value && !Objects.equals(raw(value), value.getDefault()))
                return true;
        }
        return false;
    }

    private static Object raw(ForgeConfigSpec.ConfigValue<?> value) {
        return IPortConfigValueExtension.of(value).portlib$getRaw();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void set(ForgeConfigSpec.ConfigValue<?> value, Object next) {
        ((ForgeConfigSpec.ConfigValue) value).set(next);
    }
}
