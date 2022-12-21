package java.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.Reader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.util.locale.BaseLocale;
import sun.util.locale.LocaleExtensions;
import sun.util.locale.LocaleObjectCache;

public abstract class ResourceBundle {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int INITIAL_CACHE_SIZE = 32;
    private static final ResourceBundle NONEXISTENT_BUNDLE = new ResourceBundle() {
        public Enumeration<String> getKeys() {
            return null;
        }

        /* access modifiers changed from: protected */
        public Object handleGetObject(String str) {
            return null;
        }

        public String toString() {
            return "NONEXISTENT_BUNDLE";
        }
    };
    private static final ConcurrentMap<CacheKey, BundleReference> cacheList = new ConcurrentHashMap(32);
    /* access modifiers changed from: private */
    public static final ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    private volatile CacheKey cacheKey;
    private volatile boolean expired;
    private volatile Set<String> keySet;
    private Locale locale = null;
    private String name;
    protected ResourceBundle parent = null;

    private interface CacheKeyReference {
        CacheKey getCacheKey();
    }

    public abstract Enumeration<String> getKeys();

    /* access modifiers changed from: protected */
    public abstract Object handleGetObject(String str);

    public String getBaseBundleName() {
        return this.name;
    }

    public final String getString(String str) {
        return (String) getObject(str);
    }

    public final String[] getStringArray(String str) {
        return (String[]) getObject(str);
    }

    public final Object getObject(String str) {
        Object handleGetObject = handleGetObject(str);
        if (handleGetObject == null) {
            ResourceBundle resourceBundle = this.parent;
            if (resourceBundle != null) {
                handleGetObject = resourceBundle.getObject(str);
            }
            if (handleGetObject == null) {
                throw new MissingResourceException("Can't find resource for bundle " + getClass().getName() + ", key " + str, getClass().getName(), str);
            }
        }
        return handleGetObject;
    }

    public Locale getLocale() {
        return this.locale;
    }

    private static ClassLoader getLoader(Class<?> cls) {
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        return classLoader == null ? RBClassLoader.INSTANCE : classLoader;
    }

    private static class RBClassLoader extends ClassLoader {
        /* access modifiers changed from: private */
        public static final RBClassLoader INSTANCE = ((RBClassLoader) AccessController.doPrivileged(new PrivilegedAction<RBClassLoader>() {
            public RBClassLoader run() {
                return new RBClassLoader();
            }
        }));
        private static final ClassLoader loader = ClassLoader.getSystemClassLoader();

        private RBClassLoader() {
        }

        public Class<?> loadClass(String str) throws ClassNotFoundException {
            ClassLoader classLoader = loader;
            if (classLoader != null) {
                return classLoader.loadClass(str);
            }
            return Class.forName(str);
        }

        public URL getResource(String str) {
            ClassLoader classLoader = loader;
            if (classLoader != null) {
                return classLoader.getResource(str);
            }
            return ClassLoader.getSystemResource(str);
        }

        public InputStream getResourceAsStream(String str) {
            ClassLoader classLoader = loader;
            if (classLoader != null) {
                return classLoader.getResourceAsStream(str);
            }
            return ClassLoader.getSystemResourceAsStream(str);
        }
    }

    /* access modifiers changed from: protected */
    public void setParent(ResourceBundle resourceBundle) {
        this.parent = resourceBundle;
    }

    private static class CacheKey implements Cloneable {
        private Throwable cause;
        /* access modifiers changed from: private */
        public volatile long expirationTime;
        private String format;
        private int hashCodeCache;
        /* access modifiers changed from: private */
        public volatile long loadTime;
        private LoaderReference loaderRef;
        private Locale locale;
        private String name;

        CacheKey(String str, Locale locale2, ClassLoader classLoader) {
            this.name = str;
            this.locale = locale2;
            if (classLoader == null) {
                this.loaderRef = null;
            } else {
                this.loaderRef = new LoaderReference(classLoader, ResourceBundle.referenceQueue, this);
            }
            calculateHashCode();
        }

        /* access modifiers changed from: package-private */
        public String getName() {
            return this.name;
        }

