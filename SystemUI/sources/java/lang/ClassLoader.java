package java.lang;

import com.android.launcher3.icons.cache.BaseIconCache;
import dalvik.system.PathClassLoader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.ProtectionDomain;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.misc.CompoundEnumeration;
import sun.reflect.CallerSensitive;

public abstract class ClassLoader {
    private transient long allocator;
    private transient long classTable;
    private final HashMap<String, Package> packages;
    private final ClassLoader parent;
    public final Map<List<Class<?>>, Class<?>> proxyCache;

    private static Void checkCreateClassLoader() {
        return null;
    }

    private Class<?> findBootstrapClassOrNull(String str) {
        return null;
    }

    private static URL getBootstrapResource(String str) {
        return null;
    }

    private static Enumeration<URL> getBootstrapResources(String str) throws IOException {
        return null;
    }

    @CallerSensitive
    protected static boolean registerAsParallelCapable() {
        return true;
    }

    public void clearAssertionStatus() {
    }

    /* access modifiers changed from: protected */
    public String findLibrary(String str) {
        return null;
    }

    /* access modifiers changed from: protected */
    public URL findResource(String str) {
        return null;
    }

    /* access modifiers changed from: protected */
    public final void resolveClass(Class<?> cls) {
    }

    public void setClassAssertionStatus(String str, boolean z) {
    }

    public void setDefaultAssertionStatus(boolean z) {
    }

    public void setPackageAssertionStatus(String str, boolean z) {
    }

    /* access modifiers changed from: protected */
    public final void setSigners(Class<?> cls, Object[] objArr) {
    }

    private static class SystemClassLoader {
        public static ClassLoader loader = ClassLoader.createSystemClassLoader();

        private SystemClassLoader() {
        }
    }

    /* access modifiers changed from: private */
    public static ClassLoader createSystemClassLoader() {
        return new PathClassLoader(System.getProperty("java.class.path", BaseIconCache.EMPTY_CLASS_NAME), System.getProperty("java.library.path", ""), BootClassLoader.getInstance());
    }

    private ClassLoader(Void voidR, ClassLoader classLoader) {
        this.proxyCache = new HashMap();
        this.packages = new HashMap<>();
        this.parent = classLoader;
    }

    protected ClassLoader(ClassLoader classLoader) {
        this(checkCreateClassLoader(), classLoader);
    }

    protected ClassLoader() {
        this(checkCreateClassLoader(), getSystemClassLoader());
    }

    public Class<?> loadClass(String str) throws ClassNotFoundException {
        return loadClass(str, false);
    }

    /* access modifiers changed from: protected */
    public Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        Class<?> findLoadedClass = findLoadedClass(str);
        if (findLoadedClass != null) {
            return findLoadedClass;
        }
        try {
            ClassLoader classLoader = this.parent;
            if (classLoader != null) {
                findLoadedClass = classLoader.loadClass(str, false);
            } else {
                findLoadedClass = findBootstrapClassOrNull(str);
            }
        } catch (ClassNotFoundException unused) {
        }
        return findLoadedClass == null ? findClass(str) : findLoadedClass;
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String str) throws ClassNotFoundException {
        throw new ClassNotFoundException(str);
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public final Class<?> defineClass(byte[] bArr, int i, int i2) throws ClassFormatError {
        throw new UnsupportedOperationException("can't load this type of class file");
    }

    /* access modifiers changed from: protected */
    public final Class<?> defineClass(String str, byte[] bArr, int i, int i2) throws ClassFormatError {
        throw new UnsupportedOperationException("can't load this type of class file");
    }

    /* access modifiers changed from: protected */
    public final Class<?> defineClass(String str, byte[] bArr, int i, int i2, ProtectionDomain protectionDomain) throws ClassFormatError {
        throw new UnsupportedOperationException("can't load this type of class file");
    }

    /* access modifiers changed from: protected */
    public final Class<?> defineClass(String str, ByteBuffer byteBuffer, ProtectionDomain protectionDomain) throws ClassFormatError {
        throw new UnsupportedOperationException("can't load this type of class file");
    }

    /* access modifiers changed from: protected */
    public final Class<?> findSystemClass(String str) throws ClassNotFoundException {
        return Class.forName(str, false, getSystemClassLoader());
    }

    /* access modifiers changed from: protected */
    public final Class<?> findLoadedClass(String str) {
        if (this == BootClassLoader.getInstance()) {
            this = null;
        }
        return VMClassLoader.findLoadedClass(this, str);
    }

    public URL getResource(String str) {
        URL url;
        ClassLoader classLoader = this.parent;
        if (classLoader != null) {
            url = classLoader.getResource(str);
        } else {
            url = getBootstrapResource(str);
        }
        return url == null ? findResource(str) : url;
    }

    public Enumeration<URL> getResources(String str) throws IOException {
        Enumeration[] enumerationArr = new Enumeration[2];
        ClassLoader classLoader = this.parent;
        if (classLoader != null) {
            enumerationArr[0] = classLoader.getResources(str);
        } else {
            enumerationArr[0] = getBootstrapResources(str);
        }
        enumerationArr[1] = findResources(str);
        return new CompoundEnumeration(enumerationArr);
    }

    /* access modifiers changed from: protected */
    public Enumeration<URL> findResources(String str) throws IOException {
        return Collections.emptyEnumeration();
    }

    public static URL getSystemResource(String str) {
        ClassLoader systemClassLoader = getSystemClassLoader();
        if (systemClassLoader == null) {
            return getBootstrapResource(str);
        }
        return systemClassLoader.getResource(str);
    }

    public static Enumeration<URL> getSystemResources(String str) throws IOException {
        ClassLoader systemClassLoader = getSystemClassLoader();
        if (systemClassLoader == null) {
            return getBootstrapResources(str);
        }
        return systemClassLoader.getResources(str);
    }

    public InputStream getResourceAsStream(String str) {
        URL resource = getResource(str);
        if (resource == null) {
            return null;
        }
        try {
            return resource.openStream();
        } catch (IOException unused) {
            return null;
        }
    }

    public static InputStream getSystemResourceAsStream(String str) {
        URL systemResource = getSystemResource(str);
        if (systemResource == null) {
            return null;
        }
        try {
            return systemResource.openStream();
        } catch (IOException unused) {
            return null;
        }
    }

    @CallerSensitive
    public final ClassLoader getParent() {
        return this.parent;
    }

    @CallerSensitive
    public static ClassLoader getSystemClassLoader() {
        return SystemClassLoader.loader;
    }

    static ClassLoader getClassLoader(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        return cls.getClassLoader();
    }

    /* access modifiers changed from: protected */
    public Package definePackage(String str, String str2, String str3, String str4, String str5, String str6, String str7, URL url) throws IllegalArgumentException {
        Package packageR;
        String str8 = str;
        synchronized (this.packages) {
            if (this.packages.get(str) == null) {
                packageR = new Package(str, str2, str3, str4, str5, str6, str7, url, this);
                this.packages.put(str, packageR);
            } else {
                throw new IllegalArgumentException(str);
            }
        }
        return packageR;
    }

    /* access modifiers changed from: protected */
    public Package getPackage(String str) {
        Package packageR;
        synchronized (this.packages) {
            packageR = this.packages.get(str);
        }
        return packageR;
    }

    /* access modifiers changed from: protected */
    public Package[] getPackages() {
        HashMap hashMap;
        synchronized (this.packages) {
            hashMap = new HashMap(this.packages);
        }
        return (Package[]) hashMap.values().toArray((T[]) new Package[hashMap.size()]);
    }
}
