package org.mesdag.portlib.util;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/// 标记该方法/字段只能在所在类里使用
@Documented
@ApiStatus.Internal
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Private {
}
