package kotlin.coroutines.jvm.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: DebugMetadata.kt */
public @interface DebugMetadata {
    /* renamed from: c */
    String mo24969c() default "";

    /* renamed from: f */
    String mo24970f() default "";

    /* renamed from: l */
    int[] mo24971l() default {};

    /* renamed from: m */
    String mo24972m() default "";

    /* renamed from: v */
    int mo24973v() default 1;
}
