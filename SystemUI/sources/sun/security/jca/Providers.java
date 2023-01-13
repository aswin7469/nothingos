package sun.security.jca;

import com.android.launcher3.icons.cache.BaseIconCache;
import dalvik.system.VMRuntime;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Providers {
    private static final String BACKUP_PROVIDER_CLASSNAME = "sun.security.provider.VerificationProvider";
    public static final int DEFAULT_MAXIMUM_ALLOWABLE_TARGET_API_LEVEL_FOR_BC_DEPRECATION = 27;
    private static final Set<String> DEPRECATED_ALGORITHMS;
    private static volatile Provider SYSTEM_BOUNCY_CASTLE_PROVIDER = providerList.getProvider("BC");
    private static final String[] jarVerificationProviders = {"com.android.org.conscrypt.OpenSSLProvider", "com.android.org.bouncycastle.jce.provider.BouncyCastleProvider", "com.android.org.conscrypt.JSSEProvider", BACKUP_PROVIDER_CLASSNAME};
    private static int maximumAllowableApiLevelForBcDeprecation = 27;
    private static volatile ProviderList providerList;
    private static final ThreadLocal<ProviderList> threadLists = new InheritableThreadLocal();
    private static volatile int threadListsUsed;

    static {
        providerList = ProviderList.EMPTY;
        providerList = ProviderList.fromSecurityProperties();
        int size = providerList.size();
        providerList = providerList.removeInvalid();
        if (size == providerList.size()) {
            HashSet hashSet = new HashSet();
            DEPRECATED_ALGORITHMS = hashSet;
            hashSet.addAll(Arrays.asList("KEYFACTORY.RSA"));
            return;
        }
        throw new AssertionError((Object) "Unable to configure default providers");
    }

    private Providers() {
    }

    public static Provider getSunProvider() {
        try {
            return (Provider) Class.forName(jarVerificationProviders[0]).newInstance();
        } catch (Exception e) {
            try {
                return (Provider) Class.forName(BACKUP_PROVIDER_CLASSNAME).newInstance();
            } catch (Exception unused) {
                throw new RuntimeException("Sun provider not found", e);
            }
        }
    }

    public static Object startJarVerification() {
        return beginThreadProviderList(getProviderList().getJarList(jarVerificationProviders));
    }

    public static void stopJarVerification(Object obj) {
        endThreadProviderList((ProviderList) obj);
    }

    public static ProviderList getProviderList() {
        ProviderList threadProviderList = getThreadProviderList();
        return threadProviderList == null ? getSystemProviderList() : threadProviderList;
    }

    public static void setProviderList(ProviderList providerList2) {
        if (getThreadProviderList() == null) {
            setSystemProviderList(providerList2);
        } else {
            changeThreadProviderList(providerList2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
        r0 = getSystemProviderList();
        r1 = r0.removeInvalid();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r1 == r0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        setSystemProviderList(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static sun.security.jca.ProviderList getFullProviderList() {
        /*
            java.lang.Class<sun.security.jca.Providers> r0 = sun.security.jca.Providers.class
            monitor-enter(r0)
            sun.security.jca.ProviderList r1 = getThreadProviderList()     // Catch:{ all -> 0x0025 }
            if (r1 == 0) goto L_0x0015
            sun.security.jca.ProviderList r2 = r1.removeInvalid()     // Catch:{ all -> 0x0025 }
            if (r2 == r1) goto L_0x0013
            changeThreadProviderList(r2)     // Catch:{ all -> 0x0025 }
            r1 = r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x0025 }
            return r1
        L_0x0015:
            monitor-exit(r0)     // Catch:{ all -> 0x0025 }
            sun.security.jca.ProviderList r0 = getSystemProviderList()
            sun.security.jca.ProviderList r1 = r0.removeInvalid()
            if (r1 == r0) goto L_0x0024
            setSystemProviderList(r1)
            r0 = r1
        L_0x0024:
            return r0
        L_0x0025:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0025 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.jca.Providers.getFullProviderList():sun.security.jca.ProviderList");
    }

    private static ProviderList getSystemProviderList() {
        return providerList;
    }

    private static void setSystemProviderList(ProviderList providerList2) {
        providerList = providerList2;
    }

    public static ProviderList getThreadProviderList() {
        if (threadListsUsed == 0) {
            return null;
        }
        return threadLists.get();
    }

    private static void changeThreadProviderList(ProviderList providerList2) {
        threadLists.set(providerList2);
    }

    public static synchronized ProviderList beginThreadProviderList(ProviderList providerList2) {
        ProviderList providerList3;
        synchronized (Providers.class) {
            if (ProviderList.debug != null) {
                ProviderList.debug.println("ThreadLocal providers: " + providerList2);
            }
            ThreadLocal<ProviderList> threadLocal = threadLists;
            providerList3 = threadLocal.get();
            threadListsUsed++;
            threadLocal.set(providerList2);
        }
        return providerList3;
    }

    public static synchronized void endThreadProviderList(ProviderList providerList2) {
        synchronized (Providers.class) {
            if (providerList2 == null) {
                if (ProviderList.debug != null) {
                    ProviderList.debug.println("Disabling ThreadLocal providers");
                }
                threadLists.remove();
            } else {
                if (ProviderList.debug != null) {
                    ProviderList.debug.println("Restoring previous ThreadLocal providers: " + providerList2);
                }
                threadLists.set(providerList2);
            }
            threadListsUsed--;
        }
    }

    public static void setMaximumAllowableApiLevelForBcDeprecation(int i) {
        maximumAllowableApiLevelForBcDeprecation = i;
    }

    public static int getMaximumAllowableApiLevelForBcDeprecation() {
        return maximumAllowableApiLevelForBcDeprecation;
    }

    public static synchronized void checkBouncyCastleDeprecation(String str, String str2, String str3) throws NoSuchAlgorithmException {
        synchronized (Providers.class) {
            if ("BC".equals(str) && providerList.getProvider(str) == SYSTEM_BOUNCY_CASTLE_PROVIDER) {
                checkBouncyCastleDeprecation(str2, str3);
            }
        }
    }

    public static synchronized void checkBouncyCastleDeprecation(Provider provider, String str, String str2) throws NoSuchAlgorithmException {
        synchronized (Providers.class) {
            if (provider == SYSTEM_BOUNCY_CASTLE_PROVIDER) {
                checkBouncyCastleDeprecation(str, str2);
            }
        }
    }

    private static void checkBouncyCastleDeprecation(String str, String str2) throws NoSuchAlgorithmException {
        String str3 = str + BaseIconCache.EMPTY_CLASS_NAME + str2;
        if (!DEPRECATED_ALGORITHMS.contains(str3.toUpperCase(Locale.f698US))) {
            return;
        }
        if (VMRuntime.getRuntime().getTargetSdkVersion() <= maximumAllowableApiLevelForBcDeprecation) {
            System.logE(" ******** DEPRECATED FUNCTIONALITY ********");
            System.logE(" * The implementation of the " + str3 + " algorithm from");
            System.logE(" * the BC provider is deprecated in this version of Android.");
            System.logE(" * It will be removed in a future version of Android and your");
            System.logE(" * application will no longer be able to request it.  Please see");
            System.logE(" * https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html");
            System.logE(" * for more details.");
            return;
        }
        throw new NoSuchAlgorithmException("The BC provider no longer provides an implementation for " + str3 + ".  Please see https://android-developers.googleblog.com/2018/03/cryptography-changes-in-android-p.html for more details.");
    }
}