        /* access modifiers changed from: package-private */
        public CacheKey setName(String str) {
            if (!this.name.equals(str)) {
                this.name = str;
                calculateHashCode();
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public Locale getLocale() {
            return this.locale;
        }

        /* access modifiers changed from: package-private */
        public CacheKey setLocale(Locale locale2) {
            if (!this.locale.equals(locale2)) {
                this.locale = locale2;
                calculateHashCode();
            }
            return this;
        }

        /* access modifiers changed from: package-private */
        public ClassLoader getLoader() {
            LoaderReference loaderReference = this.loaderRef;
            if (loaderReference != null) {
                return (ClassLoader) loaderReference.get();
            }
            return null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            try {
                CacheKey cacheKey = (CacheKey) obj;
                if (this.hashCodeCache != cacheKey.hashCodeCache || !this.name.equals(cacheKey.name) || !this.locale.equals(cacheKey.locale)) {
                    return false;
                }
                LoaderReference loaderReference = this.loaderRef;
                if (loaderReference != null) {
                    ClassLoader classLoader = (ClassLoader) loaderReference.get();
                    LoaderReference loaderReference2 = cacheKey.loaderRef;
                    if (loaderReference2 == null || classLoader == null || classLoader != loaderReference2.get()) {
                        return false;
                    }
                    return true;
                } else if (cacheKey.loaderRef == null) {
                    return true;
                } else {
                    return false;
                }
            } catch (ClassCastException | NullPointerException unused) {
                return false;
            }
        }

        public int hashCode() {
            return this.hashCodeCache;
        }

        private void calculateHashCode() {
            int hashCode = this.name.hashCode() << 3;
            this.hashCodeCache = hashCode;
            this.hashCodeCache = hashCode ^ this.locale.hashCode();
            ClassLoader loader = getLoader();
            if (loader != null) {
                this.hashCodeCache = loader.hashCode() ^ this.hashCodeCache;
            }
        }

        public Object clone() {
            try {
                CacheKey cacheKey = (CacheKey) super.clone();
                if (this.loaderRef != null) {
                    cacheKey.loaderRef = new LoaderReference((ClassLoader) this.loaderRef.get(), ResourceBundle.referenceQueue, cacheKey);
                }
                cacheKey.cause = null;
                return cacheKey;
            } catch (CloneNotSupportedException e) {
                throw new InternalError((Throwable) e);
            }
        }

        /* access modifiers changed from: package-private */
        public String getFormat() {
            return this.format;
        }

        /* access modifiers changed from: package-private */
        public void setFormat(String str) {
            this.format = str;
        }

        /* access modifiers changed from: private */
        public void setCause(Throwable th) {
            Throwable th2 = this.cause;
            if (th2 == null) {
                this.cause = th;
            } else if (th2 instanceof ClassNotFoundException) {
                this.cause = th;
            }
        }

        /* access modifiers changed from: private */
        public Throwable getCause() {
            return this.cause;
        }

        public String toString() {
            String locale2 = this.locale.toString();
            if (locale2.length() == 0) {
                if (this.locale.getVariant().length() != 0) {
                    locale2 = "__" + this.locale.getVariant();
                } else {
                    locale2 = "\"\"";
                }
            }
            return "CacheKey[" + this.name + ", lc=" + locale2 + ", ldr=" + getLoader() + "(format=" + this.format + ")]";
        }
    }

    private static class LoaderReference extends WeakReference<ClassLoader> implements CacheKeyReference {
        private CacheKey cacheKey;

        LoaderReference(ClassLoader classLoader, ReferenceQueue<Object> referenceQueue, CacheKey cacheKey2) {
            super(classLoader, referenceQueue);
            this.cacheKey = cacheKey2;
        }

        public CacheKey getCacheKey() {
            return this.cacheKey;
        }
    }

    private static class BundleReference extends SoftReference<ResourceBundle> implements CacheKeyReference {
        private CacheKey cacheKey;

        BundleReference(ResourceBundle resourceBundle, ReferenceQueue<Object> referenceQueue, CacheKey cacheKey2) {
            super(resourceBundle, referenceQueue);
            this.cacheKey = cacheKey2;
        }

        public CacheKey getCacheKey() {
            return this.cacheKey;
        }
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str) {
        return getBundleImpl(str, Locale.getDefault(), getLoader(Reflection.getCallerClass()), getDefaultControl(str));
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Control control) {
        return getBundleImpl(str, Locale.getDefault(), getLoader(Reflection.getCallerClass()), control);
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Locale locale2) {
        return getBundleImpl(str, locale2, getLoader(Reflection.getCallerClass()), getDefaultControl(str));
    }

    @CallerSensitive
    public static final ResourceBundle getBundle(String str, Locale locale2, Control control) {
        return getBundleImpl(str, locale2, getLoader(Reflection.getCallerClass()), control);
    }

    public static ResourceBundle getBundle(String str, Locale locale2, ClassLoader classLoader) {
        classLoader.getClass();
        return getBundleImpl(str, locale2, classLoader, getDefaultControl(str));
    }

    public static ResourceBundle getBundle(String str, Locale locale2, ClassLoader classLoader, Control control) {
        if (classLoader != null && control != null) {
            return getBundleImpl(str, locale2, classLoader, control);
        }
        throw null;
    }

    private static Control getDefaultControl(String str) {
        return Control.INSTANCE;
    }

    private static ResourceBundle getBundleImpl(String str, Locale locale2, ClassLoader classLoader, Control control) {
        String str2 = str;
        Locale locale3 = locale2;
        Control control2 = control;
        if (locale3 == null || control2 == null) {
            throw null;
        }
        CacheKey cacheKey2 = new CacheKey(str2, locale3, classLoader);
        BundleReference bundleReference = cacheList.get(cacheKey2);
        ResourceBundle resourceBundle = bundleReference != null ? (ResourceBundle) bundleReference.get() : null;
        if (isValidBundle(resourceBundle) && hasValidParentChain(resourceBundle)) {
            return resourceBundle;
        }
        boolean z = control2 == Control.INSTANCE || (control2 instanceof SingleFormatControl);
        List<String> formats = control2.getFormats(str2);
        if (z || checkList(formats)) {
            Locale locale4 = locale3;
            ResourceBundle resourceBundle2 = null;
            while (locale4 != null) {
                List<Locale> candidateLocales = control2.getCandidateLocales(str2, locale4);
                if (z || checkList(candidateLocales)) {
                    List<Locale> list = candidateLocales;
                    resourceBundle = findBundle(cacheKey2, candidateLocales, formats, 0, control, resourceBundle2);
                    if (isValidBundle(resourceBundle)) {
                        boolean equals = Locale.ROOT.equals(resourceBundle.locale);
                        if (!equals || resourceBundle.locale.equals(locale3) || (list.size() == 1 && resourceBundle.locale.equals(list.get(0)))) {
                            break;
                        } else if (equals && resourceBundle2 == null) {
                            resourceBundle2 = resourceBundle;
                        }
                    }
                    locale4 = control2.getFallbackLocale(str2, locale4);
                } else {
                    throw new IllegalArgumentException("Invalid Control: getCandidateLocales");
                }
            }
            if (resourceBundle != null) {
                return resourceBundle;
            }
            if (resourceBundle2 != null) {
                return resourceBundle2;
            }
            throwMissingResourceException(str2, locale3, cacheKey2.getCause());
            return resourceBundle2;
        }
        throw new IllegalArgumentException("Invalid Control: getFormats");
    }

    private static boolean checkList(List<?> list) {
        boolean z = list != null && !list.isEmpty();
        if (z) {
            int size = list.size();
            int i = 0;
            while (z && i < size) {
                z = list.get(i) != null;
                i++;
            }
        }
        return z;
    }

    private static ResourceBundle findBundle(CacheKey cacheKey2, List<Locale> list, List<String> list2, int i, Control control, ResourceBundle resourceBundle) {
        ResourceBundle resourceBundle2;
        boolean z;
        Locale locale2 = list.get(i);
        if (i != list.size() - 1) {
            resourceBundle2 = findBundle(cacheKey2, list, list2, i + 1, control, resourceBundle);
        } else if (resourceBundle != null && Locale.ROOT.equals(locale2)) {
            return resourceBundle;
        } else {
            resourceBundle2 = null;
        }
        while (true) {
            Reference<? extends Object> poll = referenceQueue.poll();
            if (poll == null) {
                break;
            }
            cacheList.remove(((CacheKeyReference) poll).getCacheKey());
        }
        cacheKey2.setLocale(locale2);
        ResourceBundle findBundleInCache = findBundleInCache(cacheKey2, control);
        if (isValidBundle(findBundleInCache)) {
            z = findBundleInCache.expired;
            if (!z) {
                if (findBundleInCache.parent == resourceBundle2) {
                    return findBundleInCache;
                }
                ConcurrentMap<CacheKey, BundleReference> concurrentMap = cacheList;
                BundleReference bundleReference = concurrentMap.get(cacheKey2);
                if (bundleReference != null && bundleReference.refersTo(findBundleInCache)) {
                    concurrentMap.remove(cacheKey2, bundleReference);
                }
            }
        } else {
            z = false;
        }
        ResourceBundle resourceBundle3 = NONEXISTENT_BUNDLE;
        if (findBundleInCache != resourceBundle3) {
            CacheKey cacheKey3 = (CacheKey) cacheKey2.clone();
            try {
                ResourceBundle loadBundle = loadBundle(cacheKey2, list2, control, z);
                if (loadBundle != null) {
                    if (loadBundle.parent == null) {
                        loadBundle.setParent(resourceBundle2);
                    }
                    loadBundle.locale = locale2;
                    return putBundleInCache(cacheKey2, loadBundle, control);
                }
                putBundleInCache(cacheKey2, resourceBundle3, control);
                if (cacheKey3.getCause() instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                if (cacheKey3.getCause() instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return resourceBundle2;
    }

    private static ResourceBundle loadBundle(CacheKey cacheKey2, List<String> list, Control control, boolean z) {
        Locale locale2 = cacheKey2.getLocale();
        int size = list.size();
        ResourceBundle resourceBundle = null;
        int i = 0;
        while (true) {
            if (i >= size) {
                break;
            }
            String str = list.get(i);
            try {
                resourceBundle = control.newBundle(cacheKey2.getName(), locale2, str, cacheKey2.getLoader(), z);
            } catch (LinkageError e) {
                cacheKey2.setCause(e);
            } catch (Exception e2) {
                cacheKey2.setCause(e2);
            }
            if (resourceBundle != null) {
                cacheKey2.setFormat(str);
                resourceBundle.name = cacheKey2.getName();
                resourceBundle.locale = locale2;
                resourceBundle.expired = false;
                break;
            }
            i++;
        }
        return resourceBundle;
    }

    private static boolean isValidBundle(ResourceBundle resourceBundle) {
        return (resourceBundle == null || resourceBundle == NONEXISTENT_BUNDLE) ? false : true;
    }

    private static boolean hasValidParentChain(ResourceBundle resourceBundle) {
        long currentTimeMillis = System.currentTimeMillis();
        while (resourceBundle != null) {
            if (resourceBundle.expired) {
                return false;
            }
            CacheKey cacheKey2 = resourceBundle.cacheKey;
            if (cacheKey2 != null) {
                long r4 = cacheKey2.expirationTime;
                if (r4 >= 0 && r4 <= currentTimeMillis) {
                    return false;
                }
            }
            resourceBundle = resourceBundle.parent;
        }
        return true;
    }

    private static void throwMissingResourceException(String str, Locale locale2, Throwable th) {
        if (th instanceof MissingResourceException) {
            th = null;
        }
        throw new MissingResourceException("Can't find bundle for base name " + str + ", locale " + locale2, str + BaseLocale.SEP + locale2, "", th);
    }

    private static ResourceBundle findBundleInCache(CacheKey cacheKey2, Control control) {
        ResourceBundle resourceBundle;
        ConcurrentMap<CacheKey, BundleReference> concurrentMap = cacheList;
        BundleReference bundleReference = concurrentMap.get(cacheKey2);
        if (bundleReference == null || (resourceBundle = (ResourceBundle) bundleReference.get()) == null) {
            return null;
        }
        ResourceBundle resourceBundle2 = resourceBundle.parent;
        if (resourceBundle2 == null || !resourceBundle2.expired) {
            CacheKey cacheKey3 = bundleReference.getCacheKey();
            long r4 = cacheKey3.expirationTime;
            if (!resourceBundle.expired && r4 >= 0 && r4 <= System.currentTimeMillis()) {
                if (resourceBundle != NONEXISTENT_BUNDLE) {
                    synchronized (resourceBundle) {
                        long r42 = cacheKey3.expirationTime;
                        if (!resourceBundle.expired && r42 >= 0 && r42 <= System.currentTimeMillis()) {
                            try {
                                resourceBundle.expired = control.needsReload(cacheKey3.getName(), cacheKey3.getLocale(), cacheKey3.getFormat(), cacheKey3.getLoader(), resourceBundle, cacheKey3.loadTime);
                            } catch (Exception e) {
                                cacheKey2.setCause(e);
                            }
                            if (resourceBundle.expired) {
                                resourceBundle.cacheKey = null;
                                cacheList.remove(cacheKey2, bundleReference);
                            } else {
                                setExpirationTime(cacheKey3, control);
                            }
                        }
                    }
                } else {
                    concurrentMap.remove(cacheKey2, bundleReference);
                    return null;
                }
            }
            return resourceBundle;
        }
        resourceBundle.expired = true;
        resourceBundle.cacheKey = null;
        concurrentMap.remove(cacheKey2, bundleReference);
        return null;
    }

    private static ResourceBundle putBundleInCache(CacheKey cacheKey2, ResourceBundle resourceBundle, Control control) {
        setExpirationTime(cacheKey2, control);
        if (cacheKey2.expirationTime == -1) {
            return resourceBundle;
        }
        CacheKey cacheKey3 = (CacheKey) cacheKey2.clone();
        BundleReference bundleReference = new BundleReference(resourceBundle, referenceQueue, cacheKey3);
        resourceBundle.cacheKey = cacheKey3;
        ConcurrentMap<CacheKey, BundleReference> concurrentMap = cacheList;
        BundleReference putIfAbsent = concurrentMap.putIfAbsent(cacheKey3, bundleReference);
        if (putIfAbsent == null) {
            return resourceBundle;
        }
        ResourceBundle resourceBundle2 = (ResourceBundle) putIfAbsent.get();
        if (resourceBundle2 == null || resourceBundle2.expired) {
            concurrentMap.put(cacheKey3, bundleReference);
            return resourceBundle;
        }
        resourceBundle.cacheKey = null;
        bundleReference.clear();
        return resourceBundle2;
    }

    private static void setExpirationTime(CacheKey cacheKey2, Control control) {
        long timeToLive = control.getTimeToLive(cacheKey2.getName(), cacheKey2.getLocale());
        if (timeToLive >= 0) {
            long currentTimeMillis = System.currentTimeMillis();
            cacheKey2.loadTime = currentTimeMillis;
            cacheKey2.expirationTime = currentTimeMillis + timeToLive;
        } else if (timeToLive >= -2) {
            cacheKey2.expirationTime = timeToLive;
        } else {
            throw new IllegalArgumentException("Invalid Control: TTL=" + timeToLive);
        }
    }

    @CallerSensitive
    public static final void clearCache() {
        clearCache(getLoader(Reflection.getCallerClass()));
    }

    public static final void clearCache(ClassLoader classLoader) {
        classLoader.getClass();
        Set<CacheKey> keySet2 = cacheList.keySet();
        for (CacheKey next : keySet2) {
            if (next.getLoader() == classLoader) {
                keySet2.remove(next);
            }
        }
    }

    public boolean containsKey(String str) {
        str.getClass();
        while (this != null) {
            if (this.handleKeySet().contains(str)) {
                return true;
            }
            this = this.parent;
        }
        return false;
    }

    public Set<String> keySet() {
        HashSet hashSet = new HashSet();
        while (this != null) {
            hashSet.addAll(this.handleKeySet());
            this = this.parent;
        }
        return hashSet;
    }

    /* access modifiers changed from: protected */
    public Set<String> handleKeySet() {
        if (this.keySet == null) {
            synchronized (this) {
                if (this.keySet == null) {
                    HashSet hashSet = new HashSet();
                    Enumeration<String> keys = getKeys();
                    while (keys.hasMoreElements()) {
                        String nextElement = keys.nextElement();
                        if (handleGetObject(nextElement) != null) {
                            hashSet.add(nextElement);
                        }
                    }
                    this.keySet = hashSet;
                }
            }
        }
        return this.keySet;
    }

    public static class Control {
        private static final CandidateListCache CANDIDATES_CACHE = new CandidateListCache();
        public static final List<String> FORMAT_CLASS = Collections.unmodifiableList(Arrays.asList("java.class"));
        public static final List<String> FORMAT_DEFAULT = Collections.unmodifiableList(Arrays.asList("java.class", "java.properties"));
        public static final List<String> FORMAT_PROPERTIES = Collections.unmodifiableList(Arrays.asList("java.properties"));
        /* access modifiers changed from: private */
        public static final Control INSTANCE = new Control();
        public static final long TTL_DONT_CACHE = -1;
        public static final long TTL_NO_EXPIRATION_CONTROL = -2;

        protected Control() {
        }

        public static final Control getControl(List<String> list) {
            if (list.equals(FORMAT_PROPERTIES)) {
                return SingleFormatControl.PROPERTIES_ONLY;
            }
            if (list.equals(FORMAT_CLASS)) {
                return SingleFormatControl.CLASS_ONLY;
            }
            if (list.equals(FORMAT_DEFAULT)) {
                return INSTANCE;
            }
            throw new IllegalArgumentException();
        }

        public static final Control getNoFallbackControl(List<String> list) {
            if (list.equals(FORMAT_DEFAULT)) {
                return NoFallbackControl.NO_FALLBACK;
            }
            if (list.equals(FORMAT_PROPERTIES)) {
                return NoFallbackControl.PROPERTIES_ONLY_NO_FALLBACK;
            }
            if (list.equals(FORMAT_CLASS)) {
                return NoFallbackControl.CLASS_ONLY_NO_FALLBACK;
            }
            throw new IllegalArgumentException();
        }

        public List<String> getFormats(String str) {
            str.getClass();
            return FORMAT_DEFAULT;
        }

        public List<Locale> getCandidateLocales(String str, Locale locale) {
            str.getClass();
            return new ArrayList((Collection) CANDIDATES_CACHE.get(locale.getBaseLocale()));
        }

        private static class CandidateListCache extends LocaleObjectCache<BaseLocale, List<Locale>> {
            private CandidateListCache() {
            }

            /* access modifiers changed from: protected */
            /* JADX WARNING: Can't fix incorrect switch cases order */
            /* JADX WARNING: Code restructure failed: missing block: B:34:0x009b, code lost:
                if (r1.equals("HK") == false) goto L_0x0074;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.util.List<java.util.Locale> createObject(sun.util.locale.BaseLocale r13) {
                /*
                    r12 = this;
                    java.lang.String r12 = r13.getLanguage()
                    java.lang.String r0 = r13.getScript()
                    java.lang.String r1 = r13.getRegion()
                    java.lang.String r13 = r13.getVariant()
                    java.lang.String r2 = "no"
                    boolean r3 = r12.equals(r2)
                    java.lang.String r4 = "NY"
                    java.lang.String r5 = "NO"
                    java.lang.String r6 = ""
                    r7 = 1
                    r8 = 0
                    if (r3 == 0) goto L_0x0033
                    boolean r3 = r1.equals(r5)
                    if (r3 == 0) goto L_0x0030
                    boolean r3 = r13.equals(r4)
                    if (r3 == 0) goto L_0x0030
                    r13 = r6
                    r9 = r7
                    r3 = r8
                    goto L_0x0035
                L_0x0030:
                    r3 = r7
                    r9 = r8
                    goto L_0x0035
                L_0x0033:
                    r3 = r8
                    r9 = r3
                L_0x0035:
                    java.lang.String r10 = "nb"
                    boolean r11 = r12.equals(r10)
                    if (r11 != 0) goto L_0x00f5
                    if (r3 == 0) goto L_0x0041
                    goto L_0x00f5
                L_0x0041:
                    java.lang.String r3 = "nn"
                    boolean r10 = r12.equals(r3)
                    if (r10 != 0) goto L_0x00d2
                    if (r9 == 0) goto L_0x004d
                    goto L_0x00d2
                L_0x004d:
                    java.lang.String r2 = "zh"
                    boolean r2 = r12.equals(r2)
                    if (r2 == 0) goto L_0x00cd
                    int r2 = r0.length()
                    java.lang.String r3 = "Hant"
                    java.lang.String r4 = "Hans"
                    java.lang.String r5 = "TW"
                    java.lang.String r6 = "CN"
                    if (r2 != 0) goto L_0x00ae
                    int r2 = r1.length()
                    if (r2 <= 0) goto L_0x00ae
                    r1.hashCode()
                    int r2 = r1.hashCode()
                    r9 = -1
                    switch(r2) {
                        case 2155: goto L_0x009e;
                        case 2307: goto L_0x0095;
                        case 2466: goto L_0x008a;
                        case 2644: goto L_0x007f;
                        case 2691: goto L_0x0076;
                        default: goto L_0x0074;
                    }
                L_0x0074:
                    r7 = r9
                    goto L_0x00a6
                L_0x0076:
                    boolean r2 = r1.equals(r5)
                    if (r2 != 0) goto L_0x007d
                    goto L_0x0074
                L_0x007d:
                    r7 = 4
                    goto L_0x00a6
                L_0x007f:
                    java.lang.String r2 = "SG"
                    boolean r2 = r1.equals(r2)
                    if (r2 != 0) goto L_0x0088
                    goto L_0x0074
                L_0x0088:
                    r7 = 3
                    goto L_0x00a6
                L_0x008a:
                    java.lang.String r2 = "MO"
                    boolean r2 = r1.equals(r2)
                    if (r2 != 0) goto L_0x0093
                    goto L_0x0074
                L_0x0093:
                    r7 = 2
                    goto L_0x00a6
                L_0x0095:
                    java.lang.String r2 = "HK"
                    boolean r2 = r1.equals(r2)
                    if (r2 != 0) goto L_0x00a6
                    goto L_0x0074
                L_0x009e:
                    boolean r2 = r1.equals(r6)
                    if (r2 != 0) goto L_0x00a5
                    goto L_0x0074
                L_0x00a5:
                    r7 = r8
                L_0x00a6:
                    switch(r7) {
                        case 0: goto L_0x00ac;
                        case 1: goto L_0x00aa;
                        case 2: goto L_0x00aa;
                        case 3: goto L_0x00ac;
                        case 4: goto L_0x00aa;
                        default: goto L_0x00a9;
                    }
                L_0x00a9:
                    goto L_0x00cd
                L_0x00aa:
                    r0 = r3
                    goto L_0x00cd
                L_0x00ac:
                    r0 = r4
                    goto L_0x00cd
                L_0x00ae:
                    int r2 = r0.length()
                    if (r2 <= 0) goto L_0x00cd
                    int r2 = r1.length()
                    if (r2 != 0) goto L_0x00cd
                    r0.hashCode()
                    boolean r2 = r0.equals(r4)
                    if (r2 != 0) goto L_0x00cc
                    boolean r2 = r0.equals(r3)
                    if (r2 != 0) goto L_0x00ca
                    goto L_0x00cd
                L_0x00ca:
                    r1 = r5
                    goto L_0x00cd
                L_0x00cc:
                    r1 = r6
                L_0x00cd:
                    java.util.List r12 = getDefaultList(r12, r0, r1, r13)
                    return r12
                L_0x00d2:
                    java.util.List r12 = getDefaultList(r3, r0, r1, r13)
                    int r13 = r12.size()
                    int r13 = r13 - r7
                    int r0 = r13 + 1
                    java.util.Locale r1 = java.util.Locale.getInstance(r2, r5, r4)
                    r12.add(r13, r1)
                    int r13 = r0 + 1
                    java.util.Locale r1 = java.util.Locale.getInstance(r2, r5, r6)
                    r12.add(r0, r1)
                    java.util.Locale r0 = java.util.Locale.getInstance(r2, r6, r6)
                    r12.add(r13, r0)
                    return r12
                L_0x00f5:
                    java.util.List r12 = getDefaultList(r10, r0, r1, r13)
                    java.util.LinkedList r13 = new java.util.LinkedList
                    r13.<init>()
                    java.util.Iterator r12 = r12.iterator()
                L_0x0102:
                    boolean r0 = r12.hasNext()
                    if (r0 == 0) goto L_0x0131
                    java.lang.Object r0 = r12.next()
                    java.util.Locale r0 = (java.util.Locale) r0
                    r13.add(r0)
                    java.lang.String r1 = r0.getLanguage()
                    int r1 = r1.length()
                    if (r1 != 0) goto L_0x011c
                    goto L_0x0131
                L_0x011c:
                    java.lang.String r1 = r0.getScript()
                    java.lang.String r3 = r0.getCountry()
                    java.lang.String r0 = r0.getVariant()
                    r4 = 0
                    java.util.Locale r0 = java.util.Locale.getInstance(r2, r1, r3, r0, r4)
                    r13.add(r0)
                    goto L_0x0102
                L_0x0131:
                    return r13
                */
                throw new UnsupportedOperationException("Method not decompiled: java.util.ResourceBundle.Control.CandidateListCache.createObject(sun.util.locale.BaseLocale):java.util.List");
            }

            private static List<Locale> getDefaultList(String str, String str2, String str3, String str4) {
                LinkedList<String> linkedList;
                if (str4.length() > 0) {
                    linkedList = new LinkedList<>();
                    int length = str4.length();
                    while (length != -1) {
                        linkedList.add(str4.substring(0, length));
                        length = str4.lastIndexOf(95, length - 1);
                    }
                } else {
                    linkedList = null;
                }
                LinkedList linkedList2 = new LinkedList();
                if (linkedList != null) {
                    for (String instance : linkedList) {
                        linkedList2.add(Locale.getInstance(str, str2, str3, instance, (LocaleExtensions) null));
                    }
                }
                if (str3.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, str2, str3, "", (LocaleExtensions) null));
                }
                if (str2.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, str2, "", "", (LocaleExtensions) null));
                    if (linkedList != null) {
                        for (String instance2 : linkedList) {
                            linkedList2.add(Locale.getInstance(str, "", str3, instance2, (LocaleExtensions) null));
                        }
                    }
                    if (str3.length() > 0) {
                        linkedList2.add(Locale.getInstance(str, "", str3, "", (LocaleExtensions) null));
                    }
                }
                if (str.length() > 0) {
                    linkedList2.add(Locale.getInstance(str, "", "", "", (LocaleExtensions) null));
                }
                linkedList2.add(Locale.ROOT);
                return linkedList2;
            }
        }

        public Locale getFallbackLocale(String str, Locale locale) {
            str.getClass();
            Locale locale2 = Locale.getDefault();
            if (locale.equals(locale2)) {
                return null;
            }
            return locale2;
        }

        public ResourceBundle newBundle(String str, Locale locale, String str2, final ClassLoader classLoader, final boolean z) throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(str, locale);
            if (str2.equals("java.class")) {
                try {
                    Class<?> loadClass = classLoader.loadClass(bundleName);
                    if (ResourceBundle.class.isAssignableFrom(loadClass)) {
                        return (ResourceBundle) loadClass.newInstance();
                    }
                    throw new ClassCastException(loadClass.getName() + " cannot be cast to ResourceBundle");
                } catch (ClassNotFoundException unused) {
                    return null;
                }
            } else if (str2.equals("java.properties")) {
                final String resourceName0 = toResourceName0(bundleName, "properties");
                if (resourceName0 == null) {
                    return null;
                }
                try {
                    InputStream inputStream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                        public InputStream run() throws IOException {
                            URLConnection openConnection;
                            if (!z) {
                                return classLoader.getResourceAsStream(resourceName0);
                            }
                            URL resource = classLoader.getResource(resourceName0);
                            if (resource == null || (openConnection = resource.openConnection()) == null) {
                                return null;
                            }
                            openConnection.setUseCaches(false);
                            return openConnection.getInputStream();
                        }
                    });
                    if (inputStream == null) {
                        return null;
                    }
                    try {
                        return new PropertyResourceBundle((Reader) new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    } finally {
                        inputStream.close();
                    }
                } catch (PrivilegedActionException e) {
                    throw ((IOException) e.getException());
                }
            } else {
                throw new IllegalArgumentException("unknown format: " + str2);
            }
        }

