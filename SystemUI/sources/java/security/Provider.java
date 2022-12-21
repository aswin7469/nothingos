package java.security;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import sun.security.pkcs.PKCS9Attribute;
import sun.security.util.Debug;

public abstract class Provider extends Properties {
    private static final int ALIAS_LENGTH = 10;
    private static final String ALIAS_PREFIX = "Alg.Alias.";
    private static final String ALIAS_PREFIX_LOWER = "alg.alias.";
    private static final Debug debug = Debug.getInstance("provider", "Provider");
    /* access modifiers changed from: private */
    public static final Map<String, EngineDescription> knownEngines = new HashMap();
    private static volatile ServiceKey previousKey = new ServiceKey("", "", false);
    static final long serialVersionUID = -4298000515446427739L;
    private transient Set<Map.Entry<Object, Object>> entrySet = null;
    private transient int entrySetCallCount = 0;
    private String info;
    private transient boolean initialized;
    private transient boolean legacyChanged;
    private transient Map<ServiceKey, Service> legacyMap;
    private transient Map<String, String> legacyStrings;
    private String name;
    private volatile boolean registered = false;
    private transient Map<ServiceKey, Service> serviceMap;
    private transient Set<Service> serviceSet;
    private transient boolean servicesChanged;
    private double version;

    static {
        addEngine("AlgorithmParameterGenerator", false, (String) null);
        addEngine("AlgorithmParameters", false, (String) null);
        addEngine("KeyFactory", false, (String) null);
        addEngine("KeyPairGenerator", false, (String) null);
        addEngine("KeyStore", false, (String) null);
        addEngine(PKCS9Attribute.MESSAGE_DIGEST_STR, false, (String) null);
        addEngine("SecureRandom", false, (String) null);
        addEngine("Signature", true, (String) null);
        addEngine("CertificateFactory", false, (String) null);
        addEngine("CertPathBuilder", false, (String) null);
        addEngine("CertPathValidator", false, (String) null);
        addEngine("CertStore", false, "java.security.cert.CertStoreParameters");
        addEngine("Cipher", true, (String) null);
        addEngine("ExemptionMechanism", false, (String) null);
        addEngine("Mac", true, (String) null);
        addEngine("KeyAgreement", true, (String) null);
        addEngine("KeyGenerator", false, (String) null);
        addEngine("SecretKeyFactory", false, (String) null);
        addEngine("KeyManagerFactory", false, (String) null);
        addEngine("SSLContext", false, (String) null);
        addEngine("TrustManagerFactory", false, (String) null);
        addEngine("GssApiMechanism", false, (String) null);
        addEngine("SaslClientFactory", false, (String) null);
        addEngine("SaslServerFactory", false, (String) null);
        addEngine("Policy", false, "java.security.Policy$Parameters");
        addEngine("Configuration", false, "javax.security.auth.login.Configuration$Parameters");
        addEngine("XMLSignatureFactory", false, (String) null);
        addEngine("KeyInfoFactory", false, (String) null);
        addEngine("TransformService", false, (String) null);
        addEngine("TerminalFactory", false, "java.lang.Object");
    }

    protected Provider(String str, double d, String str2) {
        this.name = str;
        this.version = d;
        this.info = str2;
        putId();
        this.initialized = true;
    }

    public String getName() {
        return this.name;
    }

    public double getVersion() {
        return this.version;
    }

    public String getInfo() {
        return this.info;
    }

    public String toString() {
        return this.name + " version " + this.version;
    }

    public synchronized void clear() {
        check("clearProviderProperties." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Remove " + this.name + " provider properties");
        }
        implClear();
    }

