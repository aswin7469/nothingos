package libcore.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {
    int from() default Integer.MIN_VALUE;

    /* renamed from: to */
    int mo66829to() default Integer.MAX_VALUE;
}
