package sun.security.jca;

import java.p026io.File;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.ProviderException;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;

final class ProviderConfig {
    private static final Class[] CL_STRING = {String.class};
    private static final int MAX_LOAD_TRIES = 30;
    private static final String P11_SOL_ARG = "${java.home}/lib/security/sunpkcs11-solaris.cfg";
    private static final String P11_SOL_NAME = "sun.security.pkcs11.SunPKCS11";
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("jca", "ProviderConfig");
    private final String argument;
    /* access modifiers changed from: private */
    public final String className;
    private boolean isLoading;
    private volatile Provider provider;
    private int tries;

    ProviderConfig(String str, String str2) {
        if (str.equals(P11_SOL_NAME) && str2.equals(P11_SOL_ARG)) {
            checkSunPKCS11Solaris();
        }
        this.className = str;
        this.argument = expand(str2);
    }

    ProviderConfig(String str) {
        this(str, "");
    }

    ProviderConfig(Provider provider2) {
        this.className = provider2.getClass().getName();
        this.argument = "";
        this.provider = provider2;
    }

    private void checkSunPKCS11Solaris() {
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                if (!new File("/usr/lib/libpkcs11.so").exists()) {
                    return Boolean.FALSE;
                }
                if ("false".equalsIgnoreCase(System.getProperty("sun.security.pkcs11.enable-solaris"))) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            }
        })) == Boolean.FALSE) {
            this.tries = 30;
        }
    }

    private boolean hasArgument() {
        return this.argument.length() != 0;
    }

    private boolean shouldLoad() {
        return this.tries < 30;
    }

    /* access modifiers changed from: private */
    public void disableLoad() {
        this.tries = 30;
    }

    /* access modifiers changed from: package-private */
    public boolean isLoaded() {
        return this.provider != null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ProviderConfig)) {
            return false;
        }
        ProviderConfig providerConfig = (ProviderConfig) obj;
        if (!this.className.equals(providerConfig.className) || !this.argument.equals(providerConfig.argument)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.className.hashCode() + this.argument.hashCode();
    }

    public String toString() {
        if (!hasArgument()) {
            return this.className;
        }
        return this.className + "('" + this.argument + "')";
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0034, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.security.Provider getProvider() {
        /*
            r4 = this;
            java.lang.String r0 = "Recursion loading provider: "
            monitor-enter(r4)
            java.security.Provider r1 = r4.provider     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r4)
            return r1
        L_0x0009:
            boolean r1 = r4.shouldLoad()     // Catch:{ all -> 0x004c }
            r2 = 0
            if (r1 != 0) goto L_0x0012
            monitor-exit(r4)
            return r2
        L_0x0012:
            boolean r1 = r4.isLoading     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0035
            sun.security.util.Debug r1 = debug     // Catch:{ all -> 0x004c }
            if (r1 == 0) goto L_0x0033
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x004c }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x004c }
            r3.append((java.lang.Object) r4)     // Catch:{ all -> 0x004c }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x004c }
            r1.println(r0)     // Catch:{ all -> 0x004c }
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ all -> 0x004c }
            java.lang.String r1 = "Call trace"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x004c }
            r0.printStackTrace()     // Catch:{ all -> 0x004c }
        L_0x0033:
            monitor-exit(r4)
            return r2
        L_0x0035:
            r0 = 0
            r1 = 1
            r4.isLoading = r1     // Catch:{ all -> 0x0048 }
            int r2 = r4.tries     // Catch:{ all -> 0x0048 }
            int r2 = r2 + r1
            r4.tries = r2     // Catch:{ all -> 0x0048 }
            java.security.Provider r1 = r4.doLoadProvider()     // Catch:{ all -> 0x0048 }
            r4.isLoading = r0     // Catch:{ all -> 0x004c }
            r4.provider = r1     // Catch:{ all -> 0x004c }
            monitor-exit(r4)
            return r1
        L_0x0048:
            r1 = move-exception
            r4.isLoading = r0     // Catch:{ all -> 0x004c }
            throw r1     // Catch:{ all -> 0x004c }
        L_0x004c:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.jca.ProviderConfig.getProvider():java.security.Provider");
    }

    private Provider doLoadProvider() {
        return (Provider) AccessController.doPrivileged(new PrivilegedAction<Provider>() {
            /* JADX WARNING: Code restructure failed: missing block: B:11:0x0040, code lost:
                if ((r0 instanceof java.lang.reflect.InvocationTargetException) != false) goto L_0x0042;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:12:0x0042, code lost:
                r0 = ((java.lang.reflect.InvocationTargetException) r0).getCause();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:14:0x004c, code lost:
                if (sun.security.jca.ProviderConfig.m5649$$Nest$sfgetdebug() != null) goto L_0x004e;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:15:0x004e, code lost:
                r1 = sun.security.jca.ProviderConfig.m5649$$Nest$sfgetdebug();
                r1.println("Error loading provider " + r4.this$0);
                r0.printStackTrace();
             */
            /* JADX WARNING: Code restructure failed: missing block: B:17:0x006a, code lost:
                if ((r0 instanceof java.security.ProviderException) == false) goto L_0x006c;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:19:0x006e, code lost:
                if ((r0 instanceof java.lang.UnsupportedOperationException) != false) goto L_0x0070;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:20:0x0070, code lost:
                sun.security.jca.ProviderConfig.m5647$$Nest$mdisableLoad(r4.this$0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:22:0x0079, code lost:
                throw ((java.security.ProviderException) r0);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
                return null;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
                return null;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
                r0 = r4.this$0;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:8:0x003c, code lost:
                return sun.security.jca.ProviderConfig.m5648$$Nest$minitProvider(r0, sun.security.jca.ProviderConfig.m5646$$Nest$fgetclassName(r0), java.lang.ClassLoader.getSystemClassLoader());
             */
            /* JADX WARNING: Code restructure failed: missing block: B:9:0x003d, code lost:
                r0 = e;
             */
            /* JADX WARNING: Failed to process nested try/catch */
            /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x002e */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.security.Provider run() {
                /*
                    r4 = this;
                    sun.security.util.Debug r0 = sun.security.jca.ProviderConfig.debug
                    if (r0 == 0) goto L_0x001d
                    sun.security.util.Debug r0 = sun.security.jca.ProviderConfig.debug
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder
                    java.lang.String r2 = "Loading provider: "
                    r1.<init>((java.lang.String) r2)
                    sun.security.jca.ProviderConfig r2 = sun.security.jca.ProviderConfig.this
                    r1.append((java.lang.Object) r2)
                    java.lang.String r1 = r1.toString()
                    r0.println(r1)
                L_0x001d:
                    sun.security.jca.ProviderConfig r0 = sun.security.jca.ProviderConfig.this     // Catch:{ Exception -> 0x002e }
                    java.lang.String r1 = r0.className     // Catch:{ Exception -> 0x002e }
                    java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
                    java.lang.ClassLoader r2 = r2.getClassLoader()     // Catch:{ Exception -> 0x002e }
                    java.security.Provider r4 = r0.initProvider(r1, r2)     // Catch:{ Exception -> 0x002e }
                    return r4
                L_0x002e:
                    sun.security.jca.ProviderConfig r0 = sun.security.jca.ProviderConfig.this     // Catch:{ Exception -> 0x003d }
                    java.lang.String r1 = r0.className     // Catch:{ Exception -> 0x003d }
                    java.lang.ClassLoader r2 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ Exception -> 0x003d }
                    java.security.Provider r4 = r0.initProvider(r1, r2)     // Catch:{ Exception -> 0x003d }
                    return r4
                L_0x003d:
                    r0 = move-exception
                    boolean r1 = r0 instanceof java.lang.reflect.InvocationTargetException
                    if (r1 == 0) goto L_0x0048
                    java.lang.reflect.InvocationTargetException r0 = (java.lang.reflect.InvocationTargetException) r0
                    java.lang.Throwable r0 = r0.getCause()
                L_0x0048:
                    sun.security.util.Debug r1 = sun.security.jca.ProviderConfig.debug
                    if (r1 == 0) goto L_0x0068
                    sun.security.util.Debug r1 = sun.security.jca.ProviderConfig.debug
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    java.lang.String r3 = "Error loading provider "
                    r2.<init>((java.lang.String) r3)
                    sun.security.jca.ProviderConfig r3 = sun.security.jca.ProviderConfig.this
                    r2.append((java.lang.Object) r3)
                    java.lang.String r2 = r2.toString()
                    r1.println(r2)
                    r0.printStackTrace()
                L_0x0068:
                    boolean r1 = r0 instanceof java.security.ProviderException
                    if (r1 != 0) goto L_0x0077
                    boolean r0 = r0 instanceof java.lang.UnsupportedOperationException
                    if (r0 == 0) goto L_0x0075
                    sun.security.jca.ProviderConfig r4 = sun.security.jca.ProviderConfig.this
                    r4.disableLoad()
                L_0x0075:
                    r4 = 0
                    return r4
                L_0x0077:
                    java.security.ProviderException r0 = (java.security.ProviderException) r0
                    throw r0
                */
                throw new UnsupportedOperationException("Method not decompiled: sun.security.jca.ProviderConfig.C48142.run():java.security.Provider");
            }
        });
    }

    /* access modifiers changed from: private */
    public Provider initProvider(String str, ClassLoader classLoader) throws Exception {
        Class<?> cls;
        Object obj;
        if (classLoader != null) {
            cls = classLoader.loadClass(str);
        } else {
            cls = Class.forName(str);
        }
        if (!hasArgument()) {
            obj = cls.newInstance();
        } else {
            obj = cls.getConstructor(CL_STRING).newInstance(this.argument);
        }
        if (obj instanceof Provider) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("Loaded provider " + obj);
            }
            return (Provider) obj;
        }
        Debug debug3 = debug;
        if (debug3 != null) {
            debug3.println(str + " is not a provider");
        }
        disableLoad();
        return null;
    }

    private static String expand(final String str) {
        if (!str.contains("${")) {
            return str;
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                try {
                    return PropertyExpander.expand(String.this);
                } catch (GeneralSecurityException e) {
                    throw new ProviderException((Throwable) e);
                }
            }
        });
    }
}
