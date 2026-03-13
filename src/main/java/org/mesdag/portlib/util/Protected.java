package org.mesdag.portlib.util;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/// 标记该方法/字段不能在其它软件包使用
@Documented
@ApiStatus.Internal
@Inherited
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface Protected {
    boolean onlyInClass() default false;
}
