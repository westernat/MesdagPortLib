package org.mesdag.portlib.diff;

import com.google.errorprone.annotations.IncompatibleModifiers;
import com.google.errorprone.annotations.Modifier;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.*;

/// 标记为加载器间的不同代码，以防止public代码被误用
@Documented
@ApiStatus.Internal
@IncompatibleModifiers(modifier = {Modifier.PRIVATE, Modifier.PROTECTED})
@Inherited
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.CONSTRUCTOR,
        ElementType.ANNOTATION_TYPE,
        ElementType.PACKAGE,
        ElementType.MODULE,
        ElementType.RECORD_COMPONENT
})
@Retention(RetentionPolicy.SOURCE)
public @interface Diff {
}
