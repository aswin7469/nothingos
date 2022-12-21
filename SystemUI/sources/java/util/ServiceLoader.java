package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.URL;
import java.p026io.BufferedReader;
import java.p026io.IOException;
import java.util.Map;

public final class ServiceLoader<S> implements Iterable<S> {
    private static final String PREFIX = "META-INF/services/";
    private final ClassLoader loader;
    /* access modifiers changed from: private */
    public ServiceLoader<S>.LazyIterator lookupIterator;
    /* access modifiers changed from: private */
    public LinkedHashMap<String, S> providers = new LinkedHashMap<>();
    private final Class<S> service;

    public void reload() {
        this.providers.clear();
        this.lookupIterator = new LazyIterator(this.service, this.loader);
    }

    private ServiceLoader(Class<S> cls, ClassLoader classLoader) {
        this.service = (Class) Objects.requireNonNull(cls, "Service interface cannot be null");
        this.loader = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
        reload();
    }

    /* access modifiers changed from: private */
    public static void fail(Class<?> cls, String str, Throwable th) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(cls.getName() + ": " + str, th);
    }

    private static void fail(Class<?> cls, String str) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(cls.getName() + ": " + str);
    }

    private static void fail(Class<?> cls, URL url, int i, String str) throws ServiceConfigurationError {
        fail(cls, url + ":" + i + ": " + str);
    }

    private int parseLine(Class<?> cls, URL url, BufferedReader bufferedReader, int i, List<String> list) throws IOException, ServiceConfigurationError {
        String readLine = bufferedReader.readLine();
        if (readLine == null) {
            return -1;
        }
        int indexOf = readLine.indexOf(35);
        if (indexOf >= 0) {
            readLine = readLine.substring(0, indexOf);
        }
        String trim = readLine.trim();
        int length = trim.length();
        if (length != 0) {
            if (trim.indexOf(32) >= 0 || trim.indexOf(9) >= 0) {
                fail(cls, url, i, "Illegal configuration-file syntax");
            }
            int codePointAt = trim.codePointAt(0);
            if (!Character.isJavaIdentifierStart(codePointAt)) {
                fail(cls, url, i, "Illegal provider-class name: " + trim);
            }
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = trim.codePointAt(charCount);
                if (!Character.isJavaIdentifierPart(codePointAt2) && codePointAt2 != 46) {
                    fail(cls, url, i, "Illegal provider-class name: " + trim);
                }
                charCount += Character.charCount(codePointAt2);
            }
            if (!this.providers.containsKey(trim) && !list.contains(trim)) {
                list.add(trim);
            }
        }
        return i + 1;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0045 A[SYNTHETIC, Splitter:B:28:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x004d A[Catch:{ IOException -> 0x0049 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x005b A[SYNTHETIC, Splitter:B:38:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0063 A[Catch:{ IOException -> 0x005f }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Iterator<java.lang.String> parse(java.lang.Class<?> r11, java.net.URL r12) throws java.util.ServiceConfigurationError {
        /*
            r10 = this;
            java.lang.String r0 = "Error closing configuration file"
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r1 = 0
            java.io.InputStream r8 = r12.openStream()     // Catch:{ IOException -> 0x003c, all -> 0x0039 }
            java.io.BufferedReader r9 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0037 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0037 }
            java.lang.String r3 = "utf-8"
            r2.<init>((java.p026io.InputStream) r8, (java.lang.String) r3)     // Catch:{ IOException -> 0x0037 }
            r9.<init>(r2)     // Catch:{ IOException -> 0x0037 }
            r1 = 1
            r5 = r1
        L_0x001a:
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r9
            r6 = r7
            int r5 = r1.parseLine(r2, r3, r4, r5, r6)     // Catch:{ IOException -> 0x0032, all -> 0x002f }
            if (r5 < 0) goto L_0x0026
            goto L_0x001a
        L_0x0026:
            r9.close()     // Catch:{ IOException -> 0x0049 }
            if (r8 == 0) goto L_0x0054
            r8.close()     // Catch:{ IOException -> 0x0049 }
            goto L_0x0054
        L_0x002f:
            r10 = move-exception
            r1 = r9
            goto L_0x0059
        L_0x0032:
            r10 = move-exception
            r1 = r9
            goto L_0x003e
        L_0x0035:
            r10 = move-exception
            goto L_0x0059
        L_0x0037:
            r10 = move-exception
            goto L_0x003e
        L_0x0039:
            r10 = move-exception
            r8 = r1
            goto L_0x0059
        L_0x003c:
            r10 = move-exception
            r8 = r1
        L_0x003e:
            java.lang.String r12 = "Error reading configuration file"
            fail(r11, r12, r10)     // Catch:{ all -> 0x0035 }
            if (r1 == 0) goto L_0x004b
            r1.close()     // Catch:{ IOException -> 0x0049 }
            goto L_0x004b
        L_0x0049:
            r10 = move-exception
            goto L_0x0051
        L_0x004b:
            if (r8 == 0) goto L_0x0054
            r8.close()     // Catch:{ IOException -> 0x0049 }
            goto L_0x0054
        L_0x0051:
            fail(r11, r0, r10)
        L_0x0054:
            java.util.Iterator r10 = r7.iterator()
            return r10
        L_0x0059:
            if (r1 == 0) goto L_0x0061
            r1.close()     // Catch:{ IOException -> 0x005f }
            goto L_0x0061
        L_0x005f:
            r12 = move-exception
            goto L_0x0067
        L_0x0061:
            if (r8 == 0) goto L_0x006a
            r8.close()     // Catch:{ IOException -> 0x005f }
            goto L_0x006a
        L_0x0067:
            fail(r11, r0, r12)
        L_0x006a:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.ServiceLoader.parse(java.lang.Class, java.net.URL):java.util.Iterator");
    }

    private class LazyIterator implements Iterator<S> {
        Enumeration<URL> configs;
        ClassLoader loader;
        String nextName;
        Iterator<String> pending;
        Class<S> service;

        private LazyIterator(Class<S> cls, ClassLoader classLoader) {
            this.configs = null;
            this.pending = null;
            this.nextName = null;
            this.service = cls;
            this.loader = classLoader;
        }

        private boolean hasNextService() {
            if (this.nextName != null) {
                return true;
            }
            if (this.configs == null) {
                try {
                    String str = ServiceLoader.PREFIX + this.service.getName();
                    ClassLoader classLoader = this.loader;
                    if (classLoader == null) {
                        this.configs = ClassLoader.getSystemResources(str);
                    } else {
                        this.configs = classLoader.getResources(str);
                    }
                } catch (IOException e) {
                    ServiceLoader.fail(this.service, "Error locating configuration files", e);
                }
            }
            while (true) {
                Iterator<String> it = this.pending;
                if (it != null && it.hasNext()) {
                    this.nextName = this.pending.next();
                    return true;
                } else if (!this.configs.hasMoreElements()) {
                    return false;
                } else {
                    this.pending = ServiceLoader.this.parse(this.service, this.configs.nextElement());
                }
            }
        }

        private S nextService() {
            if (hasNextService()) {
                String str = this.nextName;
                Class<?> cls = null;
                this.nextName = null;
                try {
                    cls = Class.forName(str, false, this.loader);
                } catch (ClassNotFoundException e) {
                    Class<S> cls2 = this.service;
                    ServiceLoader.fail(cls2, "Provider " + str + " not found", e);
                }
                if (!this.service.isAssignableFrom(cls)) {
                    ClassCastException classCastException = new ClassCastException(this.service.getCanonicalName() + " is not assignable from " + cls.getCanonicalName());
                    Class<S> cls3 = this.service;
                    ServiceLoader.fail(cls3, "Provider " + str + " not a subtype", classCastException);
                }
                try {
                    S cast = this.service.cast(cls.newInstance());
                    ServiceLoader.this.providers.put(str, cast);
                    return cast;
                } catch (Throwable th) {
                    Class<S> cls4 = this.service;
                    ServiceLoader.fail(cls4, "Provider " + str + " could not be instantiated", th);
                    throw new Error();
                }
            } else {
                throw new NoSuchElementException();
            }
        }

        public boolean hasNext() {
            return hasNextService();
        }

        public S next() {
            return nextService();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<S> iterator() {
        return new Iterator<S>() {
            Iterator<Map.Entry<String, S>> knownProviders;

            {
                this.knownProviders = ServiceLoader.this.providers.entrySet().iterator();
            }

            public boolean hasNext() {
                if (this.knownProviders.hasNext()) {
                    return true;
                }
                return ServiceLoader.this.lookupIterator.hasNext();
            }

            public S next() {
                if (this.knownProviders.hasNext()) {
                    return this.knownProviders.next().getValue();
                }
                return ServiceLoader.this.lookupIterator.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <S> ServiceLoader<S> load(Class<S> cls, ClassLoader classLoader) {
        return new ServiceLoader<>(cls, classLoader);
    }

    public static <S> ServiceLoader<S> load(Class<S> cls) {
        return load(cls, Thread.currentThread().getContextClassLoader());
    }

    public static <S> ServiceLoader<S> loadInstalled(Class<S> cls) {
        ClassLoader classLoader = null;
        for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
            classLoader = systemClassLoader;
        }
        return load(cls, classLoader);
    }

    public static <S> S loadFromSystemProperty(Class<S> cls) {
        try {
            String property = System.getProperty(cls.getName());
            if (property != null) {
                return ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
            }
            return null;
        } catch (Exception e) {
            throw new Error((Throwable) e);
        }
    }

    public String toString() {
        return "java.util.ServiceLoader[" + this.service.getName() + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
