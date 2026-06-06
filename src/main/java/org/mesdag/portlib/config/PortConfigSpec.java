package org.mesdag.portlib.config;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.PortPaths;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A STARTUP-phase config spec backed by {@code com.electronwill.nightconfig}.
 * <p>
 * Usage mirrors NeoForge's {@code ModConfigSpec.Builder}:
 * <pre>{@code
 * static final PortConfigSpec SPEC;
 * static final PortConfigSpec.BooleanValue DEBUG;
 * static final PortConfigSpec.IntValue TIMEOUT;
 *
 * static {
 *     PortConfigSpec.Builder builder = PortConfigSpec.builder("modid");
 *     DEBUG   = builder.define("general.debug", false, "Enable debug logging");
 *     TIMEOUT = builder.defineInRange("network.timeout", 30, 1, 600, "Timeout in seconds");
 *     SPEC    = builder.build();
 * }
 *
 * // In your mod constructor:
 * SPEC.load();
 *
 * // At runtime:
 * if (DEBUG.get()) { ... }
 * }</pre>
 */
public final class PortConfigSpec {

    // ==================== Core ====================

    private final String modId;
    private final String fileName;
    private final ConfigSpec spec;
    private final List<ConfigValue<?>> values;
    private final Map<String, String> comments;
    private CommentedFileConfig fileConfig;
    private boolean loaded;

    private PortConfigSpec(String modId, String fileName, ConfigSpec spec,
                           List<ConfigValue<?>> values, Map<String, String> comments) {
        this.modId = modId;
        this.fileName = fileName;
        this.spec = spec;
        this.values = values;
        this.comments = comments;
    }

    public static Builder builder(String modId) {
        return new Builder(modId, modId + "-startup.toml");
    }

    public static Builder builder(String modId, String fileName) {
        return new Builder(modId, fileName);
    }

    public void load() {
        if (loaded) return;
        Path file = PortPaths.configdir().resolve(fileName);
        fileConfig = CommentedFileConfig.builder(file, TomlFormat.instance())
                .autosave()
                .preserveInsertionOrder()
                .build();
        fileConfig.load();
        spec.correct(fileConfig);

        for (var entry : comments.entrySet()) {
            fileConfig.setComment(entry.getKey(), entry.getValue());
        }

        for (ConfigValue<?> value : values) {
            value.resolve(fileConfig);
        }
        fileConfig.save();
        loaded = true;
        PortLib.LOGGER.debug("PortConfigSpec [{}/{}] loaded", modId, fileName);
    }

    public void save() {
        if (fileConfig != null) fileConfig.save();
    }

    public boolean isLoaded() {return loaded;}

    public Path configPath() {
        return PortPaths.configdir().resolve(modId).resolve(fileName);
    }

    // ==================== Builder ====================

    public static final class Builder {
        private final String modId;
        private final String fileName;
        private final ConfigSpec spec = new ConfigSpec();
        private final List<ConfigValue<?>> values = new ArrayList<>();
        private final Map<String, String> comments = new LinkedHashMap<>();
        private final Deque<String> stack = new ArrayDeque<>();

        Builder(String modId, String fileName) {
            this.modId = modId;
            this.fileName = fileName;
        }

        private String resolve(String path) {
            if (stack.isEmpty()) return path;
            List<String> parts = new ArrayList<>(stack);
            Collections.reverse(parts);
            return String.join(".", parts) + "." + path;
        }

        public Builder push(String category) {
            stack.push(category);
            return this;
        }

        public Builder pop() {
            if (!stack.isEmpty()) stack.pop();
            return this;
        }

        public Builder comment(String comment) {
            List<String> parts = new ArrayList<>(stack);
            Collections.reverse(parts);
            comments.put(String.join(".", parts), comment);
            return this;
        }

        // ---- Boolean ----

        public BooleanValue define(String path, boolean defaultValue) {
            return define(path, defaultValue, null);
        }

