package org.mesdag.portlib.util;

import java.lang.annotation.*;

/// 提醒lib使用者要使用var来存本地变量，或直接inline
@Documented
@Target({ElementType.TYPE_USE, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface VarOrInline {
}
