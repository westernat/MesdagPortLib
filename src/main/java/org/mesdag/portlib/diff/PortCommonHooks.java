package org.mesdag.portlib.diff;

import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Diff
public class PortCommonHooks {
    private static final Set<Class<?>> checkedComponentClasses = ConcurrentHashMap.newKeySet();

    public static void validateComponent(@Nullable Object dataComponent) {
        if (!SharedConstants.IS_RUNNING_IN_IDE || dataComponent == null) {
            return;
        }

        Class<?> clazz = dataComponent.getClass();
        if (!checkedComponentClasses.contains(clazz)) {
            if (clazz.isRecord() || clazz.isEnum()) {
                checkedComponentClasses.add(clazz);
                return;
            }

            if (overridesEqualsAndHashCode(clazz)) {
                checkedComponentClasses.add(clazz);
                return;
            }

            if (isPotentialRegistryObject(dataComponent)) {
                checkedComponentClasses.add(clazz);
                return;
            }

            throw new IllegalArgumentException("Data components must implement equals and hashCode. Keep in mind they must also be immutable. Problematic class: " + clazz);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean isPotentialRegistryObject(Object value) {
        for (Registry registry : BuiltInRegistries.REGISTRY) {
            if (registry.getKey(value) != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean overridesEqualsAndHashCode(Class<?> clazz) {
        try {
            Method equals = clazz.getMethod("equals", Object.class);
            Method hashCode = clazz.getMethod("hashCode");
            return equals.getDeclaringClass() != Object.class && hashCode.getDeclaringClass() != Object.class;
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException("Failed to check for component equals and hashCode", exception);
        }
    }
}