    public synchronized void load(InputStream inputStream) throws IOException {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Load " + this.name + " provider properties");
        }
        Properties properties = new Properties();
        properties.load(inputStream);
        implPutAll(properties);
    }

    public synchronized void putAll(Map<?, ?> map) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Put all " + this.name + " provider properties");
        }
        implPutAll(map);
    }

    public synchronized Set<Map.Entry<Object, Object>> entrySet() {
        checkInitialized();
        if (this.entrySet == null) {
            int i = this.entrySetCallCount;
            this.entrySetCallCount = i + 1;
            if (i == 0) {
                this.entrySet = Collections.unmodifiableMap(this).entrySet();
            } else {
                return super.entrySet();
            }
        }
        if (this.entrySetCallCount == 2) {
            return this.entrySet;
        }
        throw new RuntimeException("Internal error.");
    }

    public Set<Object> keySet() {
        checkInitialized();
        return Collections.unmodifiableSet(super.keySet());
    }

    public Collection<Object> values() {
        checkInitialized();
        return Collections.unmodifiableCollection(super.values());
    }

    public synchronized Object put(Object obj, Object obj2) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Set " + this.name + " provider property [" + obj + "/" + obj2 + NavigationBarInflaterView.SIZE_MOD_END);
        }
        return implPut(obj, obj2);
    }

    public synchronized Object putIfAbsent(Object obj, Object obj2) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Set " + this.name + " provider property [" + obj + "/" + obj2 + NavigationBarInflaterView.SIZE_MOD_END);
        }
        return implPutIfAbsent(obj, obj2);
    }

    public synchronized Object remove(Object obj) {
        check("removeProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Remove " + this.name + " provider property " + obj);
        }
        return implRemove(obj);
    }

    public synchronized boolean remove(Object obj, Object obj2) {
        check("removeProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Remove " + this.name + " provider property " + obj);
        }
        return implRemove(obj, obj2);
    }

    public synchronized boolean replace(Object obj, Object obj2, Object obj3) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Replace " + this.name + " provider property " + obj);
        }
        return implReplace(obj, obj2, obj3);
    }

    public synchronized Object replace(Object obj, Object obj2) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Replace " + this.name + " provider property " + obj);
        }
        return implReplace(obj, obj2);
    }

    public synchronized void replaceAll(BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ReplaceAll " + this.name + " provider property ");
        }
        implReplaceAll(biFunction);
    }

    public synchronized Object compute(Object obj, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        check("putProviderProperty." + this.name);
        check("removeProviderProperty" + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Compute " + this.name + " provider property " + obj);
        }
        return implCompute(obj, biFunction);
    }

    public synchronized Object computeIfAbsent(Object obj, Function<? super Object, ? extends Object> function) {
        check("putProviderProperty." + this.name);
        check("removeProviderProperty" + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ComputeIfAbsent " + this.name + " provider property " + obj);
        }
        return implComputeIfAbsent(obj, function);
    }

    public synchronized Object computeIfPresent(Object obj, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        check("putProviderProperty." + this.name);
        check("removeProviderProperty" + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ComputeIfPresent " + this.name + " provider property " + obj);
        }
        return implComputeIfPresent(obj, biFunction);
    }

    public synchronized Object merge(Object obj, Object obj2, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        check("putProviderProperty." + this.name);
        check("removeProviderProperty" + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("Merge " + this.name + " provider property " + obj);
        }
        return implMerge(obj, obj2, biFunction);
    }

    public Object get(Object obj) {
        checkInitialized();
        return super.get(obj);
    }

    public synchronized Object getOrDefault(Object obj, Object obj2) {
        checkInitialized();
        return super.getOrDefault(obj, obj2);
    }

    public synchronized void forEach(BiConsumer<? super Object, ? super Object> biConsumer) {
        checkInitialized();
        super.forEach(biConsumer);
    }

    public Enumeration<Object> keys() {
        checkInitialized();
        return super.keys();
    }

    public Enumeration<Object> elements() {
        checkInitialized();
        return super.elements();
    }

    public String getProperty(String str) {
        checkInitialized();
        return super.getProperty(str);
    }

    private void checkInitialized() {
        if (!this.initialized) {
            throw new IllegalStateException();
        }
    }

    private void check(String str) {
        checkInitialized();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }

    private void putId() {
        super.put("Provider.id name", String.valueOf((Object) this.name));
        super.put("Provider.id version", String.valueOf(this.version));
        super.put("Provider.id info", String.valueOf((Object) this.info));
        super.put("Provider.id className", getClass().getName());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.registered = false;
        HashMap hashMap = new HashMap();
        for (Map.Entry entry : super.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        this.defaults = null;
        objectInputStream.defaultReadObject();
        implClear();
        this.initialized = true;
        putAll(hashMap);
    }

    private boolean checkLegacy(Object obj) {
        if (this.registered) {
            Security.increaseVersion();
        }
        if (((String) obj).startsWith("Provider.")) {
            return false;
        }
        this.legacyChanged = true;
        if (this.legacyStrings == null) {
            this.legacyStrings = new LinkedHashMap();
        }
        return true;
    }

    private void implPutAll(Map<?, ?> map) {
        for (Map.Entry next : map.entrySet()) {
            implPut(next.getKey(), next.getValue());
        }
        if (this.registered) {
            Security.increaseVersion();
        }
    }

    private Object implRemove(Object obj) {
        if (obj instanceof String) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.remove((String) obj);
        }
        return super.remove(obj);
    }

    private boolean implRemove(Object obj, Object obj2) {
        if ((obj instanceof String) && (obj2 instanceof String)) {
            if (!checkLegacy(obj)) {
                return false;
            }
            this.legacyStrings.remove((String) obj, obj2);
        }
        return super.remove(obj, obj2);
    }

    private boolean implReplace(Object obj, Object obj2, Object obj3) {
        if ((obj instanceof String) && (obj2 instanceof String) && (obj3 instanceof String)) {
            if (!checkLegacy(obj)) {
                return false;
            }
            this.legacyStrings.replace((String) obj, (String) obj2, (String) obj3);
        }
        return super.replace(obj, obj2, obj3);
    }

    private Object implReplace(Object obj, Object obj2) {
        if ((obj instanceof String) && (obj2 instanceof String)) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.replace((String) obj, (String) obj2);
        }
        return super.replace(obj, obj2);
    }

    private void implReplaceAll(BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        this.legacyChanged = true;
        Map<String, String> map = this.legacyStrings;
        if (map == null) {
            this.legacyStrings = new LinkedHashMap();
        } else {
            map.replaceAll(biFunction);
        }
        super.replaceAll(biFunction);
    }

    private Object implMerge(Object obj, Object obj2, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        if ((obj instanceof String) && (obj2 instanceof String)) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.merge((String) obj, (String) obj2, biFunction);
        }
        return super.merge(obj, obj2, biFunction);
    }

    private Object implCompute(Object obj, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        if (obj instanceof String) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.compute((String) obj, biFunction);
        }
        return super.compute(obj, biFunction);
    }

    private Object implComputeIfAbsent(Object obj, Function<? super Object, ? extends Object> function) {
        if (obj instanceof String) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.computeIfAbsent((String) obj, function);
        }
        return super.computeIfAbsent(obj, function);
    }

    private Object implComputeIfPresent(Object obj, BiFunction<? super Object, ? super Object, ? extends Object> biFunction) {
        if (obj instanceof String) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.computeIfPresent((String) obj, biFunction);
        }
        return super.computeIfPresent(obj, biFunction);
    }

    private Object implPut(Object obj, Object obj2) {
        if ((obj instanceof String) && (obj2 instanceof String)) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.put((String) obj, (String) obj2);
        }
        return super.put(obj, obj2);
    }

    private Object implPutIfAbsent(Object obj, Object obj2) {
        if ((obj instanceof String) && (obj2 instanceof String)) {
            if (!checkLegacy(obj)) {
                return null;
            }
            this.legacyStrings.putIfAbsent((String) obj, (String) obj2);
        }
        return super.putIfAbsent(obj, obj2);
    }

    private void implClear() {
        Map<String, String> map = this.legacyStrings;
        if (map != null) {
            map.clear();
        }
        Map<ServiceKey, Service> map2 = this.legacyMap;
        if (map2 != null) {
            map2.clear();
        }
        Map<ServiceKey, Service> map3 = this.serviceMap;
        if (map3 != null) {
            map3.clear();
        }
        this.legacyChanged = false;
        this.servicesChanged = false;
        this.serviceSet = null;
        super.clear();
        putId();
        if (this.registered) {
            Security.increaseVersion();
        }
    }

    private static class ServiceKey {
        private final String algorithm;
        private final String originalAlgorithm;
        private final String type;

        private ServiceKey(String str, String str2, boolean z) {
            this.type = str;
            this.originalAlgorithm = str2;
            String upperCase = str2.toUpperCase(Locale.ENGLISH);
            this.algorithm = z ? upperCase.intern() : upperCase;
        }

        public int hashCode() {
            return this.type.hashCode() + this.algorithm.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ServiceKey)) {
                return false;
            }
            ServiceKey serviceKey = (ServiceKey) obj;
            if (!this.type.equals(serviceKey.type) || !this.algorithm.equals(serviceKey.algorithm)) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public boolean matches(String str, String str2) {
            return this.type == str && this.originalAlgorithm == str2;
        }
    }

    private void ensureLegacyParsed() {
        if (this.legacyChanged && this.legacyStrings != null) {
            this.serviceSet = null;
            Map<ServiceKey, Service> map = this.legacyMap;
            if (map == null) {
                this.legacyMap = new LinkedHashMap();
            } else {
                map.clear();
            }
            for (Map.Entry next : this.legacyStrings.entrySet()) {
                parseLegacyPut((String) next.getKey(), (String) next.getValue());
            }
            removeInvalidServices(this.legacyMap);
            this.legacyChanged = false;
        }
    }

    private void removeInvalidServices(Map<ServiceKey, Service> map) {
        Iterator<Map.Entry<ServiceKey, Service>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (!((Service) it.next().getValue()).isValid()) {
                it.remove();
            }
        }
    }

    private String[] getTypeAndAlgorithm(String str) {
        int indexOf = str.indexOf(BaseIconCache.EMPTY_CLASS_NAME);
        if (indexOf < 1) {
            Debug debug2 = debug;
            if (debug2 == null) {
                return null;
            }
            debug2.println("Ignoring invalid entry in provider " + this.name + ":" + str);
            return null;
        }
        return new String[]{str.substring(0, indexOf), str.substring(indexOf + 1)};
    }

    private void parseLegacyPut(String str, String str2) {
        if (str.toLowerCase(Locale.ENGLISH).startsWith(ALIAS_PREFIX_LOWER)) {
            String[] typeAndAlgorithm = getTypeAndAlgorithm(str.substring(ALIAS_LENGTH));
            if (typeAndAlgorithm != null) {
                String engineName = getEngineName(typeAndAlgorithm[0]);
                String intern = typeAndAlgorithm[1].intern();
                ServiceKey serviceKey = new ServiceKey(engineName, str2, true);
                Service service = this.legacyMap.get(serviceKey);
                if (service == null) {
                    service = new Service();
                    service.type = engineName;
                    service.algorithm = str2;
                    this.legacyMap.put(serviceKey, service);
                }
                this.legacyMap.put(new ServiceKey(engineName, intern, true), service);
                service.addAlias(intern);
                return;
            }
            return;
        }
        String[] typeAndAlgorithm2 = getTypeAndAlgorithm(str);
        if (typeAndAlgorithm2 != null) {
            int indexOf = typeAndAlgorithm2[1].indexOf(32);
            if (indexOf == -1) {
                String engineName2 = getEngineName(typeAndAlgorithm2[0]);
                String intern2 = typeAndAlgorithm2[1].intern();
                ServiceKey serviceKey2 = new ServiceKey(engineName2, intern2, true);
                Service service2 = this.legacyMap.get(serviceKey2);
                if (service2 == null) {
                    service2 = new Service();
                    service2.type = engineName2;
                    service2.algorithm = intern2;
                    this.legacyMap.put(serviceKey2, service2);
                }
                service2.className = str2;
                return;
            }
            String engineName3 = getEngineName(typeAndAlgorithm2[0]);
            String str3 = typeAndAlgorithm2[1];
            String intern3 = str3.substring(0, indexOf).intern();
            String substring = str3.substring(indexOf + 1);
            while (substring.startsWith(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER)) {
                substring = substring.substring(1);
            }
            String intern4 = substring.intern();
            ServiceKey serviceKey3 = new ServiceKey(engineName3, intern3, true);
            Service service3 = this.legacyMap.get(serviceKey3);
            if (service3 == null) {
                service3 = new Service();
                service3.type = engineName3;
                service3.algorithm = intern3;
                this.legacyMap.put(serviceKey3, service3);
            }
            service3.addAttribute(intern4, str2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0032, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.security.Provider.Service getService(java.lang.String r4, java.lang.String r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            r3.checkInitialized()     // Catch:{ all -> 0x0033 }
            java.security.Provider$ServiceKey r0 = previousKey     // Catch:{ all -> 0x0033 }
            boolean r1 = r0.matches(r4, r5)     // Catch:{ all -> 0x0033 }
            r2 = 0
            if (r1 != 0) goto L_0x0015
            java.security.Provider$ServiceKey r0 = new java.security.Provider$ServiceKey     // Catch:{ all -> 0x0033 }
            r1 = 0
            r0.<init>(r4, r5, r1)     // Catch:{ all -> 0x0033 }
            previousKey = r0     // Catch:{ all -> 0x0033 }
        L_0x0015:
            java.util.Map<java.security.Provider$ServiceKey, java.security.Provider$Service> r4 = r3.serviceMap     // Catch:{ all -> 0x0033 }
            if (r4 == 0) goto L_0x0023
            java.lang.Object r4 = r4.get(r0)     // Catch:{ all -> 0x0033 }
            java.security.Provider$Service r4 = (java.security.Provider.Service) r4     // Catch:{ all -> 0x0033 }
            if (r4 == 0) goto L_0x0023
            monitor-exit(r3)
            return r4
        L_0x0023:
            r3.ensureLegacyParsed()     // Catch:{ all -> 0x0033 }
            java.util.Map<java.security.Provider$ServiceKey, java.security.Provider$Service> r4 = r3.legacyMap     // Catch:{ all -> 0x0033 }
            if (r4 == 0) goto L_0x0031
            java.lang.Object r4 = r4.get(r0)     // Catch:{ all -> 0x0033 }
            r2 = r4
            java.security.Provider$Service r2 = (java.security.Provider.Service) r2     // Catch:{ all -> 0x0033 }
        L_0x0031:
            monitor-exit(r3)
            return r2
        L_0x0033:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.Provider.getService(java.lang.String, java.lang.String):java.security.Provider$Service");
    }

    public synchronized Set<Service> getServices() {
        checkInitialized();
        if (this.legacyChanged || this.servicesChanged) {
            this.serviceSet = null;
        }
        if (this.serviceSet == null) {
            ensureLegacyParsed();
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            Map<ServiceKey, Service> map = this.serviceMap;
            if (map != null) {
                linkedHashSet.addAll(map.values());
            }
            Map<ServiceKey, Service> map2 = this.legacyMap;
            if (map2 != null) {
                linkedHashSet.addAll(map2.values());
            }
            this.serviceSet = Collections.unmodifiableSet(linkedHashSet);
            this.servicesChanged = false;
        }
        return this.serviceSet;
    }

    /* access modifiers changed from: protected */
    public synchronized void putService(Service service) {
        check("putProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println(this.name + ".putService(): " + service);
        }
        if (service == null) {
            throw new NullPointerException();
        } else if (service.getProvider() == this) {
            if (this.serviceMap == null) {
                this.serviceMap = new LinkedHashMap();
            }
            this.servicesChanged = true;
            String type = service.getType();
            ServiceKey serviceKey = new ServiceKey(type, service.getAlgorithm(), true);
            implRemoveService(this.serviceMap.get(serviceKey));
            this.serviceMap.put(serviceKey, service);
            for (String serviceKey2 : service.getAliases()) {
                this.serviceMap.put(new ServiceKey(type, serviceKey2, true), service);
            }
            putPropertyStrings(service);
        } else {
            throw new IllegalArgumentException("service.getProvider() must match this Provider object");
        }
    }

    private void putPropertyStrings(Service service) {
        String type = service.getType();
        String algorithm = service.getAlgorithm();
        super.put(type + BaseIconCache.EMPTY_CLASS_NAME + algorithm, service.getClassName());
        for (String str : service.getAliases()) {
            super.put(ALIAS_PREFIX + type + BaseIconCache.EMPTY_CLASS_NAME + str, algorithm);
        }
        for (Map.Entry entry : service.attributes.entrySet()) {
            super.put(type + BaseIconCache.EMPTY_CLASS_NAME + algorithm + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + entry.getKey(), entry.getValue());
        }
        if (this.registered) {
            Security.increaseVersion();
        }
    }

    private void removePropertyStrings(Service service) {
        String type = service.getType();
        String algorithm = service.getAlgorithm();
        super.remove(type + BaseIconCache.EMPTY_CLASS_NAME + algorithm);
        for (String str : service.getAliases()) {
            super.remove(ALIAS_PREFIX + type + BaseIconCache.EMPTY_CLASS_NAME + str);
        }
        for (Map.Entry key : service.attributes.entrySet()) {
            super.remove(type + BaseIconCache.EMPTY_CLASS_NAME + algorithm + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + key.getKey());
        }
        if (this.registered) {
            Security.increaseVersion();
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void removeService(Service service) {
        check("removeProviderProperty." + this.name);
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println(this.name + ".removeService(): " + service);
        }
        if (service != null) {
            implRemoveService(service);
        } else {
            throw new NullPointerException();
        }
    }

    private void implRemoveService(Service service) {
        if (service != null && this.serviceMap != null) {
            String type = service.getType();
            ServiceKey serviceKey = new ServiceKey(type, service.getAlgorithm(), false);
            if (service == this.serviceMap.get(serviceKey)) {
                this.servicesChanged = true;
                this.serviceMap.remove(serviceKey);
                for (String serviceKey2 : service.getAliases()) {
                    this.serviceMap.remove(new ServiceKey(type, serviceKey2, false));
                }
                removePropertyStrings(service);
            }
        }
    }

    private static class UString {
        final String lowerString;
        final String string;

        UString(String str) {
            this.string = str;
            this.lowerString = str.toLowerCase(Locale.ENGLISH);
        }

        public int hashCode() {
            return this.lowerString.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof UString)) {
                return false;
            }
            return this.lowerString.equals(((UString) obj).lowerString);
        }

        public String toString() {
            return this.string;
        }
    }

    private static class EngineDescription {
        private volatile Class<?> constructorParameterClass;
        final String constructorParameterClassName;
        final String name;
        final boolean supportsParameter;

        EngineDescription(String str, boolean z, String str2) {
            this.name = str;
            this.supportsParameter = z;
            this.constructorParameterClassName = str2;
        }

        /* access modifiers changed from: package-private */
        public Class<?> getConstructorParameterClass() throws ClassNotFoundException {
            Class<?> cls = this.constructorParameterClass;
            if (cls != null) {
                return cls;
            }
            Class<?> cls2 = Class.forName(this.constructorParameterClassName);
            this.constructorParameterClass = cls2;
            return cls2;
        }
    }

    private static void addEngine(String str, boolean z, String str2) {
        EngineDescription engineDescription = new EngineDescription(str, z, str2);
        Map<String, EngineDescription> map = knownEngines;
        map.put(str.toLowerCase(Locale.ENGLISH), engineDescription);
        map.put(str, engineDescription);
    }

    /* access modifiers changed from: private */
    public static String getEngineName(String str) {
        Map<String, EngineDescription> map = knownEngines;
        EngineDescription engineDescription = map.get(str);
        if (engineDescription == null) {
            engineDescription = map.get(str.toLowerCase(Locale.ENGLISH));
        }
        return engineDescription == null ? str : engineDescription.name;
    }

    public static class Service {
        private static final Class<?>[] CLASS0 = new Class[0];
        /* access modifiers changed from: private */
        public String algorithm;
        private List<String> aliases;
        /* access modifiers changed from: private */
        public Map<UString, String> attributes;
        /* access modifiers changed from: private */
        public String className;
        private volatile Reference<Class<?>> classRef;
        private volatile Boolean hasKeyAttributes;
        private final Provider provider;
        private boolean registered;
        private Class[] supportedClasses;
        private String[] supportedFormats;
        /* access modifiers changed from: private */
        public String type;

        private Service(Provider provider2) {
            this.provider = provider2;
            this.aliases = Collections.emptyList();
            this.attributes = Collections.emptyMap();
        }

        /* access modifiers changed from: private */
        public boolean isValid() {
            return (this.type == null || this.algorithm == null || this.className == null) ? false : true;
        }

        /* access modifiers changed from: private */
        public void addAlias(String str) {
            if (this.aliases.isEmpty()) {
                this.aliases = new ArrayList(2);
            }
            this.aliases.add(str);
        }

        /* access modifiers changed from: package-private */
        public void addAttribute(String str, String str2) {
            if (this.attributes.isEmpty()) {
                this.attributes = new HashMap(8);
            }
            this.attributes.put(new UString(str), str2);
        }

        public Service(Provider provider2, String str, String str2, String str3, List<String> list, Map<String, String> map) {
            if (provider2 == null || str == null || str2 == null || str3 == null) {
                throw null;
            }
            this.provider = provider2;
            this.type = Provider.getEngineName(str);
            this.algorithm = str2;
            this.className = str3;
            if (list == null) {
                this.aliases = Collections.emptyList();
            } else {
                this.aliases = new ArrayList(list);
            }
            if (map == null) {
                this.attributes = Collections.emptyMap();
                return;
            }
            this.attributes = new HashMap();
            for (Map.Entry next : map.entrySet()) {
                this.attributes.put(new UString((String) next.getKey()), (String) next.getValue());
            }
        }

        public final String getType() {
            return this.type;
        }

        public final String getAlgorithm() {
            return this.algorithm;
        }

        public final Provider getProvider() {
            return this.provider;
        }

        public final String getClassName() {
            return this.className;
        }

        /* access modifiers changed from: private */
        public final List<String> getAliases() {
            return this.aliases;
        }

        public final String getAttribute(String str) {
            str.getClass();
            return this.attributes.get(new UString(str));
        }

        public Object newInstance(Object obj) throws NoSuchAlgorithmException {
            if (!this.registered) {
                if (this.provider.getService(this.type, this.algorithm) == this) {
                    this.registered = true;
                } else {
                    throw new NoSuchAlgorithmException("Service not registered with Provider " + this.provider.getName() + ": " + this);
                }
            }
            try {
                EngineDescription engineDescription = (EngineDescription) Provider.knownEngines.get(this.type);
                if (engineDescription == null) {
                    return newInstanceGeneric(obj);
                }
                if (engineDescription.constructorParameterClassName != null) {
                    Class<?> constructorParameterClass = engineDescription.getConstructorParameterClass();
                    if (obj != null) {
                        if (!constructorParameterClass.isAssignableFrom(obj.getClass())) {
                            throw new InvalidParameterException("constructorParameter must be instanceof " + engineDescription.constructorParameterClassName.replace('$', '.') + " for engine type " + this.type);
                        }
                    }
                    return getImplClass().getConstructor(constructorParameterClass).newInstance(obj);
                } else if (obj == null) {
                    return getImplClass().getConstructor(new Class[0]).newInstance(new Object[0]);
                } else {
                    throw new InvalidParameterException("constructorParameter not used with " + this.type + " engines");
                }
            } catch (NoSuchAlgorithmException e) {
                throw e;
            } catch (InvocationTargetException e2) {
                throw new NoSuchAlgorithmException("Error constructing implementation (algorithm: " + this.algorithm + ", provider: " + this.provider.getName() + ", class: " + this.className + NavigationBarInflaterView.KEY_CODE_END, e2.getCause());
            } catch (Exception e3) {
                throw new NoSuchAlgorithmException("Error constructing implementation (algorithm: " + this.algorithm + ", provider: " + this.provider.getName() + ", class: " + this.className + NavigationBarInflaterView.KEY_CODE_END, e3);
            }
        }

        private Class<?> getImplClass() throws NoSuchAlgorithmException {
            Class<?> cls;
            Class<?> loadClass;
            try {
                Reference<Class<?>> reference = this.classRef;
                if (reference == null) {
                    cls = null;
                } else {
                    cls = reference.get();
                }
                if (cls == null) {
                    ClassLoader classLoader = this.provider.getClass().getClassLoader();
                    if (classLoader == null) {
                        loadClass = Class.forName(this.className);
                    } else {
                        loadClass = classLoader.loadClass(this.className);
                    }
                    if (Modifier.isPublic(cls.getModifiers())) {
                        this.classRef = new WeakReference(cls);
                    } else {
                        throw new NoSuchAlgorithmException("class configured for " + this.type + " (provider: " + this.provider.getName() + ") is not public.");
                    }
                }
                return cls;
            } catch (ClassNotFoundException e) {
                throw new NoSuchAlgorithmException("class configured for " + this.type + " (provider: " + this.provider.getName() + ") cannot be found.", e);
            }
        }

        private Object newInstanceGeneric(Object obj) throws Exception {
            Class<?> implClass = getImplClass();
            if (obj == null) {
                try {
                    return implClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (NoSuchMethodException unused) {
                    throw new NoSuchAlgorithmException("No public no-arg constructor found in class " + this.className);
                }
            } else {
                Class<?> cls = obj.getClass();
                for (Constructor constructor : implClass.getConstructors()) {
                    Class[] parameterTypes = constructor.getParameterTypes();
                    if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(cls)) {
                        return constructor.newInstance(obj);
                    }
                }
                throw new NoSuchAlgorithmException("No public constructor matching " + cls.getName() + " found in class " + this.className);
            }
        }

        public boolean supportsParameter(Object obj) {
            EngineDescription engineDescription = (EngineDescription) Provider.knownEngines.get(this.type);
            if (engineDescription == null) {
                return true;
            }
            if (!engineDescription.supportsParameter) {
                throw new InvalidParameterException("supportsParameter() not used with " + this.type + " engines");
            } else if (obj != null && !(obj instanceof Key)) {
                throw new InvalidParameterException("Parameter must be instanceof Key for engine " + this.type);
            } else if (!hasKeyAttributes()) {
                return true;
            } else {
                if (obj == null) {
                    return false;
                }
                Key key = (Key) obj;
                if (!supportsKeyFormat(key) && !supportsKeyClass(key)) {
                    return false;
                }
                return true;
            }
        }

        private boolean hasKeyAttributes() {
            Boolean bool = this.hasKeyAttributes;
            if (bool == null) {
                synchronized (this) {
                    String attribute = getAttribute("SupportedKeyFormats");
                    if (attribute != null) {
                        this.supportedFormats = attribute.split("\\|");
                    }
                    String attribute2 = getAttribute("SupportedKeyClasses");
                    boolean z = false;
                    if (attribute2 != null) {
                        String[] split = attribute2.split("\\|");
                        ArrayList arrayList = new ArrayList(split.length);
                        for (String keyClass : split) {
                            Class<?> keyClass2 = getKeyClass(keyClass);
                            if (keyClass2 != null) {
                                arrayList.add(keyClass2);
                            }
                        }
                        this.supportedClasses = (Class[]) arrayList.toArray(CLASS0);
                    }
                    if (!(this.supportedFormats == null && this.supportedClasses == null)) {
                        z = true;
                    }
                    bool = Boolean.valueOf(z);
                    this.hasKeyAttributes = bool;
                }
            }
            return bool.booleanValue();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
            return null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:4:?, code lost:
            r0 = r0.provider.getClass().getClassLoader();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:5:0x000f, code lost:
            if (r0 != null) goto L_0x0011;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0015, code lost:
            return r0.loadClass(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:?, code lost:
            return null;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0005 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private java.lang.Class<?> getKeyClass(java.lang.String r1) {
            /*
                r0 = this;
                java.lang.Class r0 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0005 }
                return r0
            L_0x0005:
                java.security.Provider r0 = r0.provider     // Catch:{ ClassNotFoundException -> 0x0016 }
                java.lang.Class r0 = r0.getClass()     // Catch:{ ClassNotFoundException -> 0x0016 }
                java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch:{ ClassNotFoundException -> 0x0016 }
                if (r0 == 0) goto L_0x0016
                java.lang.Class r0 = r0.loadClass(r1)     // Catch:{ ClassNotFoundException -> 0x0016 }
                return r0
            L_0x0016:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.security.Provider.Service.getKeyClass(java.lang.String):java.lang.Class");
        }

        private boolean supportsKeyFormat(Key key) {
            String format;
            if (this.supportedFormats == null || (format = key.getFormat()) == null) {
                return false;
            }
            for (String equals : this.supportedFormats) {
                if (equals.equals(format)) {
                    return true;
                }
            }
            return false;
        }

        private boolean supportsKeyClass(Key key) {
            if (this.supportedClasses == null) {
                return false;
            }
            Class<?> cls = key.getClass();
            for (Class isAssignableFrom : this.supportedClasses) {
                if (isAssignableFrom.isAssignableFrom(cls)) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            String str;
            String str2 = "";
            if (this.aliases.isEmpty()) {
                str = str2;
            } else {
                str = "\r\n  aliases: " + this.aliases.toString();
            }
            if (!this.attributes.isEmpty()) {
                str2 = "\r\n  attributes: " + this.attributes.toString();
            }
            return this.provider.getName() + ": " + this.type + BaseIconCache.EMPTY_CLASS_NAME + this.algorithm + " -> " + this.className + str + str2 + "\r\n";
        }
    }

    public void setRegistered() {
        this.registered = true;
    }

    public void setUnregistered() {
        this.registered = false;
    }

    public boolean isRegistered() {
        return this.registered;
    }

    public synchronized void warmUpServiceProvision() {
        checkInitialized();
        ensureLegacyParsed();
        getServices();
    }
}
