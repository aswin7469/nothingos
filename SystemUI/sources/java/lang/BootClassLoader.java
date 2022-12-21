package java.lang;

import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import java.net.URL;
import java.p026io.IOException;
import java.util.Collections;
import java.util.Enumeration;

/* compiled from: ClassLoader */
class BootClassLoader extends ClassLoader {
    private static BootClassLoader instance;

    public static synchronized BootClassLoader getInstance() {
        BootClassLoader bootClassLoader;
        synchronized (BootClassLoader.class) {
            if (instance == null) {
                instance = new BootClassLoader();
            }
            bootClassLoader = instance;
        }
        return bootClassLoader;
    }

    public BootClassLoader() {
        super((ClassLoader) null);
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String str) throws ClassNotFoundException {
        return Class.classForName(str, false, (ClassLoader) null);
    }

    /* access modifiers changed from: protected */
    public URL findResource(String str) {
        return VMClassLoader.getResource(str);
    }

    /* access modifiers changed from: protected */
    public Enumeration<URL> findResources(String str) throws IOException {
        return Collections.enumeration(VMClassLoader.getResources(str));
    }

    /* access modifiers changed from: protected */
    public Package getPackage(String str) {
        Package packageR;
        if (str == null || str.isEmpty()) {
            return null;
        }
        synchronized (this) {
            packageR = super.getPackage(str);
            if (packageR == null) {
                packageR = definePackage(str, FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN, "0.0", FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN, FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN, "0.0", FooterBarMixinMetrics.FooterButtonVisibility.UNKNOWN, (URL) null);
            }
        }
        return packageR;
    }

    public URL getResource(String str) {
        return findResource(str);
    }

    /* access modifiers changed from: protected */
    public Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        Class<?> findLoadedClass = findLoadedClass(str);
        return findLoadedClass == null ? findClass(str) : findLoadedClass;
    }

    public Enumeration<URL> getResources(String str) throws IOException {
        return findResources(str);
    }
}
