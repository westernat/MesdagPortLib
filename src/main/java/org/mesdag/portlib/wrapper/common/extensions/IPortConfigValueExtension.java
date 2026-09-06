package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 补齐新版配置界面所需的公开取值接口。
 *
 * @param <T> 配置值类型
 */
// 由 coremod 注入
public interface IPortConfigValueExtension<T> {
    @SuppressWarnings("unchecked")
    default T portlib$getRaw() {
        return ((ForgeConfigSpec.ConfigValue<T>) this).get();
    }

    @SuppressWarnings("unchecked")
    static <T> IPortConfigValueExtension<T> of(ForgeConfigSpec.ConfigValue<T> value) {
        return (IPortConfigValueExtension<T>) value;
    }
}
