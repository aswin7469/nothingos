package java.security;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.jca.GetInstance;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;

public final class Security {
    private static final Properties props;
    private static final Map<String, Class<?>> spiMap = new ConcurrentHashMap();
    private static final AtomicInteger version = new AtomicInteger();

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2 != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r2 != null) goto L_0x002c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0050 A[SYNTHETIC, Splitter:B:28:0x0050] */
    static {
        /*
            java.util.concurrent.atomic.AtomicInteger r0 = new java.util.concurrent.atomic.AtomicInteger
            r0.<init>()
            version = r0
            java.util.Properties r0 = new java.util.Properties
            r0.<init>()
            props = r0
            r1 = 0
            r2 = 0
            java.lang.Class<java.security.Security> r3 = java.security.Security.class
            java.lang.String r4 = "security.properties"
            java.io.InputStream r3 = r3.getResourceAsStream(r4)     // Catch:{ IOException -> 0x0038 }
            if (r3 != 0) goto L_0x0020
            java.lang.String r0 = "Could not find 'security.properties'."
            java.lang.System.logE(r0)     // Catch:{ IOException -> 0x0038 }
            goto L_0x002a
        L_0x0020:
            java.io.BufferedInputStream r4 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0038 }
            r4.<init>(r3)     // Catch:{ IOException -> 0x0038 }
            r0.load((java.p026io.InputStream) r4)     // Catch:{ IOException -> 0x0033, all -> 0x0030 }
            r1 = 1
            r2 = r4
        L_0x002a:
            if (r2 == 0) goto L_0x0041
        L_0x002c:
            r2.close()     // Catch:{ IOException -> 0x0041 }
            goto L_0x0041
        L_0x0030:
            r0 = move-exception
            r2 = r4
            goto L_0x004e
        L_0x0033:
            r0 = move-exception
            r2 = r4
            goto L_0x0039
        L_0x0036:
            r0 = move-exception
            goto L_0x004e
        L_0x0038:
            r0 = move-exception
        L_0x0039:
            java.lang.String r3 = "Could not load 'security.properties'"
            java.lang.System.logE(r3, r0)     // Catch:{ all -> 0x0036 }
            if (r2 == 0) goto L_0x0041
            goto L_0x002c
        L_0x0041:
            if (r1 != 0) goto L_0x0046
            initializeStatic()
        L_0x0046:
            java.util.concurrent.ConcurrentHashMap r0 = new java.util.concurrent.ConcurrentHashMap
            r0.<init>()
            spiMap = r0
            return
        L_0x004e:
            if (r2 == 0) goto L_0x0053
            r2.close()     // Catch:{ IOException -> 0x0053 }
        L_0x0053:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.Security.<clinit>():void");
    }

    private static class ProviderProperty {
        String className;
        Provider provider;

        private ProviderProperty() {
        }
    }

    private static void initializeStatic() {
        Properties properties = props;
        properties.put("security.provider.1", "com.android.org.conscrypt.OpenSSLProvider");
        properties.put("security.provider.2", "sun.security.provider.CertPathProvider");
        properties.put("security.provider.3", "com.android.org.bouncycastle.jce.provider.BouncyCastleProvider");
        properties.put("security.provider.4", "com.android.org.conscrypt.JSSEProvider");
    }

    private Security() {
    }

    private static ProviderProperty getProviderProperty(String str) {
        List<Provider> providers = Providers.getProviderList().providers();
        for (int i = 0; i < providers.size(); i++) {
            Provider provider = providers.get(i);
            String property = provider.getProperty(str);
            if (property == null) {
                Enumeration<Object> keys = provider.keys();
                while (true) {
                    if (!keys.hasMoreElements() || property != null) {
                        break;
                    }
                    String str2 = (String) keys.nextElement();
                    if (str.equalsIgnoreCase(str2)) {
                        property = provider.getProperty(str2);
                        break;
                    }
                }
            }
            if (property != null) {
                ProviderProperty providerProperty = new ProviderProperty();
                providerProperty.className = property;
                providerProperty.provider = provider;
                return providerProperty;
            }
        }
        return null;
    }

    private static String getProviderProperty(String str, Provider provider) {
        String property = provider.getProperty(str);
        if (property != null) {
            return property;
        }
        Enumeration<Object> keys = provider.keys();
        while (keys.hasMoreElements() && property == null) {
            String str2 = (String) keys.nextElement();
            if (str.equalsIgnoreCase(str2)) {
                return provider.getProperty(str2);
            }
        }
        return property;
    }

    @Deprecated
    public static String getAlgorithmProperty(String str, String str2) {
        ProviderProperty providerProperty = getProviderProperty("Alg." + str2 + BaseIconCache.EMPTY_CLASS_NAME + str);
        if (providerProperty != null) {
            return providerProperty.className;
        }
        return null;
    }

    public static synchronized int insertProviderAt(Provider provider, int i) {
        synchronized (Security.class) {
            String name = provider.getName();
            ProviderList fullProviderList = Providers.getFullProviderList();
            ProviderList insertAt = ProviderList.insertAt(fullProviderList, provider, i - 1);
            if (fullProviderList == insertAt) {
                return -1;
            }
            increaseVersion();
            Providers.setProviderList(insertAt);
            int index = insertAt.getIndex(name) + 1;
            return index;
        }
    }

    public static int addProvider(Provider provider) {
        return insertProviderAt(provider, 0);
    }

    public static synchronized void removeProvider(String str) {
        synchronized (Security.class) {
            Providers.setProviderList(ProviderList.remove(Providers.getFullProviderList(), str));
            increaseVersion();
        }
    }

    public static Provider[] getProviders() {
        return Providers.getFullProviderList().toArray();
    }

    public static Provider getProvider(String str) {
        return Providers.getProviderList().getProvider(str);
    }

    public static Provider[] getProviders(String str) {
        String str2;
        int indexOf = str.indexOf(58);
        if (indexOf == -1) {
            str2 = "";
        } else {
            String substring = str.substring(0, indexOf);
            str2 = str.substring(indexOf + 1);
            str = substring;
        }
        Hashtable hashtable = new Hashtable(1);
        hashtable.put(str, str2);
        return getProviders((Map<String, String>) hashtable);
    }

    public static Provider[] getProviders(Map<String, String> map) {
        int i;
        Provider[] providers = getProviders();
        Set<String> keySet = map.keySet();
        LinkedHashSet<Provider> linkedHashSet = new LinkedHashSet<>(5);
        if (keySet == null || providers == null) {
            return providers;
        }
        Iterator<String> it = keySet.iterator();
        boolean z = true;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String next = it.next();
            LinkedHashSet<Provider> allQualifyingCandidates = getAllQualifyingCandidates(next, map.get(next), providers);
            if (z) {
                linkedHashSet = allQualifyingCandidates;
                z = false;
            }
            if (allQualifyingCandidates == null || allQualifyingCandidates.isEmpty()) {
                linkedHashSet = null;
            } else {
                Iterator<Provider> it2 = linkedHashSet.iterator();
                while (it2.hasNext()) {
                    if (!allQualifyingCandidates.contains(it2.next())) {
                        it2.remove();
                    }
                }
            }
        }
        if (linkedHashSet == null || linkedHashSet.isEmpty()) {
            return null;
        }
        Object[] array = linkedHashSet.toArray();
        int length = array.length;
        Provider[] providerArr = new Provider[length];
        for (i = 0; i < length; i++) {
            providerArr[i] = (Provider) array[i];
        }
        return providerArr;
    }

    private static Class<?> getSpiClass(String str) {
        Map<String, Class<?>> map = spiMap;
        Class<?> cls = map.get(str);
        if (cls != null) {
            return cls;
        }
        try {
            Class<?> cls2 = Class.forName("java.security." + str + "Spi");
            map.put(str, cls2);
            return cls2;
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Spi class not found", e);
        }
    }

    static Object[] getImpl(String str, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str3 == null) {
            return GetInstance.getInstance(str2, getSpiClass(str2), str).toArray();
        }
        return GetInstance.getInstance(str2, getSpiClass(str2), str, str3).toArray();
    }

    static Object[] getImpl(String str, String str2, String str3, Object obj) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        if (str3 == null) {
            return GetInstance.getInstance(str2, getSpiClass(str2), str, obj).toArray();
        }
        return GetInstance.getInstance(str2, getSpiClass(str2), str, obj, str3).toArray();
    }

    static Object[] getImpl(String str, String str2, Provider provider) throws NoSuchAlgorithmException {
        return GetInstance.getInstance(str2, getSpiClass(str2), str, provider).toArray();
    }

    static Object[] getImpl(String str, String str2, Provider provider, Object obj) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return GetInstance.getInstance(str2, getSpiClass(str2), str, obj, provider).toArray();
    }

    public static String getProperty(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("getProperty." + str));
        }
        String property = props.getProperty(str);
        return property != null ? property.trim() : property;
    }

    public static void setProperty(String str, String str2) {
        props.put(str, str2);
        increaseVersion();
        invalidateSMCache(str);
    }

    private static void invalidateSMCache(String str) {
        final boolean equals = str.equals("package.access");
        boolean equals2 = str.equals("package.definition");
        if (equals || equals2) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    Field field;
                    boolean z;
                    try {
                        Class<?> cls = Class.forName("java.lang.SecurityManager", false, (ClassLoader) null);
                        if (equals) {
                            field = cls.getDeclaredField("packageAccessValid");
                            z = field.isAccessible();
                            field.setAccessible(true);
                        } else {
                            field = cls.getDeclaredField("packageDefinitionValid");
                            z = field.isAccessible();
                            field.setAccessible(true);
                        }
                        field.setBoolean(field, false);
                        field.setAccessible(z);
                    } catch (Exception unused) {
                    }
                    return null;
                }
            });
        }
    }

    private static LinkedHashSet<Provider> getAllQualifyingCandidates(String str, String str2, Provider[] providerArr) {
        String[] filterComponents = getFilterComponents(str, str2);
        return getProvidersNotUsingCache(filterComponents[0], filterComponents[1], filterComponents[2], str2, providerArr);
    }

    private static LinkedHashSet<Provider> getProvidersNotUsingCache(String str, String str2, String str3, String str4, Provider[] providerArr) {
        LinkedHashSet<Provider> linkedHashSet = new LinkedHashSet<>(5);
        for (int i = 0; i < providerArr.length; i++) {
            if (isCriterionSatisfied(providerArr[i], str, str2, str3, str4)) {
                linkedHashSet.add(providerArr[i]);
            }
        }
        return linkedHashSet;
    }

    private static boolean isCriterionSatisfied(Provider provider, String str, String str2, String str3, String str4) {
        String str5 = str + '.' + str2;
        if (str3 != null) {
            str5 = str5 + ' ' + str3;
        }
        String providerProperty = getProviderProperty(str5, provider);
        if (providerProperty == null) {
            String providerProperty2 = getProviderProperty("Alg.Alias." + str + BaseIconCache.EMPTY_CLASS_NAME + str2, provider);
            if (providerProperty2 != null) {
                String str6 = str + BaseIconCache.EMPTY_CLASS_NAME + providerProperty2;
                if (str3 != null) {
                    str6 = str6 + ' ' + str3;
                }
                providerProperty = getProviderProperty(str6, provider);
            }
            if (providerProperty == null) {
                return false;
            }
        }
        if (str3 == null) {
            return true;
        }
        if (isStandardAttr(str3)) {
            return isConstraintSatisfied(str3, str4, providerProperty);
        }
        return str4.equalsIgnoreCase(providerProperty);
    }

    private static boolean isStandardAttr(String str) {
        if (!str.equalsIgnoreCase("KeySize") && !str.equalsIgnoreCase("ImplementedIn")) {
            return false;
        }
        return true;
    }

    private static boolean isConstraintSatisfied(String str, String str2, String str3) {
        if (str.equalsIgnoreCase("KeySize")) {
            if (Integer.parseInt(str2) <= Integer.parseInt(str3)) {
                return true;
            }
            return false;
        } else if (str.equalsIgnoreCase("ImplementedIn")) {
            return str2.equalsIgnoreCase(str3);
        } else {
            return false;
        }
    }

    static String[] getFilterComponents(String str, String str2) {
        String str3;
        String str4;
        int indexOf = str.indexOf(46);
        if (indexOf >= 0) {
            String substring = str.substring(0, indexOf);
            if (str2.length() == 0) {
                str4 = str.substring(indexOf + 1).trim();
                if (str4.length() != 0) {
                    str3 = null;
                } else {
                    throw new InvalidParameterException("Invalid filter");
                }
            } else {
                int indexOf2 = str.indexOf(32);
                if (indexOf2 != -1) {
                    String trim = str.substring(indexOf2 + 1).trim();
                    if (trim.length() == 0) {
                        throw new InvalidParameterException("Invalid filter");
                    } else if (indexOf2 < indexOf || indexOf == indexOf2 - 1) {
                        throw new InvalidParameterException("Invalid filter");
                    } else {
                        str4 = str.substring(indexOf + 1, indexOf2);
                        str3 = trim;
                    }
                } else {
                    throw new InvalidParameterException("Invalid filter");
                }
            }
            return new String[]{substring, str4, str3};
        }
        throw new InvalidParameterException("Invalid filter");
    }

    public static Set<String> getAlgorithms(String str) {
        if (str == null || str.length() == 0 || str.endsWith(BaseIconCache.EMPTY_CLASS_NAME)) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        Provider[] providers = getProviders();
        for (Provider keys : providers) {
            Enumeration<Object> keys2 = keys.keys();
            while (keys2.hasMoreElements()) {
                String upperCase = ((String) keys2.nextElement()).toUpperCase(Locale.ENGLISH);
                if (upperCase.startsWith(str.toUpperCase(Locale.ENGLISH)) && upperCase.indexOf(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER) < 0) {
                    hashSet.add(upperCase.substring(str.length() + 1));
                }
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }

    public static void increaseVersion() {
        version.incrementAndGet();
    }

    public static int getVersion() {
        return version.get();
    }
}
