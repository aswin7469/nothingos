package dalvik.system;

import java.p026io.File;

public class DexClassLoader extends BaseDexClassLoader {
    public DexClassLoader(String str, String str2, String str3, ClassLoader classLoader) {
        super(str, (File) null, str3, classLoader);
    }
}
