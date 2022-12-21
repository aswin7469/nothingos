package jdk.internal.p027vm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
/* renamed from: jdk.internal.vm.annotation.ReservedStackAccess */
public @interface ReservedStackAccess {
}
