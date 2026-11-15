package org.mesdag.portlib.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModContainer;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.config.PortConfigView;

import java.util.*;
import java.util.function.Supplier;

/// 使用统一配置视图编辑 Forge 和 PortLib 配置。
final class PortConfigValuesScreen extends Screen {
    private final Screen parent;
    private final ModContainer mod;
    private final PortConfigView config;
    private final List<String> path;
    private final History history;
    private Button resetButton;

    PortConfigValuesScreen(Screen parent, ModContainer mod, PortConfigView config) {
        this(parent, mod, config, List.of(), new History());
    }

    private PortConfigValuesScreen(Screen parent, ModContainer mod, PortConfigView config, List<String> path, History history) {
        super(path.isEmpty() ? PortConfigurationScreen.configTitle(mod, config, ".title") : breadcrumb(parent, label(mod, path.get(path.size() - 1), null)));
        this.parent = parent;
        this.mod = mod;
        this.config = config;
        this.path = path;
        this.history = history;
    }

    @Override
    protected void init() {
        Rows rows = addRenderableWidget(new Rows(minecraft, width, height));
        Set<String> sections = new LinkedHashSet<>();
        for (PortConfigView.Entry entry : config.entries()) {
            if (entry.path().size() <= path.size() || !entry.path().subList(0, path.size()).equals(path))
                continue;
            String key = entry.path().get(path.size());
            if (entry.path().size() > path.size() + 1) {
                if (sections.add(key)) {
                    List<String> child = new ArrayList<>(path);
                    child.add(key);
                    rows.add(new Row(label(mod, key, null).copy().append("..."), Button.builder(sectionButton(mod, key, null), b -> minecraft.setScreen(new PortConfigValuesScreen(this, mod, config, child, history))).bounds(0, 0, 150, 20).build(), null));
                }
            } else rows.add(valueRow(entry, key));
        }
        addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.undo"), b -> {
            history.restore(hasShiftDown());
            rebuildWidgets();
        }).bounds(width / 2 - 154, height - 27, 100, 20).build());
        resetButton = addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.reset"), b -> {
            Map<PortConfigView.Entry, Object> previous = new LinkedHashMap<>();
            for (PortConfigView.Entry entry : config.entries()) {
                if (entry.path().size() != path.size() + 1 || !entry.path().subList(0, path.size()).equals(path) || entry.get() instanceof List<?>)
                    continue;
                Object defaults = entry.getDefault();
                Object current = entry.get();
                if (!Objects.equals(current, defaults) && entry.test(defaults)) {
                    previous.put(entry, current);
                    entry.set(defaults);
                }
            }
            history.record(previous);
            rebuildWidgets();
        }).bounds(width / 2 - 50, height - 27, 100, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose()).bounds(width / 2 + 54, height - 27, 100, 20).build());
    }

    @Override
    public void tick() {
        super.tick();
        if (resetButton != null) {
            resetButton.active = config.entries().stream().anyMatch(entry ->
                    entry.path().size() == path.size() + 1
                            && !(entry.get() instanceof List<?>)
                            && entry.path().subList(0, path.size()).equals(path)
                            && !Objects.equals(entry.get(), entry.getDefault())
            );
        }
    }

    private Row valueRow(PortConfigView.Entry entry, String key) {
        Object value = entry.get();
        AbstractWidget widget;
        if (value instanceof List<?> list) {
            widget = Button.builder(sectionButton(mod, key, entry.translationKey()), b -> minecraft.setScreen(new ListScreen(this, breadcrumb(this, label(mod, key, entry.translationKey())), entry, list, history))).bounds(0, 0, 150, 20).build();
        } else if (value instanceof Boolean bool) {
            widget = CycleButton.onOffBuilder(bool).displayOnlyValue().create(0, 0, 150, 20, CommonComponents.EMPTY, (b, selected) -> change(entry, selected));
        } else if (value instanceof Enum<?> selected) {
            widget = enumButton(entry, selected);
        } else if (value instanceof Integer integer && entry.bounds() != null
                && (long) entry.bounds().max() - entry.bounds().min() < 256) {
            int min = entry.bounds().min();
            int span = entry.bounds().max() - min;
            widget = new AbstractSliderButton(0, 0, 150, 20, Component.literal(integer.toString()),
                    span == 0 ? 0 : (integer - min) / (double) span) {
                private int selected() {return min + (int) Math.round(this.value * span);}

                @Override
                protected void updateMessage() {setMessage(Component.literal(Integer.toString(selected())));}

                @Override
                protected void applyValue() {change(entry, selected());}
            };
            widget.active = span > 0;
        } else if (value instanceof Number || value instanceof String) {
            EditBox box = new EditBox(font, 0, 0, 150, 20, Component.empty());
            box.setMaxLength(1024);
            box.setValue(String.valueOf(value));
            box.setHighlightPos(box.getCursorPosition());
            box.setResponder(text -> {
                Object parsed = parse(text, value);
                boolean valid = parsed != null && entry.test(parsed);
                box.setTextColor(valid ? 0xE0E0E0 : 0xFF5555);
                if (valid) change(entry, parsed);
            });
            widget = box;
        } else {
            Button unsupported = Button.builder(Component.literal(Objects.toString(value, "")), ignored -> {}).bounds(0, 0, 150, 20).build();
            unsupported.active = false;
            widget = unsupported;
        }
        String translation = (entry.translationKey() == null ? mod.getModId() + ".configuration." + key : entry.translationKey()) + ".tooltip";
        Component tooltip = label(mod, key, entry.translationKey()).copy().withStyle(ChatFormatting.BOLD);
        if (I18n.exists(translation))
            tooltip = tooltip.copy().append("\n\n").append(Component.translatable(translation));
        else if (entry.comment() != null && !entry.comment().isBlank())
            tooltip = tooltip.copy().append("\n\n").append(entry.comment());
        if (entry.restart())
            tooltip = tooltip.copy().append("\n").append(Component.translatable("STARTUP".equals(config.type()) ? "portlib.configuration.requires_game_restart" : "portlib.configuration.requires_world_restart").withStyle(ChatFormatting.YELLOW));
        widget.setTooltip(Tooltip.create(tooltip));
        Component name = label(mod, key, entry.translationKey());
        return new Row(value instanceof List<?> ? name.copy().append("...") : name, widget, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private AbstractWidget enumButton(PortConfigView.Entry entry, Enum<?> value) {
        List constants = Arrays.stream(value.getDeclaringClass().getEnumConstants()).filter(entry::test).toList();
        return CycleButton.builder(item -> Component.literal(((Enum<?>) item).name())).withValues(constants).withInitialValue(value).displayOnlyValue()
                .create(0, 0, 150, 20, CommonComponents.EMPTY, (button, choice) -> change(entry, choice));
    }

    private void change(PortConfigView.Entry entry, Object value) {
        if (!Objects.equals(entry.get(), value) && entry.test(value)) {
            history.record(entry, entry.get());
            entry.set(value);
        }
    }

    @Override
    public void onClose() {
        if (path.isEmpty() && history.dirty) {
            config.save();
            boolean gameRestart = "STARTUP".equals(config.type());
            if (minecraft != null && history.restart && (gameRestart || minecraft.level != null)) {
                Component message = Component.translatable(gameRestart ? "portlib.configuration.requires_game_restart"
                        : "portlib.configuration.requires_world_restart");
                minecraft.setScreen(new ConfirmScreen(confirmed -> {
                    if (confirmed && gameRestart) {
                        minecraft.stop();
                    } else if (confirmed && minecraft.level != null) {
                        minecraft.level.disconnect();
                        minecraft.clearLevel();
                        minecraft.setScreen(new TitleScreen());
                    } else {
                        minecraft.setScreen(parent);
                    }
                }, title, message, Component.translatable(gameRestart ? "menu.quit" : "menu.returnToMenu"), CommonComponents.GUI_CANCEL));
                return;
            }
        }
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
    }

    private static Component breadcrumb(Screen parent, Component child) {
        return parent.getTitle().copy().append(Component.literal(" ❯ ").withStyle(ChatFormatting.GOLD)).append(child);
    }

    private static Component label(ModContainer mod, String key, String explicit) {
        String translation = explicit == null ? mod.getModId() + ".configuration." + key : explicit;
        return I18n.exists(translation) ? Component.translatable(translation) : Component.literal(key);
    }

    private static Component sectionButton(ModContainer mod, String key, String explicit) {
        String translation = (explicit == null ? mod.getModId() + ".configuration." + key : explicit) + ".button";
        Component text = I18n.exists(translation) ? Component.translatable(translation) : Component.translatable("portlib.configuration.edit");
        return text.copy().append("...");
    }

    private static Object parse(String text, Object template) {
        try {
            if (template instanceof Integer) return Integer.decode(text);
            if (template instanceof Long) return Long.decode(text);
            if (template instanceof Double) return Double.valueOf(text);
            if (template instanceof Float) return Float.valueOf(text);
            if (template instanceof String) return text;
        } catch (RuntimeException ignored) {}
        return null;
    }

    private static final class History {
        private final Deque<Map<PortConfigView.Entry, Object>> undo = new ArrayDeque<>();
        private final Deque<Map<PortConfigView.Entry, Object>> redo = new ArrayDeque<>();
        private boolean dirty;
        private boolean restart;

        private void record(PortConfigView.Entry entry, Object previous) {
            record(Collections.singletonMap(entry, previous));
        }

        private void record(Map<PortConfigView.Entry, Object> previous) {
            if (previous.isEmpty()) return;
            restart |= previous.keySet().stream().anyMatch(PortConfigView.Entry::restart);
            undo.push(previous);
            redo.clear();
            dirty = true;
        }

        private void restore(boolean forward) {
            var source = forward ? redo : undo;
            if (source.isEmpty()) return;
            Map<PortConfigView.Entry, Object> inverse = new LinkedHashMap<>();
            source.pop().forEach((entry, value) -> {
                inverse.put(entry, entry.get());
                entry.set(value);
            });
            (forward ? undo : redo).push(inverse);
        }
    }

    /// 列表在独立页面中编辑副本；只有通过完整校验后才写回配置。
    private static final class ListScreen extends Screen {
        private Button resetButton;
        private final Screen parent;
        private final PortConfigView.Entry entry;
        private final History history;
        private final List<Object> values;
        private final List<Object> original;
        private final Deque<List<Object>> undo = new ArrayDeque<>();
        private final Deque<List<Object>> redo = new ArrayDeque<>();
        private Button done;
        private final Set<Integer> invalidEntries = new HashSet<>();

        private ListScreen(Screen parent, Component title, PortConfigView.Entry entry, List<?> value, History history) {
            super(title);
            this.parent = parent;
            this.entry = entry;
            this.history = history;
            this.values = new ArrayList<>(value);
            this.original = new ArrayList<>(value);
        }

        @Override
        protected void init() {
            Rows rows = addRenderableWidget(new Rows(minecraft, width, height));
            refreshRows(rows);
            addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.undo"), b -> {
                boolean forward = hasShiftDown();
                var source = forward ? redo : undo;
                if (source.isEmpty()) return;
                (forward ? undo : redo).push(new ArrayList<>(values));
                values.clear();
                values.addAll(source.pop());
                refreshRows(rows);
            }).bounds(width / 2 - 154, height - 27, 100, 20).build());
            resetButton = addRenderableWidget(Button.builder(Component.translatable("portlib.configuration.reset"), b -> {
                if (entry.getDefault() instanceof List<?> defaults) {
                    saveUndo();
                    values.clear();
                    values.addAll(defaults);
                    refreshRows(rows);
                }
            }).bounds(width / 2 - 50, height - 27, 100, 20).build());
            done = addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> onClose()).bounds(width / 2 + 54, height - 27, 100, 20).build());
        }

        @Override
        public void tick() {
            super.tick();
            if (resetButton != null) {
                Object defaults = entry.getDefault();
                resetButton.active = defaults instanceof List<?> && !values.equals(defaults) && entry.test(defaults);
            }
        }

        private void refreshRows(Rows rows) {
            double scroll = rows.getScrollAmount();
            rows.setFocused(null);
            rows.children().clear();
            invalidEntries.clear();
            for (int i = 0; i < values.size(); i++) {
                int index = i;
                Object value = values.get(i);
                AbstractWidget editor;
                if (value instanceof Boolean bool) {
                    editor = CycleButton.onOffBuilder(bool).displayOnlyValue().create(0, 0, 125, 20, CommonComponents.EMPTY, (b, selected) -> {
                        saveUndo();
                        values.set(index, selected);
                    });
                } else {
                    editor = createListEditBox(index, value);
                }
                Button remove = Button.builder(Component.literal("×"), b -> {
                    saveUndo();
                    values.remove(index);
                    refreshRows(rows);
                }).bounds(0, 0, 20, 20).build();
                List<Object> afterRemoval = new ArrayList<>(values);
                afterRemoval.remove(index);
                remove.active = entry.test(afterRemoval);
                Button up = Button.builder(Component.literal("▴"), b -> {
                    if (index > 0) {
                        saveUndo();
                        Collections.swap(values, index, index - 1);
                        refreshRows(rows);
                    }
                }).bounds(0, 0, 20, 20).build();
                up.active = index > 0 && canSwap(index, index - 1);
                up.visible = index > 0;
                Button down = Button.builder(Component.literal("▾"), b -> {
                    if (index + 1 < values.size()) {
                        saveUndo();
                        Collections.swap(values, index, index + 1);
                        refreshRows(rows);
                    }
                }).bounds(0, 0, 20, 20).build();
                down.active = index + 1 < values.size() && canSwap(index, index + 1);
                down.visible = index + 1 < values.size();
                rows.add(new Row(Component.literal(i + ":"), editor, remove, up, down));
            }
            Supplier<?> supplier = entry.newElement();
            if (supplier != null)
                rows.add(new Row(Component.empty(), Button.builder(Component.literal("+"), b -> {
                    Object next = supplier.get();
                    if (next != null) {
                        saveUndo();
                        values.add(next);
                        refreshRows(rows);
                    }
                }).bounds(0, 0, 150, 20).build(), null));
            // 列表变短时，原版会将滚动位置限制在新的末尾。
            rows.setScrollAmount(scroll);
        }

        private boolean canSwap(int index, int other) {
            List<Object> candidate = new ArrayList<>(values);
            Collections.swap(candidate, index, other);
            return entry.test(candidate);
        }

        private void saveUndo() {
            undo.push(new ArrayList<>(values));
            redo.clear();
        }

        /// 校验修改后的完整列表，保留依赖列表长度或其他元素的约束。
        private EditBox createListEditBox(int index, Object template) {
            EditBox box = new EditBox(font, 0, 0, 125, 20, Component.empty());
            box.setMaxLength(1024);
            box.setValue(Objects.toString(template, ""));
            box.setHighlightPos(box.getCursorPosition());
            box.setResponder(text -> {
                Object parsed = parse(text, template);
                List<Object> candidate = new ArrayList<>(values);
                if (parsed != null) candidate.set(index, parsed);
                boolean ok = parsed != null && entry.test(candidate);
                box.setTextColor(ok ? 0xE0E0E0 : 0xFF5555);
                if (ok) {
                    if (!Objects.equals(values.get(index), parsed)) saveUndo();
                    values.set(index, parsed);
                    invalidEntries.remove(index);
                } else {
                    invalidEntries.add(index);
                }
            });
            return box;
        }

        @Override
        public void onClose() {
            if (!invalidEntries.isEmpty() || !entry.test(values)) return;
            if (!original.equals(values)) {
                history.record(entry, original);
                entry.set(new ArrayList<>(values));
            }
            if (minecraft != null) minecraft.setScreen(parent);
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            renderBackground(graphics);
            if (done == null) return;
            done.active = invalidEntries.isEmpty() && entry.test(values);
            super.render(graphics, mouseX, mouseY, partialTick);
            graphics.drawCenteredString(font, title, width / 2, 10, 0xFFFFFF);
        }
    }

    private static final class Rows extends ContainerObjectSelectionList<Row> {
        private Rows(Minecraft minecraft, int width, int height) {
            super(minecraft, width, height, 32, height - 32, 26);
            setRenderBackground(false);
            setRenderTopAndBottom(false);
        }

        private void add(Row row) {addEntry(row);}

        @Override
        public int getRowWidth() {return Math.min(360, width - 24);}

        @Override
        protected int getScrollbarPosition() {return width / 2 + getRowWidth() / 2 + 6;}
    }

    private static final class Row extends ContainerObjectSelectionList.Entry<Row> {
        private final Component label;
        private final AbstractWidget editor;
        private final Button remove;
        private final Button up;
        private final Button down;

        private Row(Component label, AbstractWidget editor, Button remove) {this(label, editor, remove, null, null);}

        private Row(Component label, AbstractWidget editor, Button remove, Button up, Button down) {
            this.label = label;
            this.editor = editor;
            this.remove = remove;
            this.up = up;
            this.down = down;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick) {
            boolean listEntry = up != null;
            graphics.drawString(Minecraft.getInstance().font, label, left + (listEntry ? 48 : 4), top + 7, 0xFFFFFF);
            editor.setX(left + width / 2);
            editor.setY(top + 1);
            int editorWidth = width / 2 - 4;
            if (editor.getWidth() != editorWidth) {
                editor.setWidth(editorWidth);
                if (editor instanceof EditBox box && box.getHighlighted().isEmpty()) {
                    box.setHighlightPos(box.getCursorPosition());
                }
            }
            editor.render(graphics, mouseX, mouseY, partialTick);
            if (remove != null) {
                remove.setX(left + width / 2 - 30);
                remove.setY(top + 1);
                remove.render(graphics, mouseX, mouseY, partialTick);
            }
            if (up != null) {
                up.setX(left + 4);
                up.setY(top + 1);
                up.render(graphics, mouseX, mouseY, partialTick);
            }
            if (down != null) {
                down.setX(left + 26);
                down.setY(top + 1);
                down.render(graphics, mouseX, mouseY, partialTick);
            }
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {return up != null ? List.of(editor, up, down, remove) : remove == null ? List.of(editor) : List.of(editor, remove);}

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {return up != null ? List.of(editor, up, down, remove) : remove == null ? List.of(editor) : List.of(editor, remove);}
    }
}