        public long getTimeToLive(String str, Locale locale) {
            if (str != null && locale != null) {
                return -2;
            }
            throw null;
        }

        public boolean needsReload(String str, Locale locale, String str2, ClassLoader classLoader, ResourceBundle resourceBundle, long j) {
            URL resource;
            resourceBundle.getClass();
            if (str2.equals("java.class") || str2.equals("java.properties")) {
                str2 = str2.substring(5);
            }
            try {
                String resourceName0 = toResourceName0(toBundleName(str, locale), str2);
                if (resourceName0 == null || (resource = classLoader.getResource(resourceName0)) == null) {
                    return false;
                }
                URLConnection openConnection = resource.openConnection();
                long j2 = 0;
                if (openConnection != null) {
                    openConnection.setUseCaches(false);
                    if (openConnection instanceof JarURLConnection) {
                        JarEntry jarEntry = ((JarURLConnection) openConnection).getJarEntry();
                        if (jarEntry != null) {
                            long time = jarEntry.getTime();
                            if (time != -1) {
                                j2 = time;
                            }
                        }
                    } else {
                        j2 = openConnection.getLastModified();
                    }
                }
                if (j2 >= j) {
                    return true;
                }
                return false;
            } catch (NullPointerException e) {
                throw e;
            } catch (Exception unused) {
                return false;
            }
        }

