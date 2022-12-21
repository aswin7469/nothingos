package android.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemApi {

    public enum Client {
        PRIVILEGED_APPS,
        MODULE_LIBRARIES,
        SYSTEM_SERVER
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Container {
        SystemApi[] value();
    }

    Client client() default Client.PRIVILEGED_APPS;
}
