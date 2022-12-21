package dalvik.system;

import android.annotation.SystemApi;
import java.p026io.File;

public class PathClassLoader extends BaseDexClassLoader {
    public PathClassLoader(String str, ClassLoader classLoader) {
        super(str, (File) null, (String) null, classLoader);
    }

    public PathClassLoader(String str, String str2, ClassLoader classLoader) {
        super(str, (File) null, str2, classLoader);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public PathClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr) {
        this(str, str2, classLoader, classLoaderArr, (ClassLoader[]) null);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public PathClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr, ClassLoader[] classLoaderArr2) {
        super(str, str2, classLoader, classLoaderArr, classLoaderArr2);
    }
}
