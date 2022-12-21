package java.lang;

import dalvik.system.VMRuntime;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import sun.net.www.ParseUtil;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public class Package implements AnnotatedElement {
    /* access modifiers changed from: private */
    public static Map<String, Manifest> mans = new HashMap(10);
    /* access modifiers changed from: private */
    public static Map<String, Package> pkgs = new HashMap(31);
    /* access modifiers changed from: private */
    public static Map<String, URL> urls = new HashMap(10);
    private final String implTitle;
    private final String implVendor;
    private final String implVersion;
    private final transient ClassLoader loader;
    private transient Class<?> packageInfo;
    private final String pkgName;
    private final URL sealBase;
    private final String specTitle;
    private final String specVendor;
    private final String specVersion;

    private static native String getSystemPackage0(String str);

    private static native String[] getSystemPackages0();

    public String getName() {
        return this.pkgName;
    }

    public String getSpecificationTitle() {
        return this.specTitle;
    }

    public String getSpecificationVersion() {
        return this.specVersion;
    }

    public String getSpecificationVendor() {
        return this.specVendor;
    }

    public String getImplementationTitle() {
        return this.implTitle;
    }

    public String getImplementationVersion() {
        return this.implVersion;
    }

    public String getImplementationVendor() {
        return this.implVendor;
    }

    public boolean isSealed() {
        return this.sealBase != null;
    }

    public boolean isSealed(URL url) {
        return url.equals(this.sealBase);
    }

    public boolean isCompatibleWith(String str) throws NumberFormatException {
        int i;
        String str2 = this.specVersion;
        if (str2 == null || str2.length() < 1) {
            throw new NumberFormatException("Empty version string");
        }
        String[] split = this.specVersion.split("\\.", -1);
        int length = split.length;
        int[] iArr = new int[length];
        int i2 = 0;
        while (i2 < split.length) {
            int parseInt = Integer.parseInt(split[i2]);
            iArr[i2] = parseInt;
            if (parseInt >= 0) {
                i2++;
            } else {
                throw NumberFormatException.forInputString("" + iArr[i2]);
            }
        }
        String[] split2 = str.split("\\.", -1);
        int length2 = split2.length;
        int[] iArr2 = new int[length2];
        int i3 = 0;
        while (i3 < split2.length) {
            int parseInt2 = Integer.parseInt(split2[i3]);
            iArr2[i3] = parseInt2;
            if (parseInt2 >= 0) {
                i3++;
            } else {
                throw NumberFormatException.forInputString("" + iArr2[i3]);
            }
        }
        int max = Math.max(length2, length);
        int i4 = 0;
        while (i4 < max) {
            int i5 = i4 < length2 ? iArr2[i4] : 0;
            if (i4 < length) {
                i = iArr[i4];
            } else {
                i = 0;
            }
            if (i < i5) {
                return false;
            }
            if (i > i5) {
                return true;
            }
            i4++;
        }
        return true;
    }

    @CallerSensitive
    public static Package getPackage(String str) {
        ClassLoader classLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
        if (classLoader != null) {
            return classLoader.getPackage(str);
        }
        return getSystemPackage(str);
    }

    @CallerSensitive
    public static Package[] getPackages() {
        ClassLoader classLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
        if (classLoader != null) {
            return classLoader.getPackages();
        }
        return getSystemPackages();
    }

    static Package getPackage(Class<?> cls) {
        String name = cls.getName();
        int lastIndexOf = name.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return null;
        }
        String substring = name.substring(0, lastIndexOf);
        ClassLoader classLoader = cls.getClassLoader();
        if (classLoader != null) {
            return classLoader.getPackage(substring);
        }
        return getSystemPackage(substring);
    }

    public int hashCode() {
        return this.pkgName.hashCode();
    }

    public String toString() {
        String str;
        int targetSdkVersion = VMRuntime.getRuntime().getTargetSdkVersion();
        if (targetSdkVersion <= 0 || targetSdkVersion > 24) {
            String str2 = this.specTitle;
            String str3 = this.specVersion;
            String str4 = "";
            if (str2 == null || str2.length() <= 0) {
                str = str4;
            } else {
                str = ", " + str2;
            }
            if (str3 != null && str3.length() > 0) {
                str4 = ", version " + str3;
            }
            return "package " + this.pkgName + str + str4;
        }
        return "package " + this.pkgName;
    }

    private Class<?> getPackageInfo() {
        if (this.packageInfo == null) {
            try {
                this.packageInfo = Class.forName(this.pkgName + ".package-info", false, this.loader);
            } catch (ClassNotFoundException unused) {
                this.packageInfo = AnonymousClass1PackageInfoProxy.class;
            }
        }
        return this.packageInfo;
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        return getPackageInfo().getAnnotation(cls);
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return super.isAnnotationPresent(cls);
    }

    public <A extends Annotation> A[] getAnnotationsByType(Class<A> cls) {
        return getPackageInfo().getAnnotationsByType(cls);
    }

    public Annotation[] getAnnotations() {
        return getPackageInfo().getAnnotations();
    }

    public <A extends Annotation> A getDeclaredAnnotation(Class<A> cls) {
        return getPackageInfo().getDeclaredAnnotation(cls);
    }

    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> cls) {
        return getPackageInfo().getDeclaredAnnotationsByType(cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        return getPackageInfo().getDeclaredAnnotations();
    }

    Package(String str, String str2, String str3, String str4, String str5, String str6, String str7, URL url, ClassLoader classLoader) {
        this.pkgName = str;
        this.implTitle = str5;
        this.implVersion = str6;
        this.implVendor = str7;
        this.specTitle = str2;
        this.specVersion = str3;
        this.specVendor = str4;
        this.sealBase = url;
        this.loader = classLoader;
    }

    private Package(String str, Manifest manifest, URL url, ClassLoader classLoader) {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        Attributes attributes = manifest.getAttributes(str.replace('.', '/').concat("/"));
        if (attributes != null) {
            str7 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            str6 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            str5 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            str4 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            str3 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            str2 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            str8 = attributes.getValue(Attributes.Name.SEALED);
        } else {
            str8 = null;
            str7 = null;
            str6 = null;
            str5 = null;
            str4 = null;
            str3 = null;
            str2 = null;
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            str7 = str7 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE) : str7;
            str6 = str6 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION) : str6;
            str5 = str5 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR) : str5;
            str4 = str4 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE) : str4;
            str3 = str3 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION) : str3;
            str2 = str2 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR) : str2;
            if (str8 == null) {
                str8 = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        url = !"true".equalsIgnoreCase(str8) ? null : url;
        this.pkgName = str;
        this.specTitle = str7;
        this.specVersion = str6;
        this.specVendor = str5;
        this.implTitle = str4;
        this.implVersion = str3;
        this.implVendor = str2;
        this.sealBase = url;
        this.loader = classLoader;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
        r4 = r4.replace('.', '/').concat("/");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Package getSystemPackage(java.lang.String r4) {
        /*
            java.util.Map<java.lang.String, java.lang.Package> r0 = pkgs
            monitor-enter(r0)
            java.util.Map<java.lang.String, java.lang.Package> r1 = pkgs     // Catch:{ all -> 0x0027 }
            java.lang.Object r1 = r1.get(r4)     // Catch:{ all -> 0x0027 }
            java.lang.Package r1 = (java.lang.Package) r1     // Catch:{ all -> 0x0027 }
            if (r1 != 0) goto L_0x0025
            r2 = 46
            r3 = 47
            java.lang.String r4 = r4.replace((char) r2, (char) r3)     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = "/"
            java.lang.String r4 = r4.concat(r2)     // Catch:{ all -> 0x0027 }
            java.lang.String r2 = getSystemPackage0(r4)     // Catch:{ all -> 0x0027 }
            if (r2 == 0) goto L_0x0025
            java.lang.Package r1 = defineSystemPackage(r4, r2)     // Catch:{ all -> 0x0027 }
        L_0x0025:
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r1
        L_0x0027:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.Package.getSystemPackage(java.lang.String):java.lang.Package");
    }

    static Package[] getSystemPackages() {
        Package[] packageArr;
        String[] systemPackages0 = getSystemPackages0();
        synchronized (pkgs) {
            for (String str : systemPackages0) {
                defineSystemPackage(str, getSystemPackage0(str));
            }
            packageArr = (Package[]) pkgs.values().toArray((T[]) new Package[pkgs.size()]);
        }
        return packageArr;
    }

    private static Package defineSystemPackage(final String str, final String str2) {
        return (Package) AccessController.doPrivileged(new PrivilegedAction<Package>() {
            public Package run() {
                Package packageR;
                String str = String.this;
                URL url = (URL) Package.urls.get(str2);
                if (url == null) {
                    File file = new File(str2);
                    try {
                        url = ParseUtil.fileToEncodedURL(file);
                    } catch (MalformedURLException unused) {
                    }
                    if (url != null) {
                        Package.urls.put(str2, url);
                        if (file.isFile()) {
                            Map r3 = Package.mans;
                            String str2 = str2;
                            r3.put(str2, Package.loadManifest(str2));
                        }
                    }
                }
                URL url2 = url;
                String replace = str.substring(0, str.length() - 1).replace('/', '.');
                Manifest manifest = (Manifest) Package.mans.get(str2);
                if (manifest != null) {
                    packageR = new Package(replace, manifest, url2, (ClassLoader) null);
                } else {
                    packageR = new Package(replace, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (URL) null, (ClassLoader) null);
                }
                Package.pkgs.put(replace, packageR);
                return packageR;
            }
        });
    }

    /* access modifiers changed from: private */
    public static Manifest loadManifest(String str) {
        JarInputStream jarInputStream;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            try {
                jarInputStream = new JarInputStream(fileInputStream, false);
                Manifest manifest = jarInputStream.getManifest();
                jarInputStream.close();
                fileInputStream.close();
                return manifest;
            } catch (Throwable th) {
                fileInputStream.close();
                throw th;
            }
            throw th;
        } catch (IOException unused) {
            return null;
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
    }
}