        public String toBundleName(String str, Locale locale) {
            if (locale == Locale.ROOT) {
                return str;
            }
            String language = locale.getLanguage();
            String script = locale.getScript();
            String country = locale.getCountry();
            String variant = locale.getVariant();
            if (language == "" && country == "" && variant == "") {
                return str;
            }
            StringBuilder sb = new StringBuilder(str);
            sb.append('_');
            if (script != "") {
                if (variant != "") {
                    sb.append(language);
                    sb.append('_');
                    sb.append(script);
                    sb.append('_');
                    sb.append(country);
                    sb.append('_');
                    sb.append(variant);
                } else if (country != "") {
                    sb.append(language);
                    sb.append('_');
                    sb.append(script);
                    sb.append('_');
                    sb.append(country);
                } else {
                    sb.append(language);
                    sb.append('_');
                    sb.append(script);
                }
            } else if (variant != "") {
                sb.append(language);
                sb.append('_');
                sb.append(country);
                sb.append('_');
                sb.append(variant);
            } else if (country != "") {
                sb.append(language);
                sb.append('_');
                sb.append(country);
            } else {
                sb.append(language);
            }
            return sb.toString();
        }

        public final String toResourceName(String str, String str2) {
            StringBuilder sb = new StringBuilder(str.length() + 1 + str2.length());
            sb.append(str.replace('.', '/'));
            sb.append('.');
            sb.append(str2);
            return sb.toString();
        }

