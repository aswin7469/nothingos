package dalvik.annotation.codegen;

import android.annotation.SystemApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
@Target({ElementType.METHOD})
@Repeatable(CovariantReturnTypes.class)
@Retention(RetentionPolicy.CLASS)
public @interface CovariantReturnType {

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.CLASS)
    public @interface CovariantReturnTypes {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        CovariantReturnType[] value();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    int presentAfter();

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    Class<?> returnType();
}
