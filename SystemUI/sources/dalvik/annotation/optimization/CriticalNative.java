package dalvik.annotation.optimization;

import android.annotation.SystemApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface CriticalNative {
}
