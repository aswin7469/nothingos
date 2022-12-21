package dalvik.system;

import android.annotation.SystemApi;
import java.net.URL;
import java.p026io.IOException;
import java.util.Enumeration;
import sun.misc.CompoundEnumeration;

public final class DelegateLastClassLoader extends PathClassLoader {
    private final boolean delegateResourceLoading;

    public DelegateLastClassLoader(String str, ClassLoader classLoader) {
        this(str, (String) null, classLoader, true);
    }

    public DelegateLastClassLoader(String str, String str2, ClassLoader classLoader) {
        this(str, str2, classLoader, true);
    }

    public DelegateLastClassLoader(String str, String str2, ClassLoader classLoader, boolean z) {
        super(str, str2, classLoader);
        this.delegateResourceLoading = z;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public DelegateLastClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr) {
        this(str, str2, classLoader, classLoaderArr, (ClassLoader[]) null);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public DelegateLastClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr, ClassLoader[] classLoaderArr2) {
        super(str, str2, classLoader, classLoaderArr, classLoaderArr2);
        this.delegateResourceLoading = true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:7|8|9) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        return getParent().loadClass(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0016, code lost:
        return findClass(r1);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0012 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Class<?> loadClass(java.lang.String r1, boolean r2) throws java.lang.ClassNotFoundException {
        /*
            r0 = this;
            java.lang.Class r2 = r0.findLoadedClass(r1)
            if (r2 == 0) goto L_0x0007
            return r2
        L_0x0007:
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x0012 }
            java.lang.Class r0 = r2.loadClass(r1)     // Catch:{ ClassNotFoundException -> 0x0012 }
            return r0
        L_0x0012:
            java.lang.Class r0 = r0.findClass(r1)     // Catch:{ ClassNotFoundException -> 0x0017 }
            return r0
        L_0x0017:
            r2 = move-exception
            java.lang.ClassLoader r0 = r0.getParent()     // Catch:{ ClassNotFoundException -> 0x0021 }
            java.lang.Class r0 = r0.loadClass(r1)     // Catch:{ ClassNotFoundException -> 0x0021 }
            return r0
        L_0x0021:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: dalvik.system.DelegateLastClassLoader.loadClass(java.lang.String, boolean):java.lang.Class");
    }

    public URL getResource(String str) {
        ClassLoader parent;
        URL resource = Object.class.getClassLoader().getResource(str);
        if (resource != null) {
            return resource;
        }
        URL findResource = findResource(str);
        if (findResource != null) {
            return findResource;
        }
        if (!this.delegateResourceLoading || (parent = getParent()) == null) {
            return null;
        }
        return parent.getResource(str);
    }

    public Enumeration<URL> getResources(String str) throws IOException {
        Enumeration[] enumerationArr = new Enumeration[3];
        enumerationArr[0] = Object.class.getClassLoader().getResources(str);
        enumerationArr[1] = findResources(str);
        enumerationArr[2] = (getParent() == null || !this.delegateResourceLoading) ? null : getParent().getResources(str);
        return new CompoundEnumeration(enumerationArr);
    }
}