        private String toResourceName0(String str, String str2) {
            if (str.contains("://")) {
                return null;
            }
            return toResourceName(str, str2);
        }
    }

    private static class SingleFormatControl extends Control {
        /* access modifiers changed from: private */
        public static final Control CLASS_ONLY = new SingleFormatControl(FORMAT_CLASS);
        /* access modifiers changed from: private */
        public static final Control PROPERTIES_ONLY = new SingleFormatControl(FORMAT_PROPERTIES);
        private final List<String> formats;

        protected SingleFormatControl(List<String> list) {
            this.formats = list;
        }

        public List<String> getFormats(String str) {
            str.getClass();
            return this.formats;
        }
    }

    private static final class NoFallbackControl extends SingleFormatControl {
        /* access modifiers changed from: private */
        public static final Control CLASS_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_CLASS);
        /* access modifiers changed from: private */
        public static final Control NO_FALLBACK = new NoFallbackControl(FORMAT_DEFAULT);
        /* access modifiers changed from: private */
        public static final Control PROPERTIES_ONLY_NO_FALLBACK = new NoFallbackControl(FORMAT_PROPERTIES);

        protected NoFallbackControl(List<String> list) {
            super(list);
        }

        public Locale getFallbackLocale(String str, Locale locale) {
            if (str != null && locale != null) {
                return null;
            }
            throw null;
        }
    }
}
