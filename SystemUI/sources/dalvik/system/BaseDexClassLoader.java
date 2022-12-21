package dalvik.system;

import android.annotation.SystemApi;
import com.android.settingslib.accessibility.AccessibilityUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.URL;
import java.nio.ByteBuffer;
import java.p026io.File;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import sun.misc.CompoundEnumeration;

public class BaseDexClassLoader extends ClassLoader {
    private static volatile Reporter reporter;
    private final DexPathList pathList;
    protected final ClassLoader[] sharedLibraryLoaders;
    protected final ClassLoader[] sharedLibraryLoadersAfter;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface Reporter {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void report(Map<String, String> map);
    }

    private native String[] computeClassLoaderContextsNative();

    public BaseDexClassLoader(String str, File file, String str2, ClassLoader classLoader) {
        this(str, str2, classLoader, (ClassLoader[]) null, (ClassLoader[]) null, false);
    }

    public BaseDexClassLoader(String str, File file, String str2, ClassLoader classLoader, boolean z) {
        this(str, str2, classLoader, (ClassLoader[]) null, (ClassLoader[]) null, z);
    }

    public BaseDexClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr) {
        this(str, str2, classLoader, classLoaderArr, (ClassLoader[]) null, false);
    }

    public BaseDexClassLoader(String str, String str2, ClassLoader classLoader, ClassLoader[] classLoaderArr, ClassLoader[] classLoaderArr2) {
        this(str, str2, classLoader, classLoaderArr, classLoaderArr2, false);
    }

    /* JADX WARNING: type inference failed for: r7v2, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public BaseDexClassLoader(java.lang.String r7, java.lang.String r8, java.lang.ClassLoader r9, java.lang.ClassLoader[] r10, java.lang.ClassLoader[] r11, boolean r12) {
        /*
            r6 = this;
            r6.<init>(r9)
            r9 = 0
            if (r10 != 0) goto L_0x0008
            r10 = r9
            goto L_0x000f
        L_0x0008:
            int r0 = r10.length
            java.lang.Object[] r10 = java.util.Arrays.copyOf((T[]) r10, (int) r0)
            java.lang.ClassLoader[] r10 = (java.lang.ClassLoader[]) r10
        L_0x000f:
            r6.sharedLibraryLoaders = r10
            dalvik.system.DexPathList r10 = new dalvik.system.DexPathList
            r4 = 0
            r0 = r10
            r1 = r6
            r2 = r7
            r3 = r8
            r5 = r12
            r0.<init>(r1, r2, r3, r4, r5)
            r6.pathList = r10
            if (r11 != 0) goto L_0x0021
            goto L_0x0029
        L_0x0021:
            int r7 = r11.length
            java.lang.Object[] r7 = java.util.Arrays.copyOf((T[]) r11, (int) r7)
            r9 = r7
            java.lang.ClassLoader[] r9 = (java.lang.ClassLoader[]) r9
        L_0x0029:
            r6.sharedLibraryLoadersAfter = r9
            r10.maybeRunBackgroundVerification(r6)
            r6.reportClassLoaderChain()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dalvik.system.BaseDexClassLoader.<init>(java.lang.String, java.lang.String, java.lang.ClassLoader, java.lang.ClassLoader[], java.lang.ClassLoader[], boolean):void");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void reportClassLoaderChain() {
        if (reporter != null) {
            String[] computeClassLoaderContextsNative = computeClassLoaderContextsNative();
            if (computeClassLoaderContextsNative.length != 0) {
                HashMap hashMap = new HashMap(computeClassLoaderContextsNative.length / 2);
                for (int i = 0; i < computeClassLoaderContextsNative.length; i += 2) {
                    hashMap.put(computeClassLoaderContextsNative[i], computeClassLoaderContextsNative[i + 1]);
                }
                reporter.report(Collections.unmodifiableMap(hashMap));
            }
        }
    }

    public BaseDexClassLoader(ByteBuffer[] byteBufferArr, String str, ClassLoader classLoader) {
        super(classLoader);
        this.sharedLibraryLoaders = null;
        this.sharedLibraryLoadersAfter = null;
        DexPathList dexPathList = new DexPathList(this, str);
        this.pathList = dexPathList;
        dexPathList.initByteBufferDexPath(byteBufferArr);
        dexPathList.maybeRunBackgroundVerification(this);
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String str) throws ClassNotFoundException {
        ClassLoader[] classLoaderArr = this.sharedLibraryLoaders;
        int i = 0;
        if (classLoaderArr != null) {
            int i2 = 0;
            while (i2 < classLoaderArr.length) {
                try {
                    return classLoaderArr[i2].loadClass(str);
                } catch (ClassNotFoundException unused) {
                    i2++;
                }
            }
        }
        ArrayList<Throwable> arrayList = new ArrayList<>();
        Class<?> findClass = this.pathList.findClass(str, arrayList);
        if (findClass != null) {
            return findClass;
        }
        ClassLoader[] classLoaderArr2 = this.sharedLibraryLoadersAfter;
        if (classLoaderArr2 != null) {
            while (i < classLoaderArr2.length) {
                try {
                    return classLoaderArr2[i].loadClass(str);
                } catch (ClassNotFoundException unused2) {
                    i++;
                }
            }
        }
        if (findClass != null) {
            return findClass;
        }
        ClassNotFoundException classNotFoundException = new ClassNotFoundException("Didn't find class \"" + str + "\" on path: " + this.pathList);
        for (Throwable addSuppressed : arrayList) {
            classNotFoundException.addSuppressed(addSuppressed);
        }
        throw classNotFoundException;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addDexPath(String str) {
        addDexPath(str, false);
    }

    public void addDexPath(String str, boolean z) {
        this.pathList.addDexPath(str, (File) null, z);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public void addNativePath(Collection<String> collection) {
        this.pathList.addNativePath(collection);
    }

    /* access modifiers changed from: protected */
    public URL findResource(String str) {
        ClassLoader[] classLoaderArr = this.sharedLibraryLoaders;
        if (classLoaderArr != null) {
            for (ClassLoader resource : classLoaderArr) {
                URL resource2 = resource.getResource(str);
                if (resource2 != null) {
                    return resource2;
                }
            }
        }
        URL findResource = this.pathList.findResource(str);
        if (findResource != null) {
            return findResource;
        }
        ClassLoader[] classLoaderArr2 = this.sharedLibraryLoadersAfter;
        if (classLoaderArr2 == null) {
            return null;
        }
        for (ClassLoader resource3 : classLoaderArr2) {
            URL resource4 = resource3.getResource(str);
            if (resource4 != null) {
                return resource4;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Enumeration<URL> findResources(String str) {
        Enumeration<URL> findResources = this.pathList.findResources(str);
        ClassLoader[] classLoaderArr = this.sharedLibraryLoaders;
        if (classLoaderArr == null && this.sharedLibraryLoadersAfter == null) {
            return findResources;
        }
        int length = classLoaderArr != null ? classLoaderArr.length : 0;
        ClassLoader[] classLoaderArr2 = this.sharedLibraryLoadersAfter;
        int length2 = classLoaderArr2 != null ? classLoaderArr2.length : 0;
        Enumeration[] enumerationArr = new Enumeration[(length + length2 + 1)];
        int i = 0;
        while (i < length) {
            try {
                enumerationArr[i] = this.sharedLibraryLoaders[i].getResources(str);
            } catch (IOException unused) {
            }
            i++;
        }
        int i2 = i + 1;
        enumerationArr[i] = findResources;
        for (int i3 = 0; i3 < length2; i3++) {
            try {
                enumerationArr[i2] = this.sharedLibraryLoadersAfter[i3].getResources(str);
            } catch (IOException unused2) {
            }
            i2++;
        }
        return new CompoundEnumeration(enumerationArr);
    }

    public String findLibrary(String str) {
        return this.pathList.findLibrary(str);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0023, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Package getPackage(java.lang.String r11) {
        /*
            r10 = this;
            monitor-enter(r10)
            if (r11 == 0) goto L_0x0027
            boolean r0 = r11.isEmpty()     // Catch:{ all -> 0x0024 }
            if (r0 != 0) goto L_0x0027
            java.lang.Package r0 = super.getPackage(r11)     // Catch:{ all -> 0x0024 }
            if (r0 != 0) goto L_0x0022
            java.lang.String r3 = "Unknown"
            java.lang.String r4 = "0.0"
            java.lang.String r5 = "Unknown"
            java.lang.String r6 = "Unknown"
            java.lang.String r7 = "0.0"
            java.lang.String r8 = "Unknown"
            r9 = 0
            r1 = r10
            r2 = r11
            java.lang.Package r0 = r1.definePackage(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ all -> 0x0024 }
        L_0x0022:
            monitor-exit(r10)
            return r0
        L_0x0024:
            r11 = move-exception
            monitor-exit(r10)
            throw r11
        L_0x0027:
            monitor-exit(r10)
            r10 = 0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: dalvik.system.BaseDexClassLoader.getPackage(java.lang.String):java.lang.Package");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public String getLdLibraryPath() {
        StringBuilder sb = new StringBuilder();
        for (File next : this.pathList.getNativeLibraryDirectories()) {
            if (sb.length() > 0) {
                sb.append((char) AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            }
            sb.append((Object) next);
        }
        return sb.toString();
    }

    public String toString() {
        return getClass().getName() + NavigationBarInflaterView.SIZE_MOD_START + this.pathList + NavigationBarInflaterView.SIZE_MOD_END;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setReporter(Reporter reporter2) {
        reporter = reporter2;
    }

    public static Reporter getReporter() {
        return reporter;
    }
}