        public BooleanValue define(String path, boolean defaultValue, String comment) {
            String full = resolve(path);
            spec.define(full, defaultValue);
            BooleanValue v = new BooleanValue(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- Int ----

        public IntValue defineInRange(String path, int defaultValue, int min, int max) {
            return defineInRange(path, defaultValue, min, max, null);
        }

        public IntValue defineInRange(String path, int defaultValue, int min, int max, String comment) {
            String full = resolve(path);
            spec.defineInRange(full, defaultValue, min, max);
            IntValue v = new IntValue(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- Long ----

        public LongValue defineInRange(String path, long defaultValue, long min, long max) {
            return defineInRange(path, defaultValue, min, max, null);
        }

        public LongValue defineInRange(String path, long defaultValue, long min, long max, String comment) {
            String full = resolve(path);
            spec.defineInRange(full, defaultValue, min, max);
            LongValue v = new LongValue(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- Double ----

        public DoubleValue defineInRange(String path, double defaultValue, double min, double max) {
            return defineInRange(path, defaultValue, min, max, null);
        }

        public DoubleValue defineInRange(String path, double defaultValue, double min, double max, String comment) {
            String full = resolve(path);
            spec.defineInRange(full, defaultValue, min, max);
            DoubleValue v = new DoubleValue(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- String ----

        public StringValue define(String path, String defaultValue, String comment) {
            String full = resolve(path);
            spec.define(full, defaultValue);
            StringValue v = new StringValue(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- Enum ----

        @SafeVarargs
        public final <T extends Enum<T>> EnumValue<T> defineEnum(String path, T defaultValue, String comment, T... allowed) {
            String full = resolve(path);
            spec.define(full, defaultValue);
            EnumValue<T> v = new EnumValue<>(full, defaultValue);
            values.add(v);
            if (comment != null) comments.put(full, comment);
            return v;
        }

        // ---- List ----

        public <T> ListValue<T> defineList(String path, List<T> defaultValue) {
            return defineList(path, defaultValue, o -> true);
        }

        public <T> ListValue<T> defineList(String path, List<T> defaultValue, Predicate<Object> elementValidator) {
            String full = resolve(path);
            spec.defineList(full, defaultValue, elementValidator);
            ListValue<T> v = new ListValue<>(full, defaultValue);
            values.add(v);
            return v;
        }

        public PortConfigSpec build() {
            return new PortConfigSpec(modId, fileName, spec, values, comments);
        }
    }

    // ==================== Value types ====================

    public static abstract class ConfigValue<T> implements Supplier<T> {
        protected final String path;
        protected final T defaultValue;
        protected CommentedFileConfig config;

        ConfigValue(String path, T defaultValue) {
            this.path = path;
            this.defaultValue = defaultValue;
        }

        void resolve(CommentedFileConfig config) {
            this.config = config;
        }

        @Override
        public T get() {
            return config != null ? config.getOrElse(path, defaultValue) : defaultValue;
        }

        public void set(T value) {
            if (config != null) {
                config.set(path, value);
                config.save();
            }
        }

        public String getPath() {return path;}
    }

    public static final class BooleanValue extends ConfigValue<Boolean> {
        BooleanValue(String path, boolean defaultValue) {
            super(path, defaultValue);
        }
    }

    static class NumberValue<T extends Number> extends ConfigValue<T> {
        NumberValue(String path, T defaultValue) {
            super(path, defaultValue);
        }

        public int getAsInt() {
            return get().intValue();
        }

        public long getAsLong() {
            return get().longValue();
        }

        public float getAsFloat() {
            return get().floatValue();
        }

        public double getAsDouble() {
            return get().doubleValue();
        }
    }

    public static final class IntValue extends NumberValue<Integer> {
        IntValue(String path, int defaultValue) {
            super(path, defaultValue);
        }
    }

    public static final class LongValue extends NumberValue<Long> {
        LongValue(String path, long defaultValue) {
            super(path, defaultValue);
        }
    }

    public static final class DoubleValue extends NumberValue<Double> {
        DoubleValue(String path, double defaultValue) {
            super(path, defaultValue);
        }
    }

    public static final class StringValue extends ConfigValue<String> {
        StringValue(String path, String defaultValue) {
            super(path, defaultValue);
        }
    }

    public static final class EnumValue<T extends Enum<T>> extends ConfigValue<T> {
        EnumValue(String path, T defaultValue) {
            super(path, defaultValue);
        }
    }

    public static final class ListValue<T> extends ConfigValue<List<T>> {
        ListValue(String path, List<T> defaultValue) {
            super(path, defaultValue);
        }

        @SuppressWarnings("unchecked")
        @Override
        public List<T> get() {
            if (config == null) return defaultValue;
            List<?> raw = config.getOrElse(path, Collections.emptyList());
            if (raw.isEmpty()) return Collections.emptyList();
            try {
                return (List<T>) raw;
            } catch (ClassCastException e) {
                return defaultValue;
            }
        }
    }
}
