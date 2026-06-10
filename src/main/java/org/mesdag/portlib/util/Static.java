package org.mesdag.portlib.util;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/// 标记该方法为真正的静态方法
@Documented
@ApiStatus.NonExtendable
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Static {
}
