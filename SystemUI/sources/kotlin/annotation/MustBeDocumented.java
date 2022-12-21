package kotlin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.Metadata;

@Target({ElementType.ANNOTATION_TYPE})
@Target(allowedTargets = {AnnotationTarget.ANNOTATION_CLASS})
@Metadata(mo64986d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo64987d2 = {"Lkotlin/annotation/MustBeDocumented;", "", "kotlin-stdlib"}, mo64988k = 1, mo64989mv = {1, 7, 1}, mo64991xi = 48)
@Retention(RetentionPolicy.RUNTIME)
/* compiled from: Annotations.kt */
public @interface MustBeDocumented {
}
