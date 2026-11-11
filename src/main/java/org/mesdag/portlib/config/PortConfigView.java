package org.mesdag.portlib.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.mesdag.portlib.wrapper.common.extensions.IPortConfigValueExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/// 配置界面使用的配置源；Forge 配置与启动配置在此处适配。
public final class PortConfigView {
    private final String fileName;
    private final String type;
    private final boolean loaded;
    private final List<Entry> entries;
    private final Runnable save;

    private PortConfigView(String fileName, String type, boolean loaded, List<Entry> entries, Runnable save) {
        this.fileName = fileName;
        this.type = type;
        this.loaded = loaded;
        this.entries = List.copyOf(entries);
        this.save = save;
    }

    public String fileName() {return fileName;}

    public String type() {return type;}

    public boolean isLoaded() {return loaded;}

    public List<Entry> entries() {return entries;}

    public void save() {save.run();}

    public static List<PortConfigView> forMod(ModContainer mod) {
        List<PortConfigView> result = new ArrayList<>();
        ConfigTracker.INSTANCE.configSets().values().stream().flatMap(Collection::stream)
                .filter(config -> config.getModId().equals(mod.getModId()))
                .sorted(java.util.Comparator.comparing(ModConfig::getType).thenComparing(ModConfig::getFileName))
                .filter(config -> config.getSpec() instanceof ForgeConfigSpec)
                .forEach(config -> {
                    ForgeConfigSpec spec = (ForgeConfigSpec) config.getSpec();
                    List<Entry> entries = new ArrayList<>();
                    if (spec.isLoaded())
                        collectForge(entries, spec.getValues(), spec.getSpec(), List.of());
                    result.add(new PortConfigView(config.getFileName(), config.getType().name(), spec.isLoaded(), entries, () -> {
                        config.save();
                        spec.afterReload();
                        mod.dispatchConfigEvent(new ModConfigEvent.Reloading(config));
                    }));
                });
        for (PortConfigSpec spec : PortConfigSpec.getLoadedConfigs(mod.getModId())) {
            List<Entry> entries = collectStartup(spec);
            result.add(new PortConfigView(spec.fileName(), "STARTUP", spec.isLoaded(), entries, spec::save));
        }
        return List.copyOf(result);
    }

    private static List<Entry> collectStartup(PortConfigSpec spec) {
        List<Entry> entries = new ArrayList<>();
        for (PortConfigSpec.ConfigValue<?> value : spec.values()) {
            Predicate<Object> validator = candidate -> spec.test(value, candidate);
            Supplier<?> supplier = value instanceof PortConfigSpec.ListValue<?> list ? list.getNewElementSupplier() : null;
            entries.add(new Entry(List.of(value.getPath().split("\\.")), value::getRaw, value::getDefault,
                    candidate -> setPort(value, candidate), validator, supplier, null, spec.comment(value.getPath()), true,
                    value instanceof PortConfigSpec.IntValue integer ? new IntBounds(integer.min, integer.max) : null));
        }
        return entries;
    }

    private static void collectForge(List<Entry> output, UnmodifiableConfig values, UnmodifiableConfig specs, List<String> parent) {
        for (var item : values.valueMap().entrySet()) {
            List<String> path = new ArrayList<>(parent);
            path.add(item.getKey());
            Object metadata = specs.valueMap().get(item.getKey());
            if (item.getValue() instanceof UnmodifiableConfig child && metadata instanceof UnmodifiableConfig childSpecs) {
                collectForge(output, child, childSpecs, path);
            } else if (item.getValue() instanceof ForgeConfigSpec.ConfigValue<?> value && metadata instanceof ForgeConfigSpec.ValueSpec spec) {
                output.add(new Entry(path, () -> IPortConfigValueExtension.of(value).portlib$getRaw(), value::getDefault,
                        candidate -> setForge(value, candidate), spec::test, forgeListSupplier(value),
                        spec.getTranslationKey(), spec.getComment(), spec.needsWorldRestart(), intBounds(spec)));
            }
        }
    }

    public record IntBounds(int min, int max) {}

    private static IntBounds intBounds(ForgeConfigSpec.ValueSpec spec) {
        ForgeConfigSpec.Range<?> range = spec.getRange();
        return range != null && range.getMin() instanceof Integer min && range.getMax() instanceof Integer max
                ? new IntBounds(min, max) : null;
    }

    private static Supplier<?> forgeListSupplier(ForgeConfigSpec.ConfigValue<?> value) {
        Object current = IPortConfigValueExtension.of(value).portlib$getRaw();
        if (!(current instanceof List<?> list)) return null;
        Object defaults = value.getDefault();
        Object sample = !list.isEmpty() ? list.get(0) : defaults instanceof List<?> d && !d.isEmpty() ? d.get(0) : "";
        if (sample instanceof Boolean) return () -> false;
        if (sample instanceof Integer) return () -> 0;
        if (sample instanceof Long) return () -> 0L;
        if (sample instanceof Double) return () -> 0.0D;
        return sample instanceof String ? () -> "" : null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setForge(ForgeConfigSpec.ConfigValue value, Object candidate) {value.set(candidate);}

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setPort(PortConfigSpec.ConfigValue value, Object candidate) {value.set(candidate);}

    public record Entry(List<String> path, Supplier<Object> getter, Supplier<Object> defaultValue,
                        java.util.function.Consumer<Object> setter, Predicate<Object> validator,
                        Supplier<?> newElement, String translationKey, String comment,
                        boolean restart, IntBounds bounds) {
        public Entry {path = List.copyOf(path);}

        public Object get() {return getter.get();}

        public Object getDefault() {return defaultValue.get();}

        public void set(Object value) {setter.accept(value);}

        public boolean test(Object value) {
            if (value == null) return false;
            try {return validator.test(value);} catch (ClassCastException |
                                                       NullPointerException ignored) {return false;}
        }
    }
}
